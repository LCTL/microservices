package com.ctlok.microservices.user.management.repository;

import com.ctlok.microservices.user.management.entity.User;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by Lawrence Cheung on 2015/4/10.
 */
public interface UserRepository extends PagingAndSortingRepository<User, String> {

    @Query("{username: ?0}")
    User findByUsername( String username );

}
