package org.example.groovytemplatespoc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "Hello from Groovy Template!");
        model.addAttribute("date", LocalDate.now());
        List<String> items = Arrays.asList("Apple", "Banana", "Cherry");
        model.addAttribute("items", items);
        return "home";
    }

    @PostMapping("/greet")
    public String greet(@RequestParam String name, Model model) {
        model.addAttribute("message", "Hello, " + name + "!");
        model.addAttribute("date", LocalDate.now());
        List<String> items = Arrays.asList("Apple", "Banana", "Cherry");
        model.addAttribute("items", items);
        return "home";
    }
}
