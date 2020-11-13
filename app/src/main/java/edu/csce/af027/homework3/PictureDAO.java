package edu.csce.af027.homework3;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PictureDAO {
    @Query("SELECT * FROM picture_database")
    List<Picture> getAllPictures();

    @Query("SELECT * FROM picture_database WHERE title= :title")
    Picture getPicture(String title);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Picture picture);

    @Query("DELETE FROM picture_database")
    void deleteAll();


}
