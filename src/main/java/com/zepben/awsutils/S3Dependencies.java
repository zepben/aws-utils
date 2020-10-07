/*
 * Copyright 2020 Zeppelin Bend Pty Ltd
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.zepben.awsutils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.zepben.annotations.EverythingIsNonnullByDefault;

import java.util.function.Supplier;

@EverythingIsNonnullByDefault
public class S3Dependencies implements S3.Dependencies {

    @Override
    public Supplier<AmazonS3> s3ClientSupplier() {
        return AmazonS3ClientBuilder::defaultClient;
    }

}
