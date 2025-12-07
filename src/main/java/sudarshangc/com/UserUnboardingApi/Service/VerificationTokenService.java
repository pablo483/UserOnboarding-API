package sudarshangc.com.UserUnboardingApi.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sudarshangc.com.UserUnboardingApi.DTO.RegistrationRequest;
import sudarshangc.com.UserUnboardingApi.Entity.User;
import sudarshangc.com.UserUnboardingApi.Entity.VerificationToken;
import sudarshangc.com.UserUnboardingApi.Exception.InvalidTokenException;
import sudarshangc.com.UserUnboardingApi.Repository.UserRepository;
import sudarshangc.com.UserUnboardingApi.Repository.VerificationTokenRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class VerificationTokenService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public void register(RegistrationRequest request){

        User user=new User();
        user.setUserName(request.getUserName());
        user.setPassword( passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setIs_enabled(false);
        user.setRole(User.Role.USER);

        userRepository.save(user);
        String token = generateVerificationToken(user);

        String verificationUrl = "http://localhost:8080/public/verify-account?token=" + token;

        emailService.sentEmail(user.getEmail(), "Click This Link",verificationUrl);

    }

    @Transactional
    public void registerAdmin(RegistrationRequest request){

        User user=new User();
        user.setUserName(request.getUserName());
        user.setPassword( passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setIs_enabled(false);
        user.setRole(User.Role.ADMIN);

        userRepository.save(user);
        String token = generateVerificationToken(user);

        String verificationUrl = "http://localhost:8080/public/verify-account?token=" + token;

        emailService.sentEmail(user.getEmail(), "Click This Link",verificationUrl);

    }

    private String generateVerificationToken(User user){
        String token = UUID.randomUUID().toString();

        Instant expiration = Instant.now().plusSeconds(60 * 60 * 24);

        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .expiryDate(expiration)
                .user(user)
                .build();
        verificationTokenRepository.save(verificationToken);
        return token;
    }

    //check verify
    @Transactional
    public void verifyAccount(String token){
        Optional<VerificationToken> verificationTokenOptional = verificationTokenRepository.findByToken(token);

        if (!verificationTokenOptional.isPresent()){
        throw new InvalidTokenException("Invalid Verification Token");
    }
        VerificationToken verificationToken = verificationTokenOptional.get();
        //check expiration
        if (verificationToken.getExpiryDate().isBefore(Instant.now())){
            verificationTokenRepository.delete(verificationToken);
        }else{
            User user = verificationToken.getUser();
            user.setIs_enabled(true);
            userRepository.save(user);

            //delete the verification token after succesfull verify(one time use only)
            verificationTokenRepository.delete(verificationToken);
        }

}
}
