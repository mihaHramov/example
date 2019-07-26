package aaa.bbb.ccc.solidsnake.data.api;

import java.util.List;
import java.util.Map;

import aaa.bbb.ccc.solidsnake.model.ServerResult;
import aaa.bbb.ccc.solidsnake.model.UserEvent;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface RestApi {

    @POST("/test.php")
    @FormUrlEncoded
    Call<ServerResult> getSereverUrl(@FieldMap Map<String, String> options);
    @GET("/test.php")
    Call<Void> sendUserSClick(@Query("id") Integer id, @Query("clickid") String sclick);
    @GET("/test.php")
    Call<List<UserEvent>> getUserEvent(@Query("id") Integer id);
    @GET("/test.php")
    Call<Void> sendEmail(@Query("id") Integer id,@Query("site") String domain,@Query("email") String email);
}
