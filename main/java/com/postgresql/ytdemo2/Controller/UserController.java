package com.postgresql.ytdemo2.Controller;


import com.postgresql.ytdemo2.Service.CustomUserDetailsService;
import com.postgresql.ytdemo2.Service.UserService;
import com.postgresql.ytdemo2.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller
//@RestController
public class UserController {

    @Autowired
    UserService userService;


    @GetMapping("/signUp")
    public String addUserForm(Model model){

        return userService.prepareUserSignUp(model);

    }

    @PostMapping("/signUp")
    public String SaveUserForm(@ModelAttribute User user){

        return userService.prepareSaveUserForm(user);

    }


    @GetMapping("/deleteUser")
    public String deleteUserForm(){
        return "deleteUserById";
    }



    @GetMapping("/deleteUserById")
    public String  prepareDeleteUserById(@RequestParam Long id) {

        return userService.deleteUserById(id);

    }


    @GetMapping("/login")
    public String loginPage() {

            return "login";
            
        }
























}
