package com.axiastudio.zoefx.core.view.msgbox;

import com.axiastudio.zoefx.core.skins.Skins;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * User: tiziano
 * Date: 29/09/14
 * Time: 12:04
 */
public class MsgBoxBuilder {

    private String title="";
    private String masthead="";
    private String message="";
    private String details="";


    public static MsgBoxBuilder create(){
        return new MsgBoxBuilder();
    }

    public MsgBoxBuilder title(String title){
        this.title = title;
        return this;
    }

    public MsgBoxBuilder masthead(String masthead){
        this.masthead = masthead;
        return this;
    }

    public MsgBoxBuilder message(String message){
        this.message = message;
        return this;
    }

    public MsgBoxBuilder details(String details){
        this.details = details;
        return this;
    }

    public MsgBoxResponse showConfirm(){
        return showDialog(MsgBoxType.CONFIRM);
    }

    public MsgBoxResponse showInfo(){
        return showDialog(MsgBoxType.INFO);
    }

    private MsgBoxResponse showDialog(MsgBoxType type) {
        ResourceBundle bundle = ResourceBundle.getBundle("com.axiastudio.zoefx.core.resources.i18n");
        URL url = getClass().getResource("/com/axiastudio/zoefx/core/view/msgbox/msgbox.fxml");
        FXMLLoader loader = new FXMLLoader(url, bundle);
        loader.setResources(bundle);
        loader.setLocation(url);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        MsgBoxController controller = new MsgBoxController();
        loader.setController(controller);
        Parent root=null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        controller.setMessage(message);
        controller.setMasthead(masthead);
        controller.setDetails(details);
        controller.config(type);
        Scene scene = new Scene(root);
        Skins.getActiveSkin().getStyle().ifPresent(s -> scene.getStylesheets().add(s));
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.showAndWait();
        return controller.getResponse();
    }

}
