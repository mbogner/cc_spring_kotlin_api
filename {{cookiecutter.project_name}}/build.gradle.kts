plugins {
    id("org.sonarqube")
    kotlin("jvm") apply false
    jacoco
}

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        google()
    }

    tasks {
        val javaVersion: String by System.getProperties()

        withType<JavaCompile> {
            sourceCompatibility = javaVersion
            targetCompatibility = javaVersion
        }

        withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            compilerOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.fromTarget(javaVersion)
            }
        }

        withType<Copy> {
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        }

        withType<Test> {
            useJUnitPlatform()
        }

        withType<JacocoReport> {
            reports {
                xml.required.set(true)
                html.required.set(false)
            }
        }
    }
}

sonarqube {
    properties {
        property("sonar.sourceEncoding", "UTF-8")
        property("sonar.projectKey", "{{cookiecutter.project_key}}")
        property("sonar.projectName", "{{cookiecutter.project_name}}")
        property("sonar.sources", "src/main/kotlin,src/main/resources")
        property("sonar.exclusions", "**/src/gen/**/*")
    }
}

jacoco {
    val jacocoVersion: String by System.getProperties()
    toolVersion = jacocoVersion
}
