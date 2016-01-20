package com.lawnmower.data.objects.people;

import java.util.ArrayList;

import com.lawnmower.data.objects.DateObject;
import com.lawnmower.util.R;
import org.json.JSONException;
import org.json.JSONObject;


@SuppressWarnings("unused")
public class Technician extends Person {

    private final String TAG = this.getClass().toString();

    private int customerCount;
    private ArrayList<String> customers = new ArrayList<>();
    private DateObject DOB = new DateObject();

    public Technician() {
        super();
        super.setSubType(R.TYPE_TECH);
        this.customerCount = 0;
    }

    public Technician(JSONObject json) {

        super(json);
        try {
            this.customers = arrayListFromJson(json.getJSONArray(R.CUSTOMERS_ASSIGNED));
            this.DOB = new DateObject(json.getJSONObject(R.DATE));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.customerCount = customers.size();

    }

    public JSONObject toJSON() {

        JSONObject json = super.toJSON();
        try {
            json.put(R.CUSTOMERS_ASSIGNED, arrayListToJson(this.customers));
            json.put(R.DOB, this.DOB.toJSON());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }



    public ArrayList<String> getCustomers() {

        return this.customers;
    }

    public DateObject getDOB() {
        return DOB;
    }

    public void setDOB(DateObject DOB) {
        this.DOB = DOB;
    }

    public int getCustomerCount() {
        return this.customerCount;
    }

    public void addCustomer(Customer customer) {
        customers.add(customer.getId());
        customerCount++;
    }

    public void removeCustomer(Customer customer) {
        customers.remove(customer.getId());
        customerCount--;
    }

    public void removeCustomer(String id) {
        customers.remove(id);
    }

    public boolean hasCustomer(String id) {
        return customers.contains(id);
    }

}

