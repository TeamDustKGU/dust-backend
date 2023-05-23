package TeamDustKGU.dustbackend.common;

import TeamDustKGU.dustbackend.auth.domain.TokenRepository;
import TeamDustKGU.dustbackend.user.domain.UserRepository;
import TeamDustKGU.dustbackend.user.domain.follow.FollowRepository;
import TeamDustKGU.dustbackend.user.domain.suspension.SuspensionRepository;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ServiceTest {
    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    protected TokenRepository tokenRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected FollowRepository followRepository;

    @Autowired
    protected SuspensionRepository suspensionRepository;

    @AfterEach
    void clearDatabase() {
        databaseCleaner.cleanUpDatabase();
    }

    public void flushAndClear() {
        databaseCleaner.flushAndClear();
    }
}
