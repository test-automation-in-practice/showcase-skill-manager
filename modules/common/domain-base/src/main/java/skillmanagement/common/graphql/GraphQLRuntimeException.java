package skillmanagement.common.graphql;

import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.Collections;
import java.util.List;

public abstract class GraphQLRuntimeException extends RuntimeException implements GraphQLError {

    protected GraphQLRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public List<SourceLocation> getLocations() {
        return Collections.emptyList();
    }

}
