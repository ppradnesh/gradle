/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import gradlebuild.cleanup.extension.CleanupExtension
import gradlebuild.cleanup.services.DaemonTracker
import gradlebuild.cleanup.tasks.KillLeakingJavaProcesses

plugins {
    base
}

val trackerService = gradle.sharedServices.registerIfAbsent("daemonTracker", DaemonTracker::class) {
    parameters.gradleHomeDir.fileValue(gradle.gradleHomeDir)
    parameters.rootProjectDir.set(layout.projectDirectory)
}
extensions.create<CleanupExtension>("cleanup", trackerService)

tasks.register<KillLeakingJavaProcesses>("killExistingProcessesStartedByGradle") {
    tracker.set(trackerService)
}
