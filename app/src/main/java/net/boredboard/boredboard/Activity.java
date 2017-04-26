package net.boredboard.boredboard;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Colton on 3/21/2017.
 */

public class Activity {
    String photoID;
    String title;
    String description;
    double cost;
    String location;
    double time;
    boolean indoor, outdoor;

    public Activity(String photoID, String title, String description, double cost, double time, String location,
                    boolean indoor, boolean outdoor){
        this.photoID = photoID;
        this.title = title;
        this.description = description;
        this.time = time;
        this.cost = cost;
        this.location = location;
        this.indoor = indoor;
        this.outdoor = outdoor;
    }

    public Activity(){

    }

    public String getPhotoID() {
        return photoID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public double getCost() {
        return cost;
    }

    public String getLocation() {
        return location;
    }

    public double getTime() {
        return time;
    }

    public boolean isIndoor(){
        return indoor;
    }

    public boolean isOutdoor(){
        return outdoor;
    }

}
