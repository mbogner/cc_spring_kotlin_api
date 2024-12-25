import java.io.ByteArrayOutputStream
import java.util.Properties

plugins {
    id("org.springframework.boot")
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    id("org.openapi.generator")
    id("com.rameshkp.openapi-merger-gradle-plugin")
    jacoco
}

dependencies {
    implementation(platform(libs.bom))
    implementation(platform(libs.library.bom))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("io.swagger.core.v3:swagger-annotations")
    implementation("io.swagger.core.v3:swagger-models")

    // test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.testcontainers:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

val javaVersion: String by System.getProperties()

tasks {
    withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
        this.archiveFileName.set("${archiveBaseName.get()}.${archiveExtension.get()}")
    }

    register<Copy>("copyGradlePropertiesToClasspath") {
        group = "documentation"
        from("$rootDir/gradle.properties")
        into(layout.buildDirectory.dir("resources/main"))
    }

    val createProperties by registering {
        group = "documentation"
        doLast {
            val propertiesFile = layout.buildDirectory.file("resources/main/git.properties").get().asFile
            propertiesFile.parentFile.mkdirs()

            val properties = Properties()
            properties.setProperty("commitFull", getCommitHash())

            propertiesFile.writer().use { writer ->
                properties.store(writer, null)
            }
        }
    }

    named("processResources") {
        dependsOn("copyGradlePropertiesToClasspath")
        dependsOn(createProperties)
    }
}

fun getCommitHash(): String {
    return try {
        val stdout = ByteArrayOutputStream()
        exec {
            commandLine("git", "rev-parse", "HEAD")
            standardOutput = stdout
        }
        stdout.toString().trim()
    } catch (e: Exception) {
        "Unknown" // Fallback in case Git is not available or there's an error
    }
}

// ---------------------------

val openAPIBasePackage = "{{cookiecutter.package}}"
val openAPISpecParts: String by System.getProperties()
val openAPISpecFilePath: String by System.getProperties()
val openAPIGenOutBase: String by System.getProperties()

val openAPISpecFileNamePrefix: String by System.getProperties()
val openAPISpecFileExt: String by System.getProperties()
val openAPISpecFileName = "${openAPISpecFileNamePrefix}${project.name}"
val openAPISpecFile = "${projectDir.path}/src/main/resources/$openAPISpecFileName.$openAPISpecFileExt"
val openApiSrcDir = "${layout.buildDirectory.get()}/$openAPIGenOutBase/src/main/kotlin"

sourceSets {
    main {
        java {
            srcDirs("src/main/kotlin", openApiSrcDir)
        }
        resources {
            srcDirs("src/main/resources")
        }
    }
}

// config needs to be above any tasks referencing it
openApiMerger {
    inputDirectory.set(file("${projectDir.path}/${openAPISpecParts}"))
    output {
        fileName.set(openAPISpecFileName)
        fileExtension.set(openAPISpecFileExt)
        directory.set(file("${projectDir.path}/src/main/resources"))
    }
    openApi {
        openApiVersion.set("3.0.3")
        info {
            title.set("{{cookiecutter.project_name}} API")
            description.set("{{cookiecutter.project_name}} API")
            version.set("v1")
        }
        servers {
            register("dev") {
                url.set("https://localhost:{{cookiecutter.api_port}}")
                description.set("dev")
            }
        }
    }
}

openApiGenerate {
    // https://openapi-generator.tech/docs/generators/kotlin-spring
    generatorName.set("kotlin-spring")
    library.set("spring-boot")
    templateDir.set("$rootDir/shared/templates/openapi")

    inputSpec.set(openAPISpecFile)
    outputDir.set("${layout.buildDirectory.get()}/$openAPIGenOutBase")

    packageName.set(openAPIBasePackage)
    apiPackage.set("$openAPIBasePackage.api")
    invokerPackage.set("$openAPIBasePackage.client")
    modelPackage.set("$openAPIBasePackage.model")

    modelNameSuffix.set("Dto")
    typeMappings.set(
        mapOf(
            "DateTime" to "Instant"
        )
    )
    importMappings.set(
        mapOf(
            "Instant" to "java.time.Instant"
        )
    )
    configOptions.set(
        mapOf(
            "useBeanValidation" to "true",
            "delegatePattern" to "true",
            "useTags" to "true",
            "exceptionHandler" to "false",
            "gradleBuildFile" to "false",
            "reactive" to "false",
            "serviceInterface" to "false",
            "sortModelPropertiesByRequiredFlag" to "true",
            "sortParamsByRequiredFlag" to "true",
            "swaggerAnnotations" to "true",
            "useSpringBoot3" to "true",
        )
    )
}

val openApiClean = tasks.register<Delete>("openApiClean") {
    group = "openapi tools"
    description = "clean generated api code"
    delete("$openApiSrcDir/${openAPIBasePackage.replace(".", "/")}")
}

val openApiMergeTask = tasks.getByName("mergeOpenApiFiles")
val openApiGenerateTask = tasks.getByName("openApiGenerate")
openApiGenerateTask.dependsOn(openApiMergeTask, openApiClean)

// get rid of deprecation warning that kaptGenerateStubsKotlin
// depends on openApiGenerate but doesn't declare a dependency
val compileKotlinTask = tasks.getByName("compileKotlin")
compileKotlinTask.dependsOn(openApiGenerateTask)

val processResourcesTask = tasks.getByName("processResources")
processResourcesTask.dependsOn(openApiGenerateTask)
