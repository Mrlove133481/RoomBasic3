package com.mrlove.roombasic3.domain;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//entity声明定义，并且指定了映射数据表明
@Entity
public class Word {
    //设置主键自动增长
    @PrimaryKey(autoGenerate = true)
    private int id;
    //字段映射具体的数据表字段名，如果没有定义则用属性名，作为列名。
    @ColumnInfo(name = "word")
    private String word;
    private String chineseMeaning;
    private boolean invisible;
    //必须指定一个构造方法，room框架需要。并且只能指定一个
    //，如果有其他构造方法，则其他的构造方法必须添加@Ignore注解
    public Word(String word, String chineseMeaning) {
        this.word = word;
        this.chineseMeaning = chineseMeaning;
    }
    //Setter、Getter方法是需要添加的，为了支持room工作
    public boolean isInvisible() {
        return invisible;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getChineseMeaning() {
        return chineseMeaning;
    }

    public void setChineseMeaning(String chineseMeaning) {
        this.chineseMeaning = chineseMeaning;
    }
}
