package com.ctlok.microservices.user.management.service;

import com.ctlok.microservices.user.management.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.rx.Stream;

import java.util.Optional;


/**
 * Created by Lawrence Cheung on 2015/4/10.
 */
public interface UserManagementService {

    Stream<Page<User>> findAll( Pageable pageable );

    Stream<Optional<User>> findById( String id );

    Stream<Optional<User>> findByUsername( String username );

    Stream<Boolean> isUsernamePasswordExistAndMatch( User user );

    Stream<User> createUser( User user );

    Stream<User> updateOrCreateUser( User user );

    Stream<Boolean> deleteUser( String id );

    Stream<Boolean> deleteUser( User user );

    Stream<Boolean> updatePassword( User user, String password );

}
