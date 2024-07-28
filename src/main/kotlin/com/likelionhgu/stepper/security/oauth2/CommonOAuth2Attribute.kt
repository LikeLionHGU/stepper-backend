package com.likelionhgu.stepper.security.oauth2

import java.io.Serializable

/**
 * Represents common attributes of an OAuth2 user
 * @property oauth2UserId (required) the unique ID of the user from the OAuth2 provider (e.g. "sub" in Google)
 * @property email (required) the email of the user
 * @property username the username of the user
 * @property picture the profile picture URL of the user
 */
interface CommonOAuth2Attribute : Serializable {
    val oauth2UserId: String
    val email: String
    val username: String?
    val picture: String?
}