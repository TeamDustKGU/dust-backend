package TeamDustKGU.dustbackend.user.domain;

import TeamDustKGU.dustbackend.board.domain.Board;
import TeamDustKGU.dustbackend.global.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.PERSIST;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="users")
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Embedded
    private Email email;

    @Column(name = "auth", nullable = false) // 미인증 0, 인증 1
    private int auth;

    @Column(name = "password", nullable = false)
    private Password password;

    @Embedded
    @AttributeOverride(name = "modifiedDate", column = @Column(name = "nickname_modifiedDate"))
    private Nickname nickname;

    @Column(name = "status", nullable = false) // 비활성화 0, 활성화 1
    private int status;

    // 회원 탈퇴시 작성한 게시글 모두 삭제
    @OneToMany(mappedBy = "writer", cascade = PERSIST, orphanRemoval = true)
    private List<Board> boardList = new ArrayList<>();

    @Builder
    public User(Email email, Password password){
        this.role = Role.USER;
        this.email = email;
        this.auth = 0;
        this.password = password;
        this.nickname = null;
        this.status = 1;
    }

    public static User createUser(Email email, Password password){
        return new User(email, password);
    }

    public static User createAdmin(Email email, Password password) {
        User user = createUser(email, password);
        user.role = Role.ADMIN;
        return user;
    }

    public void updateNickname(String updateNickname) {
        if (this.nickname == null) {
            this.nickname = Nickname.from(updateNickname);
        } else {
            this.nickname.update(updateNickname);
        }
    }

    public boolean isSameUser(User user){
        return this.email.isSameEmail(user.getEmail());
    }

    public void authenticate() {
        this.auth = 1;
    }

    // Add Getter
    public String getEmailValue(){
        return email.getValue();
    }

    public String getNicknameValue(){
        if (nickname == null) {
            return "A-"+id.toString();
        }
        return nickname.getValue();
    }

    public void activate() {
        this.status = 1;
    }

    public void deactivate() {
        this.status = 0;
    }
}
