package TeamDustKGU.dustbackend.user.service.follow;

import TeamDustKGU.dustbackend.global.exception.DustException;
import TeamDustKGU.dustbackend.user.domain.User;
import TeamDustKGU.dustbackend.user.domain.follow.Follow;
import TeamDustKGU.dustbackend.user.domain.follow.FollowRepository;
import TeamDustKGU.dustbackend.user.exception.UserErrorCode;
import TeamDustKGU.dustbackend.user.service.UserFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FollowService {
    private final UserFindService userFindService;
    private final FollowRepository followRepository;

    @Transactional
    public Long register(Long followingId, Long followerId, String boardTitle, LocalDateTime boardCreatedDate) {
        validateSelfFollow(followingId, followerId);
        validateAlreadyFollow(followingId, followerId);

        User following = userFindService.findById(followingId);
        User follower = userFindService.findById(followerId);
        Follow follow = Follow.registerFollow(following, follower, boardTitle, boardCreatedDate);

        return followRepository.save(follow).getId();
    }

    private void validateSelfFollow(Long followingId, Long followerId) {
        if (followingId.equals(followerId)) {
            throw DustException.type(UserErrorCode.SELF_FOLLOW_NOT_ALLOWED);
        }
    }

    private void validateAlreadyFollow(Long followingId, Long followerId) {
        if (followRepository.existsByFollowingIdAndFollowerId(followingId, followerId)) {
            throw DustException.type(UserErrorCode.ALREADY_FOLLOW);
        }
    }

    @Transactional
    public void cancel(Long followingId, Long followerId) {
        validateCancel(followingId, followerId);
        followRepository.deleteByFollowingIdAndFollowerId(followingId, followerId);
    }

    private void validateCancel(Long followingId, Long followerId) {
        if (!followRepository.existsByFollowingIdAndFollowerId(followingId, followerId)) {
            throw DustException.type(UserErrorCode.FOLLOW_NOT_FOUND);
        }
    }
}
