package com.mrlove.roombasic3.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mrlove.roombasic3.domain.Word;

import java.util.List;

//注解配置sql语句
@Dao   //Database access object
public interface WordDao {
    @Insert
     void insertWords(Word... words);  //...表示可以传递多个同类型参数

    @Update
     void updateWords(Word... words);

    @Delete
    void  deleteWords(Word... words);

    @Query("DELETE FROM WORD")   //查询语句写的清空所有数据
    void deleteAllWords();
    //LiveData格式的数据在获取时,系统自动会调用Async来处理
    @Query("SELECT * FROM Word ORDER BY id DESC")
    LiveData<List<Word>> getAllWords();  //设置数据为可观察类型LiveData
}
