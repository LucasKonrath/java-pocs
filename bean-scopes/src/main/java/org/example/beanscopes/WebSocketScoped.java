package org.example.beanscopes;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Scope("websocket")
public class WebSocketScoped {
}
