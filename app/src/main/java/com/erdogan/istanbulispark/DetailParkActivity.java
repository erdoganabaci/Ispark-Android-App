package com.erdogan.istanbulispark;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.erdogan.istanbulispark.api.APIInterface;
import com.erdogan.istanbulispark.api.ApiRequest;
import com.erdogan.istanbulispark.models.ParkDetail;
import com.erdogan.istanbulispark.models.Tarifeler;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DetailParkActivity extends AppCompatActivity {
    private static final String TAG = "DetailParkActivity";
    TextView isparkDetailtextView;
    ArrayList<String> parkAdiListDetail;
    String isparkName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_park);

        isparkDetailtextView = findViewById(R.id.isparkDetailtextView);

        Intent intent = getIntent();
        isparkName = intent.getStringExtra("parkNameFromMain");

        getParkDetail();
        getParkCost();
    }



    public void getParkDetail() {
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
                                       /*String parkAdi = park.get(i).parkAdi;
                                       parkAdiListDetail.add(park.get(i).parkAdi);
                                       Log.d(TAG, "erdo" + parkAdi);*/
                                       if (isparkName != null){
                                           String parkName = park.get(i).parkAdi;
                                           if (parkName.contains(isparkName)){
                                               String parkId = park.get(i).parkID.toString();
                                               //parkadı
                                               String parkLat = park.get(i).latitude;
                                               String parkLon = park.get(i).longitude;
                                               String parkCapacity = park.get(i).kapasitesi.toString();
                                               String parkEmptyCapacity = park.get(i).bosKapasite.toString();
                                               String parkType = park.get(i).parkTipi;
                                               String parkIlce = park.get(i).ilce;
                                               String parkDistance = park.get(i).distance.toString();
                                               String parkFreeMin = park.get(i).ucretsizParklanmaDk.toString();

                                               String combine = "Park Id: "+parkId+"\nParkAdi: "+parkName+"\nPark Latitude: "+parkLat+"\nPark Longitude: "+parkLon+
                                               "\nPark Kapasitesi: "+parkCapacity+"\nBosKapasite: "+parkEmptyCapacity+"\nParkTipi: "+parkType
                                                       +"\nParkIlçe: "+parkIlce+"\nPark Distance: "+parkDistance+"\nUcretsiz Parklanma Dakikasi: "+parkFreeMin;

                                               //String combine = "Park Adı: "+parkAdi+"\n"+"İlçe: "+ilce;
                                               //park.get(i).ilce;
                                               isparkDetailtextView.setText(combine);
                                               //Log.d(TAG, "erdo " + ilce);

                                           }
                                       }


                                   }


                               }

                               @Override
                               public void onError(Throwable e) {
                                   Log.d(TAG,"RXJAVA2 HTTP ERROR:" + e.getMessage());
                                   Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

                               }

                               @Override
                               public void onComplete() {
                                   //Toast.makeText(getApplicationContext(),"succes",Toast.LENGTH_LONG).show();
                                   //isparkProgressBar.setVisibility(View.INVISIBLE);
                               }
                           }
                );
    }


    public void getParkCost() {
        APIInterface apiInterface = ApiRequest.getApiService();
        Observable<List<Tarifeler>> exampleCall = apiInterface.getCosts(395);
        exampleCall.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Tarifeler>>()  {
                               @Override
                               public void onSubscribe(Disposable d) {

                               }

                               @Override
                               public void onNext(List<Tarifeler> cost) {
                                   for (int i = 0; i < cost.size(); i++) {
                                       String tarife = cost.get(i).tarife;
                                       Log.d(TAG, "tarife " + tarife);



                                   }


                               }

                               @Override
                               public void onError(Throwable e) {
                                   Log.d(TAG,"RXJAVA2 HTTP ERROR:" + e.getMessage());
                                   Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

                               }

                               @Override
                               public void onComplete() {
                                   //Toast.makeText(getApplicationContext(),"succes",Toast.LENGTH_LONG).show();
                                   //isparkProgressBar.setVisibility(View.INVISIBLE);
                               }
                           }
                );
    }
}
