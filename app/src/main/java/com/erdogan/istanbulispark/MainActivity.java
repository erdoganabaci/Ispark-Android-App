package com.erdogan.istanbulispark;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.erdogan.istanbulispark.api.APIInterface;
import com.erdogan.istanbulispark.api.ApiRequest;
import com.erdogan.istanbulispark.models.ParkDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    ArrayList<String> parkAdiList;
    ListView isparkListView;
    ArrayAdapter<String> adapter ;
    EditText filterParkEditText;
    ProgressBar isparkProgressBar;
    HashMap<String, String> parkForMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //listview identification
        isparkListView = findViewById(R.id.isparkListView);
        //filter identification
        filterParkEditText = findViewById(R.id.filterParkEditText);
        //progressbar
        isparkProgressBar = findViewById(R.id.isparkProgressBar);
        parkForMain= new HashMap<String, String>();
        parkAdiList = new ArrayList<>();
        loadPark();

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,parkAdiList);
        filterParkEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        isparkListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(),parkAdiList.get(position),Toast.LENGTH_SHORT).show();

                Object MyParkObject=(Object) parent.getAdapter().getItem(position);
                Toast.makeText(getApplicationContext(),MyParkObject.toString(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),DetailParkActivity.class);
                intent.putExtra("parkNameFromMain",MyParkObject.toString());
                intent.putExtra("parkIDFromMain",parkForMain.get(MyParkObject.toString()));
                //startActivity(intent);

            }
        });

    }


    public void loadPark() {
        parkAdiList.clear();
        APIInterface apiInterface = ApiRequest.getApiService();
        Observable<List<ParkDetail>> exampleCall = apiInterface.getPark();
        exampleCall.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ParkDetail>>()  {
                               @Override
                               public void onSubscribe(Disposable d) {

                               }

                               @Override
                               public void onNext(List<ParkDetail> park) {
                                   for (int i = 0; i < park.size(); i++) {
                                       String parkAdi = park.get(i).parkAdi;
                                       String parkID = park.get(i).parkID.toString();
                                       parkAdiList.add(park.get(i).parkAdi);
                                       //view.showRestaurants(localList);
                                       Log.d(TAG, "erdo" + parkAdi);
                                       //Log.d(TAG, "park adları " + parkAdiList);
                                       parkForMain.put(parkAdi,parkID);


                                   }
                                   String totalPark = String.valueOf(parkAdiList.size());
                                   Toast.makeText(getApplicationContext(),"Toplam " +totalPark+ " Park Bulundu",Toast.LENGTH_LONG).show();
                                   isparkListView.setAdapter(adapter);
                                   Log.d(TAG, "park adları " + parkAdiList.get(parkAdiList.size() - 1));

                               }

                               @Override
                               public void onError(Throwable e) {
                                   Log.d(TAG,"RXJAVA2 HTTP ERROR:" + e.getMessage());
                                   Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

                               }

                               @Override
                               public void onComplete() {
                                   //Toast.makeText(getApplicationContext(),"succes",Toast.LENGTH_LONG).show();
                                   adapter.notifyDataSetChanged();

                                   isparkProgressBar.setVisibility(View.INVISIBLE);
                                   filterParkEditText.setEnabled(true);
                               }
                           }
                );
    }

}
