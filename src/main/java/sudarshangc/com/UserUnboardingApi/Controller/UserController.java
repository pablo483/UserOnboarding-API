package sudarshangc.com.UserUnboardingApi.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import sudarshangc.com.UserUnboardingApi.DTO.LogOutRequest;
import sudarshangc.com.UserUnboardingApi.Entity.User;
import sudarshangc.com.UserUnboardingApi.Service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {//this class is for what user can do

    @Autowired
    private UserService userService;


    @DeleteMapping("/log-out")
    public ResponseEntity<?> LogOut(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        userService.logOutUser(user);
        return new ResponseEntity<>("logOut SuccessFull", HttpStatus.OK);
    }

    //update user by username
    @PutMapping
    public User updateUserById(@RequestBody User newUser){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.updateByUserName(userName,newUser);
        return user;
    }


}
