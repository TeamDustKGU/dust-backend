package TeamDustKGU.dustbackend.user.domain;

import TeamDustKGU.dustbackend.global.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
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

    @Column(name = "auth", nullable = false)
    //인증 0, 미인증 1
    private int auth = 1;

    @Column(name = "password", nullable = false)
    private String password;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "createdDate", column = @Column(name = "nickname_createdDate")),
            @AttributeOverride(name = "modifiedDate", column = @Column(name = "nickname_modifiedDate"))
    })
    private Nickname nickname;

    //활성화 0, 비활성화 1
    @Column(name = "status", nullable = false)
    private int status = 0;

    @Builder
    public User(Role role, Email email, String password, Nickname nickname){
        this.role=role;
        this.email=email;
        this.password=password;
        this.nickname=nickname;
    }

    public static User createUser(Role role, Email email,
                                  String password, Nickname nickname){
        return new User(role, email, password, nickname);
    }

    public void updateNickname(String updateNickname){
        this.nickname = this.nickname.updateNickname(updateNickname);
    }

    public boolean isSameUser(User user){
        return this.email.isSameEmail(user.getEmail());
    }

    //Add Getter
    public String getEmailValue(){
        return email.getValue();
    }

    public String getNicknameValue(){
        return nickname.getValue();
    }
}