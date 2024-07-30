package com.likelionhgu.stepper.security

open class SecurityException(message: String) : RuntimeException(message) {

    /**
     * Indicates that the OAuth2 provider is not supported.
     */
    class UnSupportedOAuth2ProviderException(message: String) : SecurityException(message)

    /**
     * Indicates that required attributes are missing from the OAuth2 user info.
     * @see com.likelionhgu.stepper.security.oauth2.CommonOAuth2Attribute
     */
    class MissingRequiredPropertiesException(message: String) : SecurityException(message)
}
