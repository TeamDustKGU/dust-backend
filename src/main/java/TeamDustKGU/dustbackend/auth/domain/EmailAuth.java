package TeamDustKGU.dustbackend.auth.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "email_auth")
public class EmailAuth {
    private static final Long MAX_EXPIRE_TIME = 5L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String authToken;

    private Boolean expired;

    private LocalDateTime expireDate;

    @Builder
    public EmailAuth(String email, String authToken, Boolean expired, LocalDateTime expireDate) {
        this.email = email;
        this.authToken = authToken;
        this.expired = expired;
        this.expireDate = expireDate;
    }

    public static EmailAuth createEmailAuth(String email, String authToken) {
        return EmailAuth.builder()
                .email(email)
                .authToken(authToken)
                .expired(false)
                .expireDate(LocalDateTime.now().plusMinutes(MAX_EXPIRE_TIME))
                .build();
    }

    public void useToken() {
        this.expired = true;
    }
}
