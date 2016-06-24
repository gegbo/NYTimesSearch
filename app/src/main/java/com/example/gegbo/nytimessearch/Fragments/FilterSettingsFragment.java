package com.example.gegbo.nytimessearch.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.gegbo.nytimessearch.Models.Filter;
import com.example.gegbo.nytimessearch.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by gegbo on 6/23/16.
 */
public class FilterSettingsFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private EditText editText;
    private Button btnSave;
    private CheckBox cbArts;
    private CheckBox cbFashion;
    private CheckBox cbSports;
    private Spinner spinSort;

    Filter filter;
    public FilterSettingsFragment() {

    }

    public interface FragmentSettingsListener {
        void onSetFilter(Filter f);
    }

    public static FilterSettingsFragment newInstance() {
        FilterSettingsFragment frag = new FilterSettingsFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_settings, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle("Filter Settings");

        Bundle bundle = this.getArguments(); //need to work on updating filter after already set (from activity)
        if (bundle != null) {
            filter = bundle.getParcelable("filter");
        }
        else
        {
            filter = new Filter();
        }

        editText = (EditText) view.findViewById(R.id.etBeginDate);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
        editText.requestFocus();

        btnSave = (Button)view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentSettingsListener listener = (FragmentSettingsListener) getActivity();
                filter.setSort(spinSort.getSelectedItem().toString());
                listener.onSetFilter(filter);
                dismiss();
            }
        });

        cbArts = (CheckBox) view.findViewById(R.id.cbArts);
        cbArts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckboxClicked(cbArts);
            }
        });

        cbFashion = (CheckBox) view.findViewById(R.id.cbFashion);
        cbFashion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckboxClicked(cbFashion);
            }
        });

        cbSports = (CheckBox) view.findViewById(R.id.cbSports);
        cbSports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckboxClicked(cbSports);
            }
        });

        spinSort = (Spinner) view.findViewById(R.id.spinSort);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.sort_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinSort.setAdapter(adapter);

    }

    // attach to an onclick handler to show the date picker
    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setTargetFragment(this,300);
        newFragment.show(getFragmentManager(), "datePicker");

    }

    // handle the date selected
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        editText.setText((monthOfYear+1)+"/"+dayOfMonth+"/"+year);

        // Get the beginDate here from the calendar
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        filter.setBeginDate(format.format(c.getTime()));
    }

    public void onCheckboxClicked(CheckBox cb) {
        // Is the view now checked?
        boolean checked = cb.isChecked();

        // Check which checkbox was clicked
        switch(cb.getId()) {
            case R.id.cbArts:
                if (checked) {
                    filter.getNewsDesk().put("Arts",true);
                }
                else
                    filter.getNewsDesk().put("Arts",false);
                break;
            case R.id.cbFashion:
                if (checked) {
                    filter.getNewsDesk().put("Fashion",true);
                }
                else
                    filter.getNewsDesk().put("Fashion",false);
                break;
            case R.id.cbSports:
                if (checked) {
                    filter.getNewsDesk().put("Sports",true);
                }
                else
                    filter.getNewsDesk().put("Sports",false);
                break;
        }
    }

}
