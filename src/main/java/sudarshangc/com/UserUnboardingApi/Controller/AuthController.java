package sudarshangc.com.UserUnboardingApi.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sudarshangc.com.UserUnboardingApi.DTO.TokenRefreshRequest;
import sudarshangc.com.UserUnboardingApi.DTO.TokenRefreshResponse;
import sudarshangc.com.UserUnboardingApi.Entity.RefreshToken;
import sudarshangc.com.UserUnboardingApi.Entity.User;
import sudarshangc.com.UserUnboardingApi.Service.RefreshTokenService;
import sudarshangc.com.UserUnboardingApi.Service.UserDetailsServiceImp;
import sudarshangc.com.UserUnboardingApi.Utils.JwtUtils;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {//this is also authorized person can do

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserDetailsServiceImp userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenRefreshResponse> refreshAccessToken(@RequestBody TokenRefreshRequest request ){
        return refreshTokenService.findByToken(request.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(token ->{
                    User user = token.getUser();

                    UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());

                    //generate new access token short-lived
                    String newAccessToken = jwtUtils.generateToken(user.getUserName());

                    RefreshToken newRefreshTokenEntity = refreshTokenService.createRefreshToken( user, userDetails.getUsername());

                    //Invalidate the old token by deleting it from the database
                    refreshTokenService.delete(token);

                    //  Return the new pair to the client
                    return  ResponseEntity.ok(new TokenRefreshResponse(
                                    newAccessToken,
                                    newRefreshTokenEntity.getToken()
                            )
                    );
                })
                .orElseThrow(()->new RuntimeException("Refresh token is not in database,please Re-login"));

    }

    public void logout(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
    }
}


