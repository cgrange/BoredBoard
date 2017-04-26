package net.boredboard.boredboard;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Colton on 3/21/2017.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ActivityViewHolder>{

    public static final String CVClick = "card view click";
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    final long ONE_MEGABYTE = 1024 * 1024;

    public class ActivityViewHolder extends RecyclerView.ViewHolder  {
        CardView cv;
        ImageView activityPhoto;
        TextView activityTitle;

        ActivityViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            activityPhoto = (ImageView)itemView.findViewById(R.id.activity_photo);
            activityTitle = (TextView) itemView.findViewById(R.id.activity_title);
        }
    }

    List<Activity> activities;
    HomeScreen parent;

    RVAdapter(List<Activity> activities, HomeScreen parent){
        Log.d("load_activity", "in rv adapter's constructor");
        Log.d("load_activity", "activities size: " + activities.size());
        this.activities = activities;
        this.parent = parent;
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    @Override
    public ActivityViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        ActivityViewHolder avh = new ActivityViewHolder(v);
        return avh;
    }

    @Override
    public void onBindViewHolder(ActivityViewHolder activityViewHolder, final int i) {
        Log.d("load_activity", "in recycler view's bind view holder");
        Log.d("load_activity", "activities: " + activities.get(i).getTitle());
        downloadImage(activityViewHolder.activityPhoto, activities.get(i).getPhotoID());
        activityViewHolder.activityTitle.setText(activities.get(i).getTitle());
        //activityViewHolder.activityPhoto.setImageResource(activities.get(i).getPhotoID());
        activityViewHolder.cv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d(CVClick, "clicked on " + activities.get(i).getTitle());
                parent.showActivityDialog(activities.get(i));
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    private void downloadImage(final ImageView imageView, String photoId){
        Log.d("load_activity", "trying to download image");
        storageRef.child(photoId).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Log.d("load_activity", "image download was supposedly successful");
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(bitmap);
            }
        });
    }

    public void swap(List<Activity> newActivities){
        //activities.clear();
        //activities = newActivities;
        //activities.addAll(newActivities);
        Log.d("newActivities", String.valueOf(newActivities.size()));
        notifyDataSetChanged();
    }

}