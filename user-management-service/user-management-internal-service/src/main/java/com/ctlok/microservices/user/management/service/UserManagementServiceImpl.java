package com.ctlok.microservices.user.management.service;

import com.ctlok.microservices.user.management.entity.User;
import com.ctlok.microservices.user.management.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.Environment;
import reactor.rx.Stream;
import reactor.rx.Streams;

import java.util.Optional;

/**
 * Created by Lawrence Cheung on 2015/4/10.
 */
@Slf4j
@Service
public class UserManagementServiceImpl implements UserManagementService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Environment env;

    @Autowired
    public UserManagementServiceImpl( final UserRepository userRepository, final PasswordEncoder passwordEncoder, final Environment env ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.env = env;
    }

    @Override
    public Stream<Page<User>> findAll( final Pageable pageable ) {
        return Streams
                .just( pageable )
                .dispatchOn( env )
                .map( userRepository:: findAll );
    }

    @Override
    public Stream<Optional<User>> findById( final String id ) {
        return Streams
                .just( id )
                .dispatchOn( env )
                .map( ( i ) -> Optional.ofNullable( userRepository.findOne( i ) ) );
    }

    @Override
    public Stream<Optional<User>> findByUsername( final String username ) {
        return Streams
                .just( username )
                .dispatchOn( env )
                .map( ( u ) -> Optional.ofNullable( userRepository.findByUsername( u ) ) );
    }

    @Override
    public Stream<Boolean> isUsernamePasswordExistAndMatch(
            final String username, final String password ) {
        return findByUsername( username )
                .map( optional -> {
                    if ( optional.isPresent() ) {
                        final User storedUser = optional.get();
                        return passwordEncoder.matches( password, storedUser.getPassword() );
                    } else {
                        return Boolean.FALSE;
                    }
                } );
    }

    @Override
    public Stream<Boolean> isUsernamePasswordExistAndMatch( final User user ) {
        return isUsernamePasswordExistAndMatch( user.getUsername(), user.getPassword() );
    }

    @Override
    public Stream<User> createUser( final User user ) {
        return Streams
                .just( user )
                .dispatchOn( env )
                .map( u -> {
                    u.setId( null );
                    u.setPassword( passwordEncoder.encode( u.getPassword() ) );
                    return userRepository.save( u );
                } );
    }

    @Override
    public Stream<User> updateOrCreateUser( final User user ) {

        if ( user.getId() == null ) {

            return createUser( user );

        } else {

            return findById( user.getId() )
                    .map( optional -> {
                        if ( optional.isPresent() ) {
                            user.setPassword( optional.get().getPassword() );
                            return Streams.just( userRepository.save( user ) ).dispatchOn( env );
                        } else {
                            return createUser( user );
                        }
                    } )
                    .flatMap( stream -> stream );
        }

    }

    @Override
    public Stream<Boolean> deleteUser( final String id ) {
        return findById( id )
                .map( optional -> {
                    if ( optional.isPresent() ) {
                        userRepository.delete( optional.get() );
                        return Boolean.TRUE;
                    } else {
                        return Boolean.FALSE;
                    }
                } );
    }

    @Override
    public Stream<Boolean> deleteUser( final User user ) {
        return deleteUser( user.getId() );
    }

    @Override
    public Stream<Boolean> updatePassword( final String id, final String password ) {
        return findById( id )
                .map( optional -> {
                    if ( optional.isPresent() ) {
                        final User storedUser = optional.get();
                        storedUser.setPassword( passwordEncoder.encode( password ) );
                        userRepository.save( storedUser );
                        return Boolean.TRUE;
                    } else {
                        return Boolean.FALSE;
                    }
                } );
    }

    @Override
    public Stream<Boolean> updatePassword( final User user, final String password ) {
        return updatePassword( user.getId(), password );
    }

}
