package io.craigmiller160.craigbuild.gradle.tool.error

class UnresolvedDependencyException(msg: String) : RuntimeException("Unable to resolve dependency: $msg")
