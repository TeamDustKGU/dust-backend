package TeamDustKGU.dustbackend.user.domain.follow;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    // @Query
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM Follow i WHERE i.following.id = :followingId AND i.follower.id = :followerId")
    void deleteByFollowingIdAndFollowerId(@Param("followingId") Long followingId, @Param("followerId") Long followerId);

    // Query Method
    boolean existsByFollowingIdAndFollowerId(Long followingId, Long followerId);
}
