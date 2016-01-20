package com.lawnmower.data.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import com.lawnmower.data.objects.DateObject;
import com.lawnmower.data.objects.reports.Invoice;
import com.lawnmower.data.objects.reports.Report;
import com.lawnmower.data.objects.reports.Service;
import com.lawnmower.data.objects.reports.Weekly;
import com.lawnmower.util.R;
import org.json.JSONException;
import org.json.JSONObject;

// TODO load needs to be generalized

public class ReportManager extends FileManager {

    private String serviceId;
    private String weeklyId;
    private String invoiceId;
    private DateObject lastInvoice;
    private DateObject lastWeekly;
    private HashMap<String, Service> reportsService = new HashMap<>();
    private HashMap<String, Weekly> reportsWeekly = new HashMap<>();
    private HashMap<String, Invoice> reportsInvoices = new HashMap<>();

    private final static ReportManager instance = new ReportManager();

    private ReportManager() {

        super();
        loadServiceData();
        loadWeeklyData();
        loadInvoiceData();

    }

    public static ReportManager getInstance() { return instance; }

    public void put(Report report) {

        String type = report.getSubType();
        String name = type + "_" + report.getId() + ".txt";

        try {
            String path = this.fileSwitch(type).getCanonicalPath();
            File newFile = new File(path + sep + name);
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(newFile));
            fileWriter.write(report.createReport());
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // TODO there's a better way to do this than the if statements
        if (type.equals(R.TYPE_SERVICE)) {

            reportsService.put(report.getId(), (Service) report);
            write( report);

        } else if (type.equals(R.TYPE_WEEKLY)) {

            reportsWeekly.put(report.getId(), (Weekly) report);
            write(report);

        } else {

            reportsInvoices.put(report.getId(), (Invoice) report);
            write(report);

        }

    }

    public String getNewId(String type) {

        String newId = "";

        if (type.equals(R.TYPE_SERVICE)) {
            newId =  this.serviceId;
            this.serviceId = incrId(serviceId);
        } else if (type.equals(R.TYPE_WEEKLY)) {
            newId = this.weeklyId;
            this.weeklyId = incrId(weeklyId);
        } else {
            newId = this.invoiceId;
            this.invoiceId = incrId(invoiceId);
        }

        return newId;

    }

    public Report get(String id) {

        String type = typeSwitch(id);

        if (type.equals(R.TYPE_SERVICE)) {
            return reportsService.get(id);
        } else if (type.equals(R.TYPE_WEEKLY)) {
            return reportsWeekly.get(id);
        } else {
            return reportsInvoices.get(id);
        }
    }

    private void loadServiceData() {

        try {

            JSONObject read = read(R.TYPE_SERVICE);
            this.serviceId = read.getString(R.ID); // sets current id
            JSONObject json = read.getJSONObject(R.TYPE_SERVICE); // tech objects to be parsed through

            Iterator data = json.keys();

            // loop through all keys contained in json data set and creates technician from associated json object
            while (data.hasNext()) {

                String key = data.next().toString();
                Service report = new Service(json.getJSONObject(key));
                reportsService.put(key, report);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void loadWeeklyData() {

        try {

            JSONObject read = read(R.TYPE_WEEKLY);
            this.lastWeekly = new DateObject(read.getJSONObject(R.LAST_DATE));
            this.weeklyId = read.getString(R.ID); // sets current id
            JSONObject json = read.getJSONObject(R.TYPE_WEEKLY); // tech objects to be parsed through

            Iterator data = json.keys();

            // loop through all keys contained in json data set and creates technician from associated json object
            while (data.hasNext()) {

                String key = data.next().toString();
                Weekly report = new Weekly(json.getJSONObject(key));
                reportsWeekly.put(key, report);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void loadInvoiceData() {

        try {

            JSONObject read = read(R.TYPE_INVOICE);
            this.lastInvoice = new DateObject(read.getJSONObject(R.LAST_DATE));
            this.invoiceId = read.getString(R.ID); // sets current id
            JSONObject json = read.getJSONObject(R.TYPE_INVOICE); // tech objects to be parsed through

            Iterator data = json.keys();

            // loop through all keys contained in json data set and creates technician from associated json object
            while (data.hasNext()) {

                String key = data.next().toString();
                Invoice report = new Invoice(json.getJSONObject(key));
                reportsInvoices.put(key, report);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public HashMap<String,Service> getReportsService() {
        return this.reportsService;
    }

    public HashMap<String,Weekly> getReportsWeekly() {
        return this.reportsWeekly;
    }

    public HashMap<String,Invoice> getReportsInvoices() {
        return this.reportsInvoices;
    }

    private File fileSwitch(String type) {

        switch(type) {

            case R.TYPE_SERVICE:
                return super.getServiceReports();
            case R.TYPE_INVOICE:
                return super.getInvoices();
            case R.TYPE_WEEKLY:
                return super.getWeeklyAssignments();
            default:
                return null;
        }

    }

    public DateObject getLastInvoice() {
        return lastInvoice;
    }

    public void setLastInvoice(DateObject lastInvoice) {
        this.lastInvoice = lastInvoice;
        JSONObject json = read(R.TYPE_INVOICE);
        try {
            json.put(R.LAST_DATE, lastInvoice.toJSON());
            write(json, R.TYPE_INVOICE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public DateObject getLastWeekly() {
        return lastWeekly;
    }

    public void setLastWeekly(DateObject lastWeekly) {
        this.lastWeekly = lastWeekly;
        JSONObject json = read(R.TYPE_WEEKLY);
        try {
            json.put(R.LAST_DATE, lastWeekly.toJSON());
            write(json, R.TYPE_WEEKLY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}

