/*
 * Copyright 2020 Zeppelin Bend Pty Ltd
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.zepben.awsutils;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.zepben.annotations.EverythingIsNonnullByDefault;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

@EverythingIsNonnullByDefault
@SuppressWarnings({"WeakerAccess", "UnusedReturnValue"})
public class S3 {

    private final AmazonS3 s3Client;

    public S3(Dependencies dependencies) {
        this.s3Client = dependencies.s3ClientSupplier().get();
    }

    public PutObjectResult putObject(String bucketName, String key, String content) throws SdkClientException {
        ObjectMetadata metadata = new ObjectMetadata();

        int index = key.lastIndexOf('.');
        if ((index > 0) && (index < key.length() - 1))
            metadata.setContentType("text/" + key.substring(index + 1));
        else
            metadata.setContentType("text/plain");

        metadata.setContentLength(content.length());

        return putObject(bucketName, key, new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)), metadata);
    }

    public PutObjectResult putObject(String bucketName, String key, InputStream content, ObjectMetadata metadata) throws SdkClientException {
        return s3Client.putObject(bucketName, key, content, metadata);
    }

    public interface Dependencies {

        Supplier<AmazonS3> s3ClientSupplier();

    }

}
