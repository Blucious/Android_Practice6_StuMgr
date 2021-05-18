package org.group9.stumgr.dao;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import org.group9.stumgr.bean.Student;

@Database(entities = {Student.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

   public abstract StudentDao studentDao();


}
