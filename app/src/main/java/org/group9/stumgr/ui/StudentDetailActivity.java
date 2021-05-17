package org.group9.stumgr.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import org.group9.stumgr.R;
import org.group9.stumgr.bean.Student;
import org.group9.stumgr.databinding.ActivityStudentDetailBinding;
import org.group9.stumgr.service.StudentService;
import org.group9.stumgr.ui.custom.NavigableAppCompatActivity;

import java.util.List;

public class StudentDetailActivity extends NavigableAppCompatActivity {

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

      Student stu = StudentService.getById(stuId);
      if (stu == null) {
         getToastHelper().showShort("无此学生");
         finish();
      }

      StudentInfoFragment fragment = (StudentInfoFragment) getSupportFragmentManager()
         .findFragmentById(R.id.stuInfoFragment);

      fragment.setStudent(stu);
   }

}
