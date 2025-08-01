package carevn.luv2code.cms.tevc_cms_api.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1000, "Invalid key", HttpStatus.BAD_GATEWAY),
    USER_EXISTED(1001,"User existed", HttpStatus.BAD_GATEWAY),
    USERNAME_INVALID(1002, "Username must be at least {min} characters", HttpStatus.BAD_GATEWAY),
    PASSWORD_INVALID(1003, "Password must be at least {min} characters", HttpStatus.BAD_GATEWAY),
    USER_NOT_FOUND(1004, "User not found", HttpStatus.NOT_FOUND),
    USER_HAS_BEEN_DELETED(1005, "User has been deleted", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1006, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1007, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1008, "You don't have permission", HttpStatus.FORBIDDEN),
    EMAIL_EXISTED(1009, "Email already was used by other user!", HttpStatus.CONFLICT),
    MOVIE_ALREADY_EXISTS(1010, "Movie with title already exists", HttpStatus.BAD_REQUEST),
    TRAILER_NOT_FOUND(1011, "Trailer not found", HttpStatus.NOT_FOUND),
    FILE_NOT_FOUND(1012, "File not found", HttpStatus.NOT_FOUND),
    MAX_UPLOAD_SIZE_EXCEEDED(1013, "File upload size exceeds the allowed limit", HttpStatus.BAD_REQUEST),
    TRAILER_EXISTED(1014, "Trailer already exists with title and trailer key!", HttpStatus.CONFLICT),
    MOVIE_NOT_EXISTED(1015, "Movie not existed", HttpStatus.NOT_FOUND),
    INVALID_TRAILER_TYPE(1016, "Invalid trailer type", HttpStatus.BAD_REQUEST),
    COMMENT_NOT_FOUND(1017, "Comment not found", HttpStatus.NOT_FOUND),
    ACTOR_EXISTED(1018, "Actor already exists", HttpStatus.CONFLICT),
    ACTOR_NOT_EXISTED(1019, "Actor not existed", HttpStatus.NOT_FOUND),
    UPDATE_USER_FAILED(1020, "Update user failed", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_HAS_COLLECTED_MOVIE(1021, "User has already collected this movie", HttpStatus.CONFLICT),
    USER_HAS_NOT_COLLECTED_MOVIE(1022, "User did not collect this movie", HttpStatus.CONFLICT),
    ;

    int code;
    String message;
    HttpStatus statusCode;

    ErrorCode(int code, String message, HttpStatus statusCode){
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
