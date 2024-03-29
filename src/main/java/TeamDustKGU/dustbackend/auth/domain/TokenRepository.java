package TeamDustKGU.dustbackend.auth.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    // @Query
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Token t" +
            " SET t.refreshToken = :refreshToken" +
            " WHERE t.memberId = :memberId")
    void reissueRefreshTokenByRtrPolicy(@Param("memberId") Long memberId, @Param("refreshToken") String newRefreshToken);

    // Query Method
    Optional<Token> findByMemberId(Long memberId);
    void deleteByMemberId(Long memberId);
    boolean existsByMemberIdAndRefreshToken(Long memberId, String refreshToken);
}
