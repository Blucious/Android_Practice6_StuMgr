package org.group9.stumgr.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import org.group9.stumgr.R;
import org.group9.stumgr.bean.Student;
import org.group9.stumgr.databinding.ActivityStudentAddingBinding;
import org.group9.stumgr.ui.custom.NavigableAppCompatActivity;

public class StudentAddingActivity extends NavigableAppCompatActivity {

   private ActivityStudentAddingBinding bd;

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      bd = DataBindingUtil.setContentView(
         this, R.layout.activity_student_adding);

      initView();
   }

   private void initView() {
      setTitle("添加学生");

      bd.setStudent(new Student().setDefault());
   }

   /* ---------------- 菜单相关 开始 ---------------- */
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.activity_student_adding, menu);
      return super.onCreateOptionsMenu(menu);
   }

   @Override
   public boolean onOptionsItemSelected(@NonNull MenuItem item) {
      int id = item.getItemId();

      if (id == R.id.studentAddingDoneMenuItem) {
         onStudentAddingDoneOptionSelected(item);

      } else {
         return super.onOptionsItemSelected(item);
      }
      return true;
   }

   private void onStudentAddingDoneOptionSelected(MenuItem item) {


   }

   /* ---------------- 菜单相关 结束 ---------------- */
}
