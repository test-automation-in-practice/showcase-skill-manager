package skillmanagement.test.docker

import org.junit.jupiter.api.extension.ExtensionContext
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

/**
 * A [CloseableResource] wrapping a [Container].
 */
internal class ContainerResource<T : Container>(
    private val futureContainer: CompletableFuture<T>
) : ExtensionContext.Store.CloseableResource {

    override fun close() {
        getContainer().close()
    }

    fun getContainer(): T = futureContainer.get(1, TimeUnit.MINUTES)

}
