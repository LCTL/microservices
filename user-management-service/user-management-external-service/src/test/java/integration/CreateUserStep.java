package integration;

import com.ctlok.microservices.commons.external.RestResponse;
import com.ctlok.microservices.user.management.Application;
import com.ctlok.microservices.user.management.entity.User;
import com.ctlok.microservices.user.management.repository.UserRepository;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;

/**
 * @author Lawrence Cheung
 */
@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( classes = Application.class, loader = SpringApplicationContextLoader.class )
@WebIntegrationTest
public class CreateUserStep {

    private final String BASE_URI = "http://localhost:8080/users";
    private final RestTemplate restTemplate = new TestRestTemplate();

    @Autowired
    private UserRepository userRepository;

    private RestResponse<User> response;

    @Before
    public void clearData() {
        userRepository.deleteAll();
    }

    @Given( "^user with the username '(.+)', password '(.+)' and roles '(.+)'$" )
    public void createNewUser( final String username, final String password, final String roles ) {

        final User user = new ClientSideUser();
        user.setUsername( username );
        user.setPassword( password );
        user.setRoles( Arrays.asList( roles.split( "," ) ) );

        restTemplate.postForObject( BASE_URI, user, User.class );

    }

    @When( "^find by username '(.+)'$" )
    public void findByUsername( final String username ) {

        final String uri = BASE_URI + "?username={username}";

        response = restTemplate.exchange(
                uri, HttpMethod.GET, null,
                new ParameterizedTypeReference<RestResponse<User>>() {},
                username )
                .getBody();

    }

    @Then( "'(.+)' should has been found$" )
    public void verifyUser( final String username ) {

        assertThat( "response no error", response.getError(), nullValue() );
        assertThat( "user not null", response.getValue(), notNullValue() );
        assertThat( "username match", response.getValue().getUsername(), is( username ) );

    }

    @Then( "^password should be null$" )
    public void verifyUserPassword() {

        assertThat( "password not stored in raw format", response.getValue().getPassword(), nullValue() );

    }

}
