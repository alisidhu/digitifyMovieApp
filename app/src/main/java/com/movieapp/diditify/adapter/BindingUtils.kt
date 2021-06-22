package com.movieapp.diditify.adapter

import android.graphics.Typeface
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.movieapp.diditify.R
import com.movieapp.diditify.utils.Utils

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

@BindingAdapter("poster")
fun ImageView.setPoster(fullPosterPath: String?) {

    fullPosterPath?.let {
        Glide.with(this).load(it).error(R.drawable.no_image_portrait).into(this)
    }
}

@BindingAdapter("loadImage")
fun ImageView.setImage(image: String?) {

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

@BindingAdapter("overviewText")
fun TextView.setOverView(msg: String?) {

    msg?.let {
        text = if (it.isNotEmpty()) it else context.getString(R.string.overview_error)
    }
}
