package net.boredboard.boredboard;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Colton on 3/21/2017.
 */

public class FiltersDialogFragment extends DialogFragment{

    DialogFragment _this = this;

    List<Double> selectedItems = new ArrayList<>();

    public static FiltersDialogFragment newInstance() {
        FiltersDialogFragment frag = new FiltersDialogFragment();
        Bundle args = new Bundle();
        //args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }

    private int progressToDistance(int progress){
        int distanceRadius;
        switch(progress){
            case 0:
                distanceRadius = 1;
                break;
            case 1:
                distanceRadius = 5;
                break;
            case 2:
                distanceRadius = 10;
                break;
            case 3:
                distanceRadius = 20;
                break;
            case 4:
                distanceRadius = 50;
                break;
            case 5:
                distanceRadius = 100;
                break;
            case 6:
                distanceRadius = 150;
                break;
            case 7:
                distanceRadius = 200;
                break;
            default:
                distanceRadius = 20;
        }
        return distanceRadius;
    }

    private void initializeDistanceSeekBar(final View view){
        final SeekBar distanceSB = (SeekBar) view.findViewById(R.id.max_distance_seekbar);
        distanceSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int distanceRadius;
                distanceRadius = progressToDistance(progress);
                TextView distanceLabel = (TextView) view.findViewById(R.id.max_distance_label);
                distanceLabel.setText("Max Distance: " + distanceRadius + " miles");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void initializeCostSeekBar(final View view){
        SeekBar maxCostSlider = (SeekBar) view.findViewById(R.id.max_cost_seekbar);
        maxCostSlider.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        TextView costLabel = (TextView) view.findViewById(R.id.max_cost_label);
                        costLabel.setText("Max Cost: $" + progress);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}
                }

        );
    }

    private void btnGroupPersistentPress(LinearLayout buttons){
        for(int i = 0; i < buttons.getChildCount(); i++){
            final Button b = (Button)buttons.getChildAt(i);
            b.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    // show interest in events resulting from ACTION_DOWN
                    if (event.getAction() == MotionEvent.ACTION_DOWN) return true;

                    // don't handle event unless its ACTION_UP so "doSomething()" only runs once.
                    if (event.getAction() != MotionEvent.ACTION_UP) return false;

                    boolean alreadyPressed = b.isPressed();
                    LinearLayout ll = (LinearLayout)b.getParent();
                    for(int i = 0; i < ll.getChildCount(); i++){
                        Button b2 = (Button)ll.getChildAt(i);
                        b2.setPressed(false);
                    }
                    if(alreadyPressed){
                        b.setPressed(false);
                    }
                    else{
                        b.setPressed(true);
                    }
                    return true;
                }
            });
        }
    }

    private void initializeTimeButtons(final View view){
        LinearLayout buttons = (LinearLayout)view.findViewById(R.id.time_buttons);
        btnGroupPersistentPress(buttons);
    }

    private void initializePartySizeButtons(final View view){
        LinearLayout buttons = (LinearLayout)view.findViewById(R.id.party_size_buttons);
        btnGroupPersistentPress(buttons);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.filters, null);

        initializeCostSeekBar(view);
        initializeDistanceSeekBar(view);
        initializeTimeButtons(view);
        initializePartySizeButtons(view);

        builder.setView(view)
                .setTitle(R.string.filters_title)
                .setPositiveButton(R.string.apply, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SeekBar maxCostSeekBar = (SeekBar) view.findViewById(R.id.max_cost_seekbar);
                        //EditText maxCostET = (EditText) view.findViewById(R.id.max_cost_entry);
                        //NumberPicker maxTimeNP = (NumberPicker) view.findViewById(R.id.);
                        CheckBox indoor = (CheckBox) view.findViewById(R.id.indoor_check);
                        CheckBox outdoor  = (CheckBox) view.findViewById(R.id.outdoor_check);
                        double maxCost, maxDistance, maxTime;
                        try {
                            maxCost = maxCostSeekBar.getProgress();
                            //maxCost = Double.parseDouble(maxCostET.getText().toString());
                        }catch(NumberFormatException e){
                            maxCost = Double.MAX_VALUE;
                        }
                        try {
                            maxDistance = progressToDistance(((SeekBar)view.findViewById(R.id.max_distance_seekbar)).getProgress());
                        }catch(NumberFormatException e){
                            maxDistance = 25;
                        }
                        maxTime = 0;
//                        try {
//                            //TODO fix this
//                            //maxTime = maxTimeNP.getValue();
//                        }catch(NumberFormatException e){
//                            maxTime = Double.MAX_VALUE;
//                        }
                        ((HomeScreen)getActivity()).doPositiveClick(maxCost, maxDistance, maxTime,
                                indoor.isChecked(), outdoor.isChecked());

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("Testing", "testingNegative");
                        ((HomeScreen)getActivity()).doNegativeClick(selectedItems);
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void maxTime(View v){
        //TODO do something
        Log.d("filters", "max time click");
    }

    public void partySize(View v){
        //TODO do something
        Log.d("filters", "party size click");
    }
}
