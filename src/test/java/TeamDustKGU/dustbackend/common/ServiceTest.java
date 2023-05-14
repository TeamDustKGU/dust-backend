package TeamDustKGU.dustbackend.common;

import TeamDustKGU.dustbackend.auth.domain.TokenRepository;
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

    @AfterEach
    void clearDatabase() {
        databaseCleaner.cleanUpDatabase();
    }
}
