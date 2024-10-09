package com.github.bccyben.common.exception;

import jakarta.persistence.OptimisticLockException;
import jakarta.servlet.ServletException;
import jakarta.validation.ConstraintViolationException;
import com.github.bccyben.common.message.MessageIdConstants;
import com.github.bccyben.common.message.Messages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

/**
 * 例外処理における共通処理定義クラス。
 */
@ControllerAdvice(basePackages = { "com.github.bccyben" })
@Slf4j
public class ControllerExceptionAdvice {

    private final Messages messages;

    public ControllerExceptionAdvice(Messages messages) {
        this.messages = messages;
    }

    /**
     * リクエスト不正エラーハンドリング
     */
    @ExceptionHandler({
            InvalidRequestException.class,
            PermissionDeniedException.class,
            ConstraintViolationException.class,
            MultipartException.class,
            FileUploadException.class
    })
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(Exception ex) {
        log.info(ex.getMessage());
        return new ErrorResponse("", ex.getMessage());
    }

    /**
     * バリデーションエラー
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errorBuilder = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach((err) -> {
            String errorMessage = err.getDefaultMessage();
            if (errorBuilder.length() > 0) {
                errorBuilder.append("\n");
            }
            errorBuilder.append(errorMessage);
        });
        return new ErrorResponse("", errorBuilder.toString());
    }

    /**
     * データ見つかりません例外処理
     */
    @ExceptionHandler({
            ExpectDataNotFound.class
    })
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleDataNotFoundException(Exception ex) {
        log.info(ex.getMessage());
        return new ErrorResponse("", ex.getMessage());
    }

    /**
     * 排他制御例外処理
     */
    @ExceptionHandler({
            PessimisticLockingFailureException.class,
            OptimisticLockException.class,
            JbhelperConflictException.class,
            HashvalueConflictException.class
    })
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleLockRequestException(Exception ex) {
        if (ex instanceof PessimisticLockingFailureException) {
            log.info(ex.getMessage());
            return new ErrorResponse("", "locked");
        }
        log.info(ex.getMessage());
        return new ErrorResponse("", ex.getMessage());
    }

    /**
     * 情報つき排他制御例外処理
     */
    @ExceptionHandler({
        JbhelperConflictExceptionWithInfo.class
    })
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleLockRequestWithInfoException(JbhelperConflictExceptionWithInfo ex) {
        log.info(ex.getMessage());
        return new ErrorResponse("", ex.getMessage(), ex.getInfo());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleIntegrityViolation(DataIntegrityViolationException ex) {
        log.info(ex.getMessage());
        return new ErrorResponse(MessageIdConstants.INTEGRITY_VIOLATION,
                messages.getMessage(MessageIdConstants.INTEGRITY_VIOLATION));
    }

    /* @RequestBodyによる変換に失敗した場合の例外 */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIntegrityViolation(HttpMessageNotReadableException ex) {
        log.info(ex.getMessage());
        return new ErrorResponse(MessageIdConstants.MESSAGE_E0001,
                messages.getMessage(MessageIdConstants.MESSAGE_E0001));
    }

    /* 必須パラメーターエラー */
    @ExceptionHandler({ MissingServletRequestParameterException.class, MissingServletRequestPartException.class })
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIntegrityViolation(ServletException ex) {
        log.info(ex.getMessage());
        return new ErrorResponse(MessageIdConstants.MESSAGE_E0001,
                messages.getMessage(MessageIdConstants.MESSAGE_E0001));
    }

    /* 型エラー */
    @ExceptionHandler({
            MethodArgumentTypeMismatchException.class,
            IllegalArgumentException.class,
            IllegalStateException.class,
            BindException.class })
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIntegrityViolation(Exception ex) {
        log.info(ex.getMessage());
        return new ErrorResponse(MessageIdConstants.MESSAGE_E0001,
                messages.getMessage(MessageIdConstants.MESSAGE_E0001));
    }

    /* NullPointerExceptionの例外 */
    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIntegrityViolation(NullPointerException ex) {
        log.info(ex.getMessage());
        ex.printStackTrace();
        return new ErrorResponse(MessageIdConstants.MESSAGE_E0001,
                messages.getMessage(MessageIdConstants.MESSAGE_E0001));
    }

    /**
     * その他システム例外エラーハンドリング
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception ex) {
        log.error("", ex);
        return new ErrorResponse(MessageIdConstants.MESSAGE_E9999,
                messages.getMessage(MessageIdConstants.MESSAGE_E9999));
    }

    /**
     * 他サービスとの通信エラーハンドリング
     */
    @ExceptionHandler({
            HttpClientErrorException.class
    })
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpClientErrorException(Exception ex) {
        log.error(ex.getMessage());
        return new ErrorResponse(MessageIdConstants.MESSAGE_E9998, ex.getMessage());
    }
}
