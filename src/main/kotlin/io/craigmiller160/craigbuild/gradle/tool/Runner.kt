package io.craigmiller160.craigbuild.gradle.tool

import arrow.core.Either
import arrow.core.getOrHandle
import arrow.core.sequence
import com.fasterxml.jackson.databind.ObjectMapper
import io.craigmiller160.craigbuild.gradle.plugin.model.CraigBuildProject
import io.craigmiller160.craigbuild.gradle.tool.error.UnresolvedDependencyException
import io.craigmiller160.craigbuild.gradle.tool.model.Item
import io.craigmiller160.craigbuild.gradle.tool.model.Project
import java.io.File
import org.gradle.tooling.GradleConnector
import org.gradle.tooling.model.ExternalDependency
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
  val project =
      Project(
          info =
              Item(
                  group = craigBuildProject.group,
                  name = craigBuildProject.name,
                  version = craigBuildProject.version),
          dependencies = getDependencies(ideaProject))

  val json = ObjectMapper().writeValueAsString(project)
  println(json)
}

private fun getDependencies(model: IdeaProject): List<Item> =
    model.modules.all
        .asSequence()
        .flatMap { it.dependencies.all.asSequence() }
        .filter { it is ExternalDependency }
        .map { it as ExternalDependency }
        .map { dependency ->
          dependency.gradleModuleVersion?.let { Either.Right(it) }
              ?: run {
                  println(dependency.javaClass.name)
                  Either.Left(UnresolvedDependencyException(dependency.file.absolutePath))
              }
        }
        .sequence()
        .map { dependencies ->
          dependencies.map { Item(group = it.group, name = it.name, version = it.version) }
        }
        .getOrHandle { throw it }
