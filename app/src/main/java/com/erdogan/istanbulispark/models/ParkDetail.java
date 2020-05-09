
package com.erdogan.istanbulispark.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParkDetail {

    @SerializedName("ParkID")
    @Expose
    public Integer parkID;
    @SerializedName("ParkAdi")
    @Expose
    public String parkAdi;
    @SerializedName("Latitude")
    @Expose
    public String latitude;
    @SerializedName("Longitude")
    @Expose
    public String longitude;
    @SerializedName("Kapasitesi")
    @Expose
    public Integer kapasitesi;
    @SerializedName("BosKapasite")
    @Expose
    public Integer bosKapasite;
    @SerializedName("ParkTipi")
    @Expose
    public String parkTipi;
    @SerializedName("Ilce")
    @Expose
    public String ilce;
    @SerializedName("Distance")
    @Expose
    public Float distance;
    @SerializedName("UcretsizParklanmaDk")
    @Expose
    public Integer ucretsizParklanmaDk;

}
