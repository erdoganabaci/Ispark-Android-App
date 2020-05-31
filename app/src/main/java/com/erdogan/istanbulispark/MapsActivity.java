package com.erdogan.istanbulispark;

import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.erdogan.istanbulispark.api.APIInterface;
import com.erdogan.istanbulispark.api.ApiRequest;
import com.erdogan.istanbulispark.cluster.CustomClusterRenderer;
import com.erdogan.istanbulispark.cluster.ParkClusterModel;
import com.erdogan.istanbulispark.models.ParkDetail;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ClusterManager.OnClusterClickListener<ParkClusterModel>,
        ClusterManager.OnClusterItemClickListener<ParkClusterModel>,
        ClusterManager.OnClusterItemInfoWindowClickListener<ParkClusterModel> {
    private static final String TAG = "MapsActivity";

    private GoogleMap mMap;
    LocationManager locationManager;
    RxPermissions rxPermissions;
    LocationListener locationListener;
    FloatingActionButton fabMainButton;
    ArrayList<ParkClusterModel> clusterItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        rxPermissions = new RxPermissions(this); // where this is an Activity or Fragment instance
        fabMainButton = findViewById(R.id.fabMainButton);
        clusterItems = new ArrayList<>();

        fabMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

    }

    public void getLocationRequest(LocationManager locationManager,LocationListener locationListener) {
        rxPermissions
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(granted -> {
                    if (granted) { // Always true pre-M
                        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for Activity#requestPermissions for more details.
                            Toast.makeText(getApplicationContext(),"Permission Denied",Toast.LENGTH_LONG).show();
                            //return;
                        }
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 50, locationListener);
                        if (locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null){
                            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            LatLng userLastLocation = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
                            //mMap.addMarker(new MarkerOptions().position(userLastLocation).title("Buradayım"));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLastLocation,7));
                        }
                        mMap.setMyLocationEnabled(true);
                        mMap.getUiSettings().setMyLocationButtonEnabled(true);
                        //getIsparkLocation(mMap);


                    } else {
                        // Oups permission denied
                        Toast.makeText(getApplicationContext(),"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        getIsparkLocation(mMap);



        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //mMap.clear();
                System.out.println("Location: "+location.toString());
                LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation,50));

                //mMap.addMarker(new MarkerOptions().position(userLocation).title("Buradayım"));
               // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,15));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        getLocationRequest(locationManager,locationListener);



    }

    public void getIsparkLocation(GoogleMap mMap){

        ClusterManager<ParkClusterModel> clusterManager = new ClusterManager(this, mMap);  // 3
        mMap.setOnCameraIdleListener(clusterManager);
        //clusterManager.clearItems();

        ProgressDialog progressDialog = new ProgressDialog(MapsActivity.this);
        progressDialog.setMessage("Parklar Yükleniyor Lütfen bekleyiniz..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        //parkAdiList.clear();
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
                                       Integer parkKapasite = park.get(i).kapasitesi;
                                       Integer parkBosKapasite = park.get(i).bosKapasite;
                                       String parkLat = park.get(i).latitude;
                                       String parkLon = park.get(i).longitude;

                                       Double latituteDouble = Double.parseDouble(parkLat);
                                       Double longitudeDouble = Double.parseDouble(parkLon);

                                       LatLng isparkLocate= new LatLng(latituteDouble,longitudeDouble);
                                       //mMap.addMarker(new MarkerOptions().position(isparkLocate).title(parkAdi).snippet("Detaylar:"+parkKapasite.toString()+"\nBoş Kapasite: "+parkBosKapasite.toString())).setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_park_adaptive));

                                       clusterItems.add(new ParkClusterModel(parkAdi,isparkLocate));



                                       //mMap.addMarker(new MarkerOptions().position(userLocate).title(name).snippet("Detaylar:"+detail+"\nKuşuçuşu Uzaklığım:"+s+" km").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_park_icon)))

                                       //String parkID = park.get(i).parkID.toString();
                                       //parkAdiList.add(park.get(i).parkAdi);
                                       //view.showRestaurants(localList);
                                       //Log.d(TAG, "erdo" + parkAdi);
                                       //Log.d(TAG, "park adları " + parkAdiList);
                                       //parkForMain.put(parkAdi,parkID);

                                       mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                           @Override
                                           public void onInfoWindowClick(Marker marker) {
                                               Toast.makeText(getApplicationContext(),marker.getSnippet().toString(),Toast.LENGTH_LONG).show();
                                               Intent intent = new Intent(getApplicationContext(),DetailParkActivity.class);
                                               intent.putExtra("parkNameFromMain",marker.getTitle());
                                               startActivity(intent);

                                           }
                                       });


                                   }

                                   mMap.setOnMarkerClickListener(clusterManager);
                                   //mMap.setInfoWindowAdapter(clusterManager.getMarkerManager());
                                   //mMap.setOnInfoWindowClickListener(clusterManager);



                                   clusterManager.setOnClusterClickListener(MapsActivity.this::onClusterClick);
                                   clusterManager.setOnClusterItemClickListener(MapsActivity.this::onClusterItemClick);
                                   clusterManager.setOnClusterItemInfoWindowClickListener(MapsActivity.this::onClusterItemInfoWindowClick);

                                   clusterManager.addItems(clusterItems);
                                   CustomClusterRenderer renderer=  new CustomClusterRenderer(MapsActivity.this,mMap,clusterManager);
                                   clusterManager.setRenderer(renderer);


                                   //mMap.setOnCameraIdleListener (clusterManager);
                                   //mMap.setOnMarkerClickListener(clusterManager);
                                   //clusterManager.clearItems();

                                   //clusterManager.getMarkerCollection().clear();
                                   //clusterManager.getClusterMarkerCollection().clear();
                                   //clusterManager.cluster();
                                  /* String totalPark = String.valueOf(parkAdiList.size());
                                   Toast.makeText(getApplicationContext(),"Toplam " +totalPark+ " Park Bulundu",Toast.LENGTH_LONG).show();
                                   isparkListView.setAdapter(adapter);
                                   Log.d(TAG, "park adları " + parkAdiList.get(parkAdiList.size() - 1));*/

                               }

                               @Override
                               public void onError(Throwable e) {
                                   Log.d(TAG,"RXJAVA2 HTTP ERROR:" + e.getMessage());
                                   Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                   progressDialog.dismiss();
                                   //progressDialog.setMessage("Parklar Yüklenirken Hata Meydana Geldi..");


                                   AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                                   builder.setTitle("Internet Connection");
                                   builder.setMessage("Lütfen Internet Bağlantınızı Açınız");
                                   builder.setNegativeButton("Kapat", null);
                                   builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialogInterface, int i) {

                                           /*Intent intent = new Intent(Intent.ACTION_MAIN);
                                           intent.setClassName("com.android.phone","com.android.phone.NetworkSetting");
                                           startActivity(intent);*/
                                           Intent intent1 = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                                           startActivity(intent1);

                                       }
                                   });
                                   builder.show();


                               }

                               @Override
                               public void onComplete() {
                                   progressDialog.dismiss();
                                   //Toast.makeText(getApplicationContext(),"succes",Toast.LENGTH_LONG).show();

                                   //isparkProgressBar.setVisibility(View.INVISIBLE);
                               }
                           }
                );




    }


    @Override
    public boolean onClusterClick(Cluster<ParkClusterModel> cluster) {
        LatLngBounds.Builder builder = LatLngBounds.builder();


        for (ClusterItem item : cluster.getItems()) {
            LatLng clusterPosition = item.getPosition();
            builder.include(clusterPosition);
        }

        final LatLngBounds bounds = builder.build();

        try { mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception error) {
            Log.d(TAG, "onClusterClick error message: "+error);
            //Log.d(error.toString());
        }

        return true;
    }

    @Override
    public boolean onClusterItemClick(ParkClusterModel parkClusterModel) {
        //Toast.makeText(getApplicationContext(),"doakndın",Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(ParkClusterModel parkClusterModel) {

    }
}
