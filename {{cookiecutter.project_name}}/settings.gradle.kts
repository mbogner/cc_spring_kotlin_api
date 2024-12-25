rootProject.name = "{{cookiecutter.project_name}}"

val projectModules = mapOf(
    "api" to "api",
).forEach {
    include(it.key)
    project(":${it.key}").projectDir = file(it.value)
}

pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        mavenCentral()
    }

    plugins {
        val springBootVersion: String by System.getProperties()
        id("org.springframework.boot") version springBootVersion

        val kotlinVersion: String by System.getProperties()
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.spring") version kotlinVersion
        kotlin("plugin.jpa") version kotlinVersion
        kotlin("plugin.noarg") version kotlinVersion

        val openapiGeneratorVersion: String by System.getProperties()
        id("org.openapi.generator") version openapiGeneratorVersion

        val openapiMergerVersion: String by System.getProperties()
        id("com.rameshkp.openapi-merger-gradle-plugin") version openapiMergerVersion

        val sonarqubeVersion: String by System.getProperties()
        id("org.sonarqube") version sonarqubeVersion

        val gradleReleasePluginVersion: String by System.getProperties()
        id("net.researchgate.release") version gradleReleasePluginVersion
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            val bomVersion: String by System.getProperties()
            library("bom", "dev.mbo", "spring-boot-bom").version(bomVersion)

            val libraryBomVersion: String by System.getProperties()
            library("library-bom", "dev.mbo", "library-bom").version(libraryBomVersion)
        }
    }
}