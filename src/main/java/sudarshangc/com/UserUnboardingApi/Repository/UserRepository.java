package sudarshangc.com.UserUnboardingApi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sudarshangc.com.UserUnboardingApi.Entity.User;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUserName(String username);

    void deleteByUserName(String username);
}
