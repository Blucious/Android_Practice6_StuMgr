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
      setTitle("学生详情");

      Intent intent = getIntent();

      int stuId = intent.getIntExtra(UIConstants.BK_STUDENT_ID, -1);
      if (stuId == -1) {
         finish();
         return;
      }

      student = StudentService.getById(stuId);

      if (student == null) {
         getToastHelper().showShort("无此学生");
         finish();
         return;
      }

      bd.setStudent(student);
      bd.personalInfoEditButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent intent = new Intent(StudentDetailActivity.this, UpdateInfoActivity.class)
               .putExtra(UIConstants.BK_STUDENT_ID, student.getId());
            startActivityForResult(intent, UIConstants.REQ_CODE_DEFAULT);

         }
      });
      bd.scoreInfoEditButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent intent = new Intent(StudentDetailActivity.this, UpdateGradesActivity.class)
               .putExtra(UIConstants.BK_STUDENT_ID, student.getId());
            startActivityForResult(intent, UIConstants.REQ_CODE_DEFAULT);

         }
      });
   }

   public void SyncToDetail(Student student) {
      bd.setStudent(student);

   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (data != null) {
         if (resultCode == UIConstants.REQ_CODE_DEFAULT) {
            Bundle bundle = data.getExtras();
            Student student = (Student) bundle.get("stu");
            SyncToDetail(student);
            Intent intent = new Intent();
            intent.putExtra(UIConstants.BK_IS_UPDATE_NEEDED, true);
            setResult(UIConstants.REQ_CODE_DEFAULT, intent);

         }
      }

      super.onActivityResult(requestCode, resultCode, data);
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
      String studentName = bd.getStudent().getName();
      if (studentName == null) {
         studentName = "";
      }

      new AlertDialog.Builder(this)
         .setTitle("操作确认")
         .setMessage("确认删除学生 '" + studentName + "' 吗")
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
