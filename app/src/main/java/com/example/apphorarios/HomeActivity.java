package com.example.apphorarios;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.apphorarios.Controller.PagerController;
import com.example.apphorarios.RetroFit.IMyService;
import com.example.apphorarios.RetroFit.RetroFitClient;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class HomeActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    PagerController pagercontroller;
    Button btn_fichar;

    String id, token;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Iniciamos servicios
        Retrofit retroFitClient = RetroFitClient.getInstance();
        iMyService = retroFitClient.create(IMyService.class);

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        btn_fichar = findViewById(R.id.btn_home_fichar);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                token = null;
                id = null;
            }
            else {
                token = extras.getString("token");
                id = extras.getString("id");
            }
        }
        else {
            token = (String) savedInstanceState.getSerializable("token");
            id = (String) savedInstanceState.getSerializable("id");
        }

        getWeek();

        btn_fichar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FicharActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
    }

    private void getWeek() {

        compositeDisposable.add(iMyService.getWeek(token, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        try {
                            JSONObject jsonreponse = new JSONObject(response);
                            JSONArray data = jsonreponse.getJSONArray("datos");
                            ArrayList<JSONObject> weekDataArray = new ArrayList<JSONObject>();
                            for (int i = 0; i < data.length(); i++) {
                                weekDataArray.add(data.getJSONObject(i));
                            }

                            pagercontroller = new PagerController(getSupportFragmentManager(), tabLayout.getTabCount(), weekDataArray);
                            viewPager.setAdapter(pagercontroller);
                            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                @Override
                                public void onTabSelected(TabLayout.Tab tab) {
                                    viewPager.setCurrentItem(tab.getPosition());
                                    if ((tab.getPosition() >= 0) && (tab.getPosition() <= 6)) {
                                        pagercontroller.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onTabUnselected(TabLayout.Tab tab) {}

                                @Override
                                public void onTabReselected(TabLayout.Tab tab) {}
                            });

                            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }));
    }
}