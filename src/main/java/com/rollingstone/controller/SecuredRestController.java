package com.rollingstone.controller;

import com.rollingstone.response.UserRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class SecuredRestController
{
    @Autowired
    Environment env;

    @GetMapping("/status/check")
    public String status() {
        return "Working on port... " + env.getProperty("local.server.port");
    }

    @Secured("ROLE_developer")
    @DeleteMapping(path = "/{id}")
    public String deleteUser(@PathVariable String id) {
        return "Deleted User with : " + id;
    }

    @Secured("ROLE_user")
    @DeleteMapping(path = "403/{id}")
    public String deleteUserWithRoleUser(@PathVariable String id) {
        return "Deleted User with : " + id;
    }

    @PreAuthorize("hasRole('developer')")
    @DeleteMapping(path = "pre/role/{id}")
    public String deleteUserWithPreRole(@PathVariable String id) {
        return "Deleted User with hasRole...: " + id;
    }

    @PreAuthorize("hasAuthority('ROLE_developer') or #id == #jwt.subject" )
    @DeleteMapping(path = "pre/auth/{id}")
    public String deleteUserWithPreAuthority(@PathVariable String id, @AuthenticationPrincipal Jwt jwt) {
        return "Deleted User with hasAuthority: " + id + "and with JWT Subject :" + jwt.getSubject();
    }

    @PostAuthorize("returnObject.userId == #jwt.subject")
    @GetMapping(path = "/post/auth/{id}")
    public UserRest getUser(@PathVariable String id, @AuthenticationPrincipal Jwt jwt) {
        return new UserRest("Sergey", "Kargopolov","17f1e2b5-39f6-4fbe-a7d3-a57c486ef50f");
    }
}
