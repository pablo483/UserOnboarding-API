package sudarshangc.com.UserUnboardingApi.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.hibernate.result.UpdateCountOutput;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class JwtUtils {

    private String SECRET_KEY="TaK+HaV^uvCHEFsEVfypW#7g9^k*Z8$V";

    public SecretKey getSigninKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(String userName){
        Map<String,Object> claims=new HashMap<>();
        return createToken(claims,userName);
    }

    private String createToken(Map<String,Object> claims,String subject){
        return Jwts.builder()
                .claims(claims  )
                .subject(subject)
                .header().empty().add("typ","jwt")
                .and()
                .issuedAt(new Date(System.currentTimeMillis()   ))
                .expiration(new Date(System.currentTimeMillis()+1000*60*60  ))
                .signWith(getSigninKey())
                .compact();

    }

    //Create Refresh token
    public String createRefreshToken(String userName){
        return Jwts.builder()
                .subject(userName)
                .header().empty().add("typ","jwt")
                .and()
                .issuedAt(new Date(System.currentTimeMillis()   ))
                .expiration(new Date(System.currentTimeMillis()+1000L*60*60*24*7))
                .signWith(getSigninKey())
                .compact();
    }

    public String extractUserName(String Token){
        return extractAllClaims(Token).getSubject();
    }

    public Claims extractAllClaims(String Token){
        return Jwts.parser()
                .verifyWith(getSigninKey())
                .build()
                .parseSignedClaims(Token)
                .getPayload();
    }

    public Boolean validateToken(String Token){
        return !isTokenExpired(Token);
    }

    public Boolean isTokenExpired(String Token){
        return extractExpiration(Token).before(new Date());
    }

    public Date extractExpiration(String Token){
        return extractAllClaims(Token).getExpiration();
    }

}
