package TeamDustKGU.dustbackend.user.controller.suspension;

import TeamDustKGU.dustbackend.auth.exception.AuthErrorCode;
import TeamDustKGU.dustbackend.common.ControllerTest;
import TeamDustKGU.dustbackend.global.exception.DustException;
import TeamDustKGU.dustbackend.user.controller.dto.request.SuspensionRequest;
import TeamDustKGU.dustbackend.user.exception.UserErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static TeamDustKGU.dustbackend.auth.utils.TokenUtils.BEARER_TOKEN;
import static TeamDustKGU.dustbackend.auth.utils.TokenUtils.REFRESH_TOKEN;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("User [Controller Layer] -> SuspensionApiController 테스트")
public class SuspensionApiControllerTest extends ControllerTest {
    @Nested
    @DisplayName("User 비활성화 API [POST /api/user/suspension/{suspendedId}]")
    class suspend {
        private static final String BASE_URL = "/api/user/suspension/{suspendedId}";
        private static final Long SUSPENDED_ID = 1L;
        private static final Long ADMIN_ID = 2L;

        @Test
        @DisplayName("Authorization Header에 AccessToken이 없으면 유저 비활성화에 실패한다")
        void withoutAccessToken() throws Exception {
            // when
            final SuspensionRequest request = createSuspensionRequest();
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .post(BASE_URL, SUSPENDED_ID)
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
                                    "UserApi/Suspension/Suspend/Failure/Case1",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    pathParameters(
                                            parameterWithName("suspendedId").description("비활성화될 사용자 ID(PK)")
                                    ),
                                    requestFields(
                                            fieldWithPath("reason").description("비활성화 사유"),
                                            fieldWithPath("days").description("비활성화 기간")
                                    ),
                                    responseFields(
                                            fieldWithPath("status").description("HTTP 상태 코드"),
                                            fieldWithPath("errorCode").description("커스텀 예외 코드"),
                                            fieldWithPath("message").description("예외 메세지")
                                    )
                            )
                    );
        }

        @Test
        @DisplayName("관리자가 아니면 유저 비활성화에 실패한다")
        void throwExceptionByInSufficientPrivilege() throws Exception {
            //given
            given(jwtTokenProvider.isTokenValid(anyString())).willReturn(true);
            given(jwtTokenProvider.getId(anyString())).willReturn(ADMIN_ID);
            doThrow(DustException.type(UserErrorCode.USER_IS_NOT_ADMIN))
                    .when(suspensionService)
                    .suspend(anyLong(), anyLong(), any(), any(), anyString());

            //when
            final SuspensionRequest request = createSuspensionRequest();
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .post(BASE_URL, SUSPENDED_ID)
                    .header(AUTHORIZATION, BEARER_TOKEN + REFRESH_TOKEN)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

            //then
            final UserErrorCode expectedError = UserErrorCode.USER_IS_NOT_ADMIN;
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
                                    "UserApi/Suspension/Suspend/Failure/Case2",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    pathParameters(
                                            parameterWithName("suspendedId").description("비활성화될 사용자 ID(PK)")
                                    ),
                                    requestFields(
                                            fieldWithPath("reason").description("비활성화 사유"),
                                            fieldWithPath("days").description("비활성화 기간")
                                    ),
                                    responseFields(
                                            fieldWithPath("status").description("HTTP 상태 코드"),
                                            fieldWithPath("errorCode").description("커스텀 예외 코드"),
                                            fieldWithPath("message").description("예외 메세지")
                                    )
                            )
                    );
        }

        @Test
        @DisplayName("유저 비활성화에 성공한다")
        void success() throws Exception {
            //given
            given(jwtTokenProvider.isTokenValid(anyString())).willReturn(true);
            given(jwtTokenProvider.getId(anyString())).willReturn(SUSPENDED_ID);
            doReturn(1L)
                    .when(suspensionService)
                    .suspend(anyLong(), anyLong(), any(), any(), anyString());

            //when
            final SuspensionRequest request = createSuspensionRequest();
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .post(BASE_URL, SUSPENDED_ID)
                    .header(AUTHORIZATION, BEARER_TOKEN + REFRESH_TOKEN)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

            //then
            mockMvc.perform(requestBuilder)
                    .andExpectAll(
                            status().isOk()
                    )
                    .andDo(
                            document(
                                    "UserApi/Suspension/Suspend/Success",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    pathParameters(
                                            parameterWithName("suspendedId").description("비활성화될 사용자 ID(PK)")
                                    ),
                                    requestFields(
                                            fieldWithPath("reason").description("비활성화 사유"),
                                            fieldWithPath("days").description("비활성화 기간")
                                    )
                            )
                    );
        }
    }

    private SuspensionRequest createSuspensionRequest() {
        return new SuspensionRequest("정책 위반", 7);
    }
}
