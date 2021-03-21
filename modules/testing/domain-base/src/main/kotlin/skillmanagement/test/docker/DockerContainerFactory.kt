package skillmanagement.test.docker

interface DockerContainerFactory<T : Container> {
    fun createContainer(): T
}
