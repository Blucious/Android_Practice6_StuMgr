package org.group9.stumgr.ui;

import androidx.annotation.NonNull;

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
import org.group9.stumgr.ui.custom.NavigableAppCompatActivity;

public class UpdateInfoActivity extends NavigableAppCompatActivity {

   public Student student;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_update_info);

      initView();

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
         boolean succeeded = StudentService.update(student);

         if (succeeded) {
            getToastHelper().showLong("修改成功");
         } else {
            getToastHelper().showLong("修改失败");
         }

         Intent intent = new Intent().putExtra("stu", student);
         setResult(UIConstants.REQ_CODE_DEFAULT, intent);
         finish();

      } else {
         return super.onOptionsItemSelected(item);
      }
      return true;
   }

   public void initView() {
      Intent intent = getIntent();

      int stuId = intent.getIntExtra(UIConstants.BK_STUDENT_ID, -1);
      if (stuId == -1) {
         finish();
      }

      student = StudentService.getById(stuId);
      if (student == null) {
         getToastHelper().showShort("无此学生");
         finish();
      }

      UpdateProfileFragment updateProfile = (UpdateProfileFragment) getSupportFragmentManager()
         .findFragmentById(R.id.updateProfileFr);
      updateProfile.setStudent(student);

   }
}