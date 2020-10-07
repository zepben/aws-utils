/*
 * Copyright 2020 Zeppelin Bend Pty Ltd
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.zepben.awsutils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.InputStream;

import static com.zepben.testutils.hamcrest.InputStreamMatcher.matchesContent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

public class S3Test {

    private final S3.Dependencies dependencies = new MockS3Dependencies();
    private final AmazonS3 s3Client = dependencies.s3ClientSupplier().get();
    private final S3 s3 = new S3(dependencies);

    @Test
    public void putObject() {
        s3.putObject("bucket", "key", "content");
        validateRequest("bucket", "key", "content", "text/plain");

        s3.putObject("bucket", "key.", "content");
        validateRequest("bucket", "key.", "content", "text/plain");

        s3.putObject("bucket", ".key", "content");
        validateRequest("bucket", ".key", "content", "text/plain");

        s3.putObject("bucket2", "key.csv", "content2");
        validateRequest("bucket2", "key.csv", "content2", "text/csv");

        s3.putObject("bucket2", "key.with.dots.csv", "content2");
        validateRequest("bucket2", "key.with.dots.csv", "content2", "text/csv");

        s3.putObject("bucket2", "key.ext", "content2");
        validateRequest("bucket2", "key.ext", "content2", "text/ext");
    }

    private void validateRequest(String expectedBucketName, String expectedKey, String expectedContent, String expectedContentType) {
        ArgumentCaptor<String> bucketNameArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> keyArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<InputStream> inputStreamArgumentCaptor = ArgumentCaptor.forClass(InputStream.class);
        ArgumentCaptor<ObjectMetadata> metadataArgumentCaptor = ArgumentCaptor.forClass(ObjectMetadata.class);

        verify(s3Client, times(1)).putObject(bucketNameArgumentCaptor.capture(), keyArgumentCaptor.capture(), inputStreamArgumentCaptor.capture(), metadataArgumentCaptor.capture());
        clearInvocations(s3Client);

        String bucketName = bucketNameArgumentCaptor.getValue();
        String key = keyArgumentCaptor.getValue();
        InputStream inputStream = inputStreamArgumentCaptor.getValue();
        ObjectMetadata metadata = metadataArgumentCaptor.getValue();

        assertThat(bucketName, equalTo(expectedBucketName));
        assertThat(key, equalTo(expectedKey));
        assertThat(inputStream, matchesContent(expectedContent));
        assertThat(metadata.getContentType(), equalTo(expectedContentType));
    }

}
