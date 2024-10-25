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
        val kotlinVersion: String by System.getProperties()
        val springBootVersion: String by System.getProperties()
        val openapiGeneratorVersion: String by System.getProperties()
        val openapiMergerVersion: String by System.getProperties()
        val sonarqubeVersion: String by System.getProperties()

        id("org.springframework.boot") version springBootVersion
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.spring") version kotlinVersion
        kotlin("plugin.jpa") version kotlinVersion
        kotlin("plugin.noarg") version kotlinVersion
        id("org.openapi.generator") version openapiGeneratorVersion
        id("com.rameshkp.openapi-merger-gradle-plugin") version openapiMergerVersion
        id("org.sonarqube") version sonarqubeVersion
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            val bomVersion: String by System.getProperties()
            version("bom", bomVersion)
            library("bom", "dev.mbo", "spring-boot-bom").versionRef("bom")
        }
    }
}