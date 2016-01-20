package com.lawnmower.data.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


import com.lawnmower.data.objects.DateObject;
import com.lawnmower.data.objects.people.Person;
import com.lawnmower.data.objects.reports.Report;
import com.lawnmower.util.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* Abstract class that PeopleManager and ReportManager inherit from.
 * provides general methods necessary to both managers in order to reduce
 * code repition.
 */

//TODO: we can generalize put methods for the two managers if we devise a generalized method to assign new id
// id assignment should be pushed up to DataManager

public abstract class FileManager {

    protected String sep;

    private File lawnHome;
    private File data;
    private File reports;

    private File serviceReports;
    private File weeklyAssignments;
    private File invoices;

    private File customerData;
    private File techData;
    private File serviceData;
    private File weeklyData;
    private File invoiceData;

    private int idWidth = 6;

    protected FileManager() {

        initDirs();

    }

    public abstract String getNewId(String type);

    /* initializes directories for data storage and reports
     * if they do not already exist. Also initializes the json
     * files if they do not already exists, by calling
     * intiDataFiles()
     */
    private void initDirs() {

        // TODO: refactor out repeated code in this method

        try {

            File homeDir = new File(System.getProperty("user.home"));
            String home = homeDir.getCanonicalPath();
            this.sep = System.getProperty("file.separator");

            this.lawnHome = new File(home + sep + R.DESKTOP + sep + R.LAWN_DIR);
            this.data = new File(this.lawnHome.getCanonicalPath() + sep + R.DATA_DIR);
            this.reports = new File(this.lawnHome.getCanonicalPath() + sep + R.REPORTS_DIR);

            String path = this.reports.getCanonicalPath();

            this.serviceReports = new File(path + sep + R.TYPE_SERVICE + sep);
            this.weeklyAssignments = new File(path + sep + R.TYPE_WEEKLY + sep);
            this.invoices = new File(path + sep + R.TYPE_INVOICE + sep);

            if (!lawnHome.isDirectory()) { lawnHome.mkdir(); }
            if (!data.isDirectory()) { data.mkdir(); }
            if (!reports.isDirectory()) { reports.mkdir(); }
            if (!serviceReports.isDirectory()) { serviceReports.mkdir(); }
            if (!weeklyAssignments.isDirectory()) { weeklyAssignments.mkdir(); }
            if (!invoices.isDirectory()) { invoices.mkdir(); }

            initDataFiles();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Initializes five .json files for storing data related to customers, technicians,
     * service reports, weekly assignment reports, and invoices.
     *
     * structure of each json file:
     *
     * {
     * 		"id":"tXXXXXX"					// the first character of the data type, followed by six digit character
     * 		"type":{						// where "type" is the actual type of data being stored
     * 					"id":{				// where "id" is an actual id value
     * 						object fields ...
     * 					},
     * 					"id"{
     * 						object fields ...
     * 					},
     * 					.
     * 					.
     * 					.
     * 				}
     * }
     *
     * the id field at the top of the file keeps track of the lowest, unused id value.
     * Every time a new object of the given type is put, that object is assigned the current
     * value of id, and id is incremented by 1.
     *
     */
    private void initDataFiles() {

        try {

            int numFiles = 5;
            File[] fileRef = new File[numFiles];
            String path = data.getCanonicalPath() + sep;

            this.customerData = new File(path + R.CUSTOMER_DATA);
            fileRef[0] = this.customerData;
            this.techData = new File(path + R.TECH_DATA);
            fileRef[1] = this.techData;
            this.serviceData = new File(path + R.SERVICE_DATA);
            fileRef[2] = this.serviceData;
            this.weeklyData = new File(path + R.WEEKLY_DATA);
            fileRef[3] = this.weeklyData;
            this.invoiceData = new File(path + R.INVOICE_DATA);
            fileRef[4] = this.invoiceData;

            for ( int i = 0; i < numFiles ; i++ ) {

                File file = fileRef[i];
                if (!file.isFile()) {

                    BufferedWriter writer = new BufferedWriter(new FileWriter(file));

                    JSONObject json = new JSONObject();
                    JSONObject jSet = new JSONObject();

                    String type = fileSwitch(file);
                    char t = Character.toLowerCase(type.charAt(0));

                    if ( i == 3 || i == 4) {
                        json.put(R.LAST_DATE, (new DateObject().toJSON()));
                    }

                    json.put(R.ID, t + "000000");
                    json.put(type, jSet);

                    writer.write(json.toString());
                    writer.close();

                }
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }

    // called to increment the given id number
    protected String incrId(String id) {

        String token = id.charAt(0) + "";
        String type = typeSwitch(token);

        String value = (new Integer(Integer.parseInt(id.substring(1, id.length())) + 1)).toString();

        // concats extra 0s on front to make six digits
        if (value.length() < idWidth) {

            int count = idWidth - value.length();

            for ( int i = 0 ; i < count ; i++ ) {
                value = "0" + value;
            }

        }

        String newId =  token + value;

        // updates the next id value in the json file
        // recalls the proper file, puts (id, newId), clobering old value, then rewrite
        try {

            JSONObject json = read(type);
            json.put(R.ID, newId);

            BufferedWriter writer = new BufferedWriter(new FileWriter(fileSwitch(type)));
            writer.write(json.toString());
            writer.close();

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        return newId;

    }

    protected String typeSwitch(String t) {

        String type = "";

        switch(t) {

            case "t":
                type = R.TYPE_TECH;
                break;
            case "c":
                type = R.TYPE_CUSTOMER;
                break;
            case "s":
                type = R.TYPE_SERVICE;
                break;
            case "w":
                type = R.TYPE_WEEKLY;
                break;
            case "i":
                type = R.TYPE_INVOICE;
                break;
        }

        return type;

    }

    //TODO: provide better generalization for the following two methods

    // writes a person object to the appropriate json file
    protected void write(Person person) {

        String type = person.getSubType();
        JSONObject json = read(type);
        File file = fileSwitch(type);

        try {

            JSONObject data = json.getJSONObject(type); // gets appropriate json data set
            data.put(person.getId(), person.toJSON()); // if person already existed in data set, then new entry replaces previous
            json.put(type, data); // replaces old version of data set in json object

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(json.toString());
            writer.close();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }

    // writes a report object to the appropriate file
    protected void write(Report report) {

        String type = report.getSubType();
        JSONObject json = read(type);
        File file = fileSwitch(type);

        try {

            JSONObject data = json.getJSONObject(type);
            data.put(report.getId(), report.toJSON());
            json.put(type, data);

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(json.toString());
            writer.close();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }

    /* writes the given json object to the file of given type
     * NOTE: the json object here should represent an entire data set,
     * not just one element of a certain set
     */
    protected void write(JSONObject obj, String type) {

        File file = fileSwitch(type);


        try {

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(obj.toString()); // rewrites over everything preexisting
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // returns the json data set of given type
    protected JSONObject read(String type) {

        JSONObject data = null;

        File current = fileSwitch(type);

        try {

            BufferedReader reader = new BufferedReader(new FileReader(current));
            String results = reader.readLine();
            data = new JSONObject(results);
            reader.close();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return data;

    }

    // removes a JSON item of given type and id from the corresponding data set
    protected void removeJSON(String type, String id) {

        try {

            JSONObject data = read(type);
            JSONObject json = data.getJSONObject(type);
            json.remove(id);
            data.put(type, json);
            write(data, type);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // following two methods provide bijection between object type and the corresponding file

    // given a particular type, method returns file reference associated with that type
    private File fileSwitch(String type) {

        File file = null;

        switch (type) {

            case R.TYPE_TECH:
                file = this.techData;
                break;
            case R.TYPE_CUSTOMER:
                file = this.customerData;
                break;
            case R.TYPE_SERVICE:
                file = this.serviceData;
                break;
            case R.TYPE_INVOICE:
                file = this.invoiceData;
                break;
            case R.TYPE_WEEKLY:
                file = this.weeklyData;
                break;
        }

        return file;

    }

    // given a file of particular type, returns string type name associated with that file
    private String fileSwitch(File file) {

        String name = file.getName();
        String result = "";

        switch (file.getName()) {

            case R.TECH_DATA:
                result = R.TYPE_TECH;
                break;
            case R.CUSTOMER_DATA:
                result = R.TYPE_CUSTOMER;
                break;
            case R.SERVICE_DATA:
                result = R.TYPE_SERVICE;
                break;
            case R.INVOICE_DATA:
                result = R.TYPE_INVOICE;
                break;
            case R.WEEKLY_DATA:
                result = R.TYPE_WEEKLY;
                break;
        }

        return result;

    }

    public File getServiceReports() {
        return serviceReports;
    }

    public void setServiceReports(File serviceReports) {
        this.serviceReports = serviceReports;
    }

    public File getWeeklyAssignments() {
        return weeklyAssignments;
    }

    public void setWeeklyAssignments(File weeklyAssignments) {
        this.weeklyAssignments = weeklyAssignments;
    }

    public File getInvoices() {
        return invoices;
    }

    public void setInvoices(File invoices) {
        this.invoices = invoices;
    }



}
