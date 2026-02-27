import com.smushytaco.lwjgl_gradle.Preset
import com.smushytaco.offline_repo.ExportMavenRepoTask
plugins {
    alias(libs.plugins.loom)
    alias(libs.plugins.lwjgl)
    java
}
val modVersion: Provider<String> = providers.gradleProperty("mod_version")
val modGroup: Provider<String> = providers.gradleProperty("mod_group")
val modName: Provider<String> = providers.gradleProperty("mod_name")

val javaVersion: Provider<Int> = libs.versions.java.map { it.toInt() }

base.archivesName = modName
group = modGroup.get()
version = modVersion.get()
loom {
    customMinecraftMetadata.set("https://downloads.betterthanadventure.net/bta-client/${libs.versions.btaChannel.get()}/v${libs.versions.bta.get()}/manifest.json")
    accessWidenerPath = file("src/main/resources/quitebetter.classtweaker")
}
val localRepository = layout.projectDirectory.dir("offline-maven-repo").asFile
repositories {
    if (localRepository.exists()) maven(localRepository)
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://maven.fabricmc.net/") { name = "Fabric" }
    maven("https://maven.thesignalumproject.net/infrastructure") { name = "SignalumMavenInfrastructure" }
    maven("https://maven.thesignalumproject.net/releases") { name = "SignalumMavenReleases" }
    maven("https://maven.bawnorton.com/releases")
    ivy("https://github.com/Better-than-Adventure") {
        patternLayout { artifact("[organisation]/releases/download/v[revision]/[module].jar") }
        metadataSources { artifact() }
    }
    ivy("https://github.com/Testure/modnametooltip") {
        patternLayout { artifact("releases/download/v[revision]/[module]-[revision](-[classifier]).jar") }
        metadataSources { artifact() } // no ivy.xml / pom
    }
    ivy("https://downloads.betterthanadventure.net/bta-client/${libs.versions.btaChannel.get()}/") {
        patternLayout { artifact("/v[revision]/client.jar") }
        metadataSources { artifact() }
    }
    ivy("https://downloads.betterthanadventure.net/bta-server/${libs.versions.btaChannel.get()}/") {
        patternLayout { artifact("/v[revision]/server.jar") }
        metadataSources { artifact() }
    }
    ivy("https://piston-data.mojang.com") {
        patternLayout { artifact("v1/[organisation]/[revision]/[module].jar") }
        metadataSources { artifact() }
    }
}
lwjgl {
    version = libs.versions.lwjgl
    implementation(Preset.MINIMAL_OPENGL)
}
val offlineVendor by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}
dependencies {
    minecraft("::${libs.versions.bta.get()}")

    offlineVendor(libs.halplibe)
    offlineVendor(libs.btwaila)
    offlineVendor(libs.modnametooltip)
    offlineVendor(libs.mixinSquared)

    runtimeOnly(libs.clientJar)
    implementation(libs.loader)
    implementation(libs.halplibe)
    implementation(libs.modMenu)
    implementation(libs.legacyLwjgl)
    implementation(libs.btwaila)
    implementation(libs.modnametooltip)

    implementation(libs.slf4jApi)
    implementation(libs.guava)
    implementation(libs.log4j.slf4j2.impl)
    implementation(libs.log4j.core)
    implementation(libs.log4j.api)
    implementation(libs.log4j.api12)
    implementation(libs.gson)

    implementation(libs.commonsLang3)
    implementation(libs.mixinSquared)

    include(libs.commonsLang3)
    include(libs.mixinSquared)

    annotationProcessor(libs.mixinSquared)
}
java {
    toolchain {
        languageVersion = javaVersion.map { JavaLanguageVersion.of(it) }
        vendor = JvmVendorSpec.ADOPTIUM
    }
    sourceCompatibility = JavaVersion.toVersion(javaVersion.get())
    targetCompatibility = JavaVersion.toVersion(javaVersion.get())
    withSourcesJar()
}
val licenseFile = run {
    val rootLicense = layout.projectDirectory.file("LICENSE")
    val parentLicense = layout.projectDirectory.file("../LICENSE")
    when {
        rootLicense.asFile.exists() -> {
            logger.lifecycle("Using LICENSE from project root: {}", rootLicense.asFile)
            rootLicense
        }
        parentLicense.asFile.exists() -> {
            logger.lifecycle("Using LICENSE from parent directory: {}", parentLicense.asFile)
            parentLicense
        }
        else -> {
            logger.warn("No LICENSE file found in project or parent directory.")
            null
        }
    }
}
tasks {
    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        sourceCompatibility = javaVersion.get().toString()
        targetCompatibility = javaVersion.get().toString()
        if (javaVersion.get() > 8) options.release = javaVersion
    }
    named<UpdateDaemonJvm>("updateDaemonJvm") {
        languageVersion = libs.versions.gradleJava.map { JavaLanguageVersion.of(it.toInt()) }
        vendor = JvmVendorSpec.ADOPTIUM
    }
    withType<JavaExec>().configureEach { defaultCharacterEncoding = "UTF-8" }
    withType<Javadoc>().configureEach { options.encoding = "UTF-8" }
    withType<Test>().configureEach { defaultCharacterEncoding = "UTF-8" }
    withType<Jar>().configureEach {
        licenseFile?.let {
            from(it) {
                rename { original -> "${original}_${archiveBaseName.get()}" }
            }
        }
    }
    processResources {
        val resourceMap = mapOf(
            "version" to modVersion.get(),
            "fabricloader" to libs.versions.loader.get(),
            "halplibe" to libs.versions.halplibe.get(),
            "java" to libs.versions.java.get(),
            "modmenu" to libs.versions.modMenu.get()
        )
        inputs.properties(resourceMap)
        filesMatching("fabric.mod.json") { expand(resourceMap) }
        filesMatching("**/*.mixins.json") { expand(resourceMap.filterKeys { it == "java" }) }
    }
}

val exportOfflineRepo = tasks.register<ExportMavenRepoTask>("exportOfflineRepo") {
    group = "distribution"
    description = "Exports all resolved dependencies (including -sources jars when available) into a local Maven repo."
    outputDir.set(localRepository)
    val artifactView = offlineVendor.incoming.artifactView {}
    val sourcesProvider = providers.provider {
        val meta = mutableListOf<String>()
        val extraSourceFiles = mutableListOf<File>()
        artifactView.artifacts.forEach { ar ->
            val file = ar.file
            val compId = ar.id.componentIdentifier
            if (compId is ModuleComponentIdentifier) {
                val group = compId.group
                val module = compId.module
                val version = compId.version
                val ext = file.extension.ifBlank { "jar" }
                val filename = file.name
                val base = filename.removeSuffix(".$ext")
                val prefix = "$module-$version"
                val classifier = when {
                    base == prefix -> ""
                    base.startsWith("$prefix-") -> base.substring(prefix.length + 1)
                    else -> ""
                }
                meta += listOf(group, module, version, classifier, ext, filename).joinToString("|")
                try {
                    val sourcesNotation = "$group:$module:$version:sources@jar"
                    val srcCfg = configurations.detachedConfiguration(dependencies.create(sourcesNotation))
                    srcCfg.isCanBeResolved = true
                    val resolvedSources = srcCfg.resolve()
                    resolvedSources.forEach { sf ->
                        val sExt = sf.extension.ifBlank { "jar" }
                        val sFilename = sf.name
                        val sClassifier = "sources"
                        meta += listOf(group, module, version, sClassifier, sExt, sFilename).joinToString("|")
                        extraSourceFiles += sf
                    }
                } catch (_: Exception) {
                    logger.lifecycle("No -sources found for {}:{}:{}", group, module, version)
                }
            } else {
                val filename = ar.file.name
                meta += listOf("local", filename.removeSuffix(".jar"), "1.0", "", "jar", filename).joinToString("|")
            }
        }
        Pair(meta.toList(), extraSourceFiles.toList())
    }
    artifactFiles.from(artifactView.files, providers.provider { sourcesProvider.get().second })
    artifactMetadata.set(providers.provider { sourcesProvider.get().first })
}
// Removes LWJGL2 dependencies
configurations.configureEach { exclude(group = "org.lwjgl.lwjgl") }
