package org.example.lambdapoc;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import java.util.Map;

public class ApiGatewayHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        String name = null;
        if (request != null) {
            Map<String, String> query = request.getQueryStringParameters();
            if (query != null) {
                name = query.getOrDefault("name", null);
            }
            if (name == null && request.getPathParameters() != null) {
                name = request.getPathParameters().get("name");
            }
            if (name == null && request.getBody() != null && !request.getBody().isBlank()) {
                name = request.getBody().trim();
            }
        }
        if (name == null || name.isBlank()) {
            name = "World";
        }
        String body = String.format("{\"message\":\"Hello, %s!\"}", name);
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(200);
        response.setHeaders(Map.of("Content-Type", "application/json"));
        response.setBody(body);
        return response;
    }
}

