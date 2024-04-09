# AWS Utils changelog

## [1.2.0] - UNRELEASED

### Breaking Changes

* Converted to Kotlin.
* Removed `S3.Dependencies` and `S3Dependencies` in favour of direct constructor injection of the S3 client.
* Updated to latest super pom (0.36.0)

### New Features

* None.

### Enhancements

* Update `aws-java-sdk-s3` to `1.12.599`, resolving transient vulnerability
  [CVE-2022-31159](https://nvd.nist.gov/vuln/detail/CVE-2022-31159).

### Fixes

* None.

### Notes

* None.

## [1.1.0]

Initial open source release of aws-utils.
