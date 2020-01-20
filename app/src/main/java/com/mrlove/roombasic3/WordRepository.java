package com.mrlove.roombasic3;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.mrlove.roombasic3.dao.WordDao;
import com.mrlove.roombasic3.dao.WordDataBase;
import com.mrlove.roombasic3.domain.Word;

import java.util.List;

public class WordRepository {
    private LiveData<List<Word>> allWordsLive;
    private WordDao wordDao;
    public WordRepository(Context context) {
        //获取数据库实例（唯一）
        WordDataBase wordDataBase = WordDataBase.getWordDataBase(context.getApplicationContext());
        //获取数据库操作dao的实例
        wordDao = wordDataBase.getWordDao();
        //获取数据库的所有数据
        //LiveData格式的数据在获取时,系统自动会调用Async来处理
        allWordsLive = wordDao.getAllWords();
    }
    //为实现AsyncTask静态内部类提供访问的接口
    void insertWords(Word... words){
        new InsertAsyncTask(wordDao).execute(words);
    }

    void updateWords(Word... words){
        new UpdateAsyncTask(wordDao).execute(words);
    }

    void deleteWords(Word... words){
        new DeleteAsyncTask(wordDao).execute(words);
    }

    void clearWords(){
        new ClearAsyncTask(wordDao).execute();
    }


    LiveData<List<Word>> getAllWordslive() {
        return allWordsLive;
    }

    //把对数据库的操作封装到实现AsyncTask的类中，因为Room对数据库的操作是耗时操作，不允许在主线程中执行。
    static class InsertAsyncTask extends AsyncTask<Word,Void,Void>{
        private WordDao wordDao;
        InsertAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.insertWords(words);
            return null;
        }
    }

    static class ClearAsyncTask extends AsyncTask<Void,Void,Void>{
        private WordDao wordDao;
        ClearAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Void... Void) {
            wordDao.deleteAllWords();
            return null;
        }
    }

    static class UpdateAsyncTask extends AsyncTask<Word,Void,Void>{
        private WordDao wordDao;
        UpdateAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.updateWords(words);
            return null;
        }
    }

    static class DeleteAsyncTask extends AsyncTask<Word,Void,Void>{
        private WordDao wordDao;
        DeleteAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.deleteWords(words);
            return null;
        }
    }
}
