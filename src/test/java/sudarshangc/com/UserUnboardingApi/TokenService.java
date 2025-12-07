package sudarshangc.com.UserUnboardingApi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sudarshangc.com.UserUnboardingApi.Entity.RefreshToken;
import sudarshangc.com.UserUnboardingApi.Service.RefreshTokenService;

import java.util.Optional;

@SpringBootTest
public class TokenService {
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Test
    void findId(){
        String token="eyJ0eXAiOiJqd3QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJLdXNoIiwiaWF0IjoxNzY1MDQyMDM0LCJleHAiOjE3NjU2NDY4MzR9.gHKoT-zDXGwe3pWgh4VAYDTIjsM_WTbmtr2QXin6n3Y";
        Optional<RefreshToken> token1 = refreshTokenService.findByToken(token);
        token1=null;

    }


}
