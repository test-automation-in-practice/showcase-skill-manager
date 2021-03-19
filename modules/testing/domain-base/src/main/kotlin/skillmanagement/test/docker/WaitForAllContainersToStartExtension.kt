package skillmanagement.test.docker

import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext

internal class WaitForAllContainersToStartExtension : BeforeAllCallback {
    override fun beforeAll(context: ExtensionContext) {
        context.containers.forEach { it.getContainer() }
    }
}
