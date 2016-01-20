package com.lawnmower.data.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;
import com.lawnmower.data.objects.people.Customer;
import com.lawnmower.data.objects.people.Person;
import com.lawnmower.data.objects.people.Technician;
import com.lawnmower.util.R;
import org.json.JSONException;
import org.json.JSONObject;

/* Manages data related to People objects: Customers and Technicians
 * This class is a singleton
 */

@SuppressWarnings("unused")
public class PeopleManager extends FileManager {

    private final String TAG = this.getClass().toString();

    private final static PeopleManager instance = new PeopleManager();

    private String customerId;
    private String techId;
    private HashMap<String, Technician> techs = new HashMap<>();		// maps ids to techs
    private HashMap<String, Customer> customers = new HashMap<>();		// maps ids to customers
    private PriorityQueue<Entry> customerAssignQueue = new PriorityQueue<>();	// determines next tech to assign customer to

    public static PeopleManager getInstance() { return instance; }

    private PeopleManager() {

        super();			// super constructor initializes directories and files if need be
        loadTechData();		// fills above data structures from json data
        loadCustomerData();

    }

    public HashMap<String, Technician> getTechs() {

        return this.techs;

    }

    public HashMap<String, Customer> getCustomers() {

        return this.customers;

    }

    public String getNewId(String type) {

        String newId = "";

        if (type.equals(R.TYPE_TECH)) {
            newId =  this.techId;
            this.techId = incrId(techId);
        } else {
            newId = this.customerId;
            this.customerId = incrId(customerId);
        }

        return newId;

    }

    // puts a new tech object into data storage
    public void put(Technician tech) {

        write(tech);	// writes tech to data
        techs.put(tech.getId(), tech);		// puts into hashmap, clobbering old object if map already contains id
        customerAssignQueue.add(new Entry(tech.getCustomerCount(), tech.getId()));	// adds tech object to pQueue

    }

    // puts a customer into data
    public void put(Customer customer) {

        // assigns customer to a technician
        if (customer.getTech().equals("")) {
            String id = customerAssignQueue.remove().getId();
            Technician tech = techs.get(id);
            tech.addCustomer(customer);
            write(tech);
            techs.put(tech.getId(), tech);
            customerAssignQueue.add(new Entry(tech.getCustomerCount(), tech.getId()));
            customer.setTech(id);
        }

        write(customer);
        customers.put(customer.getId(), customer);

    }

    public Person get(String id) {

        if (typeSwitch(id).equals(R.TYPE_TECH)) {
            return getTech(id);
        } else {
            return getCustomer(id);
        }

    }

    // returns tech with given id or null if no such tech exists
    private Technician getTech(String id) {

        Technician tech = null;
        tech = techs.get(id);
        return tech;

    }

    // returns customer with given id or null if no such customer exists
    private Customer getCustomer(String id) {

        Customer customer = null;
        customer = customers.get(id);
        return customer;

    }

    // public method for deleting person objects
    public void delete(String id) {

        if (id.charAt(0) == 't') {
            deleteTech(id);
        } else {
            deleteCustomer(id);
        }

    }

    // removes tech with given id from storage and from current state
    private void deleteTech(String id) {

        Technician tech = techs.remove(id);
        removePriority(id);
        removeJSON(R.TYPE_TECH, id);

        ArrayList<String> reassign = tech.getCustomers(); // we have to reassign all these customers
        int length = reassign.size();

        for ( int i = 0 ; i < length ; i++ ) {
            Customer customer = customers.get(reassign.get(i));
            customer.setTech("");
            put(customer);
        }

    }

    // removes customer with given id from storage and from current state
    private void deleteCustomer(String id) {

        Customer customer = customers.remove(id);
        removeJSON(R.TYPE_CUSTOMER, id);

        techId = customer.getTech();
        Technician tech = techs.get(techId);
        tech.removeCustomer(id);
        removePriority(techId);
        put(tech);

    }

    // loads tech map with data from json files
    private void loadTechData() {

        try {

            JSONObject read = read(R.TYPE_TECH);
            this.techId = read.getString(R.ID); // sets current id
            JSONObject json = read.getJSONObject(R.TYPE_TECH); // tech objects to be parsed through

            Iterator data = json.keys();

            // loop through all keys contained in json data set and creates technician from associated json object
            while (data.hasNext()) {

                String key = data.next().toString();
                Technician tech = new Technician(json.getJSONObject(key));
                techs.put(key, tech);
                customerAssignQueue.add(new Entry(tech.getCustomerCount(), tech.getId()));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // loads customer map with data from json file
    private void loadCustomerData() {

        try {

            JSONObject read = read(R.TYPE_CUSTOMER);
            JSONObject json = read.getJSONObject(R.TYPE_CUSTOMER);
            this.customerId = read.getString(R.ID);

            Iterator data = json.keys();

            while (data.hasNext()) {

                String key = data.next().toString();
                Customer customer = new Customer(json.getJSONObject(key));
                customers.put(key, customer);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // removes pQueue entry with given id value
    private void removePriority(String id) {

        Entry delete = new Entry(0, id);
        customerAssignQueue.remove(delete);

    }

    /* private nested class for entries in the pQueue
     * ordering defined from least to greatest based on
     * customer count so that tech with associated id
     * and lowest number of customers is at head of queue
     */
    private class Entry implements Comparable<Entry> {

        private Integer count;
        private String id;

        public Entry(Integer count, String id) {
            this.count = count;
            this.id = id;
        }

        public Entry(Technician tech) {
            this(tech.getCustomerCount(), tech.getId());
        }

        @Override
        public int compareTo(Entry o) {

            int result = 0;

            if (this.getCount() < o.getCount()) result = -1;
            else if (this.getCount() > o.getCount()) result = 1;

            return result;

        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Entry)) return false;

            Entry entry = (Entry) o;
            return id.equals(entry.id);

        }

        @Override
        public int hashCode() {
            int result = count.hashCode();
            result = 31 * result + id.hashCode();
            return result;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }


    }

}
