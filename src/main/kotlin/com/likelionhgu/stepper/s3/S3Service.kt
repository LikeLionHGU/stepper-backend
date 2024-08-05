package com.likelionhgu.stepper.s3

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*


@Service
class S3Service(
    private val s3Client: AmazonS3,
    private val s3Properties: S3Properties
) {

    fun upload(multipartFile: MultipartFile): String {
        val extension = multipartFile.contentType.orEmpty().substringAfterLast("/")
        val filename = multipartFile.originalFilename ?: ".".plus(extension)

        val pathname = uniquePathname(filename)
        val metadata = objectMetadata(multipartFile)

        val putObjectRequest = PutObjectRequest(
            s3Properties.bucketName,
            pathname,
            multipartFile.inputStream,
            metadata
        )
        s3Client.putObject(putObjectRequest)

        return imageUrl(pathname)
    }

    private fun objectMetadata(multipartFile: MultipartFile): ObjectMetadata {
        val metadata = ObjectMetadata().apply {
            contentType = multipartFile.contentType
            contentLength = multipartFile.size
        }
        return metadata
    }

    private fun imageUrl(pathname: String): String {
        return s3Client.getUrl(s3Properties.bucketName, pathname).toExternalForm()
    }

    private fun uniquePathname(filename: String): String {
        return "${UUID.randomUUID()}_${filename}"
    }

    companion object {
        private val logger = LoggerFactory.getLogger(S3Service::class.java)
    }
}