package kr.edcan.knock.activity;

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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText id, pw, name, card;
    NetworkInterface service;
    RelativeLayout register, cancel;
    String user_id;
    String user_pwd;
    String user_name;
    String card_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setDefault();
        setRest();
    }

    private void setDefault() {
        id = (EditText) findViewById(R.id.user_login);
        pw = (EditText) findViewById(R.id.user_password);
        name = (EditText) findViewById(R.id.user_name);
        card = (EditText) findViewById(R.id.user_name);
        register = (RelativeLayout) findViewById(R.id.register_layout);
        cancel = (RelativeLayout) findViewById(R.id.cancel_layout);
        register.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    private void setRest() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://grooshbene.milkgun.kr:80")
                .build();
        service = restAdapter.create(NetworkInterface.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_layout:
                reqRegister();
                break;
            case R.id.cancel_layout:
                finish();
                break;
        }
    }

    private void reqRegister() {
        user_id = id.getText().toString().trim();
        user_pwd = pw.getText().toString().trim();
        user_name = name.getText().toString().trim();
        card_name = card.getText().toString().trim();
        if (user_id.equals("")) id.setError("아이디를 입력해주세요!");
        else if (user_pwd.equals("")) pw.setError("비밀번호를 입력해주세요!");
        else if (user_name.equals("")) name.setError("이름을 입력해주세요!");
//        else if (card_name.equals("")) card.setError("카드번호를 입력해주세요!");
        else {
            new MaterialDialog.Builder(RegisterActivity.this)
                    .title("다시 한번 확인해주세요!")
                    .content("ID : " + user_id + "\n" + "이름 : " + user_name + "\n위 내용으로 가입하시겠습니까?")
                    .positiveText("확인")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            service.registerUser(user_id, user_pwd, user_name, card_name, new Callback<User>() {
                                @Override
                                public void success(User user, Response response) {
                                    Toast.makeText(RegisterActivity.this, "정상적으로 가입되었습니다!\n가입한 내용으로 로그인해주세요!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    if (error.getResponse().getStatus() == 409)
                                        Toast.makeText(RegisterActivity.this, "중복된 아이디입니다!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .show();

        }
    }
}
