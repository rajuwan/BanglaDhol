package com.ebs.banglalinkbangladhol.revamp

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar

fun Context.showToast(msg : String){
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}

fun ProgressBar.show(){
    visibility = View.VISIBLE
}

fun ProgressBar.hide(){
    visibility = View.GONE
}

fun ProgressBar.invisible(){
    visibility = View.INVISIBLE
}

fun View.showSnackBar(msg : String){
    Snackbar.make(this, msg, Snackbar.LENGTH_LONG).also { snackbar ->
        snackbar.setAction("OK"){
            snackbar.dismiss()
        }
    }.show()
}


fun View.show(){
    visibility = View.VISIBLE
}

fun View.hide(){
    visibility = View.GONE
}

fun View.invisible(){
    visibility = View.INVISIBLE
}

fun showLogE(msg : String){
    Log.e("tag", msg)
}

fun ImageView.setImageWithGlide(imageUrl : String){
    Glide
        .with(this)
        .load(imageUrl)
        //.placeholder(R.drawable.no_img)
        .into(this)
}

fun View.setMargin(leftDp : Int, topDp : Int, rightDp : Int, bottomDp : Int){
    if (this.layoutParams is MarginLayoutParams) {
        val p = this.layoutParams as MarginLayoutParams
        p.setMargins(leftDp * 2, topDp * 2, rightDp * 2, bottomDp * 2)
        this.requestLayout()
    }
}

fun TextView.makeExpendable(lineNo : Int, moreText : String, viewMore : Boolean, context: Context){
    if (this.tag == null) {
        //tv.tag = this.text
    }
    val viewTreeObserver = this.viewTreeObserver
    viewTreeObserver.addOnGlobalLayoutListener {
        val text = ""
        val lineEndIndex = 0
        val obs = this.viewTreeObserver
    }
}
