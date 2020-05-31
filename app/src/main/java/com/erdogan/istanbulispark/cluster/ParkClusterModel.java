package com.erdogan.istanbulispark.cluster;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class ParkClusterModel implements ClusterItem {
    private final String parkName;
    private final LatLng latLng;
    public ParkClusterModel(String parkName, LatLng latLng) {
        this.parkName = parkName;
        this.latLng = latLng;
    }
    @Override
    public LatLng getPosition() {  // 1
        return latLng;
    }
    @Override
    public String getTitle() {  // 2
        return parkName;
    }
    @Override
    public String getSnippet() {
        return "";
    }
}