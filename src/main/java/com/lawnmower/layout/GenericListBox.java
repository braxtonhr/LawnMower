package com.lawnmower.layout;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by haydenbraxton on 12/11/15.
 */
public class GenericListBox {

    public static void display(String title, String header, ObservableList<String> inputList) {

        Stage window = new Stage();
        window.setTitle(title);

        ListView<String> stringsList = new ListView<>(inputList);

        VBox layout = new VBox();
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setSpacing(4);
        layout.getChildren().addAll(new Label(header), stringsList);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);

        window.setScene(scene);
        window.setAlwaysOnTop(true);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(200);
        window.setMinHeight(150);
        window.showAndWait();

    }

}
