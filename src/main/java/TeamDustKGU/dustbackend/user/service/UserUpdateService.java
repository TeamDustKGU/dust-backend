package TeamDustKGU.dustbackend.user.service;

import TeamDustKGU.dustbackend.global.exception.DustException;
import TeamDustKGU.dustbackend.user.domain.User;
import TeamDustKGU.dustbackend.user.exception.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserUpdateService {
    private final UserFindService userFindService;

    @Transactional
    public void updateNickname(Long userId, String value) {
        User user = userFindService.findById(userId);

        if (user.getNickname() != null) {
            validateIfDateAfter30Days(user.getNickname().getModifiedDate());
        }

        user.updateNickname(value);
    }

    public void validateIfDateAfter30Days(LocalDateTime target) {
        if (target == null) return;

        if (LocalDateTime.now().isBefore(target.plusDays(30))) {
            throw DustException.type(UserErrorCode.UPDATE_NICKNAME_AFTER_30_DAYS);
        }
    }
}
