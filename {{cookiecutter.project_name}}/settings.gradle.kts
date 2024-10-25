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
        val sonarqubeVersion: String by System.getProperties()

        id("org.springframework.boot") version springBootVersion
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.spring") version kotlinVersion
        kotlin("plugin.jpa") version kotlinVersion
        kotlin("plugin.noarg") version kotlinVersion
        id("org.openapi.generator") version openapiGeneratorVersion
        id("org.sonarqube") version sonarqubeVersion
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            // kotlin
            val kotlinVersion: String by System.getProperties()
            version("kotlin", kotlinVersion)
            library("kotlin-bom", "org.jetbrains.kotlin", "kotlin-bom").versionRef("kotlin")

            // freemarker
            val freemarkerVersion: String by System.getProperties()
            version("freemarker", freemarkerVersion)
            library("freemarker", "org.freemarker", "freemarker").versionRef("freemarker")

            // commons-io
            val commonsIoVersion: String by System.getProperties()
            version("commons-io", commonsIoVersion)
            library("commons-io", "commons-io", "commons-io").versionRef("commons-io")

            // commons-io
            val commonsPool2Version: String by System.getProperties()
            version("commons-pool2", commonsPool2Version)
            library("commons-pool2", "org.apache.commons", "commons-pool2").versionRef("commons-pool2")

            // java-otp
            val javaOtpVersion: String by System.getProperties()
            version("java-otp", javaOtpVersion)
            library("java-otp", "com.eatthepath", "java-otp").versionRef("java-otp")

            // zxing QR code
            val zxingVersion: String by System.getProperties()
            version("zxing", zxingVersion)
            library("zxing-core", "com.google.zxing", "core").versionRef("zxing")
            library("zxing-javase", "com.google.zxing", "javase").versionRef("zxing")

            // swagger
            val swaggerVersion: String by System.getProperties()
            version("swagger", swaggerVersion)
            library("swagger-annotations", "io.swagger.core.v3", "swagger-annotations").versionRef("swagger")
            library("swagger-models", "io.swagger.core.v3", "swagger-models").versionRef("swagger")
        }
    }
}