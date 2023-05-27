package TeamDustKGU.dustbackend.auth.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailAuthRepository extends JpaRepository<EmailAuth, Long>, EmailAuthQueryRepository {
}
