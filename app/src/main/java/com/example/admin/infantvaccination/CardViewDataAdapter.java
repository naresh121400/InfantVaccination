package com.example.admin.infantvaccination;

/**
 * Created by Admin on 23-Feb-18.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by venkataprasad on 02-01-2015.
 */
public class CardViewDataAdapter extends RecyclerView.Adapter<CardViewDataAdapter.ViewHolder> {

    private static ArrayList<AddChild> dataSet;
    static byte[] image;

    public CardViewDataAdapter(ArrayList<AddChild> addChildren) {

        dataSet = addChildren;
    }


    @Override
    public CardViewDataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
// create a new view
        View itemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.card_view, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CardViewDataAdapter.ViewHolder viewHolder, int i) {

        AddChild ac = dataSet.get(i);

        viewHolder.tvChildName.setText(ac.getName());
        viewHolder.tvChildDob.setText("DOB:"+ac.getDob());
        viewHolder.tvChildId.setText(ac.getId());
        image=ac.getImage();
        if(image!=null){

            DbBitmapUtility du=new DbBitmapUtility();
            Bitmap bm=du.getImage(ac.getImage());
            viewHolder.imageView.setImageBitmap(bm);

        }
        else{
            viewHolder.imageView.setBackgroundResource(R.drawable.childlogo);
        }


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvChildName;
        public TextView tvChildDob;
        public TextView tvChildId;

        public ImageView imageView;

        public ViewHolder(final View itemLayoutView) {
            super(itemLayoutView);

            tvChildName = (TextView) itemLayoutView
                    .findViewById(R.id.tvChildName);
           tvChildDob = (TextView) itemLayoutView
                    .findViewById(R.id.tvChildDob);
           tvChildId = itemLayoutView.findViewById(R.id.tvChildId);

            imageView=itemLayoutView.findViewById(R.id.imgChild);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent intent = new Intent(view.getContext(),UploadImageActivity.class);
                    intent.putExtra("cid",tvChildId.getText().toString());
                    view.getContext().startActivity(intent);
                    ((MainActivity)view.getContext()).finish();


                }
            });

            itemLayoutView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(v.getContext(), "Child Id is:" + tvChildId.getText().toString(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(v.getContext(),ChildVaccineActivity.class);
                    intent.putExtra("cid",tvChildId.getText().toString());
                    v.getContext().startActivity(intent);


                }
            });

            itemLayoutView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View view) {

                    CharSequence colors[] = new CharSequence[] {"Edit child details", "Delete selected child"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Options");
                    builder.setItems(colors, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(which==0){
                                Intent intent = new Intent(view.getContext(),UpdateChildActivity.class);
                                intent.putExtra("cid",tvChildId.getText().toString());
                                intent.putExtra("mode","1");
                                view.getContext().startActivity(intent);
                                ((MainActivity)view.getContext()).finish();
                            }
                            else if(which==1){
                                Intent intent = new Intent(view.getContext(),UpdateChildActivity.class);
                                intent.putExtra("cid",tvChildId.getText().toString());
                                intent.putExtra("mode","2");
                                view.getContext().startActivity(intent);
                                ((MainActivity)view.getContext()).finish();

                            }
                        }
                    });
                    builder.show();

                    return false;
                }
            });


        }

    }
}