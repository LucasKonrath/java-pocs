package org.example.okhttppoc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {

    @Autowired
    private OkHttpClientService okHttpClientService;

    @RequestMapping("/test")
    public String test() {
        return okHttpClientService.doCall();
    }
}
