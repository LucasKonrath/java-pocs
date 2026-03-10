package org.example.springsetterconstructorinjection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FieldInjection {

    @Autowired
    private InjectedBean injectedBean;

}
