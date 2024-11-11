import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

abstract class CodeAnalyzerPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.tasks.register("analyzeCode", AnalyzeCodeTask::class.java)
    }
}