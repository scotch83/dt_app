package be.ehb.dt_app.model;


import java.util.ArrayList;

/**
 * Created by Bart on 2/06/2015.
 */
public class SubscriptionsList  {
    private ArrayList<Subscription> subscriptions;

    public SubscriptionsList(){

    }

    public SubscriptionsList(ArrayList<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public ArrayList<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(ArrayList<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    @Override
    public String toString() {
        return "SubscriptionsList{" +
                "subscriptions=" + subscriptions +
                '}';
    }

}
