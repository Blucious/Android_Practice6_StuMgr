package org.group9.stumgr.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import org.group9.stumgr.R;
import org.group9.stumgr.bean.Student;
import org.group9.stumgr.service.StudentService;

public class Update_gradesActivity extends AppCompatActivity {
    public Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_grades);
        initview();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_student_updateinfo, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.updatedone) {
            int n = StudentService.update(student);
            Log.d("TAG", "onOptionsItemSelected: n = " + n);
            Toast.makeText(Update_gradesActivity.this, "修改成功", Toast.LENGTH_LONG).show();

            Intent intent = new Intent().putExtra("stu",student);
            setResult(UIConstants.REQ_CODE_DEFAULT,intent);
            finish();
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void initview() {
        Intent intent = getIntent();
        int stuId = intent.getIntExtra(UIConstants.BK_STUDENT_ID, -1);
        this.student = StudentService.getById(stuId);
        Updategrades updategrades = (Updategrades) getSupportFragmentManager().findFragmentById(R.id.updateGrade);
        updategrades.setStudent(student);


    }
}