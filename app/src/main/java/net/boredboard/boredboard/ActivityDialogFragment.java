package net.boredboard.boredboard;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by Colton on 3/27/2017.
 */

public class ActivityDialogFragment extends DialogFragment {

    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    final long ONE_MEGABYTE = 1024 * 1024;
    private static DecimalFormat df2;

    public static ActivityDialogFragment newInstance(Activity activity) {
        df2 = new DecimalFormat(".##");
        df2.setRoundingMode(RoundingMode.CEILING);
        ActivityDialogFragment frag = new ActivityDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", activity.getTitle());
        args.putString("description", activity.getDescription());
        args.putString("photoID", activity.getPhotoID());
        args.putDouble("cost", activity.getCost());
        args.putDouble("time", activity.getTime());
        args.putString("location", activity.getLocation());
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.activity_info, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view);

        TextView cost = (TextView)view.findViewById(R.id.activity_cost);
        if(getArguments().getDouble("cost") == 0){
            cost.setText("FREE");
            cost.setTextColor(ContextCompat.getColor(this.getActivity(), R.color.accent));
        }
        else{
            cost.setText("$ " + String.format("%.2f", getArguments().getDouble("cost")));
        }
        TextView description = (TextView)view.findViewById(R.id.activity_description);
        description.setText(getArguments().getString("description"));
        TextView time = (TextView)view.findViewById(R.id.activity_time);
        String timeStr;
        if(getArguments().getDouble("time") == .5){
            timeStr = "1/2";
        }
        else{
            timeStr = String.format("%.0f", getArguments().getDouble("time"));
        }
        time.setText(timeStr + " hr");
        TextView title = (TextView)view.findViewById(R.id.activity_title);
        title.setText(getArguments().getString("title"));
        ImageView photo = (ImageView)view.findViewById(R.id.activity_photo);
        downloadImage(photo, getArguments().getString("photoID"));
        TextView location = (TextView) view.findViewById(R.id.activity_location);
        location.setText(getArguments().getString("location"));

        // Create the AlertDialog object and return it
        return builder.create();
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

    public void displayDirections(View view){
        Log.d("displayDirections", "directions button clicked");
    }
}
