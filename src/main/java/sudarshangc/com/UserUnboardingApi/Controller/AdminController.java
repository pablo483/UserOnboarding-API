package sudarshangc.com.UserUnboardingApi.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sudarshangc.com.UserUnboardingApi.DTO.RegistrationRequest;
import sudarshangc.com.UserUnboardingApi.Entity.User;
import sudarshangc.com.UserUnboardingApi.Service.UserService;
import sudarshangc.com.UserUnboardingApi.Service.VerificationTokenService;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationTokenService verificationTokenService;

    //create admin account and verify through gmail
    @PostMapping("/create-admin")
    public ResponseEntity<?> signUpUser(@RequestBody RegistrationRequest request){

        verificationTokenService.registerAdmin(request);


        return new ResponseEntity<>("registration as Admin is  successfully created ,please check you email for activation first", HttpStatus.OK);
    }


    //get all users(for Admin)
    @GetMapping
    public List<User> getAllUser(){
        return userService.findAll();
    }

    //get user by username(for Admin)
    @GetMapping("/{username}")
    public User getUserByUserName(@PathVariable String username){
        return userService.findByUserName(username);
    }

    //delete user by userName (For Admin Only)
    @DeleteMapping("/{username}")
    public void deleteUserById(@PathVariable String username){

        userService.deleteByUserName(username);

    }


}
