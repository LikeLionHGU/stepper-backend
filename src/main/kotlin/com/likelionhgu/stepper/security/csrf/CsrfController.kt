package com.likelionhgu.stepper.security.csrf

import org.springframework.security.web.csrf.CsrfToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


/**
 * Serve the CSRF token to the client.
 */
@RestController
class CsrfController {

    @GetMapping("/v1/csrf")
    fun csrf(token: CsrfToken): CsrfToken {
        return token
    }
}