package com.lawnmower.data.objects.reports;

import java.util.ArrayList;

import com.lawnmower.data.objects.DateObject;
import com.lawnmower.util.F;
import com.lawnmower.util.R;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;


public class Invoice extends Report {

    // assume statement period goes from first day of month to last day of month
    // invoices sent at beginning of month for services the month previous

    private DateObject stmtPrdStart = new DateObject();
    private DateObject stmtPrdEnd = new DateObject();
    private ArrayList<ServiceSummary> serviceReports = new ArrayList<>();
    private String stmtPrd;

    public Invoice() {
        super();
        this.setSubType(R.TYPE_INVOICE);
        stmtPrdStart = stmtPrdStart.getPrevMonth();
        this.stmtPrdEnd = stmtPrdStart.getMonthEnd();
        setStatementPeriod(stmtPrdStart, stmtPrdEnd);
    }

    public Invoice(String id) {
        this();
        this.setId(id);
    }

    public Invoice(JSONObject json) {
        super(json);
        try {
            this.stmtPrdStart = new DateObject(json.getJSONObject(R.START));
            this.stmtPrdEnd = new DateObject(json.getJSONObject(R.END));
            this.serviceReports = arrayListFromJson(json.getJSONArray(R.SERVICE));
            this.stmtPrd = json.getString(R.PERIOD);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public JSONObject toJSON() {

        JSONObject json = new JSONObject();
        json = super.toJSON();

        try {
            json.put(R.START, stmtPrdStart.toJSON());
            json.put(R.END, stmtPrdEnd.toJSON());
            json.put(R.SERVICE, arrayListToJson(serviceReports));
            json.put(R.PERIOD, stmtPrd);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;

    }

    @Override
    public String createContent() {

        StringBuilder message = new StringBuilder();

        message.append(F.centered(this.getSubType()));
        message.append(F.newLine(1));

        message.append(F.left("Statement Period: " + stmtPrd));
        message.append(F.newLine(1));

        message.append(F.left("Service History:"));
        message.append(F.newLine(1));

        int length = serviceReports.size();

        for ( int i = 0 ; i < length ; i++ ) {
            message.append(serviceReports.get(i).toString());
            message.append(F.newLine(1));
        }
        message.append(F.newLine(1));

        message.append("Total charge: $" + (length*100));
        message.append(F.newLine(1));

        return message.toString();

    }

    protected ArrayList<ServiceSummary> arrayListFromJson(JSONArray array) {

        ArrayList<ServiceSummary> result = new ArrayList<>();
        int length = array.length();
        try {
            for ( int i = 0 ; i < length ; i++ )
                result.add(new ServiceSummary(array.getJSONObject(i)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    protected JSONArray arrayListToJson(ArrayList<ServiceSummary> aList) {

        JSONArray json = new JSONArray();
        int length = aList.size();

        for ( int i = 0 ; i < length ; i++ )
            json.put(aList.get(i).toJSON());

        return json;

    }

    private void setStatementPeriod(DateObject start, DateObject end) {

        this.stmtPrd = start.toString() + " - " + end.toString();

    }

    public DateObject getStmtPrdStart() {
        return stmtPrdStart;
    }

    public void setStmtPrdStart(DateObject stmtPrdStart) {
        this.stmtPrdStart = stmtPrdStart;
        this.stmtPrdEnd = stmtPrdStart.getMonthEnd();
        setStatementPeriod(stmtPrdStart, this.stmtPrdEnd);
    }

    public DateObject getStmtPrdEnd() {
        return stmtPrdEnd;
    }

    public void setStmtPrdEnd(DateObject stmtPrdEnd) {
        this.stmtPrdEnd = stmtPrdEnd;
        this.stmtPrdStart = stmtPrdEnd.getMonthBegin();
        setStatementPeriod(this.stmtPrdStart, stmtPrdEnd);
    }

    public ArrayList<ServiceSummary> getServiceReports() {
        return serviceReports;
    }

    public void setServiceReports(ArrayList<ServiceSummary> serviceReports) {
        this.serviceReports = serviceReports;
    }

    public void addServiceReport(ServiceSummary service) {
        this.serviceReports.add(service);
    }

    public String getStmtPrd() {
        return stmtPrd;
    }

}
