package org.group9.stumgr.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import org.apache.commons.lang3.StringUtils;
import org.group9.stumgr.R;
import org.group9.stumgr.bean.Student;
import org.group9.stumgr.databinding.ActivityStudentAddingBinding;
import org.group9.stumgr.service.StudentService;
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

      Student student = bd.getStudent();
      if (StringUtils.isEmpty(student.getName())) {
         getToastHelper().showShort("请输入学生姓名");
         return;
      }

      boolean succeeded = StudentService.insert(student);

      if (succeeded) {
         getToastHelper().showShort("添加成功");
      } else {
         getToastHelper().showLong("添加失败");
      }

      // 结束当前Activity
      Intent intent = new Intent()
         .putExtra(UIConstants.BK_IS_UPDATE_NEEDED, succeeded);
      setResult(RESULT_OK, intent);
      finish();
   }

   /* ---------------- 菜单相关 结束 ---------------- */
}
