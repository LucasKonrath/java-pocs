package org.example.hypersqlpoc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExplorerController {

    @GetMapping("/explorer")
    public String redirectToHalExplorer() {
        return "redirect:/api/rest/explorer/index.html#uri=/api/rest";
    }
}
