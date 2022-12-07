package com.toplevel.kengmakon.api;

import com.toplevel.kengmakon.models.BaseResponse;
import com.toplevel.kengmakon.models.CategoriesModel;
import com.toplevel.kengmakon.models.CategoryDetailModel;
import com.toplevel.kengmakon.models.FurnitureModel;
import com.toplevel.kengmakon.models.LikeModel;
import com.toplevel.kengmakon.models.LoginModel;
import com.toplevel.kengmakon.models.SetDetailModel;
import com.toplevel.kengmakon.models.SetModel;
import com.toplevel.kengmakon.models.SignUpModel;
import com.toplevel.kengmakon.models.UserInfoModel;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    @POST("/api/user/signup")
    Single<BaseResponse> signUp(@Body SignUpModel signUpModel);

    @POST("/api/user/login")
    Single<LoginModel.LoginResModel> login(@Body LoginModel model);

    @GET("/api/user/info")
    Single<UserInfoModel> getUserInfo(@Header("Authorization") String token);

    @GET("/api/set/list")
    Single<SetModel> getSet(@Query("page") int page,
                            @Query("size") int size);

    @GET("/api/category/list")
    Single<CategoriesModel> getCategories(@Query("page") int page,
                                          @Query("size") int size);

    @GET("/api/furniture/list")
    Single<FurnitureModel> getFurniture(@Header("Authorization") String token,
                                        @Query("page") int page,
                                        @Query("size") int size);

    @GET("/api/set/detail")
    Single<SetDetailModel> getSetDetailList(@Header("Authorization") String token,
                                            @Query("id") int id);

    @GET("/api/category/items")
    Single<CategoryDetailModel> getCategoryDetailList(@Header("Authorization") String token,
                                                      @Query("page") int page,
                                                      @Query("size") int size,
                                                      @Query("id") int id);

    @POST("api/furniture/likeDislike")
    Single<LikeModel> setLikeDislike(@Header("Authorization") String token, @Body LikeModel.LikeReqModel model);

    @GET("/api/user/wishlist")
    Single<FurnitureModel> getWishlist(@Header("Authorization") String token);
}
