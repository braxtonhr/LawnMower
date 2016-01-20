package com.lawnmower.data.objects.reports;

import com.lawnmower.util.F;
import com.lawnmower.util.R;
import org.json.JSONObject;
import org.json.JSONException;


public class Service extends Report {

    private ServiceSummary summary;
    private String authentication;

    public Service() {
        super();
        super.setSubType(R.TYPE_SERVICE);
        this.summary = new ServiceSummary();
        authentication = "";
    }

    public Service(String id) {
        this();
        this.setId(id);
    }

    public Service(JSONObject json) {
        super(json);

        try {
            this.summary = new ServiceSummary(json.getJSONObject(R.SUMMARY));
            this.authentication = json.getString(R.AUTH);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public JSONObject toJSON() {
        JSONObject report = new JSONObject();
        report = super.toJSON();
        try {
            report.put(R.SUMMARY, this.summary.toJSON());
            report.put(R.AUTH, authentication);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return report;
    }

    @Override
    public String createContent() {

        StringBuilder message = new StringBuilder();

        message.append(F.centered(this.getSubType()));
        message.append(F.newLine(1));

        message.append(this.summary.toString());
        message.append(F.newLine(1));

        message.append(F.left(this.authentication));

        return message.toString();

    }

    public ServiceSummary getSummary() {
        return summary;
    }

    public void setSummary(ServiceSummary summary) {
        this.summary = summary;
    }

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }


}

