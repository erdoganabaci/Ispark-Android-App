
package com.erdogan.istanbulispark.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tarifeler {

    @SerializedName("Tarife")
    @Expose
    public String tarife;
    @SerializedName("Fiyat")
    @Expose
    public Integer fiyat;

}
