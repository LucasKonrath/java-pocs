package org.example.springsecurity.api;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/public/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping("/hello")
    public String hello(@AuthenticationPrincipal UserDetails user) {
        return "Hello, " + (user != null ? user.getUsername() : "anonymous");
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin(@AuthenticationPrincipal UserDetails user) {
        return "Admin area for " + (user != null ? user.getUsername() : "anonymous");
    }
}

