package com.nendejan.lc101FinalProject.controllers;


import com.nendejan.lc101FinalProject.models.User;
import com.nendejan.lc101FinalProject.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
@RequestMapping("login")
public class UserController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(value="")
    public String index(Model model){
        model.addAttribute("title", "Welcome to the Scheduling App!");

        return "login/index";


    }

    @RequestMapping(value= "/registration", method = RequestMethod.GET)
    public String displayRegistrationForm(Model model){
        model.addAttribute("title", "Sign Up!");
        model.addAttribute(new User());

        return "login/registration";

    }

    @RequestMapping(value= "registration", method = RequestMethod.POST)
    public String processRegistrationForm(@ModelAttribute @Valid User newUser, Errors errors, @RequestParam String username, @RequestParam String password, @RequestParam String passwordConfirm, @RequestParam boolean isAdmin, Model model){

        if (errors.hasErrors()){
            model.addAttribute("title", "Sign Up!");
            return "login/registration";
        }
        else if(password != passwordConfirm){
            model.addAttribute("title", "Sign Up!");
            return "login/registration";
        }
        else
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setPasswordConfirm(passwordConfirm);
        newUser.setIsAdmin(isAdmin);
        userDao.save(newUser);

        return "/welcome";
    }

}
