package sudarshangc.com.UserUnboardingApi.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sudarshangc.com.UserUnboardingApi.Entity.RefreshToken;
import sudarshangc.com.UserUnboardingApi.Entity.User;
import sudarshangc.com.UserUnboardingApi.Repository.RefreshTokenRepository;
import sudarshangc.com.UserUnboardingApi.Utils.JwtUtils;

import java.time.Instant;
import java.util.Optional;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private JwtUtils jwtUtils;

    //find refreshToken entity by token
    public Optional<RefreshToken> findByToken(String token){
        Optional<RefreshToken> token1 = refreshTokenRepository.findByToken(token);
        return token1;
    }


    //check expiration
    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().isBefore(Instant.now())){
            refreshTokenRepository.delete(token );
            throw new RuntimeException("Refresh Token Expired,Please re-login ");

        }else
            return token;
    }
   //create refresh token and some extra setup
    @Transactional
    public RefreshToken createRefreshToken(User user,String userName){
        refreshTokenRepository.deleteByUser(user);

        String newRefreshToken = jwtUtils.createRefreshToken(user.getUserName());

        Instant expiration=Instant.now().plusMillis(1000L*60*60*7);

        RefreshToken refreshToken=RefreshToken.builder()
                .expiryDate(expiration)
                .token(newRefreshToken)
                .user(user)
                .build();

        return refreshTokenRepository.save(refreshToken);

    }

    @Transactional
    public void delete(RefreshToken token){
        refreshTokenRepository.delete(token);
    }

}
