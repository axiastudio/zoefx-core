package com.axiastudio.zoefx.core.view;

import javafx.beans.NamedArg;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Paint;

/**
 * User: tiziano
 * Date: 18/04/14
 * Time: 00:42
 */
public class ZoeScene extends Scene {
    public ZoeScene(@NamedArg("root") Parent parent) {
        super(parent);
    }

    public ZoeScene(@NamedArg("root") Parent parent, @NamedArg("width") double v, @NamedArg("height") double v2) {
        super(parent, v, v2);
    }

    public ZoeScene(@NamedArg("root") Parent parent, @NamedArg(value = "fill", defaultValue = "WHITE") Paint paint) {
        super(parent, paint);
    }

    public ZoeScene(@NamedArg("root") Parent parent, @NamedArg("width") double v, @NamedArg("height") double v2, @NamedArg(value = "fill", defaultValue = "WHITE") Paint paint) {
        super(parent, v, v2, paint);
    }

    public ZoeScene(@NamedArg("root") Parent parent, @NamedArg(value = "width", defaultValue = "-1") double v, @NamedArg(value = "height", defaultValue = "-1") double v2, @NamedArg("depthBuffer") boolean b) {
        super(parent, v, v2, b);
    }

    public ZoeScene(@NamedArg("root") Parent parent, @NamedArg(value = "width", defaultValue = "-1") double v, @NamedArg(value = "height", defaultValue = "-1") double v2, @NamedArg("depthBuffer") boolean b, @NamedArg(value = "antiAliasing", defaultValue = "DISABLED") SceneAntialiasing sceneAntialiasing) {
        super(parent, v, v2, b, sceneAntialiasing);
    }
}
