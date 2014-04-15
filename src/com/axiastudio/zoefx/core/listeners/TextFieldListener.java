package com.axiastudio.zoefx.core.listeners;

import com.axiastudio.zoefx.core.validators.Validator;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

/**
 * User: tiziano
 * Date: 15/04/14
 * Time: 15:50
 */
public class TextFieldListener implements ChangeListener<String> {

    private final Validator validator;

    public TextFieldListener(Validator validator) {
        this.validator = validator;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        StringProperty textProperty = (StringProperty) observable;
        TextField textField = (TextField) textProperty.getBean();
        Boolean res = validator.validate(newValue);
        if( res == null ){
            textField.setStyle("-fx-border-color: yellow");
        } else if( res ){
            textField.setStyle("-fx-border-color: null");
        } else if( !res ){
            textField.setStyle("-fx-border-color: red");
        }
    }
}
