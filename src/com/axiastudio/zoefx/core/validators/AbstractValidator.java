package com.axiastudio.zoefx.core.validators;

/**
 * User: tiziano
 * Date: 15/04/14
 * Time: 16:28
 */
public abstract class AbstractValidator {

    protected Boolean notNull=Boolean.FALSE;
    protected String code=null;

    protected Boolean validate(String value) {
        if( notNull && value==null ){
            return Boolean.FALSE;
        }
        // TODO: validate code
        return Boolean.TRUE;
    }

    public void setNotNull(Boolean notNull) {
        this.notNull = notNull;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
