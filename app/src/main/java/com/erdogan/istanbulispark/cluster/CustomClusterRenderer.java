package com.erdogan.istanbulispark.cluster;

import android.content.Context;

import com.erdogan.istanbulispark.MapsActivity;
import com.erdogan.istanbulispark.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class CustomClusterRenderer extends DefaultClusterRenderer<ParkClusterModel> {

    private final Context mContext;

    public CustomClusterRenderer(Context context, GoogleMap map,
                                 ClusterManager<ParkClusterModel> clusterManager) {
        super(context, map, clusterManager);

        mContext = context;
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster<ParkClusterModel> cluster) {
        return cluster.getSize() > 2;

    }

    @Override
    protected void onBeforeClusterRendered(Cluster<ParkClusterModel> cluster, MarkerOptions markerOptions) {
        super.onBeforeClusterRendered(cluster, markerOptions);

    }

    @Override protected void onBeforeClusterItemRendered(ParkClusterModel item,
                                                         MarkerOptions markerOptions) {

        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_park_adaptive));


    }
}