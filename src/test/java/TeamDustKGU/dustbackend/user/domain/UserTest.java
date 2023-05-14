package TeamDustKGU.dustbackend.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("User 도메인 테스트")
public class UserTest{
    @Test
    @DisplayName("User를 생성한다.")
    public void createUser() {
        Role role = Role.USER;
        Email email = new Email("qwer@gmail.com");
        String password = "qwer1234";
        Nickname nickname = new Nickname("dust");
        User user = User.createUser(role, email, password, nickname);

        assertThat(user.getRole()).isEqualTo(role);
        assertThat(user.getEmailValue()).isEqualTo(email.getValue());
        assertThat(user.getAuth()).isEqualTo(1);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getNicknameValue()).isEqualTo(nickname.getValue());
        assertThat(user.getStatus()).isEqualTo(0);
    }

    @Test
    @DisplayName("User 닉네임을 변경한다.")
    void updateNickname(){
        //given
        Role role = Role.USER;
        Email email = new Email("qwer@gmail.com");
        String password = "qwer1234";
        Nickname nickname = new Nickname("dust");
        User user = User.createUser(role, email,password, nickname);

        //when
        String change = "user1234";
        user.updateNickname(change);

        //then
        assertThat(user.getNicknameValue()).isEqualTo(change);
    }

    @Test
    @DisplayName("이메일로 동일 사용자인지 확인한다.")
    void isSameUser(){
        //given
        Role role = Role.USER;
        Email email = new Email("qwer@gmail.com");
        String password = "qwer1234";
        Nickname nickname = new Nickname("dust");
        final User user = User.createUser(role, email,password, nickname);
        User user1 = User.createUser(role, email,password, nickname);
        User user2 = User.createUser(role, new Email("dust@gmail.com"), password, nickname);

        //when
        boolean result1 = user.isSameUser(user1);
        boolean result2 = user.isSameUser(user2);

        //then
        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
    }
}
