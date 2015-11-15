package kr.edcan.knock.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import kr.edcan.knock.R;
import kr.edcan.knock.utils.Article;
import kr.edcan.knock.utils.DataAdapter;
import kr.edcan.knock.utils.ListData;
import kr.edcan.knock.utils.NetworkInterface;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {
    NetworkInterface service;
    ListView listview;
    ArrayList<ListData> arrayList;
    DataAdapter adapter;
    String prg_name[];
    SharedPreferences sh;
    SharedPreferences.Editor editor;
    List<Article> list;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setDefault();
        setRest();
        setActionbar(getSupportActionBar());
        setData();
    }

    private void setRest() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://grooshbene.milkgun.kr:80")
                .build();
        service = restAdapter.create(NetworkInterface.class);
        sh = getSharedPreferences("Knock", 0);
        editor = sh.edit();
    }

    private void setData() {
        arrayList = new ArrayList<>();
        service.listArticle(new Callback<List<Article>>() {
            @Override
            public void success(List<Article> articles, Response response) {
                String array[] = articles.get(0).prg_name.split(",");
                if (array.length != 0) {
                    for (int i = 1; i < array.length; i++) {
                        Log.e("array", array[i]);
                        arrayList.add(new ListData(array[i]));
                    }
                    adapter = new DataAdapter(MainActivity.this, arrayList);
                    listview.setAdapter(adapter);
                } else textView.setText("제어 가능한 어플리케이션이 없습니다!");
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final TextView asdf = (TextView) view.findViewById(R.id.title);
                new MaterialDialog.Builder(MainActivity.this)
                        .title("어플리케이션 제어")
                        .content(asdf.getText().toString() + " 어플리케이션을 종료하시겠습니까?")
                        .positiveText("확인")
                        .negativeText("취소")
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
                                final String title = asdf.getText().toString().trim();
                                service.killArticle(title, new Callback<String>() {
                                    @Override
                                    public void success(String s, Response response) {
                                        Toast.makeText(getApplicationContext(), title + "\n정상적으로 처리되었습니다!", Toast.LENGTH_SHORT).show();
                                        setData();
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.reload:
                setData();
                Toast.makeText(MainActivity.this, "제어할 어플리케이션을 재로드합니다.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout:
                new MaterialDialog.Builder(MainActivity.this)
                        .title("로그아웃하시겠습니까?")
                        .content("확인을 누르시면 로그아웃합니다")
                        .positiveText("로그아웃")
                        .negativeText("취소")
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
                                Toast.makeText(getApplicationContext(), "로그아웃합니다", Toast.LENGTH_SHORT).show();
                                editor.remove("id");
                                editor.commit();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                finish();
                            }
                        })
                        .show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void addList(String s) {
    }

    private void setDefault() {
        textView = (TextView)findViewById(R.id.textView);
        listview = (ListView) findViewById(R.id.listView);
    }

    public void setActionbar(ActionBar actionbar) {
        actionbar.setTitle("제어");
    }

    public void onResume(){
        super.onResume();
        setData();
    }
}
