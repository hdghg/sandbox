package com.mycompany.gb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RootServlet {

    @RequestMapping({"/", "index.html"})
    public ModelAndView indexController() {
        return new ModelAndView("index");
    }
}
