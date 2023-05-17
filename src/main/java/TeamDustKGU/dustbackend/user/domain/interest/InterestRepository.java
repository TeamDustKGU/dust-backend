package TeamDustKGU.dustbackend.user.domain.interest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InterestRepository extends JpaRepository<Interest, Long> {
    // @Query
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM Interest i WHERE i.interesting.id = :interestingId AND i.interested.id = :interestedId")
    void deleteByInterestingIdAndInterestedId(@Param("interestingId") Long interestingId, @Param("interestedId") Long interestedId);

    // Query Method
    boolean existsByInterestingIdAndAndInterestedId(Long interestingId, Long interestedId);
}
