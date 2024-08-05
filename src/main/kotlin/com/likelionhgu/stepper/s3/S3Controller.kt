package com.likelionhgu.stepper.s3

import com.likelionhgu.stepper.s3.response.S3Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class S3Controller(private val s3Service: S3Service) {

    @PostMapping("/v1/images")
    fun uploadImage(@RequestParam file: MultipartFile): ResponseEntity<S3Response> {
        val imageUrl = s3Service.upload(file)
        return ResponseEntity.status(HttpStatus.CREATED).body(S3Response(imageUrl))
    }
}