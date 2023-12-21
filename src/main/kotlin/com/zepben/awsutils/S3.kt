/*
 * Copyright 2020 Zeppelin Bend Pty Ltd
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.zepben.awsutils

import com.amazonaws.SdkClientException
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectResult
import java.io.ByteArrayInputStream
import java.io.InputStream

class S3(
    private val s3Client: AmazonS3 = AmazonS3ClientBuilder.defaultClient()
) {

    @Throws(SdkClientException::class)
    fun putObject(bucketName: String, key: String, content: String): PutObjectResult {
        val metadata = ObjectMetadata().apply {
            contentType = key.toContentType()
            contentLength = content.length.toLong()
        }

        return putObject(bucketName, key, ByteArrayInputStream(content.toByteArray()), metadata)
    }

    @Throws(SdkClientException::class)
    fun putObject(bucketName: String, key: String, content: InputStream, metadata: ObjectMetadata): PutObjectResult =
        s3Client.putObject(bucketName, key, content, metadata)

    private fun String.toContentType(): String {
        // We add 1 to the index of the last `.` as we only want the extension.
        val index = lastIndexOf('.') + 1

        // We do not want to process items when the last `.` is actually the first character. i.e. "hidden" files.
        val type = if (index in 2..<length) substring(index) else "plain"

        return "text/$type"
    }

}
