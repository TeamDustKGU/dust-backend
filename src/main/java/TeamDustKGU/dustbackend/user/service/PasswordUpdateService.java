package TeamDustKGU.dustbackend.user.service;

import TeamDustKGU.dustbackend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PasswordUpdateService {
    private final UserFindService userFindService;
    @Transactional
    public void updatePassword(Long userId, String updatePassword) {
        User user = userFindService.findById(userId);

        if (user.getPassword() != null) {
            validateIfDateAfter30Days(user.getPassword().stripIndent());
        }

        user.updatePassword(updatePassword);
    }
    public void validateIfDateAfter30Days(LocalDateTime target) {
        if (LocalDateTime.now().isBefore(target.plusDays(30))) {
            throw DustException.type(UserErrorCode.UPDATE_PASSWORD_AFTER_30_DAYS);
        }
    }
}
