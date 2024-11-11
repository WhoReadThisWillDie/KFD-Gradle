import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.internal.impldep.com.fasterxml.jackson.databind.ObjectMapper
import java.io.File

abstract class AnalyzeCodeTask : DefaultTask() {
    private val reportJson = File("report.json")
    private var classes = 0
    private var methods = 0
    private var lines = 0

    @TaskAction
    fun generateReport() {
        val startDir = File(project.rootDir, "src/main")

        startDir.walkTopDown().filter { it.isFile && (it.extension == "kt" || it.extension == "java") }
            .forEach { file ->
                analyze(file)
            }

        val report = Report(classes, methods, lines)
        reportJson.writeText(ObjectMapper().writeValueAsString(report))
    }

    private fun analyze(file: File) {
        lines += file.readLines().size

        val inputStream = CharStreams.fromFileName(file.absolutePath)

        val tokens = when (file.extension) {
            "java" -> org.antlr.v4.runtime.CommonTokenStream(JavaLexer(inputStream))
            "kt" -> CommonTokenStream(KotlinLexer(inputStream))
            else -> throw IllegalArgumentException()
        }

        val tree = when (file.extension) {
            "java" -> JavaParser(tokens).compilationUnit()
            "kt" -> KotlinParser(tokens).kotlinFile()
            else -> throw IllegalArgumentException()
        }

        val listener = when (file.extension) {
            "java" -> JavaCodeAnalyzerListener()
            "kt" -> KotlinCodeAnalyzerListener()
            else -> throw IllegalArgumentException()
        }

        val walker = ParseTreeWalker()
        walker.walk(listener, tree)
    }

    private inner class JavaCodeAnalyzerListener : JavaParserBaseListener() {
        override fun enterClassDeclaration(ctx: JavaParser.ClassDeclarationContext?) {
            classes++
        }

        override fun enterMethodDeclaration(ctx: JavaParser.MethodDeclarationContext?) {
            methods++
        }
    }

    private inner class KotlinCodeAnalyzerListener : KotlinParserBaseListener() {
        override fun enterClassDeclaration(ctx: KotlinParser.ClassDeclarationContext?) {
            classes++
        }

        override fun enterFunctionDeclaration(ctx: KotlinParser.FunctionDeclarationContext?) {
            methods++
        }
    }

    private data class Report(
        val totalClasses: Int,
        val totalMethods: Int,
        val totalLines: Int
    )
}
