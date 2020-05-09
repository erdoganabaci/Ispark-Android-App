package com.erdogan.istanbulispark.api;


import com.erdogan.istanbulispark.models.ParkDetail;
import com.erdogan.istanbulispark.models.Tarifeler;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {



    @GET("ispark/park")
    Observable<List<ParkDetail>> getPark();

    //ispark/ParkDetay

    @GET("ispark/ParkDetay")
    Observable<List<Tarifeler>> getCosts(@Query("id") int id);
   /* @Headers("Mailsac-Key: dd1UmasIwgabXuD0KWV6snOP")
    @GET("addresses/{getUserMailMessage}/messages")
    //@GET("users/{user_id}/playlists")
    //addresses/testUserMail123@mailsac.com/messages
    Observable<List<Repo>> getUserMailMessage(@Path(value = "getUserMailMessage", encoded = true) String getUserMailMessage);*/

    /*@Headers({"Mailsac-Key: dd1UmasIwgabXuD0KWV6snOP","Content-Type: application/json"})
    @GET("dirty/{getUserMailMessage}/{getMessageDetailId}")
    //https://mailsac.com/api/dirty/testusermail123@mailsac.com/eho3KaKBxe91gQCNcjkNOyQ-0
    Observable<String> getMessageDetail(@Path(value = "getUserMailMessage", encoded = true) String getUserMailMessage, @Path(value = "getMessageDetailId", encoded = true) String getMessageDetailId);
*/

    //Call<List<Playlist> getUserPlaylists(@Path(value = "user_id", encoded = true) String userId);

    //Observable<Example> getNearRestourants(@Query("lat") String lat, @Query("lon") String lon);

    //@Headers("user-key: 091204fa10ee3e2e2cd2c84b40e5dceb")
    //@GET("restaurant")
    //Observable<com.example.neredeyesemchallange.models.detail.Example> getRestaurantDetail(@Query("res_id") String res_id);

}
