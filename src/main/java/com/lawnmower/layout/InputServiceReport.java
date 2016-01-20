package com.lawnmower.layout;

import com.lawnmower.data.model.DataManager;
import com.lawnmower.data.objects.DateObject;
import com.lawnmower.data.objects.people.Technician;
import com.lawnmower.data.objects.reports.ServiceSummary;
import com.lawnmower.data.objects.reports.Service;
import com.lawnmower.util.R;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by haydenbraxton on 12/11/15.
 */
public class InputServiceReport {

    public TextField techId;
    public TextField customerId;
    public DatePicker reportDate;
    public DatePicker serviceDate;
    public TextArea serviceSummary;
    public TextField authentication;
    public Button buttonNegative;
    public Button buttonPositive;

    public Stage window;

    public DataManager dataManager;
    public static boolean result;

    public InputServiceReport() {

        final String css = getClass().getResource(R.layout.CSS).toExternalForm();

        try {
            dataManager = DataManager.getInstance();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(R.layout.ServiceReportInfo));
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

        buttonNegative.setOnAction(e -> window.close());
        buttonPositive.setOnAction(e -> {
            Service serviceReport = gatherData();
            saveReport(serviceReport);
            window.close();

        });

    }

    public void display() {

        window.showAndWait();

    }

    private void saveReport(Service report) {

        dataManager.put(report);

    }

    private Service gatherData() {

        Service report = new Service();

        report.setContactId(customerId.getText());
        report.setAuthentication(authentication.getText());
        if (reportDate.getValue() != null) report.setDate(new DateObject(reportDate.getValue()));

        ServiceSummary sumamary = new ServiceSummary();
        if (serviceDate.getValue() != null) sumamary.setDate(new DateObject(serviceDate.getValue()));
        sumamary.setSummary(serviceSummary.getText());
        String id = techId.getText();
        Technician tech = (Technician) dataManager.get(id);
        if (tech != null) sumamary.setTech(tech);

        report.setSummary(sumamary);

        return report;

    }

}


