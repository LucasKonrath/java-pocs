package com.github.diegopacheco.java.pocs.workflow.composite;

import com.github.diegopacheco.java.pocs.workflow.composite.tasks.BrewCoffee;
import com.github.diegopacheco.java.pocs.workflow.composite.tasks.GetWater;
import com.github.diegopacheco.java.pocs.workflow.composite.tasks.SelectCoffee;
import com.github.diegopacheco.java.pocs.workflow.composite.tasks.WorkflowEngineTask;

public class MainApp {
    public static void main(String[] args) {

        // Run the whole workflow
        Context ec = new Context();
        ec.set("COFFEE_TYPE","Mocha Blend");
        WorkflowEngineTask engine = new WorkflowEngineTask();
        engine.addChild(new GetWater());
        engine.addChild(new SelectCoffee());
        engine.addChild(new BrewCoffee());
        engine.execute(ec);

        // start from 2/3 of tasks
        engine.execute(ec,2);
    }
}
