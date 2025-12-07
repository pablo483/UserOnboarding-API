package sudarshangc.com.UserUnboardingApi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sudarshangc.com.UserUnboardingApi.Entity.VerificationToken;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken,Long> {
    Optional<VerificationToken> findByToken(String token);
}
