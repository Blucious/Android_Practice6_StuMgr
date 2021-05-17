package org.group9.stumgr.context;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.group9.stumgr.bean.Student;
import org.group9.stumgr.dao.AppDatabase;
import org.group9.stumgr.dao.StudentDao;
import org.group9.stumgr.service.StudentService;

import java.util.concurrent.Executors;

public class G9StuMgrApplication extends Application {
   private static G9StuMgrApplication instance;

   public static G9StuMgrApplication getInstance() {
      return instance;
   }

   private static AppDatabase db;
   private static StudentDao studentDao;

   @Override
   public void onCreate() {
      super.onCreate();

      instance = this;

      initDb();
   }

   private void initDb() {

      RoomDatabase.Callback rdc = new RoomDatabase.Callback() {
         @Override
         public void onCreate(@NonNull SupportSQLiteDatabase db) {
            Executors.newFixedThreadPool(1).execute(() -> {
               studentDao.insertAll(
                  StudentService.getRandomStudentsAsList(30)
                     .toArray(new Student[0])
               );
            });
         }
      };

      db = Room.databaseBuilder(instance,
         AppDatabase.class, "stu.db")
         .addCallback(rdc)
         .allowMainThreadQueries()
         .build();

      studentDao = db.studentDao();
   }

   public static AppDatabase getDb() {
      return db;
   }

   public static StudentDao getStudentDao() {
      return studentDao;
   }


}
