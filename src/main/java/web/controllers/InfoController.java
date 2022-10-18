package web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import web.service.UserService;

import java.security.Principal;

@Controller
public class InfoController {

    @Autowired
    UserService userService;

    @GetMapping("/info")
    public String info(Model model, Principal principal){

        model.addAttribute("title","Information");
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        return "info";

    }
}
