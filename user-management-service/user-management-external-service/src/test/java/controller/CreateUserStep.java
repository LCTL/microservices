package controller;

import com.ctlok.microservices.commons.external.RestResponse;
import com.ctlok.microservices.user.management.Application;
import com.ctlok.microservices.user.management.RestUserManagementService;
import com.ctlok.microservices.user.management.entity.User;
import com.ctlok.microservices.user.management.repository.UserRepository;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

/**
 * @author Lawrence Cheung
 */
@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( classes = Application.class, loader = SpringApplicationContextLoader.class )
@WebIntegrationTest
public class CreateUserStep {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestUserManagementService restUserManagementService;

    private RestResponse<User> response;

    @Before
    public void clearData() {
        userRepository.deleteAll();
    }

    @Given( "^user with the username '(.+)', password '(.+)' and roles '(.+)'$" )
    public void createNewUser( final String username, final String password, final String roles ) {

        final User user = new User();
        user.setUsername( username );
        user.setPassword( password );
        user.setRoles( Arrays.asList( roles.split( "," ) ) );

        restUserManagementService.create( user );

    }

    @When( "^find by username '(.+)'$" )
    public void findByUsername( final String username ) throws InterruptedException {

        final DeferredResult<RestResponse<User>> result = restUserManagementService.findByUsername( username );
        final CountDownLatch latch = new CountDownLatch( 1 );

        result.setResultHandler( ( response ) -> {
            this.response = (RestResponse<User>) response;
            latch.countDown();
        } );

        latch.await( 1, TimeUnit.SECONDS );

    }

    @Then( "^'(.+)' should has been found$" )
    public void verifyUser( final String username ) {

        assertThat( "response no error", response.getError(), nullValue() );
        assertThat( "user not null", response.getValue(), notNullValue() );
        assertThat( "username match", response.getValue().getUsername(), is( username ) );

    }

    @Then( "^'(.+)' should encoded$" )
    public void verifyPassword( final String password ) {

        assertThat( "username match", response.getValue().getPassword(), notNullValue() );
        assertThat( "username match", response.getValue().getPassword(), not( equalTo( password ) ) );

    }

    @Then( "^roles should contain '(.+)'$" )
    public void verifyRoles( final String roles ) {

        assertThat( "roles", response.getValue().getRoles(), contains( roles.split( "," ) ) );

    }

}
