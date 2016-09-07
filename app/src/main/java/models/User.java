package models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by Keval on 30-11-2015.
 */
public class User implements Parcelable {
    private long userId;
    private String userName;
    private String userNumber;
    private String userBDate;
    private String userEmail;
    private String userPass;
    private String userGroup;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public String getUserGroup() { return userGroup; }

    public String getUserPass() { return userPass;
    }

    public void setUserPass() { this.userPass = userPass; }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getUserBDate() {
        return userBDate;
    }

    public void setUserBDate(String userBDate) {
        this.userBDate = userBDate;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserGroup(String userGroup) { this.userGroup = userGroup; }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel p) {
            return new User(p);
        }

        public User[] newArray(int size) { return new User[size]; }
    };

    public User(Parcel p) {

        Log.d("Parcel object: ", p.toString());
        this.userId = p.readLong();
        this.userName = p.readString();
        this.userNumber = p.readString();
        this.userEmail = p.readString();
        this.userPass = p.readString();
        this.userBDate = p.readString();
        this.userGroup = p.readString();
    }

    public User(long userId, String userName, String userNumber, String userEmail, String userPass, String userBDate, String userGroup) {

    }

    public User() {
    }

    /**
     * Serialize Person object by using writeToParcel.
     * This function is automatically called by the
     * system when the object is serialized.
     *
     * @param dest Parcel object that gets written on
     * serialization. Use functions to write out the
     * object stored via your member variables.
     *
     * @param flags Additional flags about how the object
     * should be written. May be 0 or PARCELABLE_WRITE_RETURN_VALUE.
     * In our case, you should be just passing 0.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    @Override
    public int describeContents() {
        return 0;
    }
}
