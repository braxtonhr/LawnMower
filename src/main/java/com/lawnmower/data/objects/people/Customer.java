package com.lawnmower.data.objects.people;

import java.util.ArrayList;

import com.lawnmower.data.objects.DateObject;
import com.lawnmower.util.R;
import org.json.JSONException;
import org.json.JSONObject;


public class Customer extends Person {

    private String tech;
    private ArrayList<String> serviceReports = new ArrayList<>();


    public Customer() {
        super();
        super.setSubType(R.TYPE_CUSTOMER);
        this.tech = "";
    }

    public Customer(JSONObject json) {

        super(json);

        try {
            this.tech = json.getString(R.TYPE_TECH);
            this.serviceReports = arrayListFromJson(json.getJSONArray(R.SERVICE_REPORT));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public JSONObject toJSON() {

        JSONObject json = super.toJSON();
        try {
            json.put(R.TYPE_TECH, this.tech);
            json.put(R.SERVICE_REPORT, arrayListToJson(this.serviceReports));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public String getTech() {
        return tech;
    }

    public void setTech(String tech) {
        this.tech = tech;
    }

    public void addServiceReport(String id) {
        this.serviceReports.add(id);

    }

    public ArrayList<String> getServiceReports() {

        return serviceReports;
    }

    public void setServiceReports(ArrayList<String> serviceReports) {
        this.serviceReports = serviceReports;
    }

    public DateObject getServiceDate() {

        int dif = this.getBeginDate().difference(DateObject.now());
        int dayOffset = this.getBeginDate().weekDayDifference(DateObject.now());
        int days = dif%14;
        int weeks = dif/7;
        DateObject service = this.getBeginDate().copyOf();
        service.addWeeks(weeks);
        service.addDays(days + dayOffset);
        return service;

    }


}
