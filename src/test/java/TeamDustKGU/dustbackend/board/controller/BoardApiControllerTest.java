package TeamDustKGU.dustbackend.board.controller;

import TeamDustKGU.dustbackend.auth.exception.AuthErrorCode;
import TeamDustKGU.dustbackend.board.controller.dto.BoardRequest;
import TeamDustKGU.dustbackend.board.exception.BoardErrorCode;
import TeamDustKGU.dustbackend.common.ControllerTest;

import TeamDustKGU.dustbackend.global.exception.DustException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static TeamDustKGU.dustbackend.auth.utils.TokenUtils.BEARER_TOKEN;
import static TeamDustKGU.dustbackend.auth.utils.TokenUtils.REFRESH_TOKEN;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Board [Controller Layer] -> BoardApiController 테스트")
public class BoardApiControllerTest extends ControllerTest {
    @Nested
    @DisplayName("게시글 등록 API [POST /api/board]")
    class createBoard {
        private static final String BASE_URL = "/api/board";
        private static final Long WRITER_ID = 1L;

        @Test
        @DisplayName("Authorization Header에 AccessToken이 없으면 게시글 등록에 실패한다")
        void withoutAccessToken() throws Exception {
            // when
            final BoardRequest request = createBoardRequest();
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .post(BASE_URL)
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
                                    "BoardApi/Create/Failure/Case1",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    requestFields(
                                            fieldWithPath("title").description("등록할 제목"),
                                            fieldWithPath("content").description("등록할 내용")
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
        @DisplayName("게시글 등록에 성공한다")
        void success() throws Exception {
            // given
            given(jwtTokenProvider.isTokenValid(anyString())).willReturn(true);
            given(jwtTokenProvider.getId(anyString())).willReturn(WRITER_ID);
            doReturn(1L)
                    .when(boardService)
                    .create(anyLong(), any(), any());

            // when
            final BoardRequest request = createBoardRequest();
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .post(BASE_URL)
                    .header(AUTHORIZATION, BEARER_TOKEN + REFRESH_TOKEN)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

            // then
            mockMvc.perform(requestBuilder)
                    .andExpectAll(
                            status().isCreated()
                    )
                    .andDo(
                            document(
                                    "UserApi/Create/Success",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    requestHeaders(
                                            headerWithName(AUTHORIZATION).description("Access Token")
                                    ),
                                    requestFields(
                                            fieldWithPath("title").description("등록할 제목"),
                                            fieldWithPath("content").description("등록할 내용")
                                    )
                            )
                    );
        }
    }

    @Nested
    @DisplayName("게시글 수정 API [PATCH /api/board/{boardId}]")
    class updateBoard {
        private static final String BASE_URL = "/api/board/{boardId}";
        private static final Long WRITER_ID = 1L;
        private static final Long BOARD_ID = 2L;

        @Test
        @DisplayName("Authorization Header에 AccessToken이 없으면 게시글 수정에 실패한다")
        void withoutAccessToken() throws Exception {
            // when
            final BoardRequest request = createBoardRequest();
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .patch(BASE_URL, BOARD_ID)
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
                                    "BoardApi/Update/Failure/Case1",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    pathParameters(
                                            parameterWithName("boardId").description("수정할 게시글 ID(PK)")
                                    ),
                                    requestFields(
                                            fieldWithPath("title").description("수정할 제목"),
                                            fieldWithPath("content").description("수정할 내용")
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
        @DisplayName("다른 사람의 게시글은 수정할 수 없다")
        void throwExceptionByUserIsNotBoardWriter() throws Exception {
            // given
            given(jwtTokenProvider.isTokenValid(anyString())).willReturn(true);
            given(jwtTokenProvider.getId(anyString())).willReturn(WRITER_ID);
            doThrow(DustException.type(BoardErrorCode.USER_IS_NOT_BOARD_WRITER))
                    .when(boardService)
                    .update(anyLong(), anyLong(), any(), any());

            // when
            final BoardRequest request = createBoardRequest();
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .patch(BASE_URL, BOARD_ID)
                    .header(AUTHORIZATION, BEARER_TOKEN + REFRESH_TOKEN)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

            // then
            final BoardErrorCode expectedError = BoardErrorCode.USER_IS_NOT_BOARD_WRITER;
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
                                    "BoardApi/Update/Failure/Case2",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    requestHeaders(
                                            headerWithName(AUTHORIZATION).description("Access Token")
                                    ),
                                    pathParameters(
                                            parameterWithName("boardId").description("수정할 게시글 ID(PK)")
                                    ),
                                    requestFields(
                                            fieldWithPath("title").description("수정할 제목"),
                                            fieldWithPath("content").description("수정할 내용")
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
        @DisplayName("게시글 수정에 성공한다")
        void success() throws Exception {
            // given
            given(jwtTokenProvider.isTokenValid(anyString())).willReturn(true);
            given(jwtTokenProvider.getId(anyString())).willReturn(WRITER_ID);
            doNothing()
                    .when(boardService)
                    .update(anyLong(), anyLong(), any(), any());

            // when
            final BoardRequest request = createBoardRequest();
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .patch(BASE_URL, BOARD_ID)
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
                                    "BoardApi/Update/Success",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    requestHeaders(
                                            headerWithName(AUTHORIZATION).description("Access Token")
                                    ),
                                    pathParameters(
                                            parameterWithName("boardId").description("수정할 게시글 ID(PK)")
                                    ),
                                    requestFields(
                                            fieldWithPath("title").description("수정할 제목"),
                                            fieldWithPath("content").description("수정할 내용")
                                    )
                            )
                    );
        }
    }

    @Nested
    @DisplayName("게시글 삭제 API [DELETE /api/board/{boardId}]")
    class deleteBoard {
        private static final String BASE_URL = "/api/board/{boardId}";
        private static final Long WRITER_ID = 1L;
        private static final Long BOARD_ID = 2L;

        @Test
        @DisplayName("Authorization Header에 AccessToken이 없으면 게시글 삭제에 실패한다")
        void withoutAccessToken() throws Exception {
            // when
            final BoardRequest request = createBoardRequest();
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .delete(BASE_URL, BOARD_ID)
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
                                    "UserApi/Delete/Failure/Case1",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    pathParameters(
                                            parameterWithName("boardId").description("삭제될 게시글 ID(PK)")
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
        @DisplayName("다른 사람의 게시글은 삭제할 수 없다")
        void throwExceptionByUserIsNotBoardWriter() throws Exception {
            // given
            given(jwtTokenProvider.isTokenValid(anyString())).willReturn(true);
            given(jwtTokenProvider.getId(anyString())).willReturn(WRITER_ID);
            doThrow(DustException.type(BoardErrorCode.USER_IS_NOT_BOARD_WRITER))
                    .when(boardService)
                    .delete(anyLong(),anyLong());

            // when
            final BoardRequest request = createBoardRequest();
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .delete(BASE_URL, BOARD_ID)
                    .header(AUTHORIZATION, BEARER_TOKEN + REFRESH_TOKEN)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

            // then
            final BoardErrorCode expectedError = BoardErrorCode.USER_IS_NOT_BOARD_WRITER;
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
                                    "BoardApi/Delete/Failure/Case2",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    requestHeaders(
                                            headerWithName(AUTHORIZATION).description("Access Token")
                                    ),
                                    pathParameters(
                                            parameterWithName("boardId").description("삭제할 게시글 ID(PK)")
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
        @DisplayName("게시글 삭제에 성공한다")
        void success() throws Exception {
            // given
            given(jwtTokenProvider.isTokenValid(anyString())).willReturn(true);
            given(jwtTokenProvider.getId(anyString())).willReturn(WRITER_ID);
            doNothing()
                    .when(boardService)
                    .delete(anyLong(), anyLong());

            // when
            final BoardRequest request = createBoardRequest();
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .delete(BASE_URL, BOARD_ID)
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
                                    "BoardApi/Delete/Success",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    requestHeaders(
                                            headerWithName(AUTHORIZATION).description("Access Token")
                                    ),
                                    pathParameters(
                                            parameterWithName("boardId").description("삭제할 게시글 ID(PK)")
                                    )
                            )
                    );
        }
    }

    private BoardRequest createBoardRequest() {
        return new BoardRequest("제목", "내용");
    }
}
