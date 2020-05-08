package skillmanagement

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeArchives
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeJars
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.core.importer.ImportOptions
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import skillmanagement.test.UnitTest

@UnitTest
internal class ArchitectureTests {

    // General Setup
    private val basePackage = this::class.java.packageName
    private val options = ImportOptions()
        .with(DoNotIncludeJars())
        .with(DoNotIncludeArchives())
        .with(DoNotIncludeTests())
    private val classes: JavaClasses = ClassFileImporter(options).importPackages(basePackage)

    // Rules
    val noCyclicDependencies = SlicesRuleDefinition.slices()
        .matching("(**)")
        .should().beFreeOfCycles()

    @TestFactory
    fun `architecture rules are followed`(): List<DynamicTest> = listOf(
        "no cyclic dependencies between packages" to noCyclicDependencies
    ).map { (name, rule) -> dynamicTest(name) { rule.check(classes) } }

}
