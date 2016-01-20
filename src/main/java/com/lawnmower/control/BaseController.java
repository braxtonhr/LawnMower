package com.lawnmower.control;

import com.lawnmower.main.Main;
import com.lawnmower.util.R;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class BaseController {

    private Parent scene;

    public void navToTechMgmt() {

        try {
            scene = FXMLLoader.load(getClass().getResource(R.layout.TechnicianManagement));
            Main.changeScenes(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void navToCustomerMgmt() {

        try {
            scene = FXMLLoader.load(getClass().getResource(R.layout.CustomerManagement));
            Main.changeScenes(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void navToReporting() {

        try {
            scene = FXMLLoader.load(getClass().getResource(R.layout.ReportsHome));
            Main.changeScenes(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void onPressBack() {

        try {
            scene = FXMLLoader.load(getClass().getResource(R.layout.HomeScreen));
            Main.changeScenes(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exit() { Main.exit(); }

}
