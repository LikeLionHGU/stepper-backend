package com.likelionhgu.stepper.test

import com.likelionhgu.stepper.security.oauth2.CommonOAuth2Attribute
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DemoController {

    @GetMapping("/")
    fun hello(@AuthenticationPrincipal user: CommonOAuth2Attribute): String {
        return "Hello, ${user.username ?: user.email}!"
    }

    @PostMapping("/test")
    fun test(): String {
        return "Test"
    }
}