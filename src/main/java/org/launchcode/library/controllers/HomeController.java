package org.launchcode.library.controllers;

import org.springframework.web.bind.annotation.GetMapping;

public class HomeController {

    @GetMapping("library")
    public String index () {
        return "library/index";
    }
}
