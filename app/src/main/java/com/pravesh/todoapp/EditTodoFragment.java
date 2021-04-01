package com.pravesh.todoapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.pravesh.todoapp.model.ETodo;
import com.pravesh.todoapp.viewModel.TodoViewModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditTodoFragment extends Fragment {

    View rootView;
    EditText txtTitle, txtDescription, txtDate;
    RadioGroup rgPriority;
    Button btnSave, btnCancel;
    CheckBox chComplete;

    int todoId;

    public static final int HIGH_PRIORITY = 1;
    public static final int MEDIUM_PRIORITY = 2;
    public static final int LOW_PRIORITY = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_edit_todo, container, false);

        txtTitle = rootView.findViewById(R.id.edit_fragment_txt_name);
        txtDescription = rootView.findViewById(R.id.edit_fragment_txt_description);
        txtDate = rootView.findViewById(R.id.edit_fragment_txt_date);
        rgPriority = rootView.findViewById(R.id.edit_fragment_rg_priority);
        chComplete = rootView.findViewById(R.id.edit_fragment_chk_complete);
        btnSave = rootView.findViewById(R.id.edit_fragment_btn_save);
        btnCancel = rootView.findViewById(R.id.edit_fragment_btn_cancel);

        loadUpdateData();

        txtDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    DisplayTestDate();
                return false;

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveTodo();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAlertCancel();
            }
        });
        return rootView;
    }

    void SaveTodo() {
        ETodo eTodo = new ETodo();
        Date todoDate = new Date();
        int checkedPriority = -1;
        int priority = 0;
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            todoDate = format.parse(txtDate.getText().toString());
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        checkedPriority = rgPriority.getCheckedRadioButtonId();

        switch (checkedPriority) {
            case R.id.edit_fragment_rb_high:
                priority = HIGH_PRIORITY;
                break;
            case R.id.edit_fragment_rb_medium:
                priority = MEDIUM_PRIORITY;
                break;
            case R.id.edit_fragment_rb_low:
                priority = LOW_PRIORITY;
                break;
        }

        eTodo.setTitle(txtTitle.getText().toString());
        eTodo.setDescription(txtDescription.getText().toString());
        eTodo.setTodoDate(todoDate);
        eTodo.setPriority(priority);
        eTodo.setCompleted(chComplete.isChecked());

        TodoViewModel viewModel = new ViewModelProvider(this).get(TodoViewModel.class);

        if (todoId != -1) {
            eTodo.setId(todoId);
            viewModel.update(eTodo);
        } else {
            viewModel.insert(eTodo);
        }


        Toast.makeText(getActivity(), "Todo Saved", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    void loadUpdateData() {
        todoId = getActivity().getIntent().getIntExtra("TodoId", -1);
        TodoViewModel viewModel = new ViewModelProvider(this).get(TodoViewModel.class);

        if (todoId != -1) {
            btnSave.setText("Update");
            ETodo todo = viewModel.getTodoById(todoId);
            txtTitle.setText(todo.getTitle());
            txtDescription.setText(todo.getDescription());
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            txtDate.setText(format.format(todo.getTodoDate()));
            switch (todo.getPriority()) {
                case 1:
                    rgPriority.check(R.id.edit_fragment_rb_high);
                    break;
                case 2:
                    rgPriority.check(R.id.edit_fragment_rb_medium);
                    break;
                case 3:
                    rgPriority.check(R.id.edit_fragment_rb_low);
                    break;
            }
            chComplete.setChecked(todo.isCompleted());
        }
    }

    void DisplayTestDate() {
        Calendar calendar = Calendar.getInstance();
        int cDay = calendar.get(Calendar.DAY_OF_MONTH);
        int cMonth = calendar.get(Calendar.MONTH);
        int cYear = calendar.get(Calendar.YEAR);
        DatePickerDialog pickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                txtDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
            }
        }, cYear, cMonth, cDay);
        pickerDialog.show();
    }

    void ShowAlertCancel() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setMessage(getString(R.string.alert_cancel))
                .setTitle(getString(R.string.app_name))
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        alertDialog.show();
    }

}