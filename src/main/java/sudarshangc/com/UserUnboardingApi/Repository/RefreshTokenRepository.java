package sudarshangc.com.UserUnboardingApi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sudarshangc.com.UserUnboardingApi.Entity.RefreshToken;
import sudarshangc.com.UserUnboardingApi.Entity.User;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {

    // 1. Find the RefreshToken entity by the token string
    Optional<RefreshToken> findByToken(String token);

    // 2. Used for token rotation: delete the old token when issuing a new one
    void deleteByUser(User user);

    // 3. Find by user (optional, for lookup)
    Optional<RefreshToken> findByUser(User user);
}
