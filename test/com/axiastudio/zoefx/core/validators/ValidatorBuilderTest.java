package com.axiastudio.zoefx.core.validators;

import org.junit.Test;

/**
 * User: tiziano
 * Date: 15/04/14
 * Time: 17:35
 */
public class ValidatorBuilderTest {

    @Test
    public void test() throws Exception {
        Validator validator = ValidatorBuilder.create().minLength(3).maxLength(5).build();

        assert validator.validate("12").equals(Boolean.FALSE);
        assert validator.validate("1234").equals(Boolean.TRUE);
        assert validator.validate("123456").equals(Boolean.FALSE);

    }
}
