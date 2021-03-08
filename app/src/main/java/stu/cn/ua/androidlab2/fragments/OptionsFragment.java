package stu.cn.ua.androidlab2.fragments;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Calendar;

import stu.cn.ua.androidlab2.R;
import stu.cn.ua.androidlab2.model.Player;

public class OptionsFragment extends BaseFragment {
    private static final String ARG_PLAYER = "PLAYER";
    private static final String KEY_BIRTHDAY = "BIRTHDAY";
    private static final String KEY_DATE = "DATE";
    private static final String KEY_SHOW_DIALOG = "SHOW_DIALOG";
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private TextView bithdayTextView;
    private RadioGroup genderRadioGroup;
    private String gender;
    private Calendar date = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;
    private Boolean isDialogShow;

    public static OptionsFragment newInstance(
            @Nullable Player player) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_PLAYER, player);
        OptionsFragment fragment = new OptionsFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(
                R.layout.fragment_options,
                container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firstNameEditText = view
                .findViewById(R.id.firstNameEditText);
        lastNameEditText = view
                .findViewById(R.id.lastNameEditText);
        bithdayTextView = view.
                findViewById(R.id.dateTextView);
        genderRadioGroup = view.
                findViewById(R.id.genderRadioGroup);
        if (savedInstanceState != null) {
            String birthday = savedInstanceState
                    .getString(KEY_BIRTHDAY);
            isDialogShow = savedInstanceState.getBoolean(KEY_SHOW_DIALOG);
            Bundle bundle = savedInstanceState.getBundle(KEY_DATE);
            if(bundle != null) {
                bithdayTextView.setText(birthday);
                datePickerDialog = new DatePickerDialog(getContext(), d,
                        date.get(Calendar.YEAR),
                        date.get(Calendar.MONTH),
                        date.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.onRestoreInstanceState(bundle);
                if(isDialogShow)
                    datePickerDialog.show();
            }
        }
        setupButtons(view);
            Player player = getPlayerArg();
            if (player != null) {
                firstNameEditText.setText(player.getFirstName());
                lastNameEditText.setText(player.getLastName());
                bithdayTextView.setText(player.getBirthday());
                RadioButton maleRadioButton = view.findViewById(R.id.radio_male);
                RadioButton femaleRadioButton = view.findViewById(R.id.radio_female);
                if(player.getGender().equals("Male")){
                    maleRadioButton.setChecked(true);
                } else {
                    femaleRadioButton.setChecked(true);
                }
            }
    }
    private void setupButtons(View view) {
        view.findViewById(R.id.cancelButton)
                .setOnClickListener(v -> {
                    getAppContract().cancel();
                });
        view.findViewById(R.id.doneButton)
                .setOnClickListener(v -> {
                    switch (genderRadioGroup.getCheckedRadioButtonId()){
                        case R.id.radio_male:
                            gender = "Male";
                            break;
                        case R.id.radio_female:
                            gender = "Female";
                            break;
                    }

                    Player player = new Player(
                            firstNameEditText.getText().toString(),
                            lastNameEditText.getText().toString(),
                            bithdayTextView.getText().toString(),
                            gender
                    );
                    if (!player.isValid()) {
                        Toast.makeText(
                                getContext(),
                                R.string.empty_fields_error,
                                Toast.LENGTH_SHORT
                        ).show();
                        return;
                    }
                    getAppContract().publish(player);
                    getAppContract().cancel();
                });

        view.findViewById(R.id.selectDateButton).setOnClickListener(v -> {
            datePickerDialog = new DatePickerDialog(getContext(), d,
                    date.get(Calendar.YEAR),
                    date.get(Calendar.MONTH),
                    date.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
            isDialogShow = true;
        });
    }

    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            date.set(Calendar.YEAR, year);
            date.set(Calendar.MONTH, monthOfYear);
            date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };

    private void setInitialDateTime() {
        isDialogShow = false;
        bithdayTextView.setText(DateUtils.formatDateTime(getContext(),
                date.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_BIRTHDAY, bithdayTextView.getText().toString());
        outState.putBoolean(KEY_SHOW_DIALOG, isDialogShow);
        if (datePickerDialog != null) {
            outState.putBundle(KEY_DATE, datePickerDialog.onSaveInstanceState());
        }
    }
    private Player getPlayerArg() {
        return getArguments().getParcelable(ARG_PLAYER);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}