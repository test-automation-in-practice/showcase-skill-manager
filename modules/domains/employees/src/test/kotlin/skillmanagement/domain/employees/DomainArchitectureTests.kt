package skillmanagement.domain.employees

import com.tngtech.archunit.base.DescribedPredicate.alwaysTrue
import com.tngtech.archunit.core.domain.JavaClass.Predicates.type
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
import skillmanagement.domain.employees.gateways.GetProjectByIdAdapterFunction
import skillmanagement.domain.employees.gateways.GetSkillByIdAdapterFunction
import skillmanagement.test.UnitTest

@UnitTest
@TestInstance(PER_CLASS)
internal class DomainArchitectureTests {

    private val options = ImportOptions()
        .with(DoNotIncludeJars())
        .with(DoNotIncludeArchives())
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
            .layer("gateways").definedBy("$basePackage.gateways..")
            .layer("metrics").definedBy("$basePackage.metrics..")
            .layer("model").definedBy("$basePackage.model..")
            .layer("searchindex").definedBy("$basePackage.searchindex..")
            .layer("tasks").definedBy("$basePackage.tasks..")
            .layer("usecases").definedBy("$basePackage.usecases..")
            .whereLayer("gateways").mayOnlyBeAccessedByLayers("usecases")
            .whereLayer("metrics").mayNotBeAccessedByAnyLayer()
            .whereLayer("model").mayOnlyBeAccessedByLayers("gateways", "searchindex", "tasks", "usecases")
            .whereLayer("tasks").mayNotBeAccessedByAnyLayer()
            .whereLayer("usecases").mayOnlyBeAccessedByLayers("tasks")
            .check(classesOf(basePackage))
    }

    @Test
    fun `access to other domains`() {
        val domains = "skillmanagement.domain"
        Architectures.layeredArchitecture()
            .ignoreDependency(type(GetSkillByIdAdapterFunction::class.java), alwaysTrue())
            .ignoreDependency(type(GetProjectByIdAdapterFunction::class.java), alwaysTrue())
            .layer("employees").definedBy("$domains.employees..")
            .layer("projects").definedBy("$domains.projects..")
            .layer("skills").definedBy("$domains.skills..")
            .whereLayer("projects").mayNotBeAccessedByAnyLayer()
            .whereLayer("skills").mayNotBeAccessedByAnyLayer()
            .check(classesOf(domains))
    }

    private fun classesOf(vararg packages: String): JavaClasses =
        ClassFileImporter(options).importPackages(*packages)
}
