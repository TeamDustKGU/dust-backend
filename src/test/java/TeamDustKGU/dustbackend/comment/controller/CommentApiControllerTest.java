package TeamDustKGU.dustbackend.comment.controller;

import TeamDustKGU.dustbackend.auth.exception.AuthErrorCode;
import TeamDustKGU.dustbackend.comment.controller.dto.CommentRequest;
import TeamDustKGU.dustbackend.comment.exception.CommentErrorCode;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Comment [Controller Layer] -> CommentApiController 테스트")
public class CommentApiControllerTest extends ControllerTest {
    @Nested
    @DisplayName("댓글 등록 API [POST /api/comment/{boardId}]")
    class createComment {
        private static final String BASE_URL = "/api/comment/{boardId}";
        private static final Long WRITER_ID = 1L;
        private static final Long BOARD_ID = 2L;

        @Test
        @DisplayName("Authorization Header에 AccessToken이 없으면 댓글 등록에 실패한다")
        void withoutAccessToken() throws Exception {
            // when
            final CommentRequest request = createCommentRequest();
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .post(BASE_URL, BOARD_ID)
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
                                    "CommentApi/Create/Failure/Case1",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    pathParameters(
                                            parameterWithName("boardId").description("댓글을 등록할 게시글 ID(PK)")
                                    ),
                                    requestFields(
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
        @DisplayName("댓글 등록에 성공한다")
        void success() throws Exception {
            // given
            given(jwtTokenProvider.isTokenValid(anyString())).willReturn(true);
            given(jwtTokenProvider.getId(anyString())).willReturn(WRITER_ID);
            doReturn(1L)
                    .when(commentService)
                    .create(anyLong(), anyLong(), any());

            // when
            final CommentRequest request = createCommentRequest();
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .post(BASE_URL, BOARD_ID)
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
                                    "CommentApi/Create/Success",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    requestHeaders(
                                            headerWithName(AUTHORIZATION).description("Access Token")
                                    ),
                                    pathParameters(
                                            parameterWithName("boardId").description("댓글을 등록할 게시글 ID(PK)")
                                    ),
                                    requestFields(
                                            fieldWithPath("content").description("등록할 내용")
                                    )
                            )
                    );
        }
    }

    @Nested
    @DisplayName("댓글 삭제 API [DELETE /api/comment/{commentId}]")
    class deleteComment {
        private static final String BASE_URL = "/api/comment/{commentId}";
        private static final Long WRITER_ID = 1L;
        private static final Long COMMENT_ID = 2L;

        @Test
        @DisplayName("Authorization Header에 AccessToken이 없으면 댓글 삭제에 실패한다")
        void withoutAccessToken() throws Exception {
            // when
            final CommentRequest request = createCommentRequest();
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .delete(BASE_URL, COMMENT_ID)
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
                                    "CommentApi/Delete/Failure/Case1",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    pathParameters(
                                            parameterWithName("commentId").description("삭제될 댓글 ID(PK)")
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
        @DisplayName("다른 사람의 댓글은 삭제할 수 없다")
        void throwExceptionByUserIsNotCommentWriter() throws Exception {
            // given
            given(jwtTokenProvider.isTokenValid(anyString())).willReturn(true);
            given(jwtTokenProvider.getId(anyString())).willReturn(WRITER_ID + 100L);
            doThrow(DustException.type(CommentErrorCode.USER_IS_NOT_COMMENT_WRITER))
                    .when(commentService)
                    .delete(anyLong(), anyLong());

            // when
            final CommentRequest request = createCommentRequest();
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .delete(BASE_URL, COMMENT_ID)
                    .header(AUTHORIZATION, BEARER_TOKEN + REFRESH_TOKEN)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

            // then
            final CommentErrorCode expectedError = CommentErrorCode.USER_IS_NOT_COMMENT_WRITER;
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
                                    "CommentApi/Delete/Failure/Case2",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    requestHeaders(
                                            headerWithName(AUTHORIZATION).description("Access Token")
                                    ),
                                    pathParameters(
                                            parameterWithName("commentId").description("삭제할 댓글 ID(PK)")
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
        @DisplayName("댓글 삭제에 성공한다")
        void success() throws Exception {
            // given
            given(jwtTokenProvider.isTokenValid(anyString())).willReturn(true);
            given(jwtTokenProvider.getId(anyString())).willReturn(WRITER_ID);
            doNothing()
                    .when(commentService)
                    .delete(anyLong(), anyLong());

            // when
            final CommentRequest request = createCommentRequest();
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .delete(BASE_URL, COMMENT_ID)
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
                                    "CommentApi/Delete/Success",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    requestHeaders(
                                            headerWithName(AUTHORIZATION).description("Access Token")
                                    ),
                                    pathParameters(
                                            parameterWithName("commentId").description("삭제할 댓글 ID(PK)")
                                    )
                            )
                    );
        }
    }

    @Nested
    @DisplayName("대댓글 등록 API [POST /api/childComment/{boardId}/{parentCommentId}]")
    class createChildComment {
        private static final String BASE_URL = "/api/childComment/{boardId}/{parentCommentId}";
        private static final Long WRITER_ID = 1L;
        private static final Long BOARD_ID = 2L;
        private static final Long PARENT_COMMENT_ID = 3L;

        @Test
        @DisplayName("Authorization Header에 AccessToken이 없으면 대댓글 등록에 실패한다")
        void withoutAccessToken() throws Exception {
            // when
            final CommentRequest request = createCommentRequest();
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .post(BASE_URL, BOARD_ID, PARENT_COMMENT_ID)
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
                                    "CommentApi/childCommentCreate/Failure/Case1",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    pathParameters(
                                            parameterWithName("boardId").description("댓글을 등록할 게시글 ID(PK)"),
                                            parameterWithName("parentCommentId").description("댓글을 등록할 부모 댓글 ID(PK)")
                                    ),
                                    requestFields(
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
        @DisplayName("대댓글 등록에 성공한다")
        void successChild() throws Exception {
            // given
            given(jwtTokenProvider.isTokenValid(anyString())).willReturn(true);
            given(jwtTokenProvider.getId(anyString())).willReturn(WRITER_ID);
            doReturn(1L)
                    .when(commentService)
                    .createChild(anyLong(), anyLong(), anyLong(), any());

            // when
            final CommentRequest request = createCommentRequest();
            MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                    .post(BASE_URL, BOARD_ID, PARENT_COMMENT_ID)
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
                                    "CommentApi/childCommentCreateChild/Success",
                                    applyRequestPreprocessor(),
                                    applyResponsePreprocessor(),
                                    requestHeaders(
                                            headerWithName(AUTHORIZATION).description("Access Token")
                                    ),
                                    pathParameters(
                                            parameterWithName("boardId").description("댓글을 등록할 게시글 ID(PK)"),
                                            parameterWithName("parentCommentId").description("댓글을 등록할 부모 댓글 ID(PK)")
                                    ),
                                    requestFields(
                                            fieldWithPath("content").description("등록할 내용")
                                    )
                            )
                    );
        }
    }

    private CommentRequest createCommentRequest() {
        return new CommentRequest("내용");
    }
}
