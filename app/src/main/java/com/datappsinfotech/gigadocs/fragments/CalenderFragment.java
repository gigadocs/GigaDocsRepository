package com.datappsinfotech.gigadocs.fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.datappsinfotech.gigadocs.PrescriptionActivity;
import com.datappsinfotech.gigadocs.R;
import com.datappsinfotech.gigadocs.adapters.RecyclerViewAdapter;
import com.datappsinfotech.gigadocs.database.AppointmentDataBaseHelper;
import com.datappsinfotech.gigadocs.database.PatientDataBaseHelper;
import com.datappsinfotech.gigadocs.utils.dtos.AppointmentDTO;
import com.datappsinfotech.gigadocs.utils.dtos.CalendarDTO;
import com.datappsinfotech.gigadocs.utils.dtos.PatientDTO;
import com.ramzcalender.RWeekCalendar;
import com.ramzcalender.listener.CalenderListener;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalenderFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    public static RWeekCalendar rCalendarFragment;
    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private Paint p = new Paint();
    AppointmentDTO appointmentDTO ;
    PatientDTO patientDTO ;
    public static CalendarDTO calendarDTO;

    public static ArrayList<CalendarDTO> array;
    TextView appointmentFor;
    public static String statusValue;

    AppointmentDataBaseHelper myADb;
    public static String date;
    public static String patientNAME;
    public static String patientAppointmentID;
    public static String patientMobile;
    public static String patientAddress;
    public static String PatientEmail;
    public static String slot;
    public static String apo_id;
//    public static String statusValue;

    public static String patientMobileFromDb;
    public static String patientSlot;
    public static ArrayList<String> a1 = new ArrayList<>();

    public CalenderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_calender, container, false);
        try {
            myADb = new AppointmentDataBaseHelper(getContext());

            recyclerView = (RecyclerView)view.findViewById(R.id.card_recycler_view);
            appointmentDTO = new AppointmentDTO();
            patientDTO = new PatientDTO();
            calendarDTO =new CalendarDTO();
            rCalendarFragment=new RWeekCalendar();
            final Bundle args = new Bundle();


       /*Should add this attribute if you adding  the NOW_BACKGROUND or DATE_SELECTOR_BACKGROUND Attribute*/
            args.putString(RWeekCalendar.PACKAGENAME,getActivity().getApplicationContext().getPackageName());

       /* IMPORTANT: Customization for the calender commenting or un commenting any of the attribute below will reflect change in calender*/

//---------------------------------------------------------------------------------------------------------------------//
            args.putInt(RWeekCalendar.CALENDER_BACKGROUND, ContextCompat.getColor(getActivity(), R.color.calendar_blue));//set background color to calender

            args.putString(RWeekCalendar.DATE_SELECTOR_BACKGROUND, String.valueOf(R.color.lightPink));//set background to the selected dates

            args.putInt(RWeekCalendar.WEEKCOUNT,100000000);//add N weeks from the current week (53 or 52 week is one year)

            args.putString(RWeekCalendar.NOW_BACKGROUND,"bg_now");//set background to nowView

            args.putInt(RWeekCalendar.CURRENT_DATE_BACKGROUND, ContextCompat.getColor(getActivity(), R.color.md_black_1000));//set color to the currentdate

            args.putInt(RWeekCalendar.PRIMARY_BACKGROUND, ContextCompat.getColor(getActivity(), R.color.md_white_1000));//Set color to the primary views (Month name and dates)

            args.putInt(RWeekCalendar.SECONDARY_BACKGROUND, ContextCompat.getColor(getActivity(), R.color.md_white_1000));//Set color to the secondary views (now view and week names)

//---------------------------------------------------------------------------------------------------------------------//

            rCalendarFragment.setArguments(args);

            // Attach to the activity
            FragmentTransaction t = getActivity().getSupportFragmentManager().beginTransaction();
            t.replace(R.id.container, rCalendarFragment);
            t.commit();

            CalenderListener listener=new CalenderListener() {
                @Override
                public void onSelectPicker() {

                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getActivity().getFragmentManager(), "datePicker");

                }

                @Override
                public void onSelectDate(LocalDateTime mSelectedDate) {
                    int month = mSelectedDate.getMonthOfYear();
                    int day = mSelectedDate.getDayOfMonth();
                    if(month <10 && day <10) {
                        try {
                            date = mSelectedDate.getYear() + "-" + 0 + mSelectedDate.getMonthOfYear() + "-" + 0 + mSelectedDate.getDayOfMonth();
                            appointmentFor.setText("Appointment For: " + date);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                    else if (month <10){
                        try {
                            date = mSelectedDate.getYear() + "-" + 0 + mSelectedDate.getMonthOfYear() + "-" +mSelectedDate.getDayOfMonth();
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                    else if (day <10){
                        try {
                            date = mSelectedDate.getYear() + "-" + mSelectedDate.getMonthOfYear() + "-" + 0 +mSelectedDate.getDayOfMonth();
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        try {
                            date = mSelectedDate.getYear() + "-" + mSelectedDate.getMonthOfYear() + "-" + mSelectedDate.getDayOfMonth();
                            appointmentFor.setText("Appointment For: " + date);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        callDb(date);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            };

            //setting the listener

            rCalendarFragment.setCalenderListener(listener);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        //This is the call back from picker used in the sample you can use custom or any other picker

        //IMPORTANT: get the year,month and date from picker you using and call setDateWeek method
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.set(dayOfMonth,monthOfYear,year);
//            calendar.setTime(yourDate);

            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
           //Sets the selected date from Picker
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }



    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        int year;
        int day;
        int month;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker

            try {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
            } catch (Throwable e) {
                e.printStackTrace();
            }

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // Do something with the date chosen by the user
            Log.i("Tag10", dayOfMonth+"-"+monthOfYear+"-"+ year);
        }

    }

    @Override
    public void onResume() {
        try {
            super.onResume();
            if (adapter!=null){
                adapter.notifyDataSetChanged();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Log.i("position", String.valueOf(position));

                if (direction == ItemTouchHelper.LEFT) {
                    try {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
//                        String date1 = calendarDTO.getMobile();
                        callIntent.setData(Uri.parse("tel:" + patientMobileFromDb));
                        startActivity(callIntent);
                        Toast.makeText(getContext(), "Dialing Patient", Toast.LENGTH_SHORT).show();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                } else {
                    calendarDTO = array.get(position);
                    slot= calendarDTO.getSetCalendar();
                    AppointmentDataBaseHelper myAdb = new AppointmentDataBaseHelper(getContext());
//                    myAdb.updateData(a1.get(position),"true");
                    myAdb.updateData(slot,"true");
                    calendarDTO.setPrescribed(true);
                    calendarDTO.setPosition(position);
                        startActivity(new Intent(getActivity(), PrescriptionActivity.class));
                        Toast.makeText(getContext(), "Write prescription", Toast.LENGTH_SHORT).show();
                         patientNAME = calendarDTO.getSetPatientName();
                         patientMobile=patientMobileFromDb;
                    Cursor c1 = myAdb.getApoId(patientMobile, slot);
                    if (c1 != null) {
                        if (c1.moveToFirst()) {
                            do {
                                apo_id = c1.getString(c1.getColumnIndex(AppointmentDataBaseHelper.ACOL6));
                                Log.i("apo_id from calandr", apo_id);
                            } while (c1.moveToNext());
                        }
                    }
                    PatientDataBaseHelper myDb = new PatientDataBaseHelper(getContext());
                    Cursor c = null;
                    try {
                        c = myDb.viewSelectedDataForCalendar(patientNAME, patientMobile);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (c != null) {
                            if (c.moveToFirst()) {
                                do {
                                    patientAddress = c.getString(c.getColumnIndex(PatientDataBaseHelper.PCOL4));
                                    Log.i("addres fetch", patientAddress);
                                    PatientEmail = c.getString(c.getColumnIndex(PatientDataBaseHelper.PCOL5));
                                    Log.i("email fetch", PatientEmail);
                                } while (c.moveToNext());
                            }
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                    Bitmap icon;
                    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                        View itemView = viewHolder.itemView;
                        float height = (float) itemView.getBottom() - (float) itemView.getTop();
                        float width = height / 3;

                        if (dX > 0) {
                            try {
                                p.setColor(Color.parseColor("#388E3C"));
                                RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                                c.drawRect(background, p);
//
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                p.setColor(Color.parseColor("#D32F2F"));
                                RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                                c.drawRect(background, p);
//
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            };
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
            itemTouchHelper.attachToRecyclerView(recyclerView);
    }



    public void callDb(String date){
        try {
            Cursor c=myADb.getCalanderData(date);
            Log.d("Test5", c.toString());
            int i = 0;
            array = new ArrayList<>();
            CalendarDTO calendarDTO;
            while(c.moveToNext()){
                calendarDTO = new CalendarDTO();
                calendarDTO.setSetPatientName(c.getString(c.getColumnIndex(AppointmentDataBaseHelper.ACOL3)));
                calendarDTO.setSetCalendar(c.getString(c.getColumnIndex(AppointmentDataBaseHelper.ACOL5)));
                calendarDTO.setMobile(c.getString(c.getColumnIndex(AppointmentDataBaseHelper.ACOL2)));
                patientMobileFromDb = c.getString(c.getColumnIndex(AppointmentDataBaseHelper.ACOL2));
               patientSlot = c.getString(c.getColumnIndex(AppointmentDataBaseHelper.ACOL5));
                a1.add(patientSlot);
    //            Log.i("patientSlot", a1.toString());
    //            calendarDTO.setPrescribed(true);
    //            calendarDTO.setSaved(true);
            Cursor c1 = myADb.getStatus(date, patientSlot);

            if (c1 != null) {
                if (c1.moveToFirst()) {
                    do {
                        statusValue = c1.getString(c1.getColumnIndex(AppointmentDataBaseHelper.ACOL7));
    //                        Log.i("StatusValue", statusValue);
                    } while (c1.moveToNext());
                }
            }
            if(statusValue.equals("true")){
                calendarDTO.setSaved(true);
            }
            else{
                calendarDTO.setSaved(false);
            }
                array.add(calendarDTO);
                i++;
            }
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            adapter = new RecyclerViewAdapter(getContext(),array);
            recyclerView.setAdapter(adapter);
            initSwipe();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
