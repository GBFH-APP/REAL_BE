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

@RestControllerAdvice // 컨트롤러 전역에서 발생하는 예외를 처리
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 입력값 검증
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append(fieldError.getDefaultMessage());
        }

        log.error("handleMethodArgumentNotValidException : {}", builder.toString());
        return ResponseEntity
                .status(ErrorCode.BAD_REQUEST.getStatus().value())
                .body(new ErrorResponseDTO(ErrorCode.BAD_REQUEST, builder.toString()));
    }

    @ExceptionHandler(InvalidHostException.class)
    protected ResponseEntity<ErrorResponseDTO> handleInvalidHostException(final InvalidHostException e) {
        return ResponseEntity
                .status(ErrorCode.UNABLE_TO_RESOLVE_HOST.getStatus().value())
                .body(new ErrorResponseDTO(ErrorCode.UNABLE_TO_RESOLVE_HOST));
    }
}
