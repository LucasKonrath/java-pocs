package org.example.springsetterconstructorinjection;

import org.springframework.stereotype.Service;

@Service
public class ConstructorInjection {

    private final InjectedBean injectedBean;

    public ConstructorInjection(InjectedBean injectedBean) {
        this.injectedBean = injectedBean;
    }
}
