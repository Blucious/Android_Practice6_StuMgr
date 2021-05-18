package org.group9.stumgr.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import org.group9.stumgr.R;
import org.group9.stumgr.bean.Student;
import org.group9.stumgr.databinding.ActivityStudentDetailBinding;
import org.group9.stumgr.service.StudentService;
import org.group9.stumgr.ui.custom.NavigableAppCompatActivity;

public class StudentDetailActivity extends NavigableAppCompatActivity {
   private Student student;
   private ActivityStudentDetailBinding bd;

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      bd = DataBindingUtil.setContentView(
         this, R.layout.activity_student_detail);

      initView();
   }

   private void initView() {
      Intent intent = getIntent();

      int stuId = intent.getIntExtra(UIConstants.BK_STUDENT_ID, -1);
      if (stuId == -1) {
         finish();
      }


      this.student = StudentService.getById(stuId);

      if (student == null) {
         getToastHelper().showShort("无此学生");
         finish();
      }

      bd.setStudent(student);
      Button updateProfile = bd.personalInfoEditButton;
      updateProfile.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent intent = new Intent(StudentDetailActivity.this, UpdateInfoActivity.class)
                    .putExtra(UIConstants.BK_STUDENT_ID, student.getId());
            startActivityForResult(intent, UIConstants.REQ_CODE_DEFAULT);

         }
      });
   }



   /* ---------------- 菜单相关 开始 ---------------- */
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.activity_student_detail, menu);
      return super.onCreateOptionsMenu(menu);
   }

   @Override
   public boolean onOptionsItemSelected(@NonNull MenuItem item) {
      int id = item.getItemId();

      if (id == R.id.deleteMenuItem) {
         onDeleteOptionSelected(item);

      } else {
         return super.onOptionsItemSelected(item);
      }
      return true;
   }

   private void onDeleteOptionSelected(MenuItem item) {

      new AlertDialog.Builder(this)
         .setTitle("确认")
         .setTitle("确认删除学生 '" + bd.getStudent().getName() + "' 吗")
         .setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               deleteCurrentStudent();
            }
         })
         .setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
         }).show();

   }
   /* ---------------- 菜单相关 结束 ---------------- */

   private void deleteCurrentStudent() {

      boolean succeeded = StudentService.deleteById(bd.getStudent().getId());

      if (succeeded) {
         getToastHelper().showShort("删除成功");
      } else {
         getToastHelper().showLong("删除失败");
      }

      // 结束当前Activity
      Intent intent = new Intent()
         .putExtra(UIConstants.BK_IS_UPDATE_NEEDED, succeeded);
      setResult(RESULT_OK, intent);
      finish();
   }
}
