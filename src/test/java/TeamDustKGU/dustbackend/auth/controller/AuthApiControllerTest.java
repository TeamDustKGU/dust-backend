package TeamDustKGU.dustbackend.auth.controller;

import TeamDustKGU.dustbackend.auth.controller.dto.request.AuthRequest;
import TeamDustKGU.dustbackend.auth.exception.AuthErrorCode;
import TeamDustKGU.dustbackend.auth.service.dto.response.TokenResponse;
import TeamDustKGU.dustbackend.common.ControllerTest;
import TeamDustKGU.dustbackend.global.exception.DustException;
import TeamDustKGU.dustbackend.user.exception.UserErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static TeamDustKGU.dustbackend.auth.utils.TokenUtils.*;
import static TeamDustKGU.dustbackend.fixture.UserFixture.CHAERIN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Auth [Controller Layer] -> AuthApiController 테스트")
class AuthApiControllerTest extends ControllerTest {
    @Nested
    @DisplayName("회원가입 API [POST /api/user/signup]")
    class signup {
        private static final String BASE_URL = "/api/user/signup";

        @Test
        @DisplayName("중복되는 이메일이 있으면 회원가입에 실패한다")
        void throwExceptionByDuplicateEmail() throws Exception {
            // given
            doThrow(DustException.type(UserErrorCode.DUPLICATE_EMAIL))
                    .when(authService)
                    .signup(any());

            // when
            final AuthRequest request = createAuthRequest();
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .post(BASE_URL)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

            // then
            final UserErrorCode expectedError = UserErrorCode.DUPLICATE_EMAIL;
            mockMvc.perform(requestBuilder)
                    .andExpectAll(
                            status().isConflict(),
                            jsonPath("$.status").exists(),
                            jsonPath("$.status").value(expectedError.getStatus().value()),
                            jsonPath("$.errorCode").exists(),
                            jsonPath("$.errorCode").value(expectedError.getErrorCode()),
                            jsonPath("$.message").exists(),
                            jsonPath("$.message").value(expectedError.getMessage())
                    )
                    .andDo(
                            document(
                                    "AuthApi/Signup/Failure/Case1",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    requestFields(
                                            fieldWithPath("email").description("이메일"),
                                            fieldWithPath("password").description("비밀번호")
                                    ),
                                    responseFields(
                                            fieldWithPath("status").description("HTTP 상태 코드"),
                                            fieldWithPath("errorCode").description("커스텀 예외 코드"),
                                            fieldWithPath("message").description("예외 메시지")
                                    )
                            )
                    );
        }

        @Test
        @DisplayName("이미 이메일 인증을 요청한 회원이라면 회원가입에 실패한다")
        void throwExceptionByAlreadyEmailAuth() throws Exception {
            // given
            doThrow(DustException.type(AuthErrorCode.ALREADY_EMAIL_AUTH))
                    .when(authService)
                    .signup(any());

            // when
            final AuthRequest request = createAuthRequest();
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .post(BASE_URL)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

            // then
            final AuthErrorCode expectedError = AuthErrorCode.ALREADY_EMAIL_AUTH;
            mockMvc.perform(requestBuilder)
                    .andExpectAll(
                            status().isConflict(),
                            jsonPath("$.status").exists(),
                            jsonPath("$.status").value(expectedError.getStatus().value()),
                            jsonPath("$.errorCode").exists(),
                            jsonPath("$.errorCode").value(expectedError.getErrorCode()),
                            jsonPath("$.message").exists(),
                            jsonPath("$.message").value(expectedError.getMessage())
                    )
                    .andDo(
                            document(
                                    "AuthApi/Signup/Failure/Case2",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    requestFields(
                                            fieldWithPath("email").description("이메일"),
                                            fieldWithPath("password").description("비밀번호")
                                    ),
                                    responseFields(
                                            fieldWithPath("status").description("HTTP 상태 코드"),
                                            fieldWithPath("errorCode").description("커스텀 예외 코드"),
                                            fieldWithPath("message").description("예외 메시지")
                                    )
                            )
                    );
        }

        @Test
        @DisplayName("비밀번호 형식이 맞지 않으면 회원가입에 실패한다")
        void throwExceptionByInvalidPasswordPattern() throws Exception {
            // given
            doThrow(DustException.type(UserErrorCode.INVALID_PASSWORD_PATTERN))
                    .when(authService)
                    .signup(any());

            // when
            final AuthRequest request = createAuthRequest();
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .post(BASE_URL)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

            // then
            final UserErrorCode expectedError = UserErrorCode.INVALID_PASSWORD_PATTERN;
            mockMvc.perform(requestBuilder)
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$.status").exists(),
                            jsonPath("$.status").value(expectedError.getStatus().value()),
                            jsonPath("$.errorCode").exists(),
                            jsonPath("$.errorCode").value(expectedError.getErrorCode()),
                            jsonPath("$.message").exists(),
                            jsonPath("$.message").value(expectedError.getMessage())
                    )
                    .andDo(
                            document(
                                    "AuthApi/Signup/Failure/Case3",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    requestFields(
                                            fieldWithPath("email").description("이메일"),
                                            fieldWithPath("password").description("비밀번호")
                                    ),
                                    responseFields(
                                            fieldWithPath("status").description("HTTP 상태 코드"),
                                            fieldWithPath("errorCode").description("커스텀 예외 코드"),
                                            fieldWithPath("message").description("예외 메시지")
                                    )
                            )
                    );
        }


        @Test
        @DisplayName("회원가입에 성공한다")
        void success() throws Exception {
            // given
            given(authService.signup(any())).willReturn(1L);

            // when
            final AuthRequest request = createAuthRequest();
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .post(BASE_URL)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

            // then
            mockMvc.perform(requestBuilder)
                    .andExpectAll(
                            status().isOk()
                    )
                    .andDo(
                            document(
                                    "AuthApi/Signup/Success",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    requestFields(
                                            fieldWithPath("email").description("이메일"),
                                            fieldWithPath("password").description("비밀번호")
                                    )
                            )
                    );
        }
    }

    @Nested
    @DisplayName("이메일 인증 API [GET /api/user/signup/email-confirm]")
    class emailConfirm {
        private static final String BASE_URL = "/api/user/signup/email-confirm";

        @Test
        @DisplayName("해당 이메일에 대해 유효한 이메일 인증 정보가 없으면 이메일 인증 완료에 실패한다")
        void throwExceptionByEmailAuthNotFound() throws Exception {
            // given
            doThrow(DustException.type(AuthErrorCode.EMAIL_AUTH_NOT_FOUND))
                    .when(emailAuthService)
                    .updateAuthStatus(any(), any());

            // when
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .get(BASE_URL)
                    .param("email", "example@example.com")
                    .param("auth-token", "your-auth-token");

            // then
            final AuthErrorCode expectedError = AuthErrorCode.EMAIL_AUTH_NOT_FOUND;
            mockMvc.perform(requestBuilder)
                    .andExpectAll(
                            status().isNotFound(),
                            jsonPath("$.status").exists(),
                            jsonPath("$.status").value(expectedError.getStatus().value()),
                            jsonPath("$.errorCode").exists(),
                            jsonPath("$.errorCode").value(expectedError.getErrorCode()),
                            jsonPath("$.message").exists(),
                            jsonPath("$.message").value(expectedError.getMessage())
                    )
                    .andDo(
                            document(
                                    "AuthApi/EmailConfirm/Failure/Case1",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    requestParameters(
                                            parameterWithName("email").description("이메일 인증을 할 Email"),
                                            parameterWithName("auth-token").description("이메일 인증을 위한 Auth token")
                                    ),
                                    responseFields(
                                            fieldWithPath("status").description("HTTP 상태 코드"),
                                            fieldWithPath("errorCode").description("커스텀 예외 코드"),
                                            fieldWithPath("message").description("예외 메시지")
                                    )
                            )
                    );
        }

        @Test
        @DisplayName("회원이 조회되지 않으면 이메일 인증 완료에 실패한다")
        void throwExceptionByUserNotFound() throws Exception {
            // given
            doThrow(DustException.type(UserErrorCode.USER_NOT_FOUND))
                    .when(emailAuthService)
                    .updateAuthStatus(any(), any());

            // when
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .get(BASE_URL)
                    .param("email", "example@example.com")
                    .param("auth-token", "your-auth-token");

            // then
            final UserErrorCode expectedError = UserErrorCode.USER_NOT_FOUND;
            mockMvc.perform(requestBuilder)
                    .andExpectAll(
                            status().isNotFound(),
                            jsonPath("$.status").exists(),
                            jsonPath("$.status").value(expectedError.getStatus().value()),
                            jsonPath("$.errorCode").exists(),
                            jsonPath("$.errorCode").value(expectedError.getErrorCode()),
                            jsonPath("$.message").exists(),
                            jsonPath("$.message").value(expectedError.getMessage())
                    )
                    .andDo(
                            document(
                                    "AuthApi/EmailConfirm/Failure/Case2",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    requestParameters(
                                            parameterWithName("email").description("이메일 인증을 할 Email"),
                                            parameterWithName("auth-token").description("이메일 인증을 위한 Auth token")
                                    ),
                                    responseFields(
                                            fieldWithPath("status").description("HTTP 상태 코드"),
                                            fieldWithPath("errorCode").description("커스텀 예외 코드"),
                                            fieldWithPath("message").description("예외 메시지")
                                    )
                            )
                    );
        }

        @Test
        @DisplayName("이메일 인증에 성공한다")
        void success() throws Exception {
            // given
            doNothing()
                    .when(emailAuthService)
                    .updateAuthStatus(any(), anyString());

            // when
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .get(BASE_URL)
                    .param("email", "example@example.com")
                    .param("auth-token", "your-auth-token");

            // then
            mockMvc.perform(requestBuilder)
                    .andExpectAll(
                            status().isOk()
                    )
                    .andDo(
                            document(
                                    "AuthApi/EmailConfirm/Success",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    requestParameters(
                                            parameterWithName("email").description("이메일 인증을 할 Email"),
                                            parameterWithName("auth-token").description("이메일 인증을 위한 Auth token")
                                    )
                            )
                    );
        }
    }

    @Nested
    @DisplayName("로그인 API [POST /api/user/login]")
    class login {
        private static final String BASE_URL = "/api/user/login";

        @Test
        @DisplayName("이메일로 사용자 정보를 찾을 수 없으면 로그인에 실패한다")
        void throwExceptionByUserNotFound() throws Exception {
            // given
            doThrow(DustException.type(UserErrorCode.USER_NOT_FOUND))
                    .when(authService)
                    .login(any(), any());

            // when
            final AuthRequest request = createAuthRequest();
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .post(BASE_URL)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

            // then
            final UserErrorCode expectedError = UserErrorCode.USER_NOT_FOUND;
            mockMvc.perform(requestBuilder)
                    .andExpectAll(
                            status().isNotFound(),
                            jsonPath("$.status").exists(),
                            jsonPath("$.status").value(expectedError.getStatus().value()),
                            jsonPath("$.errorCode").exists(),
                            jsonPath("$.errorCode").value(expectedError.getErrorCode()),
                            jsonPath("$.message").exists(),
                            jsonPath("$.message").value(expectedError.getMessage())
                    )
                    .andDo(
                            document(
                                    "AuthApi/Login/Failure/Case1",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    requestFields(
                                            fieldWithPath("email").description("이메일"),
                                            fieldWithPath("password").description("비밀번호")
                                    ),
                                    responseFields(
                                            fieldWithPath("status").description("HTTP 상태 코드"),
                                            fieldWithPath("errorCode").description("커스텀 예외 코드"),
                                            fieldWithPath("message").description("예외 메시지")
                                    )
                            )
                    );
        }

        @Test
        @DisplayName("비밀번호가 일치하지 않으면 로그인에 실패한다")
        void throwExceptionByWrongPassword() throws Exception {
            // given
            doThrow(DustException.type(AuthErrorCode.WRONG_PASSWORD))
                    .when(authService)
                    .login(any(), any());

            // when
            final AuthRequest request = createAuthRequest();
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .post(BASE_URL)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

            // then
            final AuthErrorCode expectedError = AuthErrorCode.WRONG_PASSWORD;
            mockMvc.perform(requestBuilder)
                    .andExpectAll(
                            status().isUnauthorized(),
                            jsonPath("$.status").exists(),
                            jsonPath("$.status").value(expectedError.getStatus().value()),
                            jsonPath("$.errorCode").exists(),
                            jsonPath("$.errorCode").value(expectedError.getErrorCode()),
                            jsonPath("$.message").exists(),
                            jsonPath("$.message").value(expectedError.getMessage())
                    )
                    .andDo(
                            document(
                                    "AuthApi/Login/Failure/Case2",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    requestFields(
                                            fieldWithPath("email").description("이메일"),
                                            fieldWithPath("password").description("비밀번호")
                                    ),
                                    responseFields(
                                            fieldWithPath("status").description("HTTP 상태 코드"),
                                            fieldWithPath("errorCode").description("커스텀 예외 코드"),
                                            fieldWithPath("message").description("예외 메시지")
                                    )
                            )
                    );
        }

        @Test
        @DisplayName("비밀번호가 일치하지 않으면 로그인에 실패한다")
        void throwExceptionByEmailAuthNotDone() throws Exception {
            // given
            doThrow(DustException.type(AuthErrorCode.EMAIL_AUTH_NOT_DONE))
                    .when(authService)
                    .login(any(), any());

            // when
            final AuthRequest request = createAuthRequest();
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .post(BASE_URL)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

            // then
            final AuthErrorCode expectedError = AuthErrorCode.EMAIL_AUTH_NOT_DONE;
            mockMvc.perform(requestBuilder)
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$.status").exists(),
                            jsonPath("$.status").value(expectedError.getStatus().value()),
                            jsonPath("$.errorCode").exists(),
                            jsonPath("$.errorCode").value(expectedError.getErrorCode()),
                            jsonPath("$.message").exists(),
                            jsonPath("$.message").value(expectedError.getMessage())
                    )
                    .andDo(
                            document(
                                    "AuthApi/Login/Failure/Case3",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    requestFields(
                                            fieldWithPath("email").description("이메일"),
                                            fieldWithPath("password").description("비밀번호")
                                    ),
                                    responseFields(
                                            fieldWithPath("status").description("HTTP 상태 코드"),
                                            fieldWithPath("errorCode").description("커스텀 예외 코드"),
                                            fieldWithPath("message").description("예외 메시지")
                                    )
                            )
                    );
        }

        @Test
        @DisplayName("로그인에 성공한다")
        void success() throws Exception {
            // given
            TokenResponse tokenResponse = createTokenResponse();
            given(authService.login(any(), any())).willReturn(tokenResponse);

            // when
            final AuthRequest request = createAuthRequest();
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .post(BASE_URL)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

            // then
            mockMvc.perform(requestBuilder)
                    .andExpectAll(
                            status().isOk()
                    )
                    .andDo(
                            document(
                                    "AuthApi/Login/Success",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    requestFields(
                                            fieldWithPath("email").description("이메일"),
                                            fieldWithPath("password").description("비밀번호")
                                    ),
                                    responseFields(
                                            fieldWithPath("accessToken").description("발급된 Access Token"),
                                            fieldWithPath("refreshToken").description("발급된 Refresh Token")
                                    )
                            )
                    );
        }
    }

    @Nested
    @DisplayName("로그아웃 API [GET /api/user/logout]")
    class logout {
        private static final String BASE_URL = "/api/user/logout";
        private static final Long USER_ID = 1L;

        @Test
        @DisplayName("Authorization Header에 AccessToken이 없으면 로그아웃에 실패한다")
        void withoutAccessToken() throws Exception {
            // when
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .get(BASE_URL);

            // then
            final AuthErrorCode expectedError = AuthErrorCode.INVALID_PERMISSION;
            mockMvc.perform(requestBuilder)
                    .andExpectAll(
                            status().isForbidden(),
                            jsonPath("$.status").exists(),
                            jsonPath("$.status").value(expectedError.getStatus().value()),
                            jsonPath("$.errorCode").exists(),
                            jsonPath("$.errorCode").value(expectedError.getErrorCode()),
                            jsonPath("$.message").exists(),
                            jsonPath("$.message").value(expectedError.getMessage())
                    )
                    .andDo(
                            document(
                                    "AuthApi/Logout/Failure/Case1",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    responseFields(
                                            fieldWithPath("status").description("HTTP 상태 코드"),
                                            fieldWithPath("errorCode").description("커스텀 예외 코드"),
                                            fieldWithPath("message").description("예외 메시지")
                                    )
                            )
                    );
        }

        @Test
        @DisplayName("로그아웃에 성공한다")
        void success() throws Exception {
            // given
            given(jwtTokenProvider.isTokenValid(anyString())).willReturn(true);
            given(jwtTokenProvider.getId(anyString())).willReturn(USER_ID);

            // when
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .get(BASE_URL)
                    .header(AUTHORIZATION, BEARER_TOKEN + REFRESH_TOKEN);

            // then
            mockMvc.perform(requestBuilder)
                    .andExpectAll(
                            status().isNoContent()
                    )
                    .andDo(
                            document(
                                    "AuthApi/Logout/Success",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    requestHeaders(
                                            headerWithName(AUTHORIZATION).description("Access Token")
                                    )
                            )
                    );
        }
    }

    private AuthRequest createAuthRequest() {
        return new AuthRequest(CHAERIN.getEmail(), CHAERIN.getPassword());
    }
}