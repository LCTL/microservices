package com.ctlok.microservices.user.management;

import com.ctlok.microservices.commons.external.RestResponse;
import com.ctlok.microservices.commons.external.utils.DeferredResultUtils;
import com.ctlok.microservices.user.management.entity.User;
import com.ctlok.microservices.user.management.service.UserManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Created by Lawrence Cheung on 2015/4/10.
 */
@Slf4j
@RestController
@RequestMapping( "/users" )
public class RestUserManagementService {

    private final UserManagementService userManagementService;

    @Autowired
    public RestUserManagementService(
            final UserManagementService userManagementService ) {
        this.userManagementService = userManagementService;
    }

    @RequestMapping( method = RequestMethod.GET )
    public DeferredResult<RestResponse<Page<User>>> findAll(
            final Pageable pageable ) {
        return DeferredResultUtils.executeAsync( userManagementService.findAll( pageable ) );
    }

    @RequestMapping( value = "/{id}", method = RequestMethod.GET )
    public DeferredResult<RestResponse<User>> findById( @PathVariable( "id" ) final String id ) {
        return DeferredResultUtils.executeAsyncWithOptional( userManagementService.findById( id ) );
    }

    @RequestMapping( method = RequestMethod.POST )
    public DeferredResult<RestResponse<User>> create( @RequestBody final User user ) {
        return DeferredResultUtils.executeAsync( userManagementService.createUser( user ) );
    }

    @RequestMapping( value = "/{id}", method = RequestMethod.PUT )
    public DeferredResult<RestResponse<User>> updateOrCreateUser(
            @PathVariable( "id" ) final String id,
            @RequestBody final User user ) {
        user.setId( id );
        return DeferredResultUtils.executeAsync( userManagementService.updateOrCreateUser( user ) );
    }

    @RequestMapping( value = "/{id}", method = RequestMethod.DELETE )
    public DeferredResult<RestResponse<Boolean>> remove( @PathVariable( "id" ) final String id ) {
        return DeferredResultUtils.executeAsync( userManagementService.deleteUser( id ) );
    }

    @RequestMapping( method = RequestMethod.GET, params = { "username" } )
    public DeferredResult<RestResponse<User>> findByUsername(
            @RequestParam( value = "username", required = false ) final String username ) {
        return DeferredResultUtils.executeAsyncWithOptional(
                userManagementService.findByUsername( username ) );
    }

    @RequestMapping( value = "/{id}/password", method = RequestMethod.PUT )
    public DeferredResult<RestResponse<Boolean>> findByUsername(
            @PathVariable( "id" ) final String id, @RequestBody User user ) {
        return DeferredResultUtils.executeAsync( userManagementService.updatePassword( id, user.getPassword() ) );
    }

}
