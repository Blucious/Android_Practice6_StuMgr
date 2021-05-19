package org.group9.stumgr.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.group9.stumgr.R;
import org.group9.stumgr.bean.Student;
import org.group9.stumgr.databinding.ActivityStudentManagerBinding;
import org.group9.stumgr.service.PermissionManager;
import org.group9.stumgr.service.StudentService;
import org.group9.stumgr.ui.custom.BaseAppCompatActivity;
import org.group9.stumgr.util.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StudentManagerActivity extends BaseAppCompatActivity {

   private static final String TAG = StudentManagerActivity.class.getSimpleName();


   private List<Student> students;

   private ActivityStudentManagerBinding bd;
   private RecyclerView studentsRecyclerView;
   private StudentListAdapter studentListAdapter;

   private final ExecutorService executorService =
      Executors.newFixedThreadPool(1);


   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      bd = DataBindingUtil.setContentView(
         this, R.layout.activity_student_manager);

      initData();

      initView();
   }

   private void initData() {
      asyncFetchStudentsAndSyncToAdapter();
   }

   private void initView() {

      // 搜索框相关
      bd.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
         @Override
         public boolean onQueryTextSubmit(String query) {
            return false;
         }

         @Override
         public boolean onQueryTextChange(String newText) {
            studentListAdapter.getStudentCriteria().setNameFragment(newText);
            studentListAdapter.notifyDataChanged();
            return true;
         }
      });

      // 学生列表相关
      {
         // DataBinding找不到这个id，原因不明。故手动findViewById
         studentsRecyclerView = bd.getRoot().findViewById(R.id.studentRecyclerView);

         // 设置布局管理器
         studentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
         // 添加分割线装饰
         DividerItemDecoration did = new DividerItemDecoration(
            studentsRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
         studentsRecyclerView.addItemDecoration(did);

         // 适配器
         studentListAdapter = new StudentListAdapter(this, students,
            new StudentListAdapter.ViewItemOnClickListener() {
               @Override
               public void onClick(Student student, int position) {
                  Intent intent = new Intent(StudentManagerActivity.this, StudentDetailActivity.class)
                     .putExtra(UIConstants.BK_STUDENT_ID, student.getId());
                  startActivityForResult(intent, UIConstants.REQ_CODE_DEFAULT);
               }
            },
            new StudentListAdapter.DataUpdatingFinishedListener() {
               @Override
               public void onFinished() {
                  // 将列表滚动到顶部，从头开始展示更新后的内容
                  studentsRecyclerView.scrollToPosition(0);
               }
            });
         studentsRecyclerView.setAdapter(studentListAdapter);

      }

   }

   /**
    * 设置学生列表，不会进行关联数据的更新
    */
   public void setStudentsAndSyncToAdapter(@Nullable List<Student> students) {
      if (students == null) {
         students = Collections.emptyList();
      }
      this.students = students;
      if (studentListAdapter != null) {
         studentListAdapter.setStudents(students);
         studentListAdapter.notifyDataChanged();
      }

      // 显示学生数量
      ActionBar actionBar = getSupportActionBar();
      if (actionBar == null) {
         getToastHelper().showLong("共 " + students.size() + " 个学生");
      } else {
         actionBar.setSubtitle("学生 " + students.size());
      }
   }

   /**
    * 异步提取学生列表，然后将其同步到适配器
    */
   public void asyncFetchStudentsAndSyncToAdapter() {

      executorService.execute(() -> {

         List<Student> students = StudentService.getAll();

         runOnUiThread(() -> {
            setStudentsAndSyncToAdapter(students);
         });

      });
   }

   /* ---------------- Activity返回值处理相关 开始 ---------------- */
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      Log.d(TAG, "requestCode = " + requestCode + ", resultCode = " + resultCode + ", data = " + data);

      if (data != null) {
         if (data.getBooleanExtra(UIConstants.BK_IS_UPDATE_NEEDED, false)) {
            asyncFetchStudentsAndSyncToAdapter();
         }
      }

      if (requestCode == UIConstants.REQ_CODE_GET_CONTENT) {
         // 获取文件选择器返回值 将json转化为java对象
         if (resultCode == Activity.RESULT_OK) {
            //是否选择，没选择就不会继续
            Uri data1 = data.getData();
            File file = new File(FileUtils.getFilePathByUri(StudentManagerActivity.this, data1));
            // File file = UriToFile.trans(StudentActivity.this,data1);
            List<Student> newStudents = StudentService.importStuInfoFromJson(file);
            // 导入后刷新列表

            executorService.execute(() -> {

               StudentService.insertAll(newStudents);
               List<Student> refetched = StudentService.getAll();

               runOnUiThread(() -> {
                  setStudentsAndSyncToAdapter(refetched);

                  Toast.makeText(StudentManagerActivity.this,
                     "成功导入" + newStudents.size() + "条数据", Toast.LENGTH_LONG).show();
               });


            });
         }
      } else {
         super.onActivityResult(requestCode, resultCode, data);
      }
   }

   /* ---------------- Activity返回值处理相关 结束 ---------------- */


   /* ---------------- 菜单相关 开始 ---------------- */
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.activity_student_manager, menu);
      return super.onCreateOptionsMenu(menu);
   }

   @Override
   public boolean onOptionsItemSelected(@NonNull MenuItem item) {
      int id = item.getItemId();

      if (id == R.id.sortingMenuItem) {
         onSortingOptionSelected(item);

      } else if (id == R.id.searchingMenuItem) {
         onSearchingOptionSelected(item);

      } else if (id == R.id.refreshStudentsMenuItem) {
         onRefreshStudentsOptionSelected(item);

      } else if (id == R.id.addStudentMenuItem) {
         onAddStudentOptionSelected(item);

      } else if (id == R.id.exportM) {
         onExportMOptionSelected();

      } else if (id == R.id.importM) {
         onImportMOptionSelected();

      } else if (id == R.id.genStuMenuItem) {
         onGenStuOptionSelected(item);

      } else if (id == R.id.deleteAllStuMenuItem) {
         onDeleteAllStuOptionSelected(item);

      } else {
         return super.onOptionsItemSelected(item);
      }

      return true;
   }

   private void onSortingOptionSelected(@NonNull MenuItem item) {

      final int prevSortingTypeIndex = studentListAdapter.getSortingTypeIndex();

      AlertDialog alertDialog = new AlertDialog.Builder(this)
         .setIcon(R.drawable.ic_baseline_sort_30_dark)
         .setTitle("排序方式")
         .setSingleChoiceItems(StudentService.SORTING_METHOD_NAMES, prevSortingTypeIndex,
            (dialog, which) -> studentListAdapter.setSortingTypeIndex(which))
         .setPositiveButton("关闭", (dialog, which) -> dialog.dismiss())
         .setOnDismissListener(dialog -> {
            if (prevSortingTypeIndex != studentListAdapter.getSortingTypeIndex()) {
               studentListAdapter.notifyDataChanged();
            }
         })
         .create();

      alertDialog.show();
   }

   private void onSearchingOptionSelected(@NonNull MenuItem item) {

      if (bd.searchViewWrapper.getVisibility() == View.GONE) {
         // 显示搜索视图时，调用onActionViewExpanded，以展开SearchView
         bd.searchView.onActionViewExpanded();

         bd.searchViewWrapper.setVisibility(View.VISIBLE);
         item.setIcon(R.drawable.ic_baseline_search_off_30);

      } else {
         // 隐藏搜索视图时，调用onActionViewCollapsed，以清空SearchView内的文字
         bd.searchView.onActionViewCollapsed();

         bd.searchViewWrapper.setVisibility(View.GONE);
         item.setIcon(R.drawable.ic_baseline_search_30);
      }
   }

   private void onRefreshStudentsOptionSelected(MenuItem item) {
      asyncFetchStudentsAndSyncToAdapter();
   }

   private void onAddStudentOptionSelected(@NonNull MenuItem item) {
      Intent intent = new Intent(this, StudentAddingActivity.class);
      startActivityForResult(intent, UIConstants.REQ_CODE_DEFAULT);
   }

   private void onExportMOptionSelected() {
      try {
         boolean PermissionResult = new PermissionManager().build().RequestPermission(StudentManagerActivity.this, this);

         String path = StudentService.exportStuInfoByJson(StudentManagerActivity.this, students);
         Toast.makeText(StudentManagerActivity.this, "成功导出至" + path, Toast.LENGTH_LONG).show();
         Log.d(TAG, "onOptionsItemSelected: " + path);


      } catch (FileNotFoundException e) {
         e.printStackTrace();
      }
   }

   private void onImportMOptionSelected() {
      boolean PermissionResult = new PermissionManager().build().RequestPermission(StudentManagerActivity.this, this);

      Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
      intent.setType("*/*");
      intent.addCategory(Intent.CATEGORY_OPENABLE);
      startActivityForResult(intent, UIConstants.REQ_CODE_GET_CONTENT);

      // RequestPermissionRImpl.RequestPermissionAndroidR(StudentActivity.this);

   }

   private void onGenStuOptionSelected(@NonNull MenuItem item) {
      List<Student> students = StudentService.getRandomStudentsAsList(30);
      StudentService.insertAll(students);

      asyncFetchStudentsAndSyncToAdapter();
   }

   private void onDeleteAllStuOptionSelected(MenuItem item) {
      StudentService.deleteAll();

      asyncFetchStudentsAndSyncToAdapter();
   }

   /* ---------------- 菜单相关 结束 ---------------- */

}
