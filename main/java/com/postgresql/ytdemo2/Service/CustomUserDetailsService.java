package com.postgresql.ytdemo2.Service;

import com.postgresql.ytdemo2.Enums.Role;
import com.postgresql.ytdemo2.model.User;
import com.postgresql.ytdemo2.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepo userRepo;

    @Override // this method below  When someone submits the login form, Spring Security automatically takes the username they entered and passes it into the loadUserByUsername method.
    public UserDetails loadUserByUsername(String username) {//here we are overriding the implemented "loadByUsername" in the "InMemoryUserDetailsManager" class
        User user = userRepo.findByUsername(username);//a new object is created, the name if found in a row then that row is used to create another object using setters in

        if (user == null) {//the User class similar to when we input values into the dog form and then setters were used to bind these values from the form to the object
            throw new UsernameNotFoundException("user not found");//this exception is caught internally, its when a user puts a username that is not found(password would of not been even checked at this point), the BadCredentialsException is
        }//when correct username but wrong password, both are handled internally and use "Redirects to ?error" so (/login?error in the url) and redirect us to the login page automatically.THIS CODE  ABOVE IS NOT NECCESSARY AS "?ERROR" WILL STILL SHOW IN THE URL BUT THIS CODE JUST MAKES IT CLEAR THAT THE "USER IS NOT FOUND" I THINK THIS SHOULD SHOW IN THE TERMINAL
        System.out.println("Found user: " + username + " with role: " + user.getRole());
        System.out.println("Password (should be BCrypt): " + user.getPassword());

        //==
        if (user.getRole().equals(Role.ADMIN)) {  // checks if role is ADMIN for the user
            return org.springframework.security.core.userdetails.User
                    .withUsername(username)
                    .password(user.getPassword())
                    .roles("ADMIN")
                    .build();//User object is created for checking security, cant use the above "user" object returned from the database for example, but the User object details above is used to
                            //==
        } if (user.getRole().equals(Role.SENIOR)) {    //to create this object and then we use this User object to Verify password matches,Check permissions for future requests,Create security session(even though this is done internally but we still need this object to create a session
            return org.springframework.security.core.userdetails.User
                    .withUsername(username)
                    .password(user.getPassword())
                    .roles("SENIOR")
                    .build();

        }
        //return null;
        throw new UsernameNotFoundException("Invalid role for user: " + username);

    }
}