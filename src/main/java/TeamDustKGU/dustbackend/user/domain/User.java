package TeamDustKGU.dustbackend.user.domain;

import TeamDustKGU.dustbackend.global.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @Column(name = "auth", nullable = false) // 인증 0, 미인증 1
    private int auth;

    @Column(name = "password", nullable = false)
    private String password;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "createdDate", column = @Column(name = "nickname_createdDate")),
            @AttributeOverride(name = "modifiedDate", column = @Column(name = "nickname_modifiedDate"))
    })
    private Nickname nickname;

    @Column(name = "status", nullable = false) // 활성화 0, 비활성화 1
    private int status;

    @Builder
    public User(Email email, String password){
        this.role = Role.USER;
        this.email = email;
        this.auth = 1;
        this.password = password;
        this.nickname = null;
        this.status = 0;
    }

    public static User createUser(Email email, String password){
        return new User(email, password);
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

    // Add Getter
    public String getEmailValue(){
        return email.getValue();
    }

    public String getNicknameValue(){
        if (nickname == null) {
            return new String("A-"+id.toString());
        }
        return nickname.getValue();
    }
}