package sudarshangc.com.UserUnboardingApi.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import sudarshangc.com.UserUnboardingApi.DTO.RegistrationRequest;
import sudarshangc.com.UserUnboardingApi.DTO.TokenRefreshResponse;
import sudarshangc.com.UserUnboardingApi.Entity.RefreshToken;
import sudarshangc.com.UserUnboardingApi.Entity.User;
import sudarshangc.com.UserUnboardingApi.Exception.InvalidTokenException;
import sudarshangc.com.UserUnboardingApi.Service.RefreshTokenService;
import sudarshangc.com.UserUnboardingApi.Service.UserDetailsServiceImp;
import sudarshangc.com.UserUnboardingApi.Service.UserService;
import sudarshangc.com.UserUnboardingApi.Service.VerificationTokenService;
import sudarshangc.com.UserUnboardingApi.Utils.JwtUtils;

@RestController
@RequestMapping("/public")
public class PublicController {//this class means what anybody is allowed to do

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImp userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private VerificationTokenService verificationTokenService;


    @PostMapping("/register")
    public ResponseEntity<?> signUpUser(@RequestBody RegistrationRequest request){

        verificationTokenService.register(request);


        return new ResponseEntity<>("registration succesfully created ,please check you email for activation first",HttpStatus.OK);
    }

    @GetMapping("/verify-account")
    @Transactional
    public ResponseEntity<String> verifyAccount(@RequestParam("token") String token){
        try{
            verificationTokenService.verifyAccount(token);
            return ResponseEntity.ok("Account Succesfully Verified,You Can Now Log-in");
        }catch (InvalidTokenException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_GATEWAY);
        }
    }

    //create and user
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
        String accessToken = jwtUtils.generateToken(userDetails.getUsername());

        User userEntity = userService.findByUserName(user.getUserName());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userEntity, userDetails.getUsername());


        return ResponseEntity.ok(new TokenRefreshResponse(
                accessToken,
                refreshToken.getToken()
        ));
    }

}
