package com.ctlok.microservices.user.management.test.repository;

import com.ctlok.microservices.user.management.entity.User;
import com.ctlok.microservices.user.management.repository.UserRepository;
import com.ctlok.microservices.user.management.test.ApplicationConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by Lawrence Cheung on 2015/4/12.
 *
 * @author Lawrence Cheung
 */
@RunWith( SpringJUnit4ClassRunner.class )
@SpringApplicationConfiguration( classes = ApplicationConfig.class )
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Before
    public void beforeClass() {
        final User user = new User();
        user.setUsername( "Lawrence" );
        user.setPassword( "123456" );
        user.setRoles( Arrays.asList( "ROLE_USER" ) );
        userRepository.save( user );
    }

    @Test
    public void testFindByUsername() {
        final User user = userRepository.findByUsername( "Lawrence" );
        assertThat( "username match Lawrence", user.getUsername(), equalTo( "Lawrence" ) );
    }

}
