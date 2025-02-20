package com.postgresql.ytdemo2.Controller;

import com.postgresql.ytdemo2.model.User;
import com.postgresql.ytdemo2.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class restUserController {
    @Autowired
    UserRepo userRepo;

    //@GetMapping("/deleteUserById")
    //public String deleteUserById(@RequestParam Long id){
    //    if (userRepo.existsById(id)){
    //        userRepo.deleteById(id);
    //        return "success";
    //    }
    //    else{
    //        return "id not found";
    //    }

    //}












}
