package be.ehb.dt_app.model;


import java.util.List;

/**
 * Created by Bart on 2/06/2015.
 */
public class SubscriptionsList  {
    private List<Subscription> subscriptions;

    public SubscriptionsList(){

    }

    public SubscriptionsList(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    @Override
    public String toString() {
        return "SubscriptionsList{" +
                "subscriptions=" + subscriptions +
                '}';
    }

}
