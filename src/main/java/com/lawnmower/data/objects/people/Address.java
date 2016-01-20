package com.lawnmower.data.objects.people;


import com.lawnmower.util.R;
import org.json.JSONException;
import org.json.JSONObject;


public class Address {

    private String street;
    private String city;
    private String state;
    private String zip;

    public Address() {
        this.street = "";
        this.city = "";
        this.state = "";
        this.zip = "";
    }

    public Address(JSONObject address) {

        this();
        try {
            this.street = address.getString(R.STREET);
            this.city = address.getString(R.CITY);
            this.state = address.getString(R.STATE);
            this.zip = address.getString(R.ZIP);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public JSONObject toJSON() {

        JSONObject address = new JSONObject();
        try {
            address.put(R.STREET, street);
            address.put(R.CITY, city);
            address.put(R.STATE, state);
            address.put(R.ZIP, zip);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return address;

    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String firstLine() {
        return street;
    }

    public String secondLine() {
        return city + ", " + state + " " + zip;
    }

    public boolean equals(Address address) {

        boolean result = true;
        result = result && (street.equals(address.getStreet()));
        result = result && (city.equals(address.getCity()));
        result = result && (state.equals(address.getState()));
        result = result && (zip.equals(address.getZip()));

        return result;

    }

    @Override
    public String toString() {

        return street + "\n" + city + ", " + state + " " + zip;

    }

}
