package com.mrlove.roombasic3.dao;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.mrlove.roombasic3.domain.Word;

//配置你要操作的entry类，可以配置一个或者多个，version表名这个是哪个版本，如果升级需要修改的就是这里
@Database(entities = {Word.class}, version = 3) //exportSchema 默认为true，存储展示数据库的结构信息
public abstract class WordDataBase extends RoomDatabase {
    //singleton
    private static WordDataBase wordDataBase;  //单例模式，保证获取的数据库实例是唯一的

    public static synchronized WordDataBase getWordDataBase(Context context) {
        if (wordDataBase == null) {
            wordDataBase = Room.databaseBuilder(context.getApplicationContext(), WordDataBase.class, "word_database")
                    //下面注释表示允许主线程进行数据库操作，但是不推荐这样做。
                    //他可能造成主线程lock以及anr
                    //所以我们的操作都是在新线程完成的
                    // .allowMainThreadQueries()
                    //.fallbackToDestructiveMigration() 不保留数据，迁移数据
                    //保留数据迁移
                    .addMigrations(MIGRATION_2_3)
                    .build();
        }
        return wordDataBase;
    }
    //RoomDatabase提供直接访问底层数据库实现，我们通过定义抽象方法返回具体Dao
    //然后进行数据库增删该查的实现。
    public abstract WordDao getWordDao();
    //通过此方法，执行database底层sqlite语句进行数据的迁移
    static final Migration MIGRATION_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE word ADD COLUMN version TEXT");
        }
    };
    //sqlite没有字段的删除，所以只能通过数据的父子到新表的方式实现字段的删除
    static final Migration MIGRATION_2_3 = new Migration(2,3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            //首先创建一个新表word_temp，然后把要保留的字段数据填充到新表中，删除旧表，重命名新表名，完成想要数据的迁移
            database.execSQL("CREATE TABLE word_temp (id INTEGER PRIMARY KEY NOT NULL ,word TEXT,chineseMeaning TEXT)");
            database.execSQL("INSERT INTO word_temp(id,word,chineseMeaning)" +
                    "SELECT id,word,chineseMeaning FROM word");
            database.execSQL("DROP TABLE word");
            database.execSQL("ALTER TABLE word_temp RENAME TO word");
        }
    };
}
