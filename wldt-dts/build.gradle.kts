/*
 * Copyright (c) 2023. Andrea Giulianelli
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    application
    alias(libs.plugins.java.qa)
}

group = "io.github.wodt"

val ghPackageUsername: String by project
val ghPackagesPwd: String by project

repositories {
    mavenCentral()
    maven {
        url = uri("https://git.informatik.uni-hamburg.de/api/v4/groups/sane-public/-/packages/maven")
    }
    maven {
        url = uri("https://maven.pkg.github.com/Web-of-Digital-Twins/wldt-wodt-adapter")
        credentials {
            username = project.findProperty("ghPackagesUsername")?.toString() ?: ghPackageUsername
            password = project.findProperty("ghPackagesPwd")?.toString() ?: ghPackagesPwd
        }
    }
}

dependencies {
    implementation(libs.wodt.wldt)
    testImplementation(libs.bundles.java.testing)
    testRuntimeOnly(libs.junit.engine)
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        showStandardStreams = true
        showCauses = true
        showStackTraces = true
        events(*org.gradle.api.tasks.testing.logging.TestLogEvent.values())
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

application {
    mainClass.set("io.github.wodt.Launcher")
}
