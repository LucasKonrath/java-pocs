package org.example.springboot4poc;

import org.springframework.beans.factory.BeanRegistrar;
import org.springframework.beans.factory.BeanRegistry;
import org.springframework.core.env.Environment;

import java.util.UUID;

public class BeanRegistrarExample implements BeanRegistrar {
    @Override
    public void register(BeanRegistry registry, Environment env) {
        registry.registerBean(ExampleBean.class, (spec) -> {
            spec.supplier((ctx) -> new ExampleBean())
                .description("This is an example bean registered by BeanRegistrarExample")
                .prototype()
                .lazyInit();
        });
    }
}
