package com.furniture.kengmakon.api;

import com.furniture.kengmakon.models.BaseResponse;
import com.furniture.kengmakon.models.CategoriesModel;
import com.furniture.kengmakon.models.DiscountModel;
import com.furniture.kengmakon.models.FeedbackModel;
import com.furniture.kengmakon.models.ForgotPasswordReqModel;
import com.furniture.kengmakon.models.FurnitureModel;
import com.furniture.kengmakon.models.LoginModel;
import com.furniture.kengmakon.models.NotificationsModel;
import com.furniture.kengmakon.models.OrderDetailModel;
import com.furniture.kengmakon.models.OrdersModel;
import com.furniture.kengmakon.models.PushTokenReqModel;
import com.furniture.kengmakon.models.SetDetailModel;
import com.furniture.kengmakon.models.SetModel;
import com.furniture.kengmakon.models.SignUpModel;
import com.furniture.kengmakon.models.ActionsModel;
import com.furniture.kengmakon.models.BranchesModel;
import com.furniture.kengmakon.models.CashbackModel;
import com.furniture.kengmakon.models.CategoryDetailModel;
import com.furniture.kengmakon.models.FurnitureDetailModel;
import com.furniture.kengmakon.models.LikeModel;
import com.furniture.kengmakon.models.UpdatePasswordReqModel;
import com.furniture.kengmakon.models.UpdateUsernameReqModel;
import com.furniture.kengmakon.models.UserInfoModel;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    @GET("/api/set/topList")
    Single<SetModel> getTopSet();

    @GET("/api/category/list")
    Single<CategoriesModel> getCategories(@Query("page") int page,
                                          @Query("size") int size);

    @GET("/api/furniture/list")
    Single<FurnitureModel> getFurniture(@Header("Authorization") String token,
                                        @Query("page") int page,
                                        @Query("size") int size);

    @GET("/api/furniture/topList")
    Single<FurnitureModel> getFurnitureTopList(@Header("Authorization") String token);

    @GET("/api/furniture/detail")
    Single<FurnitureDetailModel> getFurnitureDetail(@Header("Authorization") String token, @Query("id") int id);

    @GET("/api/set/detail")
    Single<SetDetailModel> getSetDetailList(@Header("Authorization") String token,
                                            @Query("id") int id);

    @GET("/api/category/items")
    Single<CategoryDetailModel> getCategoryDetailList(@Header("Authorization") String token,
                                                      @Query("page") int page,
                                                      @Query("size") int size,
                                                      @Query("id") int id);
    @GET("/api/category/sets")
    Single<SetModel> getCategoryDetailSetList(@Header("Authorization") String token,
                                                      @Query("page") int page,
                                                      @Query("size") int size,
                                                      @Query("id") int id);

    @POST("api/furniture/likeDislike")
    Single<LikeModel> setLikeDislike(@Header("Authorization") String token, @Body LikeModel.LikeReqModel model);

    @GET("/api/user/wishlist")
    Single<FurnitureModel> getWishlist(@Header("Authorization") String token);

    @POST("api/user/registerToken")
    Single<BaseResponse> pushToken(@Header("Authorization") String token, @Body PushTokenReqModel pushTokenReqModel);

    @POST("api/feedback/submit")
    Single<BaseResponse> postFeedback(@Header("Authorization") String token, @Body FeedbackModel feedbackModel);

    @POST("api/user/forgetPassword")
    Single<BaseResponse> forgotPassword(@Body ForgotPasswordReqModel forgotPasswordReqModel);

    @GET("/api/user/cashback")
    Single<CashbackModel> getCashback(@Header("Authorization") String token, @Query("user_id") int user_id);

    @GET("/api/user/discount-detailed")
    Single<DiscountModel> getDiscount(@Header("Authorization") String token);

    @GET("/api/order/list")
    Single<OrdersModel> getOrders(@Header("Authorization") String token);

    @GET("/api/order/detail")
    Single<OrderDetailModel> getOrderDetail(@Header("Authorization") String token, @Query("order_id") int order_id);

    @GET("/api/info/branches")
    Single<BranchesModel> getBranches(@Query("lang") String lang);

    @GET("/api/action/list")
    Single<ActionsModel> getActions(@Query("page") int page,
                                    @Query("size") int size,
                                    @Query("lang") String lang,
                                    @Query("type") String type);

    @GET("/api/notification/list")
    Single<NotificationsModel> getNotifications(@Query("page") int page,
                                                @Query("size") int size,
                                                @Query("lang") String lang);

    @Multipart
    @POST("/api/user/updateInfo")
    Single<BaseResponse> uploadUserImage(@Header("Authorization") String token,
                                      @Part("name") RequestBody name,
                                      @Part MultipartBody.Part image);

    @POST("/api/user/updateInfo")
    Single<BaseResponse> updateUsername(@Header("Authorization") String token, @Body UpdateUsernameReqModel updateUsernameReqModel);

    @POST("/api/user/updatePassword")
    Single<BaseResponse> updatePassword(@Header("Authorization") String token, @Body UpdatePasswordReqModel updatePasswordReqModel);
}
