package com.lawnmower.data.objects.reports;

import com.lawnmower.data.objects.DTO;
import com.lawnmower.data.objects.DateObject;
import com.lawnmower.data.objects.people.Customer;
import com.lawnmower.data.objects.people.Person;
import com.lawnmower.data.objects.people.Technician;
import com.lawnmower.util.F;
import com.lawnmower.util.R;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class Report extends DTO {

    private String subType;
    private String id;
    private DateObject reportDate;
    private String contactId;
    private Person contact;

    public Report() {
        super(R.REPORT);
        this.subType = "";
        this.id = "";
        this.contactId = "";
        this.reportDate = new DateObject();
    }

    public Report(String id) {
        this();
        this.id = id;
    }

    public Report(JSONObject json) {
        this();
        try {
            this.subType = json.getString(R.TYPE);
            this.id = json.getString(R.ID);
            this.contactId = json.getString(R.SUBJECT_ID);
            this.reportDate = new DateObject(json.getJSONObject(R.DATE));

            JSONObject contact = json.getJSONObject(R.CONTACT);

            if (contact.getString(R.TYPE).equals(R.TYPE_TECH)) {
                this.contact = new Technician(contact);
            } else {
                this.contact = new Customer(contact);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String createHeader() {

        StringBuilder message = new StringBuilder();

        String tag = this.subType + " " + this.id;

        message.append(F.leftRight(R.COMPANY_NAME, tag));

        message.append(F.leftRight(R.COMPANY_ADDRESS_1, this.reportDate.toString()));
        message.append(F.left(R.COMPANY_ADDRESS_2));


        return message.toString();

    }

    public String createContact() {

        StringBuilder message = new StringBuilder();

        String name = contact.getName();
        String id = contact.getId();
        message.append(F.leftRight(name, id));

        String address = contact.getAddress().firstLine();
        String phone = contact.getPhone();
        message.append(F.leftRight(address, phone));

        address = contact.getAddress().secondLine();
        String email = contact.getEmail();
        message.append(F.leftRight(address, email));

        return message.toString();
    }

    public abstract String createContent();

    public String createReport() {

        StringBuilder report = new StringBuilder();
        report.append(F.divider());
        report.append(createHeader());
        report.append(F.divider());
        report.append(createContact());
        report.append(F.divider());
        report.append(createContent());
        report.append(F.divider());

        return report.toString();

    }

    public JSONObject toJSON() {
        JSONObject report = new JSONObject();
        try {
            report.put(R.TYPE, this.subType);
            report.put(R.ID, this.id);
            report.put(R.SUBJECT_ID, this.contactId);
            report.put(R.DATE, this.reportDate.toJSON());
            report.put(R.CONTACT, this.contact.toJSON());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return report;
    }

    public Person getContact() {
        return contact;
    }

    public void setContact(Person contact) {
        this.contact = contact;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String type) {
        this.subType = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String subjectId) {
        this.contactId = subjectId;
    }

    public DateObject getDate() {
        return reportDate;
    }

    public void setDate(DateObject date) {
        this.reportDate = date;
    }

}
