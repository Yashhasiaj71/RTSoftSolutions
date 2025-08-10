package com.example.rtsoftsolutions.miscallenous;

import static android.net.NetworkCapabilities.NET_CAPABILITY_FOREGROUND;
import static android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET;
import static android.net.NetworkCapabilities.NET_CAPABILITY_VALIDATED;
import static android.net.NetworkCapabilities.TRANSPORT_CELLULAR;
import static android.net.NetworkCapabilities.TRANSPORT_WIFI;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.util.Log;

import androidx.annotation.NonNull;

public class CheckStatus {

    NetworkRequest networkrequest;
    private ConnectivityManager.NetworkCallback networkCallback;
    private boolean isconnected;
//build network request
    public void createnetworkfunction() {
        networkrequest = new NetworkRequest.Builder().
                addCapability(NET_CAPABILITY_FOREGROUND).
                addCapability(NET_CAPABILITY_INTERNET).
                addCapability(NET_CAPABILITY_VALIDATED).
                addTransportType(TRANSPORT_CELLULAR).
                addTransportType(TRANSPORT_WIFI).build();
    }

//register callbacks
    public void registernetworkcallback() {
        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                setIsconnected(true);
            }

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                setIsconnected(false);
            }

            @Override
            public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
                super.onCapabilitiesChanged(network, networkCapabilities);
                final boolean isvalid = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED) || networkCapabilities.hasCapability(NET_CAPABILITY_FOREGROUND) || networkCapabilities.hasCapability(NET_CAPABILITY_VALIDATED) || networkCapabilities.hasCapability(NET_CAPABILITY_INTERNET);
                setIsconnected(isvalid);
            }

            @Override
            public void onUnavailable() {
                super.onUnavailable();
                setIsconnected(false);

            }
        };


    }

//register for updates
    public void registerforupdates() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(ConnectivityManager.class);
        if(connectivityManager != null) {
            connectivityManager.requestNetwork(networkrequest, networkCallback);
        }else{
            setIsconnected(true);
        }
    }

    private Object getSystemService(Class<ConnectivityManager> connectivityManagerClass) {
        return null;
    }

    // :todo : implement getters and setters

    public void setIsconnected(boolean value) {
        isconnected = value;
    }

    public boolean getIsconnected() {
        return isconnected ;
    }
}

