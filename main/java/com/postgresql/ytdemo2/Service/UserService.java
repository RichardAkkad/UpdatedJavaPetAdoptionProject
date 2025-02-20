package com.postgresql.ytdemo2.Service;

import com.postgresql.ytdemo2.model.User;
import com.postgresql.ytdemo2.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    public String prepareUserSignUp(Model model) {
        model.addAttribute("UserRequest", new User());
        return "saveUser";
    }

    public String prepareSaveUserForm(User user) {
        User newUsernameUser = userRepo.findByUsername(user.getUsername());
        if (newUsernameUser == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepo.save(user);
            return "index";
        }
        return "saveUserFailed";
    }

    public String deleteUserById(Long id){
        if (userRepo.existsById(id)){
            userRepo.deleteById(id);
            return "deleteSuccess";
        }
        else{
            return "deleteFailed";
        }

    }




}