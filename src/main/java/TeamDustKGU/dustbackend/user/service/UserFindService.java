package TeamDustKGU.dustbackend.user.service;

import TeamDustKGU.dustbackend.global.exception.DustException;
import TeamDustKGU.dustbackend.user.domain.Email;
import TeamDustKGU.dustbackend.user.domain.User;
import TeamDustKGU.dustbackend.user.domain.UserRepository;
import TeamDustKGU.dustbackend.user.exception.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserFindService {
    private final UserRepository userRepository;

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> DustException.type(UserErrorCode.USER_NOT_FOUND));
    }

    public User findByEmail(Email email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> DustException.type(UserErrorCode.USER_NOT_FOUND));
    }
}
