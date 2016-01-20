package com.lawnmower.main;

import com.lawnmower.util.R;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Created by haydenbraxton on 12/10/15.
 */
public class Main extends Application {

    public static void main(String[] args) { launch(args); }
    public static String css = "";
    private static Stage window;
    private static DateListener listener = new DateListener();

    @Override
    public void start(Stage primaryStage) throws Exception {

        css = Main.class.getClass().getResource(R.layout.CSS).toExternalForm();

        listener.start();
        Parent root = FXMLLoader.load(Main.class
                .getResource(R.layout.HomeScreen));

        Scene main = new Scene(root, R.VIEW_WIDTH, R.VIEW_HEIGHT);
        main.getStylesheets().add(css);

        window = primaryStage;
        window.setTitle("LawnMower");
        window.setMinWidth(R.VIEW_WIDTH);
        window.setMinHeight(R.VIEW_HEIGHT);


        window.setScene(main);
        window.setOnCloseRequest(e -> exit());
        window.show();

    }

    public static void changeScenes(Parent scene) {

        if (window != null) {
            Scene main = new Scene(scene, R.VIEW_WIDTH, R.VIEW_HEIGHT);
            main.getStylesheets().add(css);
            window.setScene(main);
        }
    }

    public static void exit() {

        listener.stop();
        Platform.exit();
        System.exit(0);

    }

}
