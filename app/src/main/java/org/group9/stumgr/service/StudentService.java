package org.group9.stumgr.service;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;

import org.group9.stumgr.bean.Student;
import org.group9.stumgr.bean.StudentCriteria;
import org.group9.stumgr.context.G9StuMgrApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class StudentService {

   private static final String TAG = StudentService.class.getSimpleName();

   public static List<Student> getRandomStudentsAsList(int n) {
      if (n < 0) {
         n = 0;
      }
      return StudentGenerator.genStudentList(n);
   }

   public static List<Student> getAll() {
      return G9StuMgrApplication.getStudentDao()
         .getAll();
   }

   public static Student getById(int id) {
      return G9StuMgrApplication.getStudentDao()
         .getById(id);
   }

   /**
    * @return 是插入除成功
    */
   public static boolean insert(Student student) {
      return -1 != G9StuMgrApplication.getStudentDao()
         .insert(student);
   }

   /**
    * @return 是否修改成功
    */
   public static boolean update(Student student) {
      return 1 == G9StuMgrApplication.getStudentDao()
         .update(student);
   }

   public static void insertAll(List<Student> students) {
      G9StuMgrApplication.getStudentDao()
         .insertAll(students.toArray(new Student[0]));
   }

   /**
    * @return 是否删除成功
    */
   public static boolean deleteById(int id) {
      return 1 == G9StuMgrApplication.getStudentDao()
         .deleteById(id);
   }

   /**
    * @return 影响行数
    */
   public static int deleteAll() {
      return G9StuMgrApplication.getStudentDao()
         .deleteAll();
   }

   /**
    * 导入操作
    */
   public static List<Student> importStuInfoFromJson(File file) {
      List<Student> students = null;
      Log.d(TAG, "importStuInfoByJson: " + file.getPath());


      StringBuilder jsoncontent = new StringBuilder();
      try {
         BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
         String s = null;
         while ((s = br.readLine()) != null) {
            jsoncontent.append(System.lineSeparator()).append(s);
         }
         br.close();

         Log.d(TAG, "importStuInfoByJson: " + jsoncontent);

         students = JSONArray.parseArray(jsoncontent.toString(), Student.class);
         // 清除id
         for (Student student : students) {
            student.setId(null);
         }

         Log.d(TAG, "importStuInfoByJson: " + students);

      } catch (Exception e) {
         e.printStackTrace();
      }

      return students;
   }

   /**
    * 导出操作
    */
   public static String exportStuInfoByJson(Context context, List<Student> students) throws FileNotFoundException {
      Date date = new Date();
      DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmSS");

      File file = new File(Environment.getExternalStorageDirectory().getPath() + File.separator, "info" + dateFormat.format(date) + ".json");
      FileOutputStream fileOutputStream = new FileOutputStream(file);
      //创建json集合
      JSONArray jsonArray = new JSONArray();
      for (Student s : students) {
         jsonArray.add(s);
      }

      try {
         OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
         outputStreamWriter.write(jsonArray.toJSONString());
         outputStreamWriter.flush();
         outputStreamWriter.close();
         fileOutputStream.close();
         Log.d("save", "exportStuInfoByJson: success");

      } catch (Exception e) {
         e.printStackTrace();
      }

      Log.d("TAG", "exportStuInfoByJson: " + jsonArray.toJSONString());

      return file.getPath();
   }


   /**
    * @param students 学生列表
    * @param criteria 过滤条件
    * @return 根据 {@code criteria} 过滤后的学生的列表
    */
   public static List<Student> filter(List<Student> students,
                                      StudentCriteria criteria) {
      // 长度不确定的情况LinkedList未必会比ArrayList更快，有稍微测试过。
      // 可能是因为频繁申请内存，或内存碎片的原因。
      List<Student> filtered = new ArrayList<>();

      for (Student stu : students) {
         if (criteria.test(stu)) {
            filtered.add(stu);
         }
      }

      return filtered;
   }

   /**
    * 排序类型定义
    */
   public enum SortingType {
      DEFAULT("默认"),
      NAME_ASC("名字升序"),
      NAME_DESC("名字降序"),
      TOTAL_SCORE_ASC("总成绩升序"),
      TOTAL_SCORE_DESC("总成绩降序"),
      END_OF_TERM_SCORE_ASC("期末成绩升序"),
      END_OF_TERM_SCORE_DESC("期末成绩降序"),
      NORMAL_SCORE_ASC("平时成绩升序"),
      NORMAL_SCORE_DESC("平时成绩降序");

      public final String name;

      SortingType(String name) {
         this.name = name;
      }
   }

   public final static String[] SORTING_METHOD_NAMES;

   static {
      SORTING_METHOD_NAMES = Arrays.stream(SortingType.values())
         .map(e -> e.name)
         .toArray(String[]::new);
   }

   public static void sort(List<Student> students,
                           int sortingMethodIndex) {
      sort(students, SortingType.values()[sortingMethodIndex]);
   }

   /**
    * 根据 {@code sortingMethods} 对学生列表进行原地排序
    */
   public static void sort(List<Student> students,
                           SortingType sortingType) {
      switch (sortingType) {
         default:
         case DEFAULT:
            break;
         case NAME_ASC:
            students.sort((a, b) -> a.getName().compareTo(b.getName()));
            break;
         case NAME_DESC:
            students.sort((a, b) -> b.getName().compareTo(a.getName()));
            break;
         case TOTAL_SCORE_ASC:
            students.sort((a, b) -> a.getTotalScore().compareTo(b.getTotalScore()));
            break;
         case TOTAL_SCORE_DESC:
            students.sort((a, b) -> b.getTotalScore().compareTo(a.getTotalScore()));
            break;
         case END_OF_TERM_SCORE_ASC:
            students.sort((a, b) -> a.getEtScore().compareTo(b.getEtScore()));
            break;
         case END_OF_TERM_SCORE_DESC:
            students.sort((a, b) -> b.getEtScore().compareTo(a.getEtScore()));
            break;
         case NORMAL_SCORE_ASC:
            students.sort((a, b) -> a.getNmScore().compareTo(b.getNmScore()));
            break;
         case NORMAL_SCORE_DESC:
            students.sort((a, b) -> b.getNmScore().compareTo(a.getNmScore()));
            break;
      }
   }

}
