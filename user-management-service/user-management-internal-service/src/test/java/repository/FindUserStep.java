package repository;

import com.ctlok.microservices.user.management.entity.User;
import com.ctlok.microservices.user.management.repository.UserRepository;
import com.ctlok.microservices.user.management.test.ApplicationConfig;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

/**
 * @author Lawrence Cheung
 */
@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( classes = ApplicationConfig.class, loader = SpringApplicationContextLoader.class )
public class FindUserStep {

    @Autowired
    public UserRepository userRepository;

    private User user;

    @Before
    public void clearData() {

        user = null;

        userRepository.deleteAll();

    }

    @Given( "^the user management system is initialized with the following data$" )
    public void createUser( final List<UserForCucumber> users ) {

        // for execute UserForCucumber.getRoles method
        // should be remove this code if found method to create collection by cucumber data
        users.toString();

        userRepository.save( users );

    }

    @When( "^find by username '(.+)'$" )
    public void findByUsername( final String username ) {

        user = userRepository.findByUsername( username );

    }

    @When( "^find by id '(.+)'$" )
    public void findById( final String id ) throws InterruptedException {

        user = userRepository.findOne( id );

    }


    @Then( "^'(.+)' should has been found$" )
    public void verifyUser( final String username ) {

        assertThat( "user not null", user, notNullValue() );
        assertThat( "username match", user.getUsername(), is( username ) );

    }

    @Then( "^password should be raw format: '(.+)'$" )
    public void verifyUserPassword( final String password ) {

        assertThat( "username match", user.getPassword(), is( password ) );

    }

    @Then( "^should be contains roles: '(.+)'$" )
    public void verifyUserRoles( final String roles ) {

        assertThat( "username match", user.getRoles(), contains( roles.split( "," ) ) );

    }

}
