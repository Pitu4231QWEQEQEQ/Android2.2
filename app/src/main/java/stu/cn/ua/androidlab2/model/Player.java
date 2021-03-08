package stu.cn.ua.androidlab2.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Player implements Parcelable {
    private String firstName;
    private String lastName;
    private String birthday;
    private String gender;
    public Player(String firstName, String lastName,
                  String birthday, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.gender = gender;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getBirthday() {
        return birthday;
    }
    public String getGender() {
        return gender;
    }

    public boolean isValid() {
        return !isEmpty(firstName)
                && !isEmpty(lastName)
                && !isEmpty(birthday)
                && !isEmpty(gender);
    }
    private boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }
    // --- auto-generated
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.birthday);
        dest.writeString(this.gender);
    }
    protected Player(Parcel in) {
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.birthday = in.readString();
        this.gender = in.readString();
    }

    @NonNull
    @Override
    public String toString() {
        return this.firstName + " " + this.lastName + " " + this.birthday + " " + this.gender;
    }

    public static final Creator<Player> CREATOR =
            new Creator<Player>() {
                @Override
                public Player createFromParcel(Parcel source) {
                    return new Player(source);
                }
                @Override
                public Player[] newArray(int size) {
                    return new Player[size];
                }
            };
}