package skillmanagement

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeArchives
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeJars
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
internal class ArchitectureTests {

    // General Setup
    private val basePackage = this::class.java.packageName
    private val options = ImportOptions()
        .with(DoNotIncludeJars())
        .with(DoNotIncludeArchives())
        .with(DoNotIncludeTests())
    private val allClasses: JavaClasses = ClassFileImporter(options).importPackages(basePackage)
    private val domainClasses: JavaClasses = ClassFileImporter(options).importPackages("$basePackage.domain")

    @Test
    fun `no cyclic dependencies between packages`() {
        SlicesRuleDefinition.slices()
            .matching("(**)")
            .should().beFreeOfCycles()
            .check(allClasses)
    }

    @Test
    fun `sub domains boundaries are respected`() {
        Architectures.layeredArchitecture()
            .layer("employees").definedBy("$basePackage.domain.employees..")
            .layer("projects").definedBy("$basePackage.domain.projects..")
            .layer("skills").definedBy("$basePackage.domain.skills..")
            .whereLayer("projects").mayOnlyBeAccessedByLayers("employees")
            .whereLayer("skills").mayOnlyBeAccessedByLayers("employees")
            .whereLayer("employees").mayNotBeAccessedByAnyLayer()
            .check(domainClasses)
    }

}
