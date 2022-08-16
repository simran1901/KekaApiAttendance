package com.apps.keka.api.attendance.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import feign.FeignException;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "users-ws", fallbackFactory = UsersFallbackFactory.class)
public interface UsersServiceClient {

    @GetMapping(value = "/users/check",
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Boolean> isUser(@RequestHeader(value = "Authorization") String authHeader);

    @GetMapping(value = "/admins/check",
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Boolean> isAdmin(@RequestHeader(value = "Authorization") String authHeader);
}

@Component
class UsersFallbackFactory implements FallbackFactory<UsersServiceClient> {

    @Override
    public UsersServiceClient create(Throwable cause) {
        // TODO Auto-generated method stub
        return new UsersServiceClientFallback(cause);
    }

}

class UsersServiceClientFallback implements UsersServiceClient {

    private final Throwable cause;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public UsersServiceClientFallback(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public ResponseEntity<Boolean> isUser(String authHeader) {

        if (cause instanceof FeignException && ((FeignException) cause).status() == 404) {
            logger.error("404 error took place when isUser was called with header: " + authHeader + ". Error message: "
                    + cause.getLocalizedMessage());
        } else {
            logger.error("Other error took place: " + cause.getLocalizedMessage());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
    }

    @Override
    public ResponseEntity<Boolean> isAdmin(String authHeader) {

        if (cause instanceof FeignException && ((FeignException) cause).status() == 404) {
            logger.error("404 error took place when isUser was called with header: " + authHeader + ". Error message: "
                    + cause.getLocalizedMessage());
        } else {
            logger.error("Other error took place: " + cause.getLocalizedMessage());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
    }

}
