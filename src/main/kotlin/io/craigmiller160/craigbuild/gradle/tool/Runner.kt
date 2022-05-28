package io.craigmiller160.craigbuild.gradle.tool

import io.craigmiller160.craigbuild.gradle.plugin.model.CraigBuildProject
import java.io.File
import org.gradle.tooling.GradleConnector
import org.gradle.tooling.model.idea.IdeaProject

fun main(args: Array<String>) {
  if (args.isEmpty()) {
    throw RuntimeException("Must provide project file path")
  }

  val (craigBuildProject, ideaProject) =
      GradleConnector.newConnector().forProjectDirectory(File(args.first())).connect().use {
          connection ->
        Pair(
            connection.getModel(CraigBuildProject::class.java),
            connection.getModel(IdeaProject::class.java))
      }
}
