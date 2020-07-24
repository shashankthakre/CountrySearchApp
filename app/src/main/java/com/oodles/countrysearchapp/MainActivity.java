package com.oodles.countrysearchapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    private RecyclerView cd_recyclerView;
    private EditText country_id_et;
    private TextView search_tv;
    private Call<Countries> callbackCall = null;
    private Call<List<Countries>> callbackCall2 = null;
    private CountryListItem adapter;
    LinearLayoutManager layoutManager;
    DividerItemDecoration dividerItemDecoration;
    ArrayList<Countries> countries = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cd_recyclerView = findViewById(R.id.cd_recyclerView);
        country_id_et = findViewById(R.id.country_id_et);
        search_tv = findViewById(R.id.search_tv);


        layoutManager = new LinearLayoutManager(MainActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL);

        cd_recyclerView.setHasFixedSize(true);
        cd_recyclerView.setLayoutManager(layoutManager);

        adapter = new CountryListItem(MainActivity.this, countries);
        cd_recyclerView.setAdapter(adapter);

        search_tv.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String countryIso = country_id_et.getText().toString();

                        if (countryIso.length() == 0)
                        {
                            countryIso = "all";

                            // call for all countries data....
                            ApiInterface apiInterface = RestAdapter.createAPI();

                            callbackCall2 = apiInterface.getAllCountryDetail(countryIso);
                            callbackCall2.enqueue(new Callback<List<Countries>>() {
                                @Override
                                public void onResponse(Call<List<Countries>> call, Response<List<Countries>> response) {

                                    countries.clear();
                                    countries = (ArrayList<Countries>) response.body();

                                    adapter = new CountryListItem(MainActivity.this, countries);
                                    cd_recyclerView.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onFailure(Call<List<Countries>> call, Throwable t) {

                                }

                            });
                        }
                        else
                        {
                            countryIso = "alpha/" + countryIso;

                            //call for signle country data....
                            ApiInterface apiInterface = RestAdapter.createAPI();

                            callbackCall = apiInterface.getCountryDetail(countryIso);
                            callbackCall.enqueue(new Callback<Countries>() {
                                @Override
                                public void onResponse(Call<Countries> call, Response<Countries> response) {
                                    Countries resp = response.body();
                                    if (resp != null && response.code() == 200) {

                                        countries.clear();
                                        countries.add(resp);


                                        cd_recyclerView.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                adapter.notifyDataSetChanged();
                                            }
                                        });
                                    } else {

                                    }
                                }

                                @Override
                                public void onFailure(Call<Countries> call, Throwable t) {
                                    //   if (!call.isCanceled()) onFailRequest(page_no);
                                }

                            });

                        }

                    }
                }
        );
    }
}