package com.mrlove.roombasic3;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.mrlove.roombasic3.databinding.CellCard2Binding;
import com.mrlove.roombasic3.databinding.CellNormal2Binding;
import com.mrlove.roombasic3.domain.Word;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

//RecyclerView内容管理器类 泛型参数指定为自己的<MyAdapter.MyViewHolder>
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private final static String TAG = "mrloveqin";
    private List<Word> allWords = new ArrayList<>();
    private boolean useCardView;
    private CellNormal2Binding normalBinding;
    private CellCard2Binding cardBinding;
    private MyViewModel myViewModel;

    //返回所有的数据
    void setAllWords(List<Word> allWords) {
        this.allWords = allWords;
    }

    //初始化布局标志位,和myViewModel
    public MyAdapter(boolean useCardView,MyViewModel myViewModel) {
        this.useCardView = useCardView;
        this.myViewModel = myViewModel;
    }

    //当适配器创建的时候调用
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //RecyclerView中调用Databinding，绑定布局，获取binding实例
        //根据标识位初始化获取不同的binding
        if (useCardView) {
            cardBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.cell_card2, parent, false);
            //把binding作为参数，返回一个自定义的MyViewHolder
            return new MyViewHolder(cardBinding);
        } else {
            normalBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.cell_normal2, parent, false);
            return new MyViewHolder(normalBinding);
        }

    }

    //当调用ViewHolder时响应
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        //获取当前位置的一行数据
        final Word word = allWords.get(position);
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
        //先初始化按钮的点击事件为空，再初始化释义以及按钮的状态；
        //如果不先置为空，RecyclerView有数据回收重复使用的功能，会导致在页面数据滚出屏幕外，
        //又滚回来时，造成点击事件还存在，从而自动再执行，这样就会让初始化释义以及按钮的状态时
        //与上次设置的状态相反，导致数据混乱。
        holder.aSwitch.setOnCheckedChangeListener(null);
        //第一次加载时，从数据库提取数据，设置初始状态
        if(word.isInvisible()){
            holder.textChinese.setVisibility(View.GONE);
            holder.aSwitch.setChecked(true);
        }else {
            holder.textChinese.setVisibility(View.VISIBLE);
            holder.aSwitch.setChecked(false);
        }
        //设置隐藏释义按钮的点击时间
        holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    holder.textChinese.setVisibility(View.GONE);
                    word.setInvisible(true);
                    myViewModel.updateWords(word);
                }
                else {
                    holder.textChinese.setVisibility(View.VISIBLE);
                    word.setInvisible(false);
                    myViewModel.updateWords(word);
                }
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
        private CellNormal2Binding normalBinding;
        private CellCard2Binding cardBinding;
        private TextView textNumber, textEnglish, textChinese;
        private Switch aSwitch;

        //两种参数不同的构造函数初始化不同的布局控件
        MyViewHolder(@NotNull CellNormal2Binding binding) {
            super(binding.getRoot());
            normalBinding = binding;
            textNumber = normalBinding.textNumber;
            textEnglish = normalBinding.textEnglish;
            textChinese = normalBinding.textChinese;
            aSwitch = normalBinding.switchbutton;
        }

        MyViewHolder(@NotNull CellCard2Binding binding) {
            super(binding.getRoot());
            cardBinding = binding;
            textNumber = cardBinding.textNumber;
            textEnglish = cardBinding.textEnglish;
            textChinese = cardBinding.textChinese;
            aSwitch = cardBinding.switchbutton;
        }
    }
}
