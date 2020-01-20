package com.mrlove.roombasic3;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.mrlove.roombasic3.databinding.CellCardBinding;
import com.mrlove.roombasic3.databinding.CellNormalBinding;
import com.mrlove.roombasic3.domain.Word;

import java.util.ArrayList;
import java.util.List;

//RecyclerView内容管理器类 泛型参数指定为自己的<MyAdapter.MyViewHolder>
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<Word> allWords = new ArrayList<>();
    private boolean useCardView;
    private CellNormalBinding normalBinding;
    private CellCardBinding cardBinding;

    //返回所有的数据
    void setAllWords(List<Word> allWords) {
        this.allWords = allWords;
    }

    //初始化布局标志位
    public MyAdapter(boolean useCardView) {
        this.useCardView = useCardView;
    }

    //当适配器创建的时候调用
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //RecyclerView中调用Databinding，绑定布局，获取binding实例
        //根据标识位初始化获取不同的binding
        if (useCardView) {
            cardBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.cell_card, parent, false);
            //把binding作为参数，返回一个自定义的MyViewHolder
            return new MyViewHolder(cardBinding);
        } else {
            normalBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.cell_normal, parent, false);
            return new MyViewHolder(normalBinding);
        }
    }

    //当调用ViewHolder时响应
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        //获取当前位置的一行数据
        Word word = allWords.get(position);
        //设置数据
        holder.textNumber.setText(String.valueOf(position + 1)); //让页面显示从1开始，而不用显示word.getId数据库中实际的位置
        holder.textEnglish.setText(word.getWord());
        holder.textChinese.setText(word.getChineseMeaning());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://m.youdao.com/dict?le=eng&q=" + holder.textEnglish.getText());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        //返回一个数据的容量
        return allWords.size();
    }

    //把onCreateViewHolder方法中的binding存起来，可以下次再用。这样做的好处就是不必每次都到布局文件中去拿到你的View，提高了效率。
    static class MyViewHolder extends RecyclerView.ViewHolder {
        private CellNormalBinding normalBinding;
        private CellCardBinding cardBinding;
        private TextView textNumber, textEnglish, textChinese;
        //两种参数不同的构造函数初始化不同的布局控件
        MyViewHolder(CellNormalBinding binding) {
            super(binding.getRoot());
            normalBinding = binding;
            textNumber = normalBinding.textNumber;
            textEnglish = normalBinding.textEnglish;
            textChinese = normalBinding.textChinese;
        }

        MyViewHolder(CellCardBinding binding) {
            super(binding.getRoot());
            cardBinding = binding;
            textNumber = cardBinding.textNumber;
            textEnglish = cardBinding.textEnglish;
            textChinese = cardBinding.textChinese;
        }
    }
}
