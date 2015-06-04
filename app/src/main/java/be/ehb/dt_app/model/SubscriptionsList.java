package be.ehb.dt_app.model;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Bart on 2/06/2015.
 */
public class SubscriptionsList extends SugarRecord<Subscription> {
    private List<Subscription> subscriptions;

    public SubscriptionsList(){

    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    @Override
    public String toString() {
        return "SubscriptionsList{" +
                "subscriptions=" + subscriptions +
                '}';
    }
}
