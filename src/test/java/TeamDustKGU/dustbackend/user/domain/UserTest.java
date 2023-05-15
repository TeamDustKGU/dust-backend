package TeamDustKGU.dustbackend.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static TeamDustKGU.dustbackend.fixture.UserFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("User 도메인 테스트")
public class UserTest{
    @Test
    @DisplayName("User를 생성한다")
    public void createUser() {
        User user = CHAERIN.toUser();

        assertAll(
                () -> assertThat(user.getRole()).isEqualTo(Role.USER),
                () -> assertThat(user.getEmailValue()).isEqualTo(CHAERIN.getEmail()),
                () -> assertThat(user.getAuth()).isEqualTo(1),
                () -> assertThat(user.getPassword()).isEqualTo(CHAERIN.getPassword()),
                () -> assertThat(user.getNickname()).isNull(),
                () -> assertThat(user.getStatus()).isEqualTo(0)
        );
    }

    @Test
    @DisplayName("User 닉네임을 변경한다")
    void updateNickname(){
        // given
        User user1 = CHAERIN.toUser();
        User user2 = SUNKYOUNG.toUser();
        User user3 = SEOKHO.toUser();

        // when
        user1.updateNickname("별명1");
        user1.updateNickname("별명2");
        user2.updateNickname("별명1");

        // then
        assertThat(user1.getNicknameValue()).isEqualTo("별명2");
        assertThat(user2.getNicknameValue()).isEqualTo("별명1");
        assertThat(user3.getNickname()).isNull();
    }

    @Test
    @DisplayName("이메일로 동일 사용자인지 확인한다")
    void isSameUser(){
        // given
        User user1 = CHAERIN.toUser();
        User user2 = User.createUser(Email.from(CHAERIN.getEmail()), "password");
        User user3 = User.createUser(Email.from("diff" + CHAERIN.getEmail()), "password");

        // then
        assertThat(user1.isSameUser(user2)).isTrue();
        assertThat(user1.isSameUser(user3)).isFalse();
    }
}
