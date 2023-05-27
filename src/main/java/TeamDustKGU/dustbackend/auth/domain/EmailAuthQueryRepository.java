package TeamDustKGU.dustbackend.auth.domain;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailAuthQueryRepository {
    boolean existValidAuthByEmail(String email, LocalDateTime currentTime);
    Optional<EmailAuth> findValidAuthByEmail(String email, String authToken, LocalDateTime currentTime);
}
