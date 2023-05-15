package TeamDustKGU.dustbackend.fixture;

import TeamDustKGU.dustbackend.user.domain.Email;
import TeamDustKGU.dustbackend.user.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserFixture {
    SUNKYOUNG("abc@naver.com", "1234"),
    CHAERIN("def@gmail.com", "123456789"),
    SEOKHO("ghi123@kakao.com", "hihihi"),
    NAHYUN("jklmnop@kyonggi.ac.kr", "abc123!")
    ;

    private final String email;
    private final String password;

    public User toUser() {
        return User.createUser(Email.from(email), password);
    }
}
