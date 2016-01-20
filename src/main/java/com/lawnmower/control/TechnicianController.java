package com.lawnmower.control;

import com.lawnmower.data.model.DataManager;
import com.lawnmower.data.objects.people.Customer;
import com.lawnmower.data.objects.people.Technician;
import com.lawnmower.layout.AlertBox;
import com.lawnmower.layout.GenericListBox;
import com.lawnmower.layout.TechInfoBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

import java.net.URL;
import java.util.*;

/**
 * Created by haydenbraxton on 12/10/15.
 */
public class TechnicianController extends BaseController implements Initializable {

    public DataManager dataManager;
    public ListView<String> techList;
    public ObservableList<String> namesList;
    public HashMap<String, Technician> techs;
    HashMap<String, String> inverse;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        dataManager = DataManager.getInstance();
        loadData();

    }

    public void printWeekly() {


    }

    public void viewAssingedCustomers() {

        String name = techList.getSelectionModel().getSelectedItem();

        if (name != null) {

            ObservableList<String> customerList = FXCollections.observableArrayList();
            String id = inverse.get(name);
            Technician tech = techs.get(id);

            if (tech != null) {

                HashMap<String, Customer> customers = dataManager.getCustomers(tech.getCustomers());

                for (String k : customers.keySet()) {
                    customerList.add(k + " : " + customers.get(k).getName());
                }
            }

            GenericListBox.display("Customers", "Customers", customerList);

        }

    }

    public void techInfo() {

        String name = techList.getSelectionModel().getSelectedItem();

        if (name != null) {

            TechInfoBox infoBox = new TechInfoBox();
            Technician tech = techs.get(inverse.get(name));
            infoBox.displayTech(tech);

        }

    }

    public void editInfo() {

        Technician tech;
        String name = techList.getSelectionModel().getSelectedItem();
        TechInfoBox infoBox = new TechInfoBox();
        boolean updated = true;

        if (name != null) {

            tech = techs.get(inverse.get(name));
            updated = infoBox.editTech(tech);

            System.out.println(updated);

        }

        if (updated) {

            loadData();

        }

    }

    public void add() {

        TechInfoBox infoBox = new TechInfoBox();
        infoBox.newTech();
        loadData();

    }

    public void delete() {

        String name = techList.getSelectionModel().getSelectedItem();

        if (name != null) {

            boolean result = AlertBox.display("Delete Technician",
                    "Do you want to delete this technician?",
                    "Cancel", "Delete");

            if (result) {
                dataManager.delete(inverse.get(name));
                loadData();
            }
        }

    }

    private void loadData() {

        techs = dataManager.getTechs();
        inverse = new HashMap<>();

        for (String key : techs.keySet()) {
            inverse.put(techs.get(key).getName(), key);
        }

        System.out.println(inverse);
        namesList = FXCollections.observableArrayList();
        namesList.setAll(inverse.keySet());
        techList.setItems(namesList);
        techList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    }

}

