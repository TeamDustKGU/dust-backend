package TeamDustKGU.dustbackend.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER("USER"),
    ADMIN("ADMIN");

    private String role;

    private Role(String role){
        this.role=role;
    }
}
