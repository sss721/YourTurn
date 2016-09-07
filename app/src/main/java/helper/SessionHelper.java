package helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Apoorva on 12/13/2015.
 */
public class SessionHelper {

    private SharedPreferences prefs;

    public SessionHelper(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setusername(String username) {
        prefs.edit().putString("username", username).commit();
    }

    public String getusername() {
        String username = prefs.getString("username","");
        return username;
    }

    public void setGroupID(String groupID) {
        prefs.edit().putString("groupid", groupID).commit();
    }

    public String getGroupID() {
        String groupID = prefs.getString("groupid","");
        return groupID;
    }


}
