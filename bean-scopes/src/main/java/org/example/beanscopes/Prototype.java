package org.example.beanscopes;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Scope("prototype")
// This annotation indicates that a new instance of this class will be created
// each time it is requested from the Spring container.
public class Prototype {
}
