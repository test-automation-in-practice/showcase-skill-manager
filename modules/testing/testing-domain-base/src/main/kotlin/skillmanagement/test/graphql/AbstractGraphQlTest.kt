package skillmanagement.test.graphql

import org.springframework.core.io.ClassPathResource
import org.springframework.graphql.test.tester.GraphQlTester
import skillmanagement.test.readText

abstract class AbstractGraphQlTest {

    protected abstract val graphQlTester: GraphQlTester

    protected fun assertRequestResponse(documentPath: String, responsePath: String) {
        graphQlTester.document(ClassPathResource(documentPath).readText()).execute()
            .errors().verify()
            .path("$").matchesJsonStrictly(ClassPathResource(responsePath).readText())
    }

}
