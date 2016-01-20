package com.lawnmower.control;

import com.lawnmower.data.model.DataManager;
import com.lawnmower.data.objects.people.Customer;
import com.lawnmower.layout.AlertBox;
import com.lawnmower.layout.CustomerInfoBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Created by haydenbraxton on 12/11/15.
 */
public class CustomerController extends BaseController implements Initializable {

    public DataManager dataManager;
    public ListView<String> customerListView;
    public ObservableList<String> namesList;
    public HashMap<String, Customer> customers;
    HashMap<String, String> inverse;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        dataManager = DataManager.getInstance();
        loadData();

    }

    public void viewInvoices() {

    }

    public void viewServiceReports() {

    }

    public void customerInfo() {

        String name = customerListView.getSelectionModel().getSelectedItem();

        if (name != null) {

            CustomerInfoBox infoBox = new CustomerInfoBox();
            Customer customer = customers.get(inverse.get(name));
            infoBox.displayCustomer(customer);

        }

    }

    public void editInfo() {

        Customer customer;
        String name = customerListView.getSelectionModel().getSelectedItem();
        boolean updated = true;

        if (name != null) {

            CustomerInfoBox infoBox = new CustomerInfoBox();
            customer = customers.get(inverse.get(name));
            updated = infoBox.editCustomer(customer);
            System.out.println(updated);

        }

        if (updated) {

            loadData();

        }

    }

    public void add() {

        CustomerInfoBox infoBox = new CustomerInfoBox();
        infoBox.newCustomer();
        loadData();

    }

    public void delete() {

        String name = customerListView.getSelectionModel().getSelectedItem();

        if (name != null) {

            boolean result = AlertBox.display("Delete Customer",
                    "Do you want to delete this customer?",
                    "Cancel", "Delete");

            if (result) {
                dataManager.delete(inverse.get(name));
                loadData();
            }
        }

    }

    private void loadData() {

        customers = dataManager.getCustomers();
        inverse = new HashMap<>();

        for (String key : customers.keySet()) {
            inverse.put(customers.get(key).getName(), key);
        }

        System.out.println(inverse);
        namesList = FXCollections.observableArrayList();
        namesList.setAll(inverse.keySet());
        customerListView.setItems(namesList);
        customerListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    }

}
