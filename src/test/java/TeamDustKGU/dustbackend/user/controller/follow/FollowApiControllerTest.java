package TeamDustKGU.dustbackend.user.controller.follow;

import TeamDustKGU.dustbackend.auth.exception.AuthErrorCode;
import TeamDustKGU.dustbackend.common.ControllerTest;
import TeamDustKGU.dustbackend.global.exception.DustException;
import TeamDustKGU.dustbackend.user.controller.dto.request.FollowRequest;
import TeamDustKGU.dustbackend.user.exception.UserErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static TeamDustKGU.dustbackend.auth.utils.TokenUtils.BEARER_TOKEN;
import static TeamDustKGU.dustbackend.auth.utils.TokenUtils.REFRESH_TOKEN;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("User [Controller Layer] -> FollowApiController 테스트")
public class FollowApiControllerTest extends ControllerTest {
    @Nested
    @DisplayName("관심유저 등록 API [POST /api/user/follow/{followerId}]")
    class register {
        private static final String BASE_URL = "/api/user/follow/{followerId}";
        private static final Long FOLLOWING_ID = 1L;
        private static final Long FOLLOWER_ID = 2L;

        @Test
        @DisplayName("Authorization Header에 AccessToken이 없으면 관심유저 등록에 실패한다")
        void withoutAccessToken() throws Exception {
            // when
            final FollowRequest request = createFollowRequest();
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .post(BASE_URL, FOLLOWER_ID)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

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
                                    "UserApi/Follow/Register/Failure/Case1",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    pathParameters(
                                            parameterWithName("followerId").description("관심유저로 등록될 사용자 ID(PK)")
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
        @DisplayName("본인을 관심유저로 등록할 수 없다")
        void throwExceptionBySelfFollowNotAllowed() throws Exception {
            // given
            given(jwtTokenProvider.isTokenValid(anyString())).willReturn(true);
            given(jwtTokenProvider.getId(anyString())).willReturn(FOLLOWING_ID);
            doThrow(DustException.type(UserErrorCode.SELF_FOLLOW_NOT_ALLOWED))
                    .when(followService)
                    .register(anyLong(), anyLong(), any(), any());

            // when
            final FollowRequest request = createFollowRequest();
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .post(BASE_URL, FOLLOWER_ID)
                    .header(AUTHORIZATION, BEARER_TOKEN + REFRESH_TOKEN)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

            // then
            final UserErrorCode expectedError = UserErrorCode.SELF_FOLLOW_NOT_ALLOWED;
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
                                    "UserApi/Follow/Register/Failure/Case2",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    pathParameters(
                                            parameterWithName("followerId").description("관심유저로 등록될 사용자 ID(PK)")
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
        @DisplayName("한 사용자에게 두 번이상 관심유저로 등록할 수 없다")
        void throwExceptionByAlreadyFollow() throws Exception {
            // given
            given(jwtTokenProvider.isTokenValid(anyString())).willReturn(true);
            given(jwtTokenProvider.getId(anyString())).willReturn(FOLLOWING_ID);
            doThrow(DustException.type(UserErrorCode.ALREADY_FOLLOW))
                    .when(followService)
                    .register(anyLong(), anyLong(), any(), any());

            // when
            final FollowRequest request = createFollowRequest();
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .post(BASE_URL, FOLLOWER_ID)
                    .header(AUTHORIZATION, BEARER_TOKEN + REFRESH_TOKEN)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

            // then
            final UserErrorCode expectedError = UserErrorCode.ALREADY_FOLLOW;
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
                                    "UserApi/Follow/Register/Failure/Case3",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    pathParameters(
                                            parameterWithName("followerId").description("관심유저로 등록될 사용자 ID(PK)")
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
        @DisplayName("관심유저 등록에 성공한다")
        void success() throws Exception {
            // given
            given(jwtTokenProvider.isTokenValid(anyString())).willReturn(true);
            given(jwtTokenProvider.getId(anyString())).willReturn(FOLLOWING_ID);
            doReturn(1L)
                    .when(followService)
                    .register(anyLong(), anyLong(), any(), any());

            // when
            final FollowRequest request = createFollowRequest();
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .post(BASE_URL, FOLLOWER_ID)
                    .header(AUTHORIZATION, BEARER_TOKEN + REFRESH_TOKEN)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

            // then
            mockMvc.perform(requestBuilder)
                    .andExpectAll(
                            status().isNoContent()
                    )
                    .andDo(
                            document(
                                    "UserApi/Follow/Register/Success",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    pathParameters(
                                            parameterWithName("followerId").description("관심유저로 등록될 회원 ID(PK)")
                                    ),
                                    requestFields(
                                            fieldWithPath("boardTitle").description("관심유저로 설정한 회원의 게시글 제목"),
                                            fieldWithPath("boardCreatedDate").description("관심유저로 설정한 회원의 게시글 생성 날짜")
                                    )
                            )
                    );
        }
    }

    @Nested
    @DisplayName("관심유저 취소 API [DELETE /api/user/follow/{followerId}]")
    class cancel {
        private static final String BASE_URL = "/api/user/follow/{followerId}";
        private static final Long FOLLOWING_ID = 1L;
        private static final Long FOLLOWER_ID = 2L;

        @Test
        @DisplayName("Authorization Header에 AccessToken이 없으면 관심유저 취소에 실패한다")
        void withoutAccessToken() throws Exception {
            // when
            final FollowRequest request = createFollowRequest();
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .delete(BASE_URL, FOLLOWER_ID)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

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
                                    "UserApi/Follow/Cancel/Failure/Case1",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    pathParameters(
                                            parameterWithName("followerId").description("관심유저로 등록될 사용자 ID(PK)")
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
        @DisplayName("관심유저로 등록되어 있지 않은 회원을 취소할 수 없다")
        void throwExceptionByFollowNotFound() throws Exception {
            // given
            given(jwtTokenProvider.isTokenValid(anyString())).willReturn(true);
            given(jwtTokenProvider.getId(anyString())).willReturn(FOLLOWING_ID);
            doThrow(DustException.type(UserErrorCode.FOLLOW_NOT_FOUND))
                    .when(followService)
                    .cancel(anyLong(), anyLong());

            // when
            final FollowRequest request = createFollowRequest();
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .delete(BASE_URL, FOLLOWER_ID)
                    .header(AUTHORIZATION, BEARER_TOKEN + REFRESH_TOKEN)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

            // then
            final UserErrorCode expectedError = UserErrorCode.FOLLOW_NOT_FOUND;
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
                                    "UserApi/Follow/Cancel/Failure/Case2",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    pathParameters(
                                            parameterWithName("followerId").description("관심유저로 등록될 사용자 ID(PK)")
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
        @DisplayName("관심유저 취소에 성공한다")
        void success() throws Exception {
            // given
            given(jwtTokenProvider.isTokenValid(anyString())).willReturn(true);
            given(jwtTokenProvider.getId(anyString())).willReturn(FOLLOWING_ID);
            doNothing()
                    .when(followService)
                    .cancel(anyLong(), anyLong());

            // when
            final FollowRequest request = createFollowRequest();
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .delete(BASE_URL, FOLLOWER_ID)
                    .header(AUTHORIZATION, BEARER_TOKEN + REFRESH_TOKEN)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

            // then
            mockMvc.perform(requestBuilder)
                    .andExpectAll(
                            status().isOk()
                    )
                    .andDo(
                            document(
                                    "UserApi/Follow/Cancel/Success",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    pathParameters(
                                            parameterWithName("followerId").description("관심유저로 등록될 사용자 ID(PK)")
                                    )
                            )
                    );
        }
    }

    private FollowRequest createFollowRequest() {
        return new FollowRequest("익명의 게시글", LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT)
    );
}
}
