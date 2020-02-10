package com.w20.databasedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class EmployeeActivity extends AppCompatActivity {

    private static final String TAG = "EmployeeActivity";
    //SQLiteDatabase mDatabase;
    DatabaseHelper mDatabase;

    List<Employee> employeeList;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        listView = findViewById(R.id.lvEmployees);
        employeeList = new ArrayList<>();

        //mDatabase = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);

        mDatabase = new DatabaseHelper(this);
        loadEmployees();
    }

    private void loadEmployees() {
//        String sql = "SELECT * FROM employees";
//        Cursor cursor = mDatabase.rawQuery(sql, null);


        Cursor cursor = mDatabase.getAllEmployees();

        if (cursor.moveToFirst()) {
            do {
                employeeList.add(new Employee(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getDouble(4)
                ));
            } while (cursor.moveToNext());
            cursor.close();

            // show items in a listView
            // we use a custom adapter to show employees


            EmpAdaptor adaptor = new EmpAdaptor(this, R.layout.empformat, employeeList, mDatabase);


            listView.setAdapter(adaptor);

        }
    }
}
