package kr.edcan.knock.utils;

/**
 * Copyright by 2015 Sunrin Internet High School EDCAN
 * Created by Junseok on 2015-11-02.
 */
public class User {
    public String user_id, user_pw, user_name, user_api;
    public boolean user_connection;

    public User(String user_id, String user_pw, String user_name, String user_api) {
        this.user_id = user_id;
        this.user_pw = user_pw;
        this.user_name = user_name;
        this.user_api = user_api;
    }

}
