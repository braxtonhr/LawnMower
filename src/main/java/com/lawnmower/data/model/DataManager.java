package com.lawnmower.data.model;

import com.lawnmower.data.objects.DTO;
import com.lawnmower.data.objects.DateObject;
import com.lawnmower.data.objects.people.Customer;
import com.lawnmower.data.objects.people.Technician;
import com.lawnmower.data.objects.reports.Invoice;
import com.lawnmower.data.objects.reports.Weekly;
import com.lawnmower.data.objects.reports.Service;
import com.lawnmower.util.R;

import java.awt.image.AreaAveragingScaleFilter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

// TODO: differentiate between put(Object object) and putNew(Object object)

/* This class is a singleton that exists to be a middle-man between the controller and the data.
 * The class does not save any instance state
 */
public class DataManager {

    private PeopleManager peopleManager = PeopleManager.getInstance();
    private ReportManager reportManager = ReportManager.getInstance();
    private static final DataManager instance = new DataManager();
    private final String TAG = this.getClass().toString() + "/";

    private DataManager() {

    }

    public static DataManager getInstance() {
        return instance;
    }

    // gets data transfer object specified by the given id
    public DTO get(String id) {

        String t = id.charAt(0) + "";

        if (t.equals("c") || t.equals("t")) {
            return peopleManager.get(id);
        } else {
            return reportManager.get(id);
        }

    }

    public synchronized void delete(String id) {

        peopleManager.delete(id);

    }

    public synchronized String put(Service service) {

        // contactId will be set according to user input when controller creates service object and then passes here.
        // we're just taking that id and assigning the proper contact object

        String id = service.getContactId();
        service.setId(reportManager.getNewId(R.TYPE_SERVICE));

//		HashMap<String,Customer> customers = peopleManager.getCustomers();
        Customer customer = (Customer) peopleManager.get(id);   // customers.get(id);

        service.setContact(customer);
        reportManager.put(service);

        customer.addServiceReport(service.getId());

        peopleManager.put(customer);

        return service.getId();

    }

    public synchronized String put(Technician tech) {

        if (tech.getId().equals("")) { tech.setId(peopleManager.getNewId(R.TYPE_TECH)); }
        peopleManager.put(tech);
        return tech.getId();

    }

    public synchronized String put(Customer customer) {

        customer.setId(peopleManager.getNewId(R.TYPE_CUSTOMER));
        peopleManager.put(customer);
        return customer.getId();

    }

    public HashMap<String, Customer> getCustomers(ArrayList<String> custIdList) {

        HashMap<String,Customer> customers = new HashMap<>();

        if (custIdList != null) {

            for (String id : custIdList) {

                Customer customer = (Customer) peopleManager.get(id);

                if (customer != null) {
                    customers.put(id, customer);
                } else System.out.println("crap!");

            }

        }

        return customers;

    }

    public synchronized DateObject getLastWeekly() {
        return reportManager.getLastWeekly();
    }

    public synchronized DateObject getLastInvoice() {
        return reportManager.getLastInvoice();
    }

    public synchronized Invoice createInvoice(Customer customer, DateObject date) {

        // customer is the Customer object representing the customer for which we need to produce an invoice for
        // date is any DateObject from the month in which was just finished that we need to bill for

        Invoice invoice = new Invoice();
        invoice.setContact(customer);
        invoice.setContactId(customer.getId());
        invoice.setId(reportManager.getNewId(R.TYPE_INVOICE));

        HashMap<String,Service> serviceReports = reportManager.getReportsService();

        ArrayList<String> service = customer.getServiceReports();

        for (String s : service) {

            Service report = serviceReports.get(s);

            invoice.addServiceReport(report.getSummary());

        }

        reportManager.put(invoice);

        return invoice;

    }

    public synchronized Weekly createWeekly(Technician tech, DateObject date) {

        ArrayList<String> customers = tech.getCustomers();

        Weekly report = new Weekly();
        report.setContact(tech);
        report.setContactId(tech.getId());
        report.setId(reportManager.getNewId(R.TYPE_WEEKLY));

        for (String s : customers) {

            Customer customer = (Customer) peopleManager.get(s);
            DateObject service = customer.getServiceDate();
            System.out.println(customer.getBeginDate() + " " + service);

            if (service.isThisWeek()) {
                report.addEntry(customer, service);
            }

        }

        reportManager.put(report);

        return report;

    }

    public synchronized void allInvoices(DateObject date) {

        HashMap<String,Customer> customers = peopleManager.getCustomers();

        Iterator data = customers.keySet().iterator();
        Invoice invoice = new Invoice();

        while (data.hasNext()) {
            String id = data.next().toString();
            Customer customer = customers.get(id);
            invoice = createInvoice(customer, date);
        }
        reportManager.setLastInvoice(invoice.getDate());
    }

    public synchronized void allWeeklyAssignments(DateObject date) {

        HashMap<String,Technician> techs = peopleManager.getTechs();

        Iterator data = techs.keySet().iterator();
        Weekly report = new Weekly();

        while (data.hasNext()) {
            report = createWeekly(techs.get(data.next().toString()), date);
        }
        reportManager.setLastWeekly(report.getDate());
    }

    public synchronized HashMap<String,Customer> getCustomers() {
        return peopleManager.getCustomers();
    }

    public synchronized HashMap<String,Technician> getTechs() {
        return peopleManager.getTechs();
    }

    public synchronized HashMap<String,Service> getServiceReports() {
        return reportManager.getReportsService();
    }

    public synchronized HashMap<String,Weekly> getWeeklyReports() {
        return reportManager.getReportsWeekly();
    }

    public synchronized HashMap<String,Invoice> getInvoices() {
        return reportManager.getReportsInvoices();
    }


}
