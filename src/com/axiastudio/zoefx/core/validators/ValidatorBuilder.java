package com.axiastudio.zoefx.core.validators;

/**
 * User: tiziano
 * Date: 15/04/14
 * Time: 16:06
 */
public class ValidatorBuilder {

    private ValidatorType type = ValidatorType.STRING;
    private Boolean notNull=Boolean.FALSE;
    private Integer minLength=0;
    private Integer maxLength=-1;
    private String code=null;

    public ValidatorBuilder() {
    }

    public static ValidatorBuilder create() {
        return new ValidatorBuilder();
    }

    public ValidatorBuilder type(ValidatorType validatorType){
        type = validatorType;
        return this;
    }

    public ValidatorBuilder minLength(Integer min){
        minLength = min;
        return this;
    }

    public ValidatorBuilder maxLength(Integer max){
        maxLength = max;
        return this;
    }

    public ValidatorBuilder code(String src){
        code = src;
        return this;
    }

    public Validator build() {
        if( type.equals(ValidatorType.STRING) ) {
            StringValidator validator = new StringValidator();
            validator.setMinLength(minLength);
            validator.setMaxLength(maxLength);
            validator.setNotNull(notNull);
            validator.setCode(code);
            return validator;
        }
        return null;
    }

}
