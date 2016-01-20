package com.lawnmower.main;

import com.lawnmower.data.model.DataManager;
import com.lawnmower.data.objects.DateObject;

/**
 * Created by haydenbraxton on 12/10/15.
 */
/* defines a thread that keeps track of the date and calls methods to create invoices and
	 * weekly technician assignments at the beginning of the month and on Mondays, respectively.
	 * A DateListener object is instantiated and run in the constructor and continues for the entire
	 * time for which the program is running
	 */
public class DateListener implements Runnable {

    Thread thread;

    DateObject today = new DateObject();
    DateObject lastWeekly;
    DateObject lastInvoice;

    DataManager dataManager = DataManager.getInstance();

    boolean stop = false;

    public DateListener() {
        this.lastWeekly = dataManager.getLastWeekly();
        this.lastInvoice = dataManager.getLastInvoice();
    }


    @Override
    public void run() {

			/* continues indefinitely
			 * constantly checks the date and creates weekly assignments and invoices as necessary.
			 * makes weekly technician assignments on every sunday before the work week starts.
			 * makes invoices for previous month on the first of the next month
			 */
        while (!stop) {

            this.today = new DateObject();

            if (today.month() < 11 && today.month() > 2) {


                if (!lastWeekly.isThisWeek()) {
                    dataManager.allWeeklyAssignments(today);
                    this.lastWeekly = today;
                }

                if (!lastInvoice.monthEquals(today)) {
                    dataManager.allInvoices(today);
                    this.lastInvoice = today;
                }

            }
            thread.interrupt();
        }

    }

    public void start() {

        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }

    }

    public void stop() {
        stop = true;
    }

}
