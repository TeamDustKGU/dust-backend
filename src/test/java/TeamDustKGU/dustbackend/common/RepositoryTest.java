package TeamDustKGU.dustbackend.common;

import TeamDustKGU.dustbackend.global.config.JpaAuditingConfiguration;
import TeamDustKGU.dustbackend.global.config.QueryDslConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({QueryDslConfiguration.class, JpaAuditingConfiguration.class})
public class RepositoryTest {
}
