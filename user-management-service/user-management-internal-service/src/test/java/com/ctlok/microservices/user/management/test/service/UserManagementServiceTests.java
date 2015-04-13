package com.ctlok.microservices.user.management.test.service;

import com.ctlok.microservices.user.management.entity.User;
import com.ctlok.microservices.user.management.repository.UserRepository;
import com.ctlok.microservices.user.management.service.UserManagementService;
import com.ctlok.microservices.user.management.test.ApplicationConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Created by Lawrence Cheung on 2015/4/13.
 *
 * @author Lawrence Cheung
 */
@RunWith( SpringJUnit4ClassRunner.class )
@SpringApplicationConfiguration( classes = ApplicationConfig.class )
public class UserManagementServiceTests {

    @Autowired
    private UserManagementService userManagementService;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void createUser() {
        final User user = new User();
        user.setId( "0" );
        user.setUsername( "Lawrence" );
        user.setPassword( "123456" );
        user.setRoles( Arrays.asList( "ROLE_USER", "ROLE_ADMIN" ) );
        userRepository.save( user );
    }

    @After
    public void deleteAll() {
        userRepository.deleteAll();
    }

    @Test
    public void testFindAll() throws Throwable {

        final Pageable pageable = new PageRequest( 0, 20 );
        final Page<User> page = userManagementService
                .findAll( pageable )
                .next()
                .await( 1, TimeUnit.SECONDS );

        assertThat( "not null", page, notNullValue() );
        assertThat( "page number is 0", page.getNumber(), equalTo( 0 ) );
        assertThat( "number of element > 0", page.getNumberOfElements(), greaterThan( 0 ) );

    }

    @Test
    public void testFindById() throws Throwable {

        final Optional<User> optional = userManagementService
                .findById( "0" )
                .next()
                .await( 1, TimeUnit.SECONDS );

        assertThat( "value presented", optional.isPresent(), equalTo( true ) );

        final User user = optional.get();

        assertThat( "user id is 0", user.getId(), equalTo( "0" ) );
        assertThat( "username is Lawrence", user.getUsername(), equalTo( "Lawrence" ) );

    }

    @Test
    public void testIsUsernamePasswordExistAndMatch() throws Throwable {

        testCreateUser();

        final User user = new User();
        user.setUsername( "PP" );
        user.setPassword( "123456" );
        final Boolean match = userManagementService
                .isUsernamePasswordExistAndMatch( user )
                .next()
                .await( 1, TimeUnit.SECONDS );

        assertThat( "password match", match, equalTo( Boolean.TRUE ) );

    }

    @Test
    public void testFindByUsername() throws Throwable {

        final Optional<User> optional = userManagementService
                .findByUsername( "Lawrence" )
                .next()
                .await( 1, TimeUnit.SECONDS );

        assertThat( "value presented", optional.isPresent(), equalTo( true ) );

        final User user = optional.get();

        assertThat( "username match Lawrence", user.getUsername(), equalTo( "Lawrence" ) );
        assertThat( "raw password", user.getPassword(), equalTo( "123456" ) );
        assertThat( "contains ROLE_USER, ROLE_ADMIN", user.getRoles(), contains( "ROLE_USER", "ROLE_ADMIN" ) );

    }

    @Test
    public void testCreateUser() throws Throwable {

        final User user = new User();
        user.setUsername( "PP" );
        user.setPassword( "123456" );
        user.setRoles( Arrays.asList( "ROLE_USER", "ROLE_ADMIN" ) );

        final User storedUser = userManagementService
                .createUser( user )
                .next()
                .await( 1, TimeUnit.SECONDS );

        System.out.println( user.getRoles() );

        assertThat( "not null", storedUser, notNullValue() );
        assertThat( "id not null", storedUser.getId(), notNullValue() );
        assertThat( "username match Lawrence", storedUser.getUsername(), equalTo( "PP" ) );
        assertThat( "encoded password", storedUser.getPassword(), not( equalTo( "123456" ) ) );
        assertThat( "contains ROLE_USER, ROLE_ADMIN", storedUser.getRoles(), contains( "ROLE_USER", "ROLE_ADMIN" ) );

    }

    @Test
    public void testUpdateOrCreateUser() throws Throwable {

        final User user = new User();
        user.setUsername( "PP" );
        user.setPassword( "123456" );
        user.setRoles( Arrays.asList( "ROLE_USER", "ROLE_ADMIN" ) );

        final User storedUser = userManagementService
                .updateOrCreateUser( user )
                .next()
                .await( 1, TimeUnit.SECONDS );

        assertThat( "not null", storedUser, notNullValue() );
        assertThat( "id not null", storedUser.getId(), notNullValue() );
        assertThat( "username is PP", storedUser.getUsername(), equalTo( "PP" ) );

        storedUser.setUsername( "KK" );

        final User updatedUser = userManagementService
                .updateOrCreateUser( storedUser )
                .next()
                .await( 1, TimeUnit.SECONDS );

        assertThat( "not null", updatedUser, notNullValue() );
        assertThat( "id not null", updatedUser.getId(), notNullValue() );
        assertThat( "id equal stored user", updatedUser.getId(), equalTo( storedUser.getId() ) );
        assertThat( "username change to KK", updatedUser.getUsername(), equalTo( "KK" ) );

    }

    @Test
    public void testDeleteUserById() throws Throwable {

        final Boolean deleted = userManagementService
                .deleteUser( "0" )
                .next()
                .await( 1, TimeUnit.SECONDS );

        assertThat( "Deleted", deleted, equalTo( true ) );

    }

    @Test
    public void testDeleteUser() throws Throwable {

        final User user = userManagementService
                .findByUsername( "Lawrence" )
                .next()
                .await( 1, TimeUnit.SECONDS )
                .orElseThrow( Exception::new );

        final Boolean deleted = userManagementService
                .deleteUser( user )
                .next()
                .await( 1, TimeUnit.SECONDS );

        assertThat( "Deleted", deleted, equalTo( true ) );

    }

    @Test
    public void testUpdatePasswordById() throws Throwable {

        final User user = new User();
        user.setUsername( "Lawrence" );
        user.setPassword( "654321" );

        final Boolean success = userManagementService
                .updatePassword( "0", "654321" )
                .next()
                .await( 1, TimeUnit.SECONDS );

        assertThat( "Deleted", success, equalTo( true ) );

        final Boolean match = userManagementService
                .isUsernamePasswordExistAndMatch( user )
                .next()
                .await( 1, TimeUnit.SECONDS );

        assertThat( "password match", match, equalTo( Boolean.TRUE ) );

    }

    @Test
    public void testUpdatePassword() throws Throwable {

        final User user = new User();
        user.setUsername( "Lawrence" );
        user.setPassword( "654321" );

        final User lawrence = userManagementService
                .findByUsername( "Lawrence" )
                .next()
                .await( 1, TimeUnit.SECONDS )
                .orElseThrow( Exception::new );

        final Boolean success = userManagementService
                .updatePassword( lawrence, "654321" )
                .next()
                .await( 1, TimeUnit.SECONDS );

        assertThat( "Deleted", success, equalTo( true ) );

        final Boolean match = userManagementService
                .isUsernamePasswordExistAndMatch( user )
                .next()
                .await( 1, TimeUnit.SECONDS );

        assertThat( "password match", match, equalTo( Boolean.TRUE ) );

    }


}
