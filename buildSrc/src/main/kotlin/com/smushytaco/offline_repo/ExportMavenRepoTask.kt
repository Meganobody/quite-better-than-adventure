package com.smushytaco.offline_repo

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.*
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

@CacheableTask
abstract class ExportMavenRepoTask : DefaultTask() {

    /** Artifact metadata strings in the form: group|artifact|version|classifier|extension|filename */
    @get:Input
    abstract val artifactMetadata: ListProperty<String>

    /** The actual artifact files matching the metadata above (input files) */
    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val artifactFiles: ConfigurableFileCollection

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun produceRepo() {
        val out = outputDir.get().asFile
        if (out.exists()) out.deleteRecursively()
        out.mkdirs()

        // Build a map from filename -> File (artifactFiles contains all files)
        val fileMap = artifactFiles.files.associateBy { it.name }

        artifactMetadata.get().forEach { meta ->
            // meta: "group|artifact|version|classifier|ext|filename"
            val parts = meta.split("|")
            if (parts.size < 6) {
                logger.warn("Skipping invalid metadata entry: {}", meta)
                return@forEach
            }
            val group = parts[0]
            val artifactId = parts[1]
            val version = parts[2]
            val classifier = parts[3].nullIfBlank()
            val ext = parts[4]
            val filename = parts[5]

            val sourceFile = fileMap[filename]
            if (sourceFile == null || !sourceFile.exists()) {
                logger.warn("Artifact file referenced by metadata not found: {}", filename)
                return@forEach
            }

            // Build maven layout target dir
            val groupPath = group.replace('.', File.separatorChar)
            val targetDir = File(out, "$groupPath${File.separator}$artifactId${File.separator}$version")
            targetDir.mkdirs()

            // target artifact file name
            val baseName = if (classifier.isNullOrEmpty()) "$artifactId-$version" else "$artifactId-$version-$classifier"
            val targetArtifact = File(targetDir, "$baseName.$ext")

            logger.lifecycle("Writing artifact {} -> {}", sourceFile.name, targetArtifact.absolutePath)
            Files.copy(sourceFile.toPath(), targetArtifact.toPath(), StandardCopyOption.REPLACE_EXISTING)

            // If POM doesn't exist, create a minimal POM that includes coordinates.
            val pomFile = File(targetDir, "$artifactId-$version.pom")
            if (!pomFile.exists()) {
                pomFile.writeText(
                    """<?xml version="1.0" encoding="UTF-8"?>
                    |<project xmlns="http://maven.apache.org/POM/4.0.0"
                    |         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                    |         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                    |         http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    |  <modelVersion>4.0.0</modelVersion>
                    |  <groupId>$group</groupId>
                    |  <artifactId>$artifactId</artifactId>
                    |  <version>$version</version>
                    |</project>
                    """.trimMargin()
                )
            }
        }

        logger.lifecycle("Local maven repo created at: {}", out.absolutePath)
    }

    private fun String.nullIfBlank(): String? = if (this.isBlank()) null else this
}
