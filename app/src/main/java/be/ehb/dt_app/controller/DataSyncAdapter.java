package be.ehb.dt_app.controller;


import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import org.springframework.web.client.RestTemplate;

import java.util.List;

import be.ehb.dt_app.model.Subscription;
import be.ehb.dt_app.model.SubscriptionsList;

/**
 * Created by Mattia on 09/06/15.
 */
public class DataSyncAdapter extends AbstractThreadedSyncAdapter {


    private String server = null;

    public DataSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }

    public DataSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    public DataSyncAdapter(String server, Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        this.server = server;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        RestTemplate restTemplate = new RestTemplate();

        List<Subscription> localSubscriptions = Subscription.listAll(Subscription.class);
        SubscriptionsList listToSync = new SubscriptionsList(localSubscriptions);
        restTemplate.postForObject(server, listToSync, null, "subscriptions");


    }
}
