package com.lawnmower.data.objects.reports;

import java.util.ArrayList;

import com.lawnmower.data.objects.DateObject;
import com.lawnmower.data.objects.people.Customer;
import com.lawnmower.util.F;
import com.lawnmower.util.R;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class Weekly extends Report {

    private ArrayList<Entry> assignments = new ArrayList<>();

    public Weekly() {
        super();
        this.setSubType(R.TYPE_WEEKLY);
    }

    public Weekly(String id) {
        this();
        this.setId(id);
    }

    public Weekly(JSONObject json) {

        super(json);

        try {

            this.assignments = arrayListFromJson(json.getJSONArray(R.ASSIGN));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public JSONObject toJSON() {

        JSONObject json = new JSONObject();
        json = super.toJSON();

        try {
            json.put(R.ASSIGN, arrayListToJson(assignments));
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

        message.append(F.left("Weekly Assignments:"));
        message.append(F.newLine(1));

        int length = assignments.size();

        for ( int i = 0 ; i < length ; i++ ) {
            message.append(assignments.get(i).toString());
            message.append(F.newLine(1));
        }

        return message.toString();

    }

    public void addEntry(Customer customer, DateObject date) {

        assignments.add(new Entry(date, customer));

    }

    protected ArrayList<Entry> arrayListFromJson(JSONArray array) {

        ArrayList<Entry> result = new ArrayList<>();
        int length = array.length();
        try {
            for ( int i = 0 ; i < length ; i++ )
                result.add(new Entry(array.getJSONObject(i)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    protected JSONArray arrayListToJson(ArrayList<Entry> aList) {

        JSONArray json = new JSONArray();
        int length = aList.size();

        for ( int i = 0 ; i < length ; i++ )
            json.put(aList.get(i).toJSON());

        return json;

    }

    private class Entry {

        private DateObject serviceDate = new DateObject();
        private Customer customer = new Customer();

        private Entry() {}

        private Entry(DateObject date, Customer customer) {
            this.serviceDate = date;
            this.customer = customer;
        }

        private Entry(JSONObject json) {
            this();

            try {
                this.serviceDate = new DateObject(json.getJSONObject(R.SERVICE_DATE));
                this.customer = new Customer(json.getJSONObject(R.TYPE_CUSTOMER));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        private JSONObject toJSON() {

            JSONObject json = new JSONObject();

            try {
                json.put(R.SERVICE_DATE, serviceDate.toJSON());
                json.put(R.TYPE_CUSTOMER, customer.toJSON());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return json;

        }

        @Override
        public String toString() {

            StringBuilder string = new StringBuilder();

            string.append(F.left(this.serviceDate.toString()));
            string.append(F.left(this.customer.getName() + ", " + this.customer.getId()));
            string.append(F.left(this.customer.getAddress().firstLine()));
            string.append(F.left(this.customer.getAddress().secondLine()));
            string.append(F.left(this.customer.getPhone()));
            string.append(F.left(this.customer.getEmail()));

            return string.toString();

        }

    }

}
