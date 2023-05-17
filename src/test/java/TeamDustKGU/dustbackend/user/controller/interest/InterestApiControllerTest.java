package TeamDustKGU.dustbackend.user.controller.interest;

import TeamDustKGU.dustbackend.auth.exception.AuthErrorCode;
import TeamDustKGU.dustbackend.common.ControllerTest;
import TeamDustKGU.dustbackend.global.exception.DustException;
import TeamDustKGU.dustbackend.user.controller.dto.request.InterestRequest;
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

@DisplayName("User [Controller Layer] -> InterestApiController 테스트")
public class InterestApiControllerTest extends ControllerTest {
    @Nested
    @DisplayName("관심유저 등록 API [POST /api/user/interest/{interested}]")
    class register {
        private static final String BASE_URL = "/api/user/interest/{interestedId}";
        private static final Long INTERESTING_ID = 1L;
        private static final Long INTERESTED_ID = 2L;

        @Test
        @DisplayName("Authorization Header에 AccessToken이 없으면 관심유저 등록에 실패한다")
        void withoutAccessToken() throws Exception {
            // when
            final InterestRequest request = createInterestRequest();
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .post(BASE_URL, INTERESTED_ID)
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
                                    "UserApi/Interest/Register/Failure/Case1",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    pathParameters(
                                            parameterWithName("interestedId").description("관심유저로 등록될 사용자 ID(PK)")
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
        void throwExceptionBySelfInterestNotAllowed() throws Exception {
            // given
            given(jwtTokenProvider.isTokenValid(anyString())).willReturn(true);
            given(jwtTokenProvider.getId(anyString())).willReturn(INTERESTING_ID);
            doThrow(DustException.type(UserErrorCode.SELF_INTEREST_NOT_ALLOWED))
                    .when(interestService)
                    .register(anyLong(), anyLong(), any(), any());

            // when
            final InterestRequest request = createInterestRequest();
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .post(BASE_URL, INTERESTED_ID)
                    .header(AUTHORIZATION, BEARER_TOKEN + REFRESH_TOKEN)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

            // then
            final UserErrorCode expectedError = UserErrorCode.SELF_INTEREST_NOT_ALLOWED;
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
                                    "UserApi/Interest/Register/Failure/Case2",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    pathParameters(
                                            parameterWithName("interestedId").description("관심유저로 등록될 사용자 ID(PK)")
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
        void throwExceptionByAlreadyInterest() throws Exception {
            // given
            given(jwtTokenProvider.isTokenValid(anyString())).willReturn(true);
            given(jwtTokenProvider.getId(anyString())).willReturn(INTERESTING_ID);
            doThrow(DustException.type(UserErrorCode.ALREADY_INTEREST))
                    .when(interestService)
                    .register(anyLong(), anyLong(), any(), any());

            // when
            final InterestRequest request = createInterestRequest();
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .post(BASE_URL, INTERESTED_ID)
                    .header(AUTHORIZATION, BEARER_TOKEN + REFRESH_TOKEN)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

            // then
            final UserErrorCode expectedError = UserErrorCode.ALREADY_INTEREST;
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
                                    "UserApi/Interest/Register/Failure/Case3",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    pathParameters(
                                            parameterWithName("interestedId").description("관심유저로 등록될 사용자 ID(PK)")
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
            given(jwtTokenProvider.getId(anyString())).willReturn(INTERESTING_ID);
            doReturn(1L)
                    .when(interestService)
                    .register(anyLong(), anyLong(), any(), any());

            // when
            final InterestRequest request = createInterestRequest();
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .post(BASE_URL, INTERESTED_ID)
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
                                    "UserApi/Interest/Register/Success",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    pathParameters(
                                            parameterWithName("interestedId").description("관심유저로 등록될 회원 ID(PK)")
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
    @DisplayName("관심유저 취소 API [DELETE /api/user/interest/{interested}]")
    class cancel {
        private static final String BASE_URL = "/api/user/interest/{interestedId}";
        private static final Long INTERESTING_ID = 1L;
        private static final Long INTERESTED_ID = 2L;

        @Test
        @DisplayName("Authorization Header에 AccessToken이 없으면 관심유저 취소에 실패한다")
        void withoutAccessToken() throws Exception {
            // when
            final InterestRequest request = createInterestRequest();
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .delete(BASE_URL, INTERESTED_ID)
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
                                    "UserApi/Interest/Cancel/Failure/Case1",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    pathParameters(
                                            parameterWithName("interestedId").description("관심유저로 등록될 사용자 ID(PK)")
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
        void throwExceptionByInterestNotFound() throws Exception {
            // given
            given(jwtTokenProvider.isTokenValid(anyString())).willReturn(true);
            given(jwtTokenProvider.getId(anyString())).willReturn(INTERESTING_ID);
            doThrow(DustException.type(UserErrorCode.INTEREST_NOT_FOUND))
                    .when(interestService)
                    .cancel(anyLong(), anyLong());

            // when
            final InterestRequest request = createInterestRequest();
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .delete(BASE_URL, INTERESTED_ID)
                    .header(AUTHORIZATION, BEARER_TOKEN + REFRESH_TOKEN)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

            // then
            final UserErrorCode expectedError = UserErrorCode.INTEREST_NOT_FOUND;
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
                                    "UserApi/Interest/Cancel/Failure/Case2",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    pathParameters(
                                            parameterWithName("interestedId").description("관심유저로 등록될 사용자 ID(PK)")
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
            given(jwtTokenProvider.getId(anyString())).willReturn(INTERESTING_ID);
            doNothing()
                    .when(interestService)
                    .cancel(anyLong(), anyLong());

            // when
            final InterestRequest request = createInterestRequest();
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .delete(BASE_URL, INTERESTED_ID)
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
                                    "UserApi/Interest/Cancel/Success",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    pathParameters(
                                            parameterWithName("interestedId").description("관심유저로 등록될 사용자 ID(PK)")
                                    )
                            )
                    );
        }
    }

    private InterestRequest createInterestRequest() {
        return new InterestRequest("익명의 게시글", LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT)
    );
}
}
