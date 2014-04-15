package com.axiastudio.zoefx.core.validators;

/**
 * User: tiziano
 * Date: 15/04/14
 * Time: 16:05
 */
public interface Validator<T> {

    public Boolean validate(T value);

}
