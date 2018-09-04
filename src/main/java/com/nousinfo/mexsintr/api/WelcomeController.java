package com.nousinfo.mexsintr.api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
public class WelcomeController {
private static final String welcomemsg = "Welcome Mr. %s!";
    @GetMapping("/welcome/user")
    @ResponseBody
    public String welcomeUser(@RequestParam(name="name", required=false, defaultValue="Java Fan") String name) {
        return String.format(welcomemsg, name);
    }
}