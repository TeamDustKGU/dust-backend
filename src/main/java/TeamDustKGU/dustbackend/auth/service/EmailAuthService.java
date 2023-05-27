package TeamDustKGU.dustbackend.auth.service;

import TeamDustKGU.dustbackend.auth.domain.EmailAuth;
import TeamDustKGU.dustbackend.auth.domain.EmailAuthRepository;
import TeamDustKGU.dustbackend.auth.exception.AuthErrorCode;
import TeamDustKGU.dustbackend.global.exception.DustException;
import TeamDustKGU.dustbackend.user.domain.Email;
import TeamDustKGU.dustbackend.user.domain.User;
import TeamDustKGU.dustbackend.user.service.UserFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@EnableAsync
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmailAuthService {
    private final JavaMailSender javaMailSender;
    private final EmailAuthRepository emailAuthRepository;
    private final UserFindService userFindService;

    @Transactional
    public String create(String email) {
        validateEmailAuth(email);

        EmailAuth emailAuth = emailAuthRepository.save(EmailAuth.createEmailAuth(email, UUID.randomUUID().toString()));
        return emailAuth.getAuthToken();
    }

    private void validateEmailAuth(String email) {
        if (emailAuthRepository.existValidAuthByEmail(email, LocalDateTime.now())) {
            throw DustException.type(AuthErrorCode.ALREADY_EMAIL_AUTH);
        }
    }

    @Async
    public void send(String email, String authToken) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("<회원가입 이메일 인증>");
        simpleMailMessage.setText("http://localhost:8080/api/signup/confirm-mail?email="+email+"&auth-token="+authToken);

        javaMailSender.send(simpleMailMessage);
    }

    @Transactional
    public void updateAuthStatus(Email email, String authToken) {
        EmailAuth emailAuth = emailAuthRepository.findValidAuthByEmail(email.getValue(), authToken, LocalDateTime.now())
                .orElseThrow(() -> DustException.type(AuthErrorCode.EMAIL_AUTH_NOT_FOUND));
        emailAuth.useToken();

        User user = userFindService.findByEmail(email);
        user.authenticate();
    }
}
