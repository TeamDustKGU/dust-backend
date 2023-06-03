package TeamDustKGU.dustbackend.user.service.suspension;

import TeamDustKGU.dustbackend.global.exception.DustException;
import TeamDustKGU.dustbackend.user.domain.Role;
import TeamDustKGU.dustbackend.user.domain.User;
import TeamDustKGU.dustbackend.user.domain.suspension.Suspension;
import TeamDustKGU.dustbackend.user.domain.suspension.SuspensionRepository;
import TeamDustKGU.dustbackend.user.exception.UserErrorCode;
import TeamDustKGU.dustbackend.user.service.UserFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SuspensionService {
    private final UserFindService userFindService;
    private final SuspensionRepository suspensionRepository;

    @Transactional
    public Long suspend(Long adminId, Long suspendedId, LocalDateTime startDate, LocalDateTime endDate, String reason) {
        User admin = userFindService.findById(adminId);
        validateAdmin(admin.getRole());

        User suspendedUser = userFindService.findById(suspendedId);
        validateAlreadySuspended(suspendedUser.getStatus());

        suspendedUser.deactivate();
        Suspension suspension = Suspension.createSuspension(reason, startDate, endDate, suspendedUser);
        return suspensionRepository.save(suspension).getId();
    }

    public void validateAdmin(Role role) {
        if(!(role == Role.ADMIN)) {
            throw DustException.type(UserErrorCode.USER_IS_NOT_ADMIN);
        }
    }

    public void validateAlreadySuspended(int status) {
        if(status == 0) {
            throw DustException.type(UserErrorCode.ALREADY_SUSPENDED);
        }
    }
}
