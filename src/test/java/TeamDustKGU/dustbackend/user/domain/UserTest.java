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
        int auth = 1;
        String password = "qwer1234";
        Nickname nickname = new Nickname("dust");
        int status = 0;
        User user1 = User.createUser(role, email, auth,password, nickname, status);


        assertThat(user1.getRole()).isEqualTo(role);
        assertThat(user1.getEmailValue()).isEqualTo(email.getValue());
        assertThat(user1.getAuth()).isEqualTo(auth);
        assertThat(user1.getPassword()).isEqualTo(password);
        assertThat(user1.getNicknameValue()).isEqualTo(nickname.getValue());
        assertThat(user1.getStatus()).isEqualTo(status);

    }

    @Test
    @DisplayName("User 닉네임을 변경한다.")
    void updateNickname(){
        //given
        Role role = Role.USER;
        Email email = new Email("qwer@gmail.com");
        int auth = 1;
        String password = "qwer1234";
        Nickname nickname = new Nickname("dust");
        int status = 0;
        User user1 = User.createUser(role, email, auth,password, nickname, status);

        //when
        String change = "user1234";
        user1.updateNickname(change);

        //then
        assertThat(user1.getNicknameValue()).isEqualTo(change);
    }

    @Test
    @DisplayName("이메일로 동일 사용자인지 확인한다.")
    void isSameUser(){
        //given
        Role role = Role.USER;
        Email email = new Email("qwer@gmail.com");
        int auth = 1;
        String password = "qwer1234";
        Nickname nickname = new Nickname("dust");
        int status = 0;
        final User user = User.createUser(role, email, auth,password, nickname, status);
        User user1 = User.createUser(role, email, auth,password, nickname, status);
        User user2 = User.createUser(role, new Email("dust@gmail.com"),
                auth,password, nickname, status);

        //when
        boolean result1 = user.isSameUser(user1);
        boolean result2 = user.isSameUser(user2);

        //then
        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
    }
}
