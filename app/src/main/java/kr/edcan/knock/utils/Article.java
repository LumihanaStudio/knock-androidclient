package kr.edcan.knock.utils;

import com.google.gson.annotations.SerializedName;

/**
 * Copyright by 2015 Sunrin Internet High School EDCAN
 * Created by Junseok on 2015-11-15.
 */
public class Article {
    @SerializedName("prg_name")
    public String prg_name;
    public Article(String prg_name){
        this.prg_name = prg_name;
    }
}
