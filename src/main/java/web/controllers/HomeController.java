package web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import web.models.User;
import web.service.UserService;

import java.security.Principal;

@Controller
public class HomeController {

    @Autowired
    UserService userService;

    @GetMapping("/")
    public String home(Model model, Principal principal){
        model.addAttribute("title","Home Page");

        User user = new User();
        if(principal==null){
           user= userService.createGuest();
        }
        else user =userService.getUserByPrincipal(principal);

        model.addAttribute("user", user);
        return "home";
    }

}
