package com.nendejan.lc101FinalProject.controllers;


import com.nendejan.lc101FinalProject.models.User;
import com.nendejan.lc101FinalProject.models.data.UserDao;

import com.nendejan.lc101FinalProject.models.data.workplaceDao;
import com.nendejan.lc101FinalProject.models.workplace;
import org.springframework.beans.factory.annotation.Autowired;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;





import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;




@Controller
@RequestMapping("login")
public class UserController {

    @Autowired
    private workplaceDao workplaceDao;
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

    @RequestMapping(value= "/registration", method = RequestMethod.POST)
    public String processRegistrationForm(HttpServletResponse response, @ModelAttribute @Valid User newUser, Errors errors, @RequestParam String username, @RequestParam String password, @RequestParam String passwordConfirm, @RequestParam String email, Model model){

        if (errors.hasErrors()){
            model.addAttribute("title", "Sign Up!");
            return "login/registration";
        }
        /*
        TODO: VALIDATION setup REGEX for username and email fields
        TODO: VALIDATION passwords are plain text? need to add hashing*/


        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
        String passwordConfirmHash = BCrypt.hashpw(passwordConfirm, BCrypt.gensalt());


        boolean passwordsMatch = false;
        boolean usernameTaken = false;
        boolean emailTaken = false;
        for (User user : userDao.findAll()){
            if (user.getUsername().equals(username)){
                usernameTaken = true;
            }}
        for (User user : userDao.findAll()){
            if (user.getEmail().equals(email)){
                emailTaken = true;
            }
        }



       /*TODO: CSS/UX when you setup css make this font red "error"*/

       if (usernameTaken == true) {
           model.addAttribute("usernameTakenError", "That user name is currently in use, please choose another.");
           return "login/registration";
       }

       /*TODO VALIDTION: recheck indentation here*/
        if (emailTaken == true) {
            model.addAttribute("emailTakenError", "That email already belongs to an account.");
            return "login/registration";
        }

       if (password.equals(passwordConfirm)){
            passwordsMatch =true;}

       else if (passwordsMatch == false){
            model.addAttribute("title", "Sign Up!");
            model.addAttribute("passwordConfirmError", "Passwords must match.");
            return "login/registration";
        }




        else
        newUser.setUsername(username);
        newUser.setPassword(passwordHash);
        newUser.setPasswordConfirm(passwordConfirmHash);
        newUser.setEmail(email);

        userDao.save(newUser);



        Cookie usernameGreeter  = new Cookie("nameToGreet", newUser.getUsername());
        usernameGreeter.setMaxAge(24 * 60 * 60);
        response.addCookie(usernameGreeter);

        return "redirect:welcome";


    }
    @RequestMapping(value= "/welcome", method = RequestMethod.GET)
    public String displayWelcomePage (HttpServletRequest request,
                                      @CookieValue(value="nameToGreet", defaultValue ="User") String cookieValue, Model model) {
        request.getCookies();

        /*TODO:  VALIDATION set up registration link email*/

        model.addAttribute("title", "Welcome, " + cookieValue + " an email will be sent out shortly with a registration link enclosed.");


        return "login/welcome";
    }

    @RequestMapping(value="/sign-in", method = RequestMethod.GET)
    public String displaySignInPage(Model model){
        model.addAttribute("title", "Sign In!");
        model.addAttribute("signInFail", null);
        model.addAttribute(new User());


        return "login/sign-in";
    }



   @RequestMapping(value="/sign-in", method= RequestMethod.POST)
    public String processSignInPage(HttpServletResponse response,@RequestParam String username, @RequestParam String password, Model model){
/*TODO Check this logic after setting up dashboard view and handlers*/

        boolean correctLogin = false;

/*TODO VALIDATION : Setup error message for incorrect credentials*/
        User thisUser = userDao.findByUsername(username);
       /* if(thisUser.getPassword().equals(passwordHash))*/
       if (BCrypt.checkpw(password, thisUser.getPassword())){
            correctLogin = true;
        }


        if (correctLogin == true){

            /*TODO VALIDATION : Hash this cookie?*/
            String cookieValueString = Integer.toString(thisUser.getId());
            Cookie loggedInCookie = new Cookie("loggedInCookie", cookieValueString);
            loggedInCookie.setMaxAge(24 * 60 * 60);
            /* TODO how long should a user remain logged in?*/

            response.addCookie(loggedInCookie);

            return "redirect:dashboard";

        }

        else {
            model.addAttribute("title", "Sign In!");
            model.addAttribute("signInFail", "Incorrect Username and Password Combination.");

            return "login/sign-in";
            }
    }



/*TODO create log out button*/


    @RequestMapping(value="/dashboard", method=RequestMethod.GET)
    public String displayDashBoardPage(HttpServletRequest request, HttpServletResponse response, Model model, @CookieValue(value="loggedInCookie", defaultValue ="loggedInUserIdString") String cookieValue){

        int loggedInUserId = Integer.parseInt(cookieValue);

        User loggedInUser = userDao.findOne(loggedInUserId);


        if(loggedInUser.getWorkplace() ==  null){
            model.addAttribute("title", "Welcome, " + loggedInUser.getUsername() + "!" );
            model.addAttribute("createAWorkplace", "Create A Workplace.");
            model.addAttribute("findAWorkplace", "Find Your Workplace");
            model.addAttribute("workplaces", workplaceDao.findAll());


            /*TODO VALIDATION : Hash this cookie?*/
            String cookieValueString = Integer.toString(loggedInUserId);
            Cookie loggedInCookie = new Cookie("loggedInCookie", cookieValueString);
            loggedInCookie.setMaxAge(24 * 60 * 60);
            /* TODO how long should a user remain logged in?*/

            response.addCookie(loggedInCookie);



            return "login/dashboard";
        }
        model.addAttribute("createAWorkplace", "Create A Workplace.");
        model.addAttribute("title", "Welcome, " + loggedInUser.getUsername() + "!" );
        model.addAttribute("workplaceName", "You work at " + loggedInUser.getWorkplace().getWorkplaceName() + ".");
        return "login/dashboard";

    }




    @RequestMapping(value="/dashboard", method=RequestMethod.POST)
    public String processDashBoardPage ( HttpServletRequest request, @RequestParam String usersWorkplace, Model model, @CookieValue(value="loggedInCookie", defaultValue ="loggedInUserIdString") String cookieValue){


        int loggedInUserId = Integer.parseInt(cookieValue);

        User loggedInUser = userDao.findOne(loggedInUserId);


        workplace thisUsersWorkplace = workplaceDao.findByWorkplaceName(usersWorkplace);

        loggedInUser.setWorkplace(thisUsersWorkplace);
        thisUsersWorkplace.addUsers(loggedInUser);
        userDao.save(loggedInUser);
        workplaceDao.save(thisUsersWorkplace);








        model.addAttribute("createAWorkplace", "Create A Workplace.");
        model.addAttribute("title", "Welcome, " + loggedInUser.getUsername() + "!" );
        model.addAttribute("workplaceName", "You work at " + loggedInUser.getWorkplace().getWorkplaceName() + ".");

        return "login/dashboard";


    }
}






