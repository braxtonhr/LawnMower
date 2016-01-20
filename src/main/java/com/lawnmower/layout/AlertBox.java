package com.lawnmower.layout;

import com.lawnmower.util.R;
import javafx.stage.Modality;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by haydenbraxton on 12/11/15.
 */
public class AlertBox {

    static boolean result = true;
    static final String css = AlertBox.class.getResource(R.layout.CSS).toExternalForm();

    public static boolean display(String title, String message, String negative, String positive) {

        Stage window = new Stage();
        window.setTitle(title);
        Button buttonNegative = new Button(negative);
        Button buttonPositive = new Button(positive);
        buttonNegative.setOnAction(e -> {
            result = false;
            window.close();
        });

        buttonPositive.setOnAction(e -> {
            result = true;
            window.close();
        });

        VBox layout = new VBox();
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setSpacing(4);
        layout.getChildren().addAll(new Label(message), buttonNegative, buttonPositive);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        scene.getStylesheets().add(css);

        window.setScene(scene);
        window.setAlwaysOnTop(true);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(200);
        window.setMinHeight(150);
        window.showAndWait();

        return result;

    }

}
