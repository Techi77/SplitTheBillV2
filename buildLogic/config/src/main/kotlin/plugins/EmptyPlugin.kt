package plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

class EmptyPlugin : Plugin<Project> {
    override fun apply(target: Project) {} // пустой, т.к. нужен для структуры
}