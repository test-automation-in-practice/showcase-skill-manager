package skillmanagement.test

import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.snippet.Snippet
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.result.ContentResultMatchersDsl

fun ContentResultMatchersDsl.relaxedJson(jsonSupplier: () -> String) = json(jsonSupplier(), false)

fun ContentResultMatchersDsl.strictJson(jsonSupplier: () -> String) = json(jsonSupplier(), true)

fun ResultActionsDsl.andDocument(identifier: String, vararg snippets: Snippet) = andDo {
    handle(document(identifier, preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), *snippets))
}
