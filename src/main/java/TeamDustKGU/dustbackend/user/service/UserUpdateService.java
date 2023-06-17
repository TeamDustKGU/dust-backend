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
            validateSameNickname(user.getNicknameValue(), value);
        }

        user.updateNickname(value);
    }

    private void validateSameNickname(String oldValue, String newValue) {
        if (oldValue.equals(newValue)) {
            throw DustException.type(UserErrorCode.CANNOT_UPDATE_SAME_NICKNAME);
        }
    }

    @Transactional
    public void updatePassword(Long userId, String updatePassword) {
        User user = userFindService.findById(userId);

        if (user.getPassword() != null) {
            validateIfDateAfter30Days(user.getPassword().getModifiedDate());
        }
        user.updatePassword(updatePassword);
    }

    public void validateIfDateAfter30Days(LocalDateTime target) {
        if (LocalDateTime.now().isBefore(target.plusDays(30))) {
            throw DustException.type(UserErrorCode.UPDATE_AFTER_30_DAYS);
        }
    }

}
