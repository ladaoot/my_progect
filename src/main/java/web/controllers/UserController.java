package web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.models.User;
import web.models.role.Role;
import web.repository.UserRepository;
import web.service.UserService;

import java.security.Principal;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    UserService userService;

    @GetMapping("/login")
    public String logIn(Model model){
        return "login";
    }

    @GetMapping("/registration")
    public String getRegistration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String createUser(User user){
        userService.createUser(user);
        return "redirect:/login";
    }

    @GetMapping("/user/{id}")
    public  String personalPage(Model model, @PathVariable("id") Long id, Principal principal){
        User user =userService.getUserByPrincipal(principal);

        if(!user.getId().equals(id) && !user.getRoles().contains(Role.ADMIN)){
            model.addAttribute("error","You cant see another users page");
            return "redirect:/";
        }

        model.addAttribute("user", user);
        return "user-info";
    }

}
