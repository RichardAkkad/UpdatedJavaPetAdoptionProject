package com.postgresql.ytdemo2.AuthenticationProvider;

import com.postgresql.ytdemo2.model.User;
import com.postgresql.ytdemo2.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.time.LocalTime;



public class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider {


    @Autowired
    UserRepo userRepo;


    @Override
    public Authentication authenticate(Authentication authentication) {// a Authentication object is automatically created and assigned to authentication variable here  which contains the raw, unverified credentials
                                                                        //that the user just typed in into the login form and also we can use these parameter so we can call the original authenticate method again
        // Extra checks

        String username=authentication.getName();// methods from the Authentication class and class implements this interface being "UsernamePasswordAuthenticationToken"
        User user = userRepo.findByUsername(username);
        if (user != null && user.getExpiryDate().isBefore(LocalDate.now())) {
            throw new AccountExpiredException("Account has expired");


        }


        LocalTime currentTime = LocalTime.now();
        if (currentTime.getHour() < 9 || currentTime.getHour() >= 23) {
            throw new LockedException("Login only allowed during business hours");
        }






        return super.authenticate(authentication);// need to "extend Dao...." at the class declaration so that we can override so we come to this method first
    } // and then go back to the super class authenticate method











}



