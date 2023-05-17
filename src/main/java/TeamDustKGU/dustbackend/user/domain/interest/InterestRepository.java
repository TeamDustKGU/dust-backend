package TeamDustKGU.dustbackend.user.domain.interest;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestRepository extends JpaRepository<Interest, Long> {
    // Query Method
    boolean existsByInterestingIdAndAndInterestedId(Long interestingId, Long interestedId);
}
