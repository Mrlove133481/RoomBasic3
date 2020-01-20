package com.mrlove.roombasic3;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mrlove.roombasic3.domain.Word;

import java.util.List;

public class MyViewModel extends AndroidViewModel {
    private WordRepository wordRepository;

    public MyViewModel(@NonNull Application application) {
        super(application);
        wordRepository = new WordRepository(application);  //初始化得到wordRepository工具类
    }

    LiveData<List<Word>> getAllWordsLive() {
        return wordRepository.getAllWordslive();
    }

    void insertWords(Word... words) {
        wordRepository.insertWords(words);
    }

    void updateWords(Word... words) {
        wordRepository.updateWords(words);
    }

    void deleteWords(Word... words) {
        wordRepository.deleteWords(words);
    }

    void clearWords() {
        wordRepository.clearWords();
    }


}
