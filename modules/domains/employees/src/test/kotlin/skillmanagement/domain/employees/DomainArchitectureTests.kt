package skillmanagement.domain.employees

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.core.importer.ImportOptions
import com.tngtech.archunit.library.Architectures
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import skillmanagement.test.UnitTest

@UnitTest
@TestInstance(PER_CLASS)
internal class DomainArchitectureTests {

    private val options = ImportOptions()
        .with(DoNotIncludeTests())

    private val basePackage = this::class.java.packageName

    @Test
    fun `no cyclic dependencies between packages`() {
        SlicesRuleDefinition.slices()
            .matching("(**)")
            .should().beFreeOfCycles()
            .check(classesOf(basePackage))
    }

    @Test
    fun `sub domains boundaries are respected`() {
        Architectures.layeredArchitecture()
            .layer("metrics").definedBy("$basePackage.metrics..")
            .layer("model").definedBy("$basePackage.model..")
            .layer("searchindex").definedBy("$basePackage.searchindex..")
            .layer("tasks").definedBy("$basePackage.tasks..")
            .layer("usecases").definedBy("$basePackage.usecases..")
            .whereLayer("metrics").mayNotBeAccessedByAnyLayer()
            .whereLayer("model").mayOnlyBeAccessedByLayers("searchindex", "tasks", "usecases")
            .whereLayer("searchindex").mayNotBeAccessedByAnyLayer()
            .whereLayer("tasks").mayNotBeAccessedByAnyLayer()
            .whereLayer("usecases").mayOnlyBeAccessedByLayers("tasks")
            .check(classesOf(basePackage))
    }

    private fun classesOf(vararg packages: String): JavaClasses =
        ClassFileImporter(options).importPackages(*packages)
}
