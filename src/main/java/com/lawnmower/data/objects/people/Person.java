package com.lawnmower.data.objects.people;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import com.lawnmower.data.objects.DTO;
import com.lawnmower.data.objects.DateObject;
import com.lawnmower.util.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class Person extends DTO {

    private String id;
    private String subType;
    private String lastName;
    private String firstName;
    private DateObject beginDate = new DateObject();
    private Address address;
    private String phone;
    private String email;
    private ArrayList<String> invoices = new ArrayList<>();

    public Person() {
        super(R.PERSON);
        this.lastName = "";
        this.firstName = "";
        this.id = "";
        this.address = new Address();
        this.phone = "";
        this.email = "";
    }

    public Person(String id) {
        this();
        this.id = id;
    }

    public Person(JSONObject json) {

        this();
        try {
            this.id = json.getString(R.ID);
            this.subType = json.getString(R.TYPE);
            this.lastName = json.getString(R.LAST_NAME);
            this.firstName = json.getString(R.FIRST_NAME);
            this.beginDate = new DateObject(json.getJSONObject(R.DATE));
            this.address = new Address(json.getJSONObject(R.ADDRESS));
            this.phone = json.getString(R.PHONE);
            this.email = json.getString(R.EMAIL);

            JSONArray jsonInvoices = json.getJSONArray(R.INVOICE);
            this.invoices = arrayListFromJson(jsonInvoices);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public JSONObject toJSON() {

        JSONObject json = new JSONObject();

        try {
            json.put(R.ID, this.id);
            json.put(R.TYPE, this.subType);
            json.put(R.LAST_NAME, this.lastName);
            json.put(R.FIRST_NAME, this.firstName);
            json.put(R.DATE, this.beginDate.toJSON());
            json.put(R.ADDRESS, this.address.toJSON());
            json.put(R.PHONE, this.phone);
            json.put(R.EMAIL, this.email);

            json.put(R.INVOICE, arrayListToJson(this.invoices));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    protected ArrayList<String> arrayListFromJson(JSONArray array) {

        ArrayList<String> result = new ArrayList<>();
        int length = array.length();
        try {
            for ( int i = 0 ; i < length ; i++ )
                result.add(array.getString(i));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    protected JSONArray arrayListToJson(ArrayList<String> aList) {

        JSONArray json = new JSONArray();
        int length = aList.size();

        for ( int i = 0 ; i < length ; i++ )
            json.put(aList.get(i));

        return json;

    }

    protected GregorianCalendar dateFromJson(JSONArray json) {

        GregorianCalendar date = new GregorianCalendar();
        try {
            date.set(json.getInt(0), json.getInt(1), json.getInt(2));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return date;

    }

    public String getName() {
        return lastName + ", " + firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getId() {
        return id;
    }

    public String getSubType() {
        return subType;
    }

    protected void setSubType(String type) {
        this.subType = type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DateObject getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(DateObject beginDate) {
        this.beginDate = beginDate;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<String> getInvoices() {
        return invoices;
    }

    public void setInvoices(ArrayList<String> invoices) {
        this.invoices = invoices;
    }

    @Override
    public String toString() {
        return getId();
    }

    @Override
    public int hashCode() {

        return id.hashCode();

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Person other = (Person) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
