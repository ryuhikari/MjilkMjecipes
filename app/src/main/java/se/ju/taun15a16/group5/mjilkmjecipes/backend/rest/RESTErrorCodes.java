package se.ju.taun15a16.group5.mjilkmjecipes.backend.rest;

/**
 * Created by kevin on 20.11.2016.
 */

public enum RESTErrorCodes {
    USERNAME_MISSING("UserNameMissing"),
    INVALID_USERNAME("InvalidUserName"),
    DUPLICATE_USERNAME("DuplicateUserName"),
    PASSWORD_MISSING("PasswordMissing"),
    PASSWORD_TOO_SHORT("PasswordTooShort"),
    PASSWORD_REQUIRES_NON_ALPHANUMERIC("PasswordRequiresNonAlphanumeric"),
    PASSWORD_REQUIRES_DIGIT("PasswordRequiresDigit"),
    PASSWORD_REQUIRES_LOWER("PasswordRequiresLower"),
    PASSWORD_REQUIRES_UPPER("PasswortRequiresUpper"),
    TOKEN_MISSING("TokenMissing"),
    TOKEN_INVALID("TokenInvalid"),
    LONGITUDE_MISSING("LongitudeMissing"),
    LATITUDE_MISSING("LatitudeMissing"),
    RECIPE_ID_DOES_NOT_EXIST("RecipeIdDoesNotExist"),
    INVALID_REQUEST("invalid_request"),
    UNSUPPORTED_GRANT_TYPE("unsupported_grant_type"),
    INVALID_CLIENT("invalid_client"),
    NAME_MISSING("NameMissing"),
    NAME_WRONG_LENGTH("NameWrongLength"),
    DESCRIPTION_MISSING("DescriptionMissing"),
    DESCRIPTION_WRONG_LENGTH("DescriptionWrongLength"),
    DIRECTIONS_MISSING("DirectionsMissing"),
    DIRECTION_ORDER_MISSING("DirectionOrderMissing"),
    DIRECTION_DESCRIPTION_MISSING("DirectionDescriptionMissing"),
    DIRECTION_DESCRIPTION_WRONG_LENGTH("DirectionDescriptionWrongLength"),
    TEXT_MISSING("TextMissing"),
    TEXT_WRONG_LENGTH("TextWrongLength"),
    GRADE_MISSING("GradeMissing"),
    GRADE_WRONG_VALUE("GradeWrongValue"),
    COMMENTER_ID_MISSING("CommenderIdMissing"),
    COMMENTER_ALREADY_COMMENT("CommenterAlreadyComment"),
    TERM_MISSING("TermMissing"),
    UNKNOWN_ERROR("");

    private String description;

    RESTErrorCodes(String description){
        this.description = description;
    }


    public String getDescription() {
        return description;
    }

    public static RESTErrorCodes fromString(String description){
        if(description != null){
            for(RESTErrorCodes c : RESTErrorCodes.values()){
                if(description.equals(c.description)){
                    return c;
                }
            }
            RESTErrorCodes code = RESTErrorCodes.UNKNOWN_ERROR;
            code.description = description;
            return code;
        }
        return null;
    }
}
