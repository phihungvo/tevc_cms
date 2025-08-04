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
    PERMISSION_NOT_FOUND(1023, "Permission not found", HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND(1024, "Role not found", HttpStatus.NOT_FOUND),
    ROLE_ALREADY_EXISTS(1025, "Role already exists", HttpStatus.CONFLICT),
    PERMISSION_ALREADY_EXISTS(1026, "Permission already exists", HttpStatus.CONFLICT),
    INVALID_PERMISSION_FORMAT(1027, "Invalid permission format - should be 'resource:action'", HttpStatus.BAD_REQUEST),
    EMPLOYEE_NOT_FOUND(1028, "Employee not found", HttpStatus.NOT_FOUND),
    EMPLOYEE_CODE_EXISTS(1029, "Employee code already exists", HttpStatus.CONFLICT),
    DEPARTMENT_NOT_FOUND(1030, "Department not found", HttpStatus.NOT_FOUND),
    DEPARTMENT_NAME_EXISTS(1031, "Department name already exists", HttpStatus.CONFLICT),
    DEPARTMENT_HAS_EMPLOYEES(1032, "Department has employees", HttpStatus.BAD_REQUEST),
    POSITION_NOT_FOUND(1033, "Position not found", HttpStatus.NOT_FOUND),
    POSITION_TITLE_EXISTS(1034, "Position title already exists", HttpStatus.CONFLICT),
    PROJECT_NOT_FOUND(1035, "Project not found", HttpStatus.NOT_FOUND),
    PROJECT_MANAGER_NOT_FOUND(1036, "Project manager not found", HttpStatus.NOT_FOUND),
    TIMESHEET_NOT_FOUND(1037, "Timesheet not found", HttpStatus.NOT_FOUND),
    TIMESHEET_ALREADY_PROCESSED(1038, "Timesheet already processed", HttpStatus.CONFLICT),
    CONTRACT_NOT_FOUND(1039, "Contract not found", HttpStatus.NOT_FOUND),
    CONTRACT_ALREADY_EXISTS(1040, "Contract already exists", HttpStatus.CONFLICT),
    CONTRACT_NOT_ACTIVE(1041, "Contract not active", HttpStatus.BAD_REQUEST),
    LEAVE_NOT_FOUND(1042, "Leave not found", HttpStatus.NOT_FOUND),
    LEAVE_ALREADY_PROCESSED(1043, "Leave already processed", HttpStatus.CONFLICT),
    PAYROLL_NOT_FOUND(1044, "Payroll not found", HttpStatus.NOT_FOUND),
    PAYROLL_ALREADY_EXISTS(1045, "Payroll already exists", HttpStatus.CONFLICT),
    PAYROLL_ALREADY_PROCESSED(1046, "Payroll already processed", HttpStatus.CONFLICT),
    PERFORMANCE_NOT_FOUND(1047, "Performance not found", HttpStatus.NOT_FOUND),
    REVIEWER_NOT_FOUND(1048, "Reviewer not found", HttpStatus.NOT_FOUND),
    REVIEWER_ALREADY_EXISTS(1049, "Reviewer already exists", HttpStatus.CONFLICT),
    TRAINING_ALREADY_EXISTS(1050, "Training already exists for this date.", HttpStatus.CONFLICT),
    TRAINING_NOT_FOUND(1051, "Training not found", HttpStatus.NOT_FOUND),
    TRAINING_ALREADY_PROCESSED(1052, "Training already processed", HttpStatus.CONFLICT),
    POSITION_HAS_EMPLOYEES(1053, "Position has employees", HttpStatus.BAD_REQUEST)
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
