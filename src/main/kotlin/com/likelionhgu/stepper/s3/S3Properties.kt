package com.likelionhgu.stepper.s3

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "custom.aws")
data class S3Properties(
    val accessKey: String,
    val secretAccessKey: String,
    val bucketName: String,
) {
}