package TeamDustKGU.dustbackend.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Nickname VO 테스트")
public class NicknameTest {
    @Test
    @DisplayName("닉네임을 변경한다.")
    void updateNickname(){
        //given
        Nickname nickname = new Nickname("dust");

        //when
        String change = "dustChange";
        nickname.updateNickname(change);

        //then
        assertThat(nickname.getValue()).isEqualTo(change);
    }
}
