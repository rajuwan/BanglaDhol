package com.ebs.banglalinkbangladhol.revamp.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ebs.banglalinkbangladhol.R
import com.ebs.banglalinkbangladhol.activity.BanglaDholSignUpLogInActivity
import com.ebs.banglalinkbangladhol.fragment.FeedBackFragment.RequestThreadPostFeedBack
import com.ebs.banglalinkbangladhol.others.CheckUserInfo
import com.ebs.banglalinkbangladhol.revamp.Common
import com.ebs.banglalinkbangladhol.revamp.api.RetrofitClient
import com.ebs.banglalinkbangladhol.revamp.showToast
import kotlinx.android.synthetic.main.activity_feedback.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class FeedbackActivity : AppCompatActivity() {

    private val myApi  = RetrofitClient.getInstance(this@FeedbackActivity).aPi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        imageView4.setOnClickListener {
            finish()
        }

        sendBTN.setOnClickListener {
            postFeedback(feedbackET.text.toString())
        }

    }

    private fun postFeedback(feedback: String){
        try {
            if (!CheckUserInfo.isValidPhoneNumber(CheckUserInfo.getUserMsisdn())) {
                val mainIntent = Intent(this, BanglaDholSignUpLogInActivity::class.java)
                startActivity(mainIntent)
            } else {
                if (feedback.length >= 5) {

                    myApi.sendFeedback(CheckUserInfo.getUserMsisdn(), feedback).enqueue(object : Callback<Any>{
                        override fun onResponse(call: Call<Any>, response: Response<Any>) {
                            if (response.isSuccessful){
                                this@FeedbackActivity.showToast("Thanks for your feedback")
                                finish()
                            }
                        }

                        override fun onFailure(call: Call<Any>, t: Throwable) {
                            this@FeedbackActivity.showToast("Please try again later")
                        }

                    })

                } else {
                    Toast.makeText(this, "Please type at least 5 letters", Toast.LENGTH_LONG).show()
                }
            }
        } catch (ex: Exception) {
            Log.d("Error in Refund", ex.toString())
        }
    }



}