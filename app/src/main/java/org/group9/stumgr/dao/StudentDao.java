package org.group9.stumgr.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.group9.stumgr.bean.Student;

import java.util.List;

@Dao
public interface StudentDao {

   @Query("SELECT * FROM student")
   List<Student> getAll();

   @Query("SELECT * FROM student " +
      "WHERE id = :id")
   Student getById(int id);

   @Insert
   void insertAll(Student... students);

   /**
    * @return 影响的行数
    */
   @Update
   int update(Student student);

   /**
    * @return 影响的行数
    */
   @Query("DELETE FROM student " +
      "WHERE id = :id")
   int deleteById(int id);

   @Query("DELETE FROM student")
   int deleteAll();
}
