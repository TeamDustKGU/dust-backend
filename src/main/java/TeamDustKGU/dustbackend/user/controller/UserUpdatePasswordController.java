package TeamDustKGU.dustbackend.user.controller;

import TeamDustKGU.dustbackend.user.domain.User;
import TeamDustKGU.dustbackend.user.service.PasswordUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class UserUpdatePasswordController {
    private final PasswordUpdateService passwordUpdateService;

    @GetMapping("/updatePassword/{id}/{password}")
    public void updatePassword(@PathVariable Long userId, @PathVariable String password) {
        User user = userFindService.findById(userId);
        passwordUpdateService.updatePassword(user, password);
    }
}
