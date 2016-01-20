package com.lawnmower.data.objects.reports;

import com.lawnmower.data.objects.DateObject;
import com.lawnmower.data.objects.people.Technician;
import com.lawnmower.util.F;
import com.lawnmower.util.R;
import org.json.JSONException;
import org.json.JSONObject;


public class ServiceSummary {

    private Technician tech = new Technician();
    private String summary;
    private DateObject serviceDate = new DateObject();

    public ServiceSummary() {
        this.summary = "";
    }

    public ServiceSummary(Technician tech) {
        this();
        this.tech = tech;
    }


    public ServiceSummary(JSONObject json) {
        this();
        try {
            this.tech = new Technician(json.getJSONObject(R.TECH));
            this.summary = json.getString(R.SUMMARY);
            this.serviceDate = new DateObject(json.getJSONObject(R.DATE));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public JSONObject toJSON() {

        JSONObject json = new JSONObject();

        try {
            json.put(R.TECH, this.tech.toJSON());
            json.put(R.SUMMARY, this.summary);
            json.put(R.DATE, this.serviceDate.toJSON());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;

    }



    public Technician getTech() {
        return tech;
    }


    public void setTech(Technician tech) {
        this.tech = tech;
    }


    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public DateObject getDate() {
        return serviceDate;
    }

    public void setDate(DateObject date) {
        this.serviceDate = date;
    }

    @Override
    public String toString() {

        StringBuilder message = new StringBuilder();

        String name = tech.getName();
        String id = tech.getId();

        message.append(F.leftRight(name, id));
        message.append(F.left(tech.getPhone()));
        message.append(F.left(tech.getEmail()));
        message.append(F.newLine(1));

        message.append("Date of Service: " + this.serviceDate.toString());
        message.append(F.newLine(1));

        message.append(F.left("Summary of work completed:"));
        message.append(F.left(this.summary));
        message.append(F.left("service charge: " + R.CHARGE));

        return message.toString();
    }

}