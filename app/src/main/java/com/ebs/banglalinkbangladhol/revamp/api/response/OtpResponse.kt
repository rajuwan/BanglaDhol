package com.ebs.banglalinkbangladhol.revamp.api.response


import com.google.gson.annotations.SerializedName

data class OtpResponse(
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: String?
)