package com.ebs.banglalinkbangladhol.revamp

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.content.res.TypedArray
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.widget.Toast
import androidx.multidex.BuildConfig
import com.ebs.banglalinkbangladhol.revamp.api.ApiInterface
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParser

import org.jetbrains.annotations.NotNull
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import kotlin.math.roundToInt

object Common {

    private var apiInterface : ApiInterface? = null
    private var renuItem: String = ""

    /** Checking Network State */
    fun isNetworkAvailable(context: Context) : Boolean {
        var result = false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivityManager.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { // after Q version
                it.getNetworkCapabilities(connectivityManager.activeNetwork)?.apply {
                    result = when{
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        else -> false
                    }
                }
            } else {  // VERSION.SDK_INT < M"
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                return activeNetworkInfo?.isConnected ?: result
            }
        }
        return result
    }

    // deviceLogOut


    fun showAppUpdatePopup(mContext: Context?, newVersionCode: Long, msgBody: String, enforce: Int) {

        val currentVersionCode : Long =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                mContext!!.applicationContext.packageManager.getPackageInfo(mContext.packageName, 0).longVersionCode
            } else {
                mContext!!.packageManager.getPackageInfo(mContext.packageName, 0).versionCode.toLong()
            }
        Log.e("tag", "ver- $currentVersionCode")

        if (newVersionCode > currentVersionCode){
            val materialAlertDialog = MaterialAlertDialogBuilder(mContext)
            if(enforce == 1){ // check major update
                materialAlertDialog.setCancelable(false)
                    .setTitle("Update required")
                    .setMessage(msgBody)
                    .setPositiveButton("Update") { dialogInterface, i ->
                        try {
                            mContext.startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=com.ebs.banglalinkiportal")
                                )
                            )
                        } catch (e: Exception){
                            //SweetToast.info(mContext, e.message.toString())
                        }
                    }.create().show()
            } else {
                materialAlertDialog.setCancelable(true)
                    .setTitle("Update available")
                    .setMessage(msgBody)
                    .setNegativeButton("Cancel") { dialogInterface, i ->
                        dialogInterface.dismiss()
                    }
                    .setPositiveButton("Update") { dialogInterface, i ->
                        try {
                            mContext.startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=com.ebs.banglalinkiportal")
                                )
                            )
                        } catch (e: Exception){
                            //SweetToast.info(mContext, e.message.toString())
                        }
                    }.create().show()
            }
        } else {
            Log.e("tag", "$newVersionCode :: ${BuildConfig.VERSION_CODE}")
        }
    } // showAppUpdatePopup





    fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
        if (view.layoutParams is MarginLayoutParams) {
            val p = view.layoutParams as MarginLayoutParams
            p.setMargins(left, top, right, bottom)
            view.requestLayout()
        }
    }

    fun getAppInfo(context: Context): List<String>{
        val infoList = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val versionName = BuildConfig.VERSION_NAME
            val versionCode = BuildConfig.VERSION_CODE

            infoList.add(0, versionName)
            infoList.add(1, versionCode.toString())

            return infoList
        } else {
            return try {
                val info = context.packageManager.getPackageInfo(context.packageName, 0)
                infoList.add(0, info.versionName.toString())
                infoList.add(1, info.versionCode.toString())
                infoList
            } catch (e: PackageManager.NameNotFoundException){
                e.printStackTrace()
                emptyList()
            }
        }
    }


    fun printHashKey(context: Context) {
        try {
            val info: PackageInfo = context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = String(Base64.encode(md.digest(), 0))
                Log.e("tag", "KEY->> $hashKey")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e("ss", "printHashKey()", e)
        } catch (e: Exception) {
            Log.e("ss", "printHashKey()", e)
        }
    }


    fun shareUrl(context: Context, subject: String, message: String){
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            context.startActivity(Intent.createChooser(shareIntent, "Choose one"))
        } catch (e: java.lang.Exception) {
            showLogE(e.message!!)
        }
    }

    /** -------------------------------
     * PLAYING AUDIO PART ---- START
     * -------------------------------- **/
    private var mediaPlayer: MediaPlayer? = null

    @Throws(java.lang.Exception::class)
    fun playTinyAudio(context: Context?, url: String?) { // with URL
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, Uri.parse(url))
        }

        if (mediaPlayer!!.isPlaying) {
            killTinyMediaPlayer()
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(context, Uri.parse(url))
            }
            mediaPlayer!!.start()
        } else {
            mediaPlayer!!.start()
        }

        mediaPlayer!!.setOnCompletionListener {
            killTinyMediaPlayer()
        }
    }

    @Throws(java.lang.Exception::class)
    fun playTinyAudio(context: Context?, resId: Int?) { // With file
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, resId!!)
        }

        if (mediaPlayer!!.isPlaying) {
            killTinyMediaPlayer()
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(context, resId!!)
            }
            mediaPlayer!!.start()
        } else {
            mediaPlayer!!.start()
        }

        mediaPlayer!!.setOnCompletionListener {
            killTinyMediaPlayer()
        }

    }

    private fun killTinyMediaPlayer() { // Kill Previous instance for better performance
        if (mediaPlayer != null) {
            try {
                mediaPlayer!!.reset()
                mediaPlayer!!.release()
                mediaPlayer = null
                Log.e("tag", "killed Tiny player")
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    fun isValidPhoneNumber(mobile: String, cc: String): Boolean {
        var regEx  = "".toRegex()
        regEx = when {
            cc.contains("BD") -> {
                "^[0-9]{13}$".toRegex()
            }
            cc.contains("MY") -> {
                "^[0-9+]{12}$".toRegex()
            }
            else -> {
                "^[0-9]{13}$".toRegex()
            }
        }
        return mobile.matches(regEx)
    }

    fun phoneFormattingForLogin(rawPhone: String): String {
        var finalPhone = ""
        finalPhone = if (rawPhone.startsWith("+60")) {
            rawPhone.replace("+", "%2B")
        } else {
            rawPhone
        }
        return finalPhone
    } // phoneFormattingForLogin

    fun getStatusbarAppbarBottombarInDp(context: Context) : ArrayList<Int>{
        val list = ArrayList<Int>()

        // status bar height
        var statusBarHeight = 0
        val resourceIdStatus = context.resources.getIdentifier(
            "status_bar_height",
            "dimen",
            "android"
        )
        if (resourceIdStatus > 0) {
            statusBarHeight = context.resources.getDimensionPixelSize(resourceIdStatus)
        }
        //list.add(0, pxToDp(statusBarHeight))
        list.add(0, statusBarHeight)

        // action bar height
        var actionBarHeight = 0
        val styledAttributes: TypedArray =
            context.theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
        actionBarHeight = styledAttributes.getDimension(0, 0f).roundToInt()
        styledAttributes.recycle()
        //list.add(1, pxToDp(actionBarHeight))
        list.add(1, actionBarHeight)


        // navigation bar height
        var navigationBarHeight = 0
        val resourceId : Int = context.resources.getIdentifier(
            "navigation_bar_height",
            "dimen",
            "android"
        )
        if (resourceId > 0) {
            navigationBarHeight = context.resources.getDimensionPixelSize(resourceId)
        }
        //list.add(2, pxToDp(navigationBarHeight))
        list.add(2, navigationBarHeight)


        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height: Int = displayMetrics.heightPixels
        val width: Int = displayMetrics.widthPixels

        return list
    }

    fun pxToDp(px: Int): Int {
        return (px / Resources.getSystem().displayMetrics.density).toInt()
    }


    fun alertDialog(context: Context, title: String, msg: String, dismissText: String){
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(msg)
            .setNegativeButton(dismissText) { dialogInterface, i ->
                dialogInterface.dismiss()
            }.create().show()
    }



}