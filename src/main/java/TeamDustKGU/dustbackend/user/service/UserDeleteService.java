package TeamDustKGU.dustbackend.user.service;

import TeamDustKGU.dustbackend.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserDeleteService {
    private final UserRepository userRepository;

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}