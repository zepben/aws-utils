/*
 * Copyright 2020 Zeppelin Bend Pty Ltd
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.zepben.awsutils

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.ObjectMetadata
import com.zepben.testutils.hamcrest.InputStreamMatcher.Companion.matchesContent
import io.mockk.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import java.io.InputStream

class S3Test {

    private val s3Client = mockk<AmazonS3Client> { every { putObject(any(), any(), any(), any()) } returns mockk() }
    private val s3 = S3(s3Client)

    @Test
    fun putObject() {
        s3.putObject("bucket", "key", "content")
        validateRequest("bucket", "key", "content", "text/plain")

        s3.putObject("bucket", "key.", "content")
        validateRequest("bucket", "key.", "content", "text/plain")

        s3.putObject("bucket", ".key", "content")
        validateRequest("bucket", ".key", "content", "text/plain")

        s3.putObject("bucket2", "key.csv", "content2")
        validateRequest("bucket2", "key.csv", "content2", "text/csv")

        s3.putObject("bucket2", "key.with.dots.csv", "content2")
        validateRequest("bucket2", "key.with.dots.csv", "content2", "text/csv")

        s3.putObject("bucket2", "key.ext", "content2")
        validateRequest("bucket2", "key.ext", "content2", "text/ext")
    }

    private fun validateRequest(expectedBucketName: String, expectedKey: String, expectedContent: String, expectedContentType: String) {
        val bucketName = slot<String>()
        val key = slot<String>()
        val inputStream = slot<InputStream>()
        val metadata = slot<ObjectMetadata>()

        verify { s3Client.putObject(capture(bucketName), capture(key), capture(inputStream), capture(metadata)) }
        clearMocks(s3Client, answers = false)

        assertThat(bucketName.captured, equalTo(expectedBucketName))
        assertThat(key.captured, equalTo(expectedKey))
        assertThat(inputStream.captured, matchesContent(expectedContent))
        assertThat(metadata.captured.contentType, equalTo(expectedContentType))
    }

}
