package com.movieapp.diditify.utils

import android.graphics.Typeface
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.movieapp.diditify.R

@BindingAdapter("overviewText")
fun TextView.setOverView(msg: String?) {

    msg?.let {
        text = if (it.isNotEmpty()) it else context.getString(R.string.overview_error)
    }
}
@BindingAdapter("poster")
fun ImageView.setPoster(fullPosterPath: String?) {

    try {
        fullPosterPath?.let {
            Glide.with(this).load(it).error(R.drawable.no_image_portrait).into(this)
        }
    } catch (e: Exception) {
        e.message
    }
}
@BindingAdapter("year")
fun TextView.setMovieYear(releaseDate: String?) {
    if (releaseDate == null) {
        return
    }
    this.text = releaseDate
    if (Utils.isCurrentYear(releaseDate)) {
        this.setTextColor(ContextCompat.getColor(this.context, android.R.color.holo_red_dark))
        this.setTypeface(this.typeface, Typeface.BOLD)
    } else {
        this.setTextColor(ContextCompat.getColor(this.context, android.R.color.white))
        this.setTypeface(this.typeface, Typeface.NORMAL)
    }
}



@BindingAdapter("loadImage")
fun ImageView.setImage(image: Int) {

    image?.let {
        Glide.with(this).load(it).into(this)
    }
}

@BindingAdapter("itemImage")
fun ImageView.setItemImage(fullProfilePath: String?) {

    fullProfilePath?.let {
        Glide.with(this).load(it).error(R.drawable.no_image_portrait).into(this)
    }
}

