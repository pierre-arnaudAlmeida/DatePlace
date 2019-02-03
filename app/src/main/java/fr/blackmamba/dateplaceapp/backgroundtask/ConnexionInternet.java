package fr.blackmamba.dateplaceapp.backgroundtask;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnexionInternet {
    /**
     * Va vérifier si l'activité possède une connexion a internet
     * @param activity activité que l'on veut tester
     * @return
     */
    public static boolean isConnectedInternet(Activity activity)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null)
        {
            NetworkInfo.State networkState = networkInfo.getState();
            if (networkState.compareTo(NetworkInfo.State.CONNECTED) == 0)
            {
                return true;
            }
            else return false;
        }
        else return false;
    }
}
