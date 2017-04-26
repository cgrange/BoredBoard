package net.boredboard.boredboard;

import android.app.DialogFragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HomeScreen extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager rvLayoutManager;
    private List<Activity> activities;
    FirebaseDatabase activityDB;
    StorageReference imageRef;
    HomeScreen _this = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);

        myToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.primary));
        setSupportActionBar(myToolbar);

        recyclerView = (RecyclerView) findViewById(R.id.rv);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        //rvLayoutManager = new StaggeredGridLayoutManager(this);
        //rvLayoutManager = new GridLayoutManager(this, 2);
        rvLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(rvLayoutManager);

        //set up the databases (1 for activity info, one for images
        activityDB = FirebaseDatabase.getInstance();
        imageRef = FirebaseStorage.getInstance().getReference();

        initializeData();
    }

    private void uploadActivityInfo(){
        DatabaseReference activityRef = activityDB.getReference();
        activityRef.push().setValue(new Activity("home_run_derby.png", "Home Run Derby", "swing it. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", 0, .5, "150 University Pkwy, Provo, UT 84604", false, true));
        activityRef.push().setValue(new Activity("frisbee_soccer.png", "Frisbee Soccer", "huck it. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", 0, .5, "E 1230 N St, Provo, UT 84604", false, true));
        activityRef.push().setValue(new Activity("mountain_biking.png", "Big Springs Mountain Biking", "bike it. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", 0, 2, "84601, Spring Hollow Rd, Provo, UT 84604", false, true));
        activityRef.push().setValue(new Activity("nickel_city.png", "Nickel City", "arcade it. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", 10, 1, "1515 State St, Orem, UT 84097", true, false));
        activityRef.push().setValue(new Activity("soccer_golf.png", "Soccer Golf", "kick it. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", 0, .5, "Kiwanis Park, North 1100 East, Provo, UT", false, true));
        activityRef.push().setValue(new Activity("beach_volleyball.png", "Beach Volleyball", "play it. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", 0, .5, "Helaman Halls Provo, UT 84604", false, true));
        activityRef.push().setValue(new Activity("ice_skating.png", "Ice Skating", "skate it. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", 5, 1, "100 N Seven Peaks Blvd, Provo, UT 84606", true, false));
        activityRef.push().setValue(new Activity("hot_springs.png", "Diamond Fork Hot Springs", "soak it. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", 0, 3, "Fifth Water Hot Springs, Diamond Fork Rd, Springville, UT 84663", false, true));
        activityRef.push().setValue(new Activity("live_music.png", "Live Music at the Mall", "hear it. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", 0, .5, "1200 Towne Centre Blvd, Provo, UT 84601", true, false));
        activityRef.push().setValue(new Activity("longboarding.png", "Longboard the Canyon", "board it. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", 0, 2, "3600 S Fork Rd, Provo, UT 84604", false, true));
        activityRef.push().setValue(new Activity("rope_swing.png", "Mona Rope Swing", "swing it. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", 0, .5, "Burraston Ponds, Burraston Rd, Nephi, UT 84648", false, true));
        activityRef.push().setValue(new Activity("rumba.png", "Latin Dancing", "dance it. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", 6, 1, "116 Center St, Provo, UT 84601", true, false));
    }

    private void uploadActivity(Activity activity) {
        //***************** UPLOAD INFO ********************
        DatabaseReference activityRef = activityDB.getReference();
        activityRef.push().setValue(new Activity("beach_volleyball.png", "Beach Volleyball", "play it", 0, .5, "Helaman Halls Provo, UT 84604", false, true));

        //***************** UPLOAD IMAGE *******************
        Uri file = Uri.fromFile(new File("path/to/images/rivers.png"));
        StorageReference riversRef = imageRef.child("images/rivers.png");

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }

    private void downloadActivity() throws IOException {
        File localFile = File.createTempFile("images", "jpg");
        imageRef.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Successfully downloaded data to local file
                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                // Handle failed download
                // ...
            }
        });
    }

    public void initializeData(){
       //uploadActivityInfo();

        activities = new ArrayList<>();

        //set up the databases (1 for activity info, one for images
        activityDB = FirebaseDatabase.getInstance();
        imageRef = FirebaseStorage.getInstance().getReference();

        activityDB.getReference().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Activity activity = dataSnapshot.getValue(Activity.class);
                Log.d("load_activity", "title: " + activity.getTitle());
                Log.d("load_activity", "prev child key: " + prevChildKey);
                activities.add(activity);
                if(rvAdapter == null){
                    rvAdapter = new RVAdapter(activities, _this);
                    recyclerView.setAdapter(rvAdapter);
                }
                else{
                    ((RVAdapter)recyclerView.getAdapter()).swap(activities);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void filterData(double maxCost, double maxDistance, double maxTime, boolean indoor, boolean outdoor){
        for(int i = 0; i < activities.size(); i++){
            Activity activity = activities.get(i);
            if(activity.getCost() > maxCost){
                Log.d("removed activities", activity.getTitle() + " activity cost: " + activity.getCost() + " max cost: " + maxCost);
                activities.remove(activity);
                i--;
            }
//            else if(activity.getTime() > maxTime){
//                activities.remove(activity);
//                i--;
//            }
            else if(indoor &! outdoor){
                if(!activity.isIndoor()){
                    activities.remove(activity);
                    i--;
                }
            }
            else if(outdoor &! indoor){
                if(!activity.isOutdoor()){
                    activities.remove(activity);
                    i--;
                }
            }
            //I'll need to get google's APIs working to do distance
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.app_bar_menu, menu);
        //getMenuInflater().inflate(R.menu.app_bar_menu, menu);
        return true;
    }

    public void showFilterDialog(MenuItem item) {
        DialogFragment newFragment = FiltersDialogFragment.newInstance();
        newFragment.show(getFragmentManager(), "filter dialog");
        //newFragment.getWindow().setLayout(600, 400);
    }

    void showActivityDialog(Activity activity) {
        DialogFragment fragment = ActivityDialogFragment.newInstance(activity);
        fragment.show(getFragmentManager(), "activity_info dialog");
    }

    public void doPositiveClick(double maxCost, double maxDistance, double maxTime, boolean indoor, boolean outdoor) {
        // Do stuff here.
        Log.d("FragmentAlertDialog", "Positive click!");
        Log.d("appliedFilters", "max cost: " + maxCost + " max distance: " + maxDistance + " max time: " + maxTime + " indoor: " + indoor + " outdoor: " + outdoor);
        filterData(maxCost, maxDistance, maxTime, indoor, outdoor);
        Log.d("newActivitiesBe4swap: ", String.valueOf(activities.size()));
        ((RVAdapter)recyclerView.getAdapter()).swap(activities);
    }

    public void doNegativeClick(List<Double> selectedItems) {
        // Do stuff here.
        Log.d("FragmentAlertDialog", "Negative click!");
    }

}
