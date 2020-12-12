package com.zkdlu.msaa;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class TestController {
    @GetMapping
    public String index() {
        return "Hello A Service";
    }
}
