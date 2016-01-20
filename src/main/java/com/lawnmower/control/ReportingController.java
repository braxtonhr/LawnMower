package com.lawnmower.control;

import com.lawnmower.layout.InputServiceReport;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by haydenbraxton on 12/11/15.
 */
public class ReportingController extends BaseController implements Initializable {

    InputServiceReport inputBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        inputBox = new InputServiceReport();

    }


    public void inputServiceReport() {

        inputBox.display();

    }

}
