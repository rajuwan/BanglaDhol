package com.ebs.banglalinkbangladhol.revamp.api


import com.ebs.banglalinkbangladhol.revamp.Constant
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    // 01876277510
    //Pass: 062814

    //--------------------- HOME -----------------
    @FormUrlEncoded
    @POST(Constant.HOME)
    fun fetchHomeContent(
        @Field("msisdn") msisdn : String,
        @Field("view") view : String,
        @Field("ct") ct : String,
        @Field("version") version : String
    ): Call<MutableList<String>>

    @FormUrlEncoded
    @POST(Constant.SEARCH)
    fun searchContent(
        @Field("s") keyword : String,
        @Field("username") username : String,
        @Field("page") page : Int
    ): Call<String>

    // SeeAll with pagination
    @FormUrlEncoded
    @POST(Constant.CAT_JSON)
    fun getSeeAllList(
        @Field("msisdn") msisdn : String,
        @Field("ct") ct : String,
        @Field("tc") tc : Int,
        @Field("page") page : Int
    ): Call<String>

    // Content details
    @FormUrlEncoded
    @POST(Constant.VIDEO_DETAILS)
    fun getCententDetails(
        @Field("cc") cc : String,
        @Field("username") msisdn : String,
        @Field("token") token : String
    ): Call<MutableList<String>>

    // Similar item
    @FormUrlEncoded
    @POST(Constant.CAT_JSON)
    fun getSimilarItem(
        @Field("username") msisdn : String,
        @Field("ct") ct : String,
        @Field("tc") tc : String,
        @Field("page") page : String
    ): Call<String>

    // drama serial
    @FormUrlEncoded
    @POST(Constant.DRAMA_SERIAL)
    fun getDramaSerial(
        @Field("username") msisdn : String,
        @Field("cc") cc : String
    ): Call<String>

    // my list
    @FormUrlEncoded
    @POST(Constant.MY_LIST)
    fun getMyList(
        @Field("username") msisdn : String,
        @Field("page") page : String
    ): Call<String>

    // add to list
    @FormUrlEncoded
    @POST(Constant.ADD_MY_LIST)
    fun addMyList(
        @Field("myval") myval : String,
        @Field("username") msisdn : String
    ): Call<Any>


    // post play time
    @FormUrlEncoded
    @Headers("Content-Type: application/json")
    @POST(Constant.POST_PLAY_TIME)
    fun postPlayTime(
        @Field("time") time : String,
        @Field("contentid") contentid : String,
        @Field("username") msisdn : String,
        @Field("playtime") playtime : String
    ): Call<Void>

    // post prev time
    @FormUrlEncoded
    @POST(Constant.POST_PLAY_TIME_PREV)
    @Headers("Content-Type: application/json")
    fun postPrevTime(
        @Field("time") time : Int,
        @Field("contentid") contentid : String,
        @Field("username") msisdn : String,
        @Field("playtime") playtime : Int
    ): Call<Void>


    // remove from list
    @FormUrlEncoded
    @POST(Constant.REMOVE_MY_LIST)
    fun removeFromMyList(
        @Field("myval") myval : String,
        @Field("username") msisdn : String
    ): Call<Any>

    // add to comment
    @FormUrlEncoded
    @POST(Constant.POST_COMMENT)
    fun addComment(
        @Field("myval") myval : String,
        @Field("username") msisdn : String,
        @Field("comment") comment : String
    ): Call<Any>

    // add to comment
    @FormUrlEncoded
    @POST(Constant.MY_RATING)
    fun addRating(
        @Field("contentid") myval : String,
        @Field("username") msisdn : String,
        @Field("rating") rating : String,
        @Field("deviceid") deviceid : String
    ): Call<Any>



    // Feedback
    @FormUrlEncoded
    @POST(Constant.FEEDBACK)
    fun sendFeedback(
        @Field("msisdn") msisdn : String,
        @Field("comment") comment : String
    ): Call<Any>

    /*// add my list
    @FormUrlEncoded
    @POST(Constant.ADD_MY_LIST)
    fun addMyList(
        @Field("myval") myval : String,
        @Field("username") msisdn : String
    ): Response<Info>*/




    // SDP call
    @FormUrlEncoded
    @POST(Constant.SUB_INSTANT_SDP)
    fun getSDP(
        @Field("msisdn") msisdn: String,
        @Field("d") selectedPackage: String,
        @Field("renew") renew: String
    ): Call<MutableList<String>>


    // SDP call
    @FormUrlEncoded
    @POST(Constant.UPDATE_SUB)
    suspend fun updateSubStatus(
        @Field("msisdn") msisdn: String,
        @Field("aocTransID") transactionId: String
    ): Response<String>

    // msisdn
    @GET(Constant.REQUEST_MSISDN)
    fun getMsisdn(): Call<MutableList<String>>


    // ---------------- LOGIN OR, REGISTRATION in same api ------------------
    @FormUrlEncoded
    @POST(Constant.MAKE_LOGIN_TOKEN)
    fun userLoginToken(
        @Field("username") msisdn : String,
        @Field("password") password : String,
        @Field("deviceId") deviceId : String,
        @Field("imei") imei : String,
        @Field("imsi") imsi : String,
        @Field("softwareVersion") softwareVersion : String,
        @Field("simSerialNumber") simSerialNumber : String,
        @Field("operator") operator : String,
        @Field("operatorName") operatorName : String,
        @Field("brand") brand : String,
        @Field("model") model : String,
        @Field("release") release : String,
        @Field("sdkVersion") sdkVersion : String,
        @Field("versionCode") versionCode : String,
        @Field("haspin") haspin : String
    ): Call<MutableList<String>>

    // ---------------- un sub api ------------------
    @FormUrlEncoded
    @POST(Constant.UNSUB_INSTANT)
    suspend fun usSubscription(
        @Field("msisdn") msisdn: String
    ): Response<MutableList<String>>

    @FormUrlEncoded
    @POST(Constant.MAKE_LOGOUT)
    fun deviceLogOut(
        @Field("username") msisdn : String,
        @Field("password") password : String,
        @Field("deviceId") deviceId : String,
        @Field("imei") imei : String,
        @Field("imsi") imsi : String,
        @Field("softwareVersion") softwareVersion : String,
        @Field("simSerialNumber") simSerialNumber : String,
        @Field("operator") operator : String,
        @Field("operatorName") operatorName : String,
        @Field("brand") brand : String,
        @Field("model") model : String,
        @Field("release") release : String,
        @Field("sdkVersion") sdkVersion : String,
        @Field("versionCode") versionCode : String,
        @Field("haspin") haspin : String,
        @Field("changedevice") changedevice : String
    ): Call<MutableList<String>>

    @FormUrlEncoded
    @POST(Constant.SIGN_UP)
    fun signUp(
        @Field("msisdn") msisdn : String,
        @Field("deviceId") deviceId : String,
        @Field("imei") imei : String,
        @Field("imsi") imsi : String,
        @Field("softwareVersion") softwareVersion : String,
        @Field("simSerialNumber") simSerialNumber : String,
        @Field("operator") operator : String,
        @Field("operatorName") operatorName : String,
        @Field("brand") brand : String,
        @Field("model") model : String,
        @Field("release") release : String,
        @Field("sdkVersion") sdkVersion : String,
        @Field("versionCode") versionCode : String
    ): Call<String>

    @FormUrlEncoded
    @POST(Constant.SIGN_UP)
    fun forgotPassword(
        @Field("forget") forget : String,
        @Field("msisdn") msisdn : String,
        @Field("deviceId") deviceId : String,
        @Field("imei") imei : String,
        @Field("imsi") imsi : String,
        @Field("softwareVersion") softwareVersion : String,
        @Field("simSerialNumber") simSerialNumber : String,
        @Field("operator") operator : String,
        @Field("operatorName") operatorName : String,
        @Field("brand") brand : String,
        @Field("model") model : String,
        @Field("release") release : String,
        @Field("sdkVersion") sdkVersion : String,
        @Field("versionCode") versionCode : String
    ): Call<String>

    // POST
    @FormUrlEncoded
    @POST(Constant.FRIEBASE_GCM_URL)
    fun sendFirebaseGCMid(
        @Field("src") src : String,
        @Field("username") userName : String,
        @Field("gcmid") fireBaseId : String,
        @Field("deviceid") deviceId : String,
        @Field("brand") brand : String,
        @Field("model") model : String
    ): Call<MutableList<String>>

}