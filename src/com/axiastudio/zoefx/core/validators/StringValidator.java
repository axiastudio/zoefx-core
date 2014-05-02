package com.axiastudio.zoefx.core.validators;

/**
 * User: tiziano
 * Date: 15/04/14
 * Time: 16:06
 */
public class StringValidator extends AbstractValidator implements Validator<String> {

    private Integer minLength=0;
    private Integer maxLength=-1;
    private Boolean notNull=false;

    public Boolean validate(String value) {
        if( value == null ){
            if( notNull ) {
                return Boolean.FALSE;
            }
            return Boolean.TRUE;
        }
        if( !super.validate(value) ){
            return Boolean.FALSE;
        }
        if( value.length()<minLength ){
            return Boolean.FALSE;
        }
        if( maxLength>-1 && value.length()>maxLength ){
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }
}
