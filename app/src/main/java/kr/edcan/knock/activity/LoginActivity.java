package kr.edcan.knock.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import kr.edcan.knock.R;
import kr.edcan.knock.utils.NetworkInterface;
import kr.edcan.knock.utils.User;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    NetworkInterface service;
    EditText user_id, user_pw;
    RelativeLayout login, register, cancel;

    SharedPreferences sh;
    SharedPreferences.Editor editor;

    MaterialDialog load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        load = new MaterialDialog.Builder(LoginActivity.this)
                .content("잠시만 기다려주세요")
                .progress(true, 0)
                .cancelable(false)
                .show();
        setRest();
        setDefault();
        sh = getSharedPreferences("Knock", 0);
        editor = sh.edit();
        checkUser(sh);
    }

    private void checkUser(SharedPreferences sh) {
        final String id = sh.getString("id", "").trim();
        if (!id.equals("")) {
//            service.loginValid(id, new Callback<String>() {
//                @Override
//                public void success(String s, Response response) {
            load.dismiss();
            Toast.makeText(getApplicationContext(), id + " 유저로 로그인합니다!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        else load.dismiss();
//                }
//
//                @Override
//                public void failure(RetrofitError error) {
//                    Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//                    load.dismiss();
//                }
//            });
    }

    private void setDefault() {
        login = (RelativeLayout) findViewById(R.id.login_layout);
        register = (RelativeLayout) findViewById(R.id.register_layout);
        cancel = (RelativeLayout) findViewById(R.id.cancel_layout);
        user_id = (EditText) findViewById(R.id.user_login);
        user_pw = (EditText) findViewById(R.id.user_password);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_layout:
                reqLogin();
                break;
            case R.id.register_layout:
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                break;
            case R.id.cancel_layout:
                finish();
                break;
        }
    }

    private void reqLogin() {
        String id = user_id.getText().toString().trim();
        String pw = user_pw.getText().toString().trim();
        if (id.equals("")) user_id.setError("아이디를 입력해주세요");
        else if (pw.equals("")) user_pw.setError("비밀번호를 입력해주세요");
        else {
            service.userLogin(id, pw, new Callback<User>() {
                @Override
                public void success(User user, Response response) {
                    Toast.makeText(LoginActivity.this, "정상적으로 로그인되었습니다", Toast.LENGTH_SHORT).show();
                    editor.putString("id", user.user_id);
                    editor.putString("pw", user.user_pw);
                    editor.putString("name", user.user_name);
                    editor.putString("api", user.user_api);
                    editor.putBoolean("isLogin", user.user_connection);
                    editor.commit();
                    Toast.makeText(getApplicationContext(), user.user_id + user.user_pw, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }

                @Override
                public void failure(RetrofitError error) {
                    if (error.getResponse() == null) {
                        Toast.makeText(LoginActivity.this, "서버로부터의 응답이 없습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (error.getResponse().getStatus() == 400)
                            Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호가 잘못되었습니다!", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void setRest() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://grooshbene.milkgun.kr:80")
                .build();
        service = restAdapter.create(NetworkInterface.class);
    }
}
