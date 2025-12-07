package sudarshangc.com.UserUnboardingApi.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sudarshangc.com.UserUnboardingApi.DTO.LogOutRequest;
import sudarshangc.com.UserUnboardingApi.Entity.RefreshToken;
import sudarshangc.com.UserUnboardingApi.Entity.User;
import sudarshangc.com.UserUnboardingApi.Repository.RefreshTokenRepository;
import sudarshangc.com.UserUnboardingApi.Repository.UserRepository;


import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;



//    public User saveUser(User user){
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        return userRepository.save(user);
//    }

    public List<User> findAll(){
      return userRepository.findAll();
    }

    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }

    public User findByUserName(String userName){
        return userRepository.findByUserName(userName);
    }


    @Transactional
    public void deleteByUserName(String userName){
        User user = userRepository.findByUserName(userName);
        refreshTokenRepository.deleteByUser(user);//delete refresh token first by user
        userRepository.deleteByUserName(userName);//then delete user from users table  by username
    }

    public User updateByUserName(String userName, User newUser){
        User old = userRepository.findByUserName(userName);
        if (newUser!=null){
            old.setUserName(newUser.getUserName()!=null && !newUser.getUserName().equals("")? newUser.getUserName()  : old.getUserName());
            old.setEmail(newUser.getEmail()!=null && !newUser.getEmail().equals("")? newUser.getEmail()  : old.getEmail() );
            old.setPassword(newUser.getPassword()!=null && !newUser.getPassword().equals("")? newUser.getPassword()  : old.getPassword());
            old.setPassword(passwordEncoder.encode(old.getPassword()));
             userRepository.save(old);
        }
        return old;
    }
    @Transactional
    public void logOutUser(User user){
        refreshTokenRepository.deleteByUser(user);

    }

}
