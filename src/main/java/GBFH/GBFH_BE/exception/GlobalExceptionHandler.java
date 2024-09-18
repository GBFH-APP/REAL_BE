package GBFH.GBFH_BE.exception;

import GBFH.GBFH_BE.code.ErrorCode;
import GBFH.GBFH_BE.dto.response.ErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // 컨트롤러 전역에서 발생하는 예외를 처리
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 입력값 검증
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity
                .status(ErrorCode.BAD_REQUEST.getStatus().value())
                .body(new ErrorResponseDTO(ErrorCode.BAD_REQUEST, errors));
    }

    @ExceptionHandler(InvalidHostException.class)
    protected ResponseEntity<ErrorResponseDTO> handleInvalidHostException(final InvalidHostException e) {
        return ResponseEntity
                .status(ErrorCode.UNABLE_TO_RESOLVE_HOST.getStatus().value())
                .body(new ErrorResponseDTO(ErrorCode.UNABLE_TO_RESOLVE_HOST));
    }

    @ExceptionHandler(NotLostException.class)
    protected ResponseEntity<ErrorResponseDTO> handleNotLostException(final NotLostException e) {
        return ResponseEntity
                .status(ErrorCode.NOT_LOST_POST.getStatus().value())
                .body(new ErrorResponseDTO(ErrorCode.NOT_LOST_POST));
    }

    @ExceptionHandler(PostNotFoundException.class)
    protected ResponseEntity<ErrorResponseDTO> handlePostNotFoundException(final PostNotFoundException e) {
        return ResponseEntity
                .status(ErrorCode.POST_NOT_FOUND.getStatus().value())
                .body(new ErrorResponseDTO(ErrorCode.POST_NOT_FOUND));
    }

    @ExceptionHandler(WrongPaginationException.class)
    protected ResponseEntity<ErrorResponseDTO> handleWrongPaginationException(final WrongPaginationException e) {
        return ResponseEntity
                .status(ErrorCode.WRONG_PAGINATION.getStatus().value())
                .body(new ErrorResponseDTO(ErrorCode.WRONG_PAGINATION));
    }

    @ExceptionHandler(CommentNotFoundException.class)
    protected ResponseEntity<ErrorResponseDTO> handleCommentNotFoundException(final CommentNotFoundException e) {
        return ResponseEntity
                .status(ErrorCode.COMMENT_NOT_FOUND.getStatus().value())
                .body(new ErrorResponseDTO(ErrorCode.COMMENT_NOT_FOUND));
    }

    @ExceptionHandler(FileNotFoundException.class)
    protected ResponseEntity<ErrorResponseDTO> handleFileNotFoundException(final FileNotFoundException e) {
        return ResponseEntity
                .status(ErrorCode.FILE_NOT_FOUND.getStatus().value())
                .body(new ErrorResponseDTO(ErrorCode.FILE_NOT_FOUND));
    }

    @ExceptionHandler(NoPermissionException.class)
    protected ResponseEntity<ErrorResponseDTO> handleNoPermissionException(final NoPermissionException e) {
        return ResponseEntity
                .status(ErrorCode.HAVE_NO_PERMISSION.getStatus().value())
                .body(new ErrorResponseDTO(ErrorCode.HAVE_NO_PERMISSION));
    }

    @ExceptionHandler(MenuNotFoundException.class)
    protected ResponseEntity<ErrorResponseDTO> handleMenuNotFoundException(final MenuNotFoundException e) {
        return ResponseEntity
                .status(ErrorCode.MENU_NOT_FOUND.getStatus().value())
                .body(new ErrorResponseDTO(ErrorCode.MENU_NOT_FOUND));
    }
}
