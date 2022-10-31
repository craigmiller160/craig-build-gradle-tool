# craig-build-gradle-tool

Used to analyze Gradle projects for the `craig-build` tool and output important project information.

## Building & Publishing

Because this is meant to support `craig-build`, it should be built manually. Use `gradle publish` to build & publish to Nexus.

## Gradle Version

The `craig-build-gradle-plugin` library's tooling API version must be kept in sync with the version of Gradle being used.