package com.example.admin.infantvaccination;

/**
 * Created by Admin on 23-Feb-18.
 */

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by venkataprasad on 02-01-2015.
 */
public class CardChildDataAdapter extends RecyclerView.Adapter<CardChildDataAdapter.ViewHolder> {


    private static ArrayList<ListModelClass> listVaccineData;

    public CardChildDataAdapter(ArrayList<ListModelClass> lmc) {

        listVaccineData = lmc;
    }


    @Override
    public CardChildDataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
// create a new view
        View itemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.card_view_vaccine, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        String todayDate=mDay+"/"+mMonth+"/"+mYear;

        ListModelClass lmc = listVaccineData.get(position);

        holder.tvVaccineName.setText(lmc.getLvname());
        holder.tvSchedule.setText(lmc.getLschedule());
        holder.tvGiven.setText(lmc.getLgiven());
        String status=lmc.getLstatus().toString();




        if(status.equals("GIVEN")){
            holder.imgStatus.setBackgroundResource(R.drawable.wright);
        }
        else{


            //----------------------------Checking dates for due vaccines to change color

            String dates[]=lmc.getLschedule().toString().split("/");
            int k=Integer.parseInt(dates[1]);
            k=k-1;
            String scheduleDate=dates[0]+"/"+k+"/"+dates[2];

            try {
                Date start = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                        .parse(todayDate);
                Date end = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                        .parse(scheduleDate);

                if(start.equals(end)){

                    holder.imgStatus.setBackgroundResource(R.drawable.online);

                }
                else if(start.before(end)){

                    holder.imgStatus.setBackgroundResource(R.drawable.today_due);

                }
                else if(start.after(end) && !status.equals("GIVEN")){
                    holder.imgStatus.setBackgroundResource(R.drawable.wrong);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }



            //----------------------------End---------------------------------------------



        }
       // holder.tvStatus.setText(lmc.getLstatus());
        holder.tvVid.setText(lmc.getLid());

    }

    @Override
    public int getItemCount() {
        return listVaccineData.size();
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvVaccineName;
        public TextView tvSchedule;
        public TextView tvGiven;
        public TextView tvStatus;
        public TextView tvVid;
        public ImageView imgStatus;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            tvVaccineName = (TextView) itemLayoutView
                    .findViewById(R.id.tvVaccineName);
            tvSchedule = (TextView) itemLayoutView
                    .findViewById(R.id.tvSchedule);
            tvGiven = itemLayoutView.findViewById(R.id.tvGiven);
           // tvStatus = itemLayoutView.findViewById(R.id.tvStatus);
            tvVid = itemLayoutView.findViewById(R.id.tvVid);
            imgStatus=itemLayoutView.findViewById(R.id.imgStatus);
            itemLayoutView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   // Intent it = new Intent
                  //  Toast.makeText(v.getContext(), tvVid.getText().toString(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(v.getContext(),UpdateChildVaccineAct.class);
                    intent.putExtra("vid",tvVid.getText().toString());
                    intent.putExtra("vname",tvVaccineName.getText().toString());
                    intent.putExtra("vschedule",tvSchedule.getText().toString());
                    v.getContext().startActivity(intent);
                    ((ChildVaccineActivity)v.getContext()).finish();

                }
            });


        }

    }
}