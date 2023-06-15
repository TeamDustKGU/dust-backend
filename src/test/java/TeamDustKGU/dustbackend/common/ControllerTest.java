package TeamDustKGU.dustbackend.common;

import TeamDustKGU.dustbackend.auth.controller.AuthApiController;
import TeamDustKGU.dustbackend.auth.controller.TokenReissueApiController;
import TeamDustKGU.dustbackend.auth.service.AuthService;
import TeamDustKGU.dustbackend.auth.service.EmailAuthService;
import TeamDustKGU.dustbackend.auth.service.TokenManager;
import TeamDustKGU.dustbackend.auth.service.TokenReissueService;
import TeamDustKGU.dustbackend.auth.utils.JwtTokenProvider;
import TeamDustKGU.dustbackend.board.controller.BoardApiController;
import TeamDustKGU.dustbackend.board.controller.like.BoardLikeApiController;
import TeamDustKGU.dustbackend.board.service.BoardFindService;
import TeamDustKGU.dustbackend.board.service.BoardService;
import TeamDustKGU.dustbackend.board.service.like.BoardLikeService;
import TeamDustKGU.dustbackend.comment.controller.CommentApiController;
import TeamDustKGU.dustbackend.comment.service.CommentService;
import TeamDustKGU.dustbackend.global.config.WebSecurityConfig;
import TeamDustKGU.dustbackend.user.controller.UserUpdateApiController;
import TeamDustKGU.dustbackend.user.controller.follow.FollowApiController;
import TeamDustKGU.dustbackend.user.controller.suspension.SuspensionApiController;
import TeamDustKGU.dustbackend.user.service.UserFindService;
import TeamDustKGU.dustbackend.user.service.UserUpdateService;
import TeamDustKGU.dustbackend.user.service.follow.FollowService;
import TeamDustKGU.dustbackend.user.service.suspension.SuspensionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

@ImportAutoConfiguration(WebSecurityConfig.class)
@WebMvcTest({
        TokenReissueApiController.class,
        FollowApiController.class,
        UserUpdateApiController.class,
        BoardApiController.class,
        CommentApiController.class,
        SuspensionApiController.class,
        BoardApiController.class,
        BoardLikeApiController.class,
        AuthApiController.class
})
@AutoConfigureRestDocs
public abstract class ControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected JwtTokenProvider jwtTokenProvider;

    @MockBean
    protected TokenReissueService tokenReissueService;

    @MockBean
    protected UserFindService userFindService;

    @MockBean
    protected FollowService followService;

    @MockBean
    protected UserUpdateService userUpdateService;

    @MockBean
    protected SuspensionService suspensionService;
    
    @MockBean
    protected BoardFindService boardFindService;

    @MockBean
    protected BoardService boardService;

    @MockBean
    protected CommentService commentService;

    @MockBean
    protected BoardLikeService boardLikeService;

    @MockBean
    protected TokenManager tokenManager;

    @MockBean
    protected AuthService authService;

    @MockBean
    protected EmailAuthService emailAuthService;

    protected OperationRequestPreprocessor applyRequestPreprocessor() {
        return preprocessRequest(prettyPrint());
    }

    protected OperationResponsePreprocessor applyResponsePreprocessor() {
        return preprocessResponse(prettyPrint());
    }

    protected String convertObjectToJson(Object data) throws JsonProcessingException {
        return objectMapper.writeValueAsString(data);
    }
}
