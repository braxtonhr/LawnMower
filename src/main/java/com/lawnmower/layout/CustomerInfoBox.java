package com.lawnmower.layout;

import com.lawnmower.data.model.DataManager;
import com.lawnmower.data.objects.DateObject;
import com.lawnmower.data.objects.people.Address;
import com.lawnmower.data.objects.people.Customer;
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
public class CustomerInfoBox {

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
    public Label technician;
    public Button buttonPositive;
    public Button buttonNegative;
    public Stage window;

    public DataManager dataManager;
    public static boolean result;
    public AlertBox alertBox;

    public CustomerInfoBox() {

        dataManager = DataManager.getInstance();
        final String css = getClass().getResource(R.layout.CSS).toExternalForm();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(R.layout.CustomerInfo));
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

    public void newCustomer() {

        Customer customer = new Customer();

        enableEdits();

        buttonPositive.setDisable(false);
        buttonNegative.setDisable(false);

        buttonNegative.setOnAction(e -> window.close());
        buttonPositive.setOnAction(e -> {
            setData(customer);
            dataManager.put(customer);
            window.close();
        });

        window.showAndWait();

    }

    public boolean editCustomer(Customer customer) {

        reloadData(customer);

        enableEdits();

        buttonPositive.setDisable(false);
        buttonNegative.setDisable(false);

        buttonNegative.setOnAction(e -> window.close());
        buttonPositive.setOnAction(e -> {
            result = alertBox.display(
                    "Edit Customer",
                    "Do you want to save these changes?",
                    "Cancel", "Save Changes");
            if (result) {
                setData(customer);
                dataManager.put(customer);
                window.close();
            }

            System.out.println(result);

        });

        window.showAndWait();

        return result;
    }

    public void displayCustomer(Customer customer) {

        reloadData(customer);

        disableEdits();

        window.show();
    }

    private void reloadData(Customer customer) {

        lastName.setText(customer.getLastName());
        firstName.setText(customer.getFirstName());
        nameBox.getChildren().add(new Label(customer.getId()));

        streetAddress.setText(customer.getAddress().getStreet());
        city.setText(customer.getAddress().getCity());
        state.setText(customer.getAddress().getState());
        zip.setText(customer.getAddress().getZip());
        phone.setText(customer.getPhone());
        email.setText(customer.getEmail());
        technician.setText(technician.getText() + customer.getTech());

        startDate.getEditor().setText(customer.getBeginDate().toString());
    }

    private void setData(Customer customer) {

        customer.setLastName(lastName.getText());
        customer.setFirstName(firstName.getText());
        Address address = customer.getAddress();
        address.setStreet(streetAddress.getText());
        address.setCity(city.getText());
        address.setState(state.getText());
        address.setZip(zip.getText());
        customer.setEmail(email.getText());
        customer.setPhone(phone.getText());
        if (startDate.getValue() != null) { customer.setBeginDate(new DateObject(startDate.getValue())); }

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

        buttonPositive.setDisable(false);
        buttonNegative.setDisable(false);

    }

}
