package TeamDustKGU.dustbackend.user.service.interest;

import TeamDustKGU.dustbackend.global.exception.DustException;
import TeamDustKGU.dustbackend.user.domain.User;
import TeamDustKGU.dustbackend.user.domain.interest.Interest;
import TeamDustKGU.dustbackend.user.domain.interest.InterestRepository;
import TeamDustKGU.dustbackend.user.exception.UserErrorCode;
import TeamDustKGU.dustbackend.user.service.UserFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InterestService {
    private final UserFindService userFindService;
    private final InterestRepository interestRepository;

    @Transactional
    public Long register(Long interestingId, Long interestedId, String boardTitle, LocalDateTime boardCreatedDate) {
        validateSelfInterest(interestingId, interestedId);
        validateAlreadyInterest(interestingId, interestedId);

        User interestingUser = userFindService.findById(interestingId);
        User interestedUser = userFindService.findById(interestedId);
        Interest interest = Interest.registerInterest(interestingUser, interestedUser, boardTitle, boardCreatedDate);

        return interestRepository.save(interest).getId();
    }

    private void validateSelfInterest(Long interestingId, Long interestedId) {
        if (interestingId == interestedId) {
            throw DustException.type(UserErrorCode.SELF_INTEREST_NOT_ALLOWED);
        }
    }

    private void validateAlreadyInterest(Long interestingId, Long interestedId) {
        if (interestRepository.existsByInterestingIdAndAndInterestedId(interestingId, interestedId)) {
            throw DustException.type(UserErrorCode.ALREADY_INTEREST);
        }
    }
}
