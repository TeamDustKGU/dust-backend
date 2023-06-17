package TeamDustKGU.dustbackend.user.service;

import TeamDustKGU.dustbackend.user.domain.User;
import TeamDustKGU.dustbackend.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserDeleteService {
    private final UserFindService userFindService;
    private final UserRepository userRepository;

    @Transactional
    public void deleteUser(Long userId) {
        User user = userFindService.findById(userId);
        userRepository.delete(user);
    }
}
