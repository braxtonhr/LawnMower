package com.lawnmower.layout;

import com.lawnmower.data.model.DataManager;
import com.lawnmower.data.objects.DateObject;
import com.lawnmower.data.objects.people.Address;
import com.lawnmower.data.objects.people.Technician;
import com.lawnmower.util.R;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by haydenbraxton on 12/11/15.
 */
public class TechInfoBox {

    public TextField lastName;
    public TextField firstName;
    public TextField streetAddress;
    public TextField city;
    public TextField state;
    public TextField zip;
    public TextField email;
    public TextField phone;
    public HBox nameBox;
    public DatePicker startDate;
    public DatePicker dob;
    public Button buttonPositive;
    public Button buttonNegative;
    public Stage window;

    public DataManager dataManager;
    public static boolean result;
    public AlertBox alertBox;

    public TechInfoBox() {

        final String css = getClass().getResource(R.layout.CSS).toExternalForm();
        dataManager = DataManager.getInstance();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(R.layout.TechInfo));
            loader.setController(this);
            Parent alert = (loader.load());
            window = new Stage();
            window.setTitle("LawnMower");
            Scene scene = new Scene(alert);
            scene.getStylesheets().add(css);
            window.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void newTech() {

        Technician newTech = new Technician();

        enableEdits();

        buttonNegative.setOnAction(e -> window.close());
        buttonPositive.setOnAction(e -> {
            setData(newTech);
            dataManager.put(newTech);
            window.close();
        });

        window.showAndWait();

    }

    public boolean editTech(Technician tech) {

        reloadData(tech);

        enableEdits();

        buttonNegative.setOnAction(e -> window.close());
        buttonPositive.setOnAction(e -> {
            result = alertBox.display(
                    "Edit Technician",
                    "Do you want to save these changes?",
                    "Cancel", "Save Changes");
            if (result) {
                setData(tech);
                dataManager.put(tech);
                window.close();
            }

            System.out.println(result);

        });

        window.showAndWait();

        return result;
    }

    public void displayTech(Technician tech) {

        reloadData(tech);

        disableEdits();

        window.show();
    }

    private void reloadData(Technician tech) {

        lastName.setText(tech.getLastName());
        firstName.setText(tech.getFirstName());
        nameBox.getChildren().add(new Label(tech.getId()));
        streetAddress.setText(tech.getAddress().getStreet());
        city.setText(tech.getAddress().getCity());
        state.setText(tech.getAddress().getState());
        zip.setText(tech.getAddress().getZip());
        phone.setText(tech.getPhone());
        email.setText(tech.getEmail());

        startDate.getEditor().setText(tech.getBeginDate().toString());
        dob.getEditor().setText(tech.getDOB().toString());
    }

    private void setData(Technician tech) {

        tech.setLastName(lastName.getText());
        tech.setFirstName(firstName.getText());
        Address address = tech.getAddress();
        address.setStreet(streetAddress.getText());
        address.setCity(city.getText());
        address.setState(state.getText());
        address.setZip(zip.getText());
        tech.setEmail(email.getText());
        tech.setPhone(phone.getText());
        if (startDate.getValue() != null) { tech.setBeginDate(new DateObject(startDate.getValue())); }
        if (dob.getValue() != null ) { tech.setDOB(new DateObject(dob.getValue())); }

    }

    private void disableEdits() {

        lastName.setEditable(false);
        firstName.setEditable(false);
        streetAddress.setEditable(false);
        city.setEditable(false);
        state.setEditable(false);
        zip.setEditable(false);
        email.setEditable(false);
        phone.setEditable(false);

        startDate.setEditable(false);
        dob.setEditable(false);

        buttonPositive.setDisable(true);
        buttonNegative.setDisable(true);

    }

    private void enableEdits() {

        lastName.setEditable(true);
        firstName.setEditable(true);
        streetAddress.setEditable(true);
        city.setEditable(true);
        state.setEditable(true);
        zip.setEditable(true);
        email.setEditable(true);
        phone.setEditable(true);

        startDate.setEditable(true);
        dob.setEditable(true);

        buttonPositive.setDisable(false);
        buttonNegative.setDisable(false);

    }

}
