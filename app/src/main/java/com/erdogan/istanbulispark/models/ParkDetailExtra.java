
package com.erdogan.istanbulispark.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParkDetailExtra {

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
    @SerializedName("GuncellemeTarihi")
    @Expose
    public String guncellemeTarihi;
    @SerializedName("CalismaSaatleri")
    @Expose
    public String calismaSaatleri;
    @SerializedName("UcretsizParklanmaDk")
    @Expose
    public Integer ucretsizParklanmaDk;
    @SerializedName("AylikAbonelikUcreti")
    @Expose
    public Integer aylikAbonelikUcreti;
    @SerializedName("Adres")
    @Expose
    public String adres;
    @SerializedName("AreaPolygon")
    @Expose
    public List<List<List<Float>>> areaPolygon = null;
    @SerializedName("Tarifeler")
    @Expose
    public List<Tarifeler> tarifeler = null;
    @SerializedName("LokasyonAdi")
    @Expose
    public String lokasyonAdi;

}
