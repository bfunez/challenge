package com.example.challenge.ui

import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.challenge.R
import com.example.challenge.utils.NetworkClient
import kotlinx.coroutines.*
import java.net.URL

/**
 * Binding adapter to show/hide the view if value provided is true/false
 */
@set:BindingAdapter("visible")
var View.visible
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

/**
 * binding adapter to load the images from the web, not using glide :/
 * @receiver ImageView
 * @param url String
 */
@BindingAdapter("loadImage")
fun ImageView.loadImage(url : String){
    val imagePart = URL(url) //used to strip the image url path and get the file name
    CoroutineScope(Dispatchers.IO).launch {
        val bitmap = NetworkClient().requestToDownloadImage("https://bv-content.beenverified.com/fit-in/60x0/filters:autojpg()${imagePart.file}")
        withContext(Dispatchers.Main){
            bitmap?.let {
                val drawable = BitmapDrawable(this@loadImage.context.resources, bitmap)
                drawable.setBounds(0, 0, bitmap.width, bitmap.height)
                this@loadImage.setImageDrawable(drawable)

            } ?: this@loadImage.setImageResource(R.drawable.ic_error)

        }
    }
}