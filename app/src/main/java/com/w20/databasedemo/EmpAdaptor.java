package com.w20.databasedemo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class EmpAdaptor extends ArrayAdapter {

    Context contextO;
    List<Employee> employees;
    int layoutRes;
    //SQLiteDatabase mDatabase;
    DatabaseHelper mDatabase;

    public EmpAdaptor(@NonNull Context context, int resource, List<Employee> employees, DatabaseHelper mDatabase) {
        super(context, resource, employees);
        this.contextO = context;
        this.employees = employees;
        this.layoutRes = resource;
        this.mDatabase = mDatabase;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        LayoutInflater inflater = LayoutInflater.from(contextO);
        View v = inflater.inflate(layoutRes, null);

        TextView tvName = v.findViewById(R.id.name_ID);
        TextView tvDept = v.findViewById(R.id.department_ID);
        TextView tvJoinDate = v.findViewById(R.id.joiningDate_ID);
        TextView tvSalary = v.findViewById(R.id.salary_ID);

        final Employee e = employees.get(position);
        tvName.setText(e.getName());
        tvDept.setText(e.getDept());
        tvJoinDate.setText(e.getJoiningdate());
        tvSalary.setText(String.valueOf(e.getSalary()));



        v.findViewById(R.id.btnEditEmp_ID).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEMP(e);
            }
        });



        v.findViewById(R.id.btnDelEmp_ID).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                delEMP(e);
            }
        });



        return v;
    }



    private void delEMP(final Employee e){

        AlertDialog.Builder builder = new AlertDialog.Builder(contextO);
        builder.setTitle("Are you sure you want to delete ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               // String sql = "DELETE FROM employees WHERE id = ?";
               // mDatabase.execSQL(sql, new Integer[]{e.getId()});
                mDatabase.deleteEmpoyees(e.getId());
                loadEmployees();
            }
        });



        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.create().show();
    }

    private void loadEmployees() {



//        String sql = "SELECT * FROM employees";
//        Cursor cursor = mDatabase.rawQuery(sql, null);

        Cursor cursor = mDatabase.getAllEmployees();
        employees.clear();

        if (cursor.moveToFirst()) {


            do {
                employees.add(new Employee(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getDouble(4)
                ));
            } while (cursor.moveToNext());
            cursor.close();
        }

            // show items in a listView
            // we use a custom adapter to show employees

            notifyDataSetChanged();


    }


    private void updateEMP(final Employee e){

        Log.i("CHECK", "updateEMP: ");

        AlertDialog.Builder editAlert = new AlertDialog.Builder(contextO);


        View v = LayoutInflater.from(contextO).inflate(R.layout.emp_update, null);

        editAlert.setView(v);


        final AlertDialog dd = editAlert.create();

        dd.show();

        final EditText editTextName = v.findViewById(R.id.editTextName);
        final EditText editTextSalary = v.findViewById(R.id.editTextSalary);
        final Spinner spinnerDept = v.findViewById(R.id.spinnerDepartment);


        editTextName.setText(e.getName());
        editTextSalary.setText(String.valueOf(e.getSalary()));



        v.findViewById(R.id.SAVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                String name = editTextName.getText().toString().trim();
                String salary = editTextSalary.getText().toString().trim();
                String dept = spinnerDept.getSelectedItem().toString();

                // using the Calendar object to get the current time
//                Calendar calendar = Calendar.getInstance();
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
//                String joiningDate = sdf.format(calendar.getTime());

                if (name.isEmpty()) {
                    editTextName.setError("name field is mandatory");
                    editTextName.requestFocus();
                    return;
                }

                if (salary.isEmpty()) {
                    editTextSalary.setError("salary field cannot be empty");
                    editTextSalary.requestFocus();
                    return;
                }

                //String sql = "UPDATE employees" +
                      //  " SET name = ?, department = ?, salary = ?" +
                     //   "WHERE id = ?";
                //mDatabase.execSQL(sql, new String[]{name, dept, salary, String.valueOf(e.id)});
                if(mDatabase.updateEmployee(e.getId(), name, dept,Double.parseDouble(salary))){
                    Toast.makeText(contextO, "Employee updated", Toast.LENGTH_SHORT).show();
                }

                loadEmployees();
                dd.dismiss();

            }



        });






    }
}
