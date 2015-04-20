package service;

import com.ctlok.microservices.user.management.entity.User;
import com.ctlok.microservices.user.management.repository.UserRepository;
import com.ctlok.microservices.user.management.service.UserManagementService;
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
import repository.UserForCucumber;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

/**
 * @author Lawrence Cheung
 */
@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( classes = ApplicationConfig.class, loader = SpringApplicationContextLoader.class )
public class FindUserStep {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    private UserManagementService userManagementService;

    private User user;
    private List<User> users;

    @Before
    public void clearData() {

        user = null;
        users = null;

        userRepository.deleteAll();

    }

    @Given( "^the user management system is initialized with the following data$" )
    public void createUserByService( final List<UserForCucumber> users ) {

        // for execute UserForCucumber.getRoles method
        // should be remove this code if found method to create collection by cucumber data
        users.toString();

        users.forEach( user -> {
            try {
                userManagementService
                        .createUser( user )
                        .next()
                        .onError( IllegalStateException::new )
                        .await( 1, TimeUnit.SECONDS );
            } catch ( InterruptedException e ) {
                throw new IllegalStateException( e );
            }
        } );

    }

    @When( "^find by username '(.+)'$" )
    public void findByUsername( final String username ) throws InterruptedException {

        user = userManagementService
                .findByUsername( username )
                .next()
                .await( 1, TimeUnit.SECONDS )
                .orElseThrow( IllegalStateException::new );

    }

    @When( "^find by id '(.+)'$" )
    public void findById( final String id ) throws InterruptedException {

        user = userManagementService
                .findById( id )
                .next()
                .await( 1, TimeUnit.SECONDS )
                .orElseThrow( IllegalStateException::new );

    }

    @When( "^find by multiple ids$" )
    public void findByIds( final List<String> ids ) throws InterruptedException {

        users = userManagementService
                .findByIds( ids )
                .next()
                .await( 1, TimeUnit.SECONDS );

    }

    @Then( "^'(.+)' should has been found$" )
    public void verifyUser( final String username ) {

        assertThat( "user not null", user, notNullValue() );
        assertThat( "username match", user.getUsername(), is( username ) );

    }

    @Then( "^password should be hashed format: '(.+)'$" )
    public void verifyUserPassword( final String password ) {

        assertThat( "username match", user.getPassword(), not( is( password ) ) );

    }

    @Then( "^should be contains roles: '(.+)'$" )
    public void verifyUserRoles( final String roles ) {

        assertThat( "role contains", user.getRoles(), contains( roles.split( "," ) ) );

    }

    @Then( "^user list should contains id$" )
    public void verifyUserList( final List<String> ids ) {

        assertThat( "users not null", users, notNullValue() );
        assertThat( "users not empty", users.isEmpty(), is( false ) );
        assertThat( "users size match", users.size(), is( ids.size() ) );

        users
                .parallelStream()
                .forEach( user -> assertThat( "user id contain in input id", ids.contains( user.getId() ), is( true ) ) );

    }

}
