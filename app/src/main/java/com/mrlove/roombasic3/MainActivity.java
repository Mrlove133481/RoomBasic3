package com.mrlove.roombasic3;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mrlove.roombasic3.databinding.ActivityMainBinding;
import com.mrlove.roombasic3.domain.Word;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    MyViewModel myViewModel;
    //DatabindingBinding 由框架编译时生成，负责通知界面同步更新(命名方式：xml文件名 + Binding)；
    ActivityMainBinding binding;
    MyAdapter myAdaptercard, myAdapternormal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //DataBindingUtil 将布局文件与Activity关联,生成DatabindingBinding实例binding；
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        //获取ViewModel实例
        myViewModel = new ViewModelProvider(this, new SavedStateViewModelFactory(getApplication(), this)).get(MyViewModel.class);
        //为布局文件设置源数据
        binding.setData(myViewModel);
        myAdapternormal = new MyAdapter(false);
        myAdaptercard = new MyAdapter(true);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerview.setAdapter(myAdapternormal);

        binding.switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    binding.recyclerview.setAdapter(myAdaptercard);
                } else {
                    binding.recyclerview.setAdapter(myAdapternormal);
                }
            }
        });

        //为实现LiveData的数据设置观察者，以便当数据改变时通知UI更新数据
        myViewModel.getAllWordsLive().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                myAdaptercard.setAllWords(words);
                myAdaptercard.notifyDataSetChanged();
                myAdapternormal.setAllWords(words);
                myAdapternormal.notifyDataSetChanged();
            }
        });
        //添加
        binding.buttoninsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Word word1 = new Word("hello", "你好");
                Word word2 = new Word("world", "世界");
                myViewModel.insertWords(word1, word2);

            }
        });
        //删除所有
        binding.buttonclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myViewModel.clearWords();

            }
        });
        //更新
        binding.buttonupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Word word = new Word("haha", "哈哈");
                word.setId(40);
                myViewModel.updateWords(word);
            }
        });
        //删除某一项
        binding.buttondelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Word word = new Word("haha", "哈哈");
                word.setId(40);
                myViewModel.deleteWords(word);
            }
        });


    }
}
