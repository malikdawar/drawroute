package com.maps.route.utils.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

/**
 * An Extension to getColorCompat of view via context
 * @return void
 * @author Dawar Malik.
 */
fun Context.getColorCompat(@ColorRes colorRes: Int) = ContextCompat.getColor(this, colorRes)

/**
 * An Extension to getColorCompat via fragment
 * @return void
 * @author Dawar Malik.
 */
fun Fragment.getColor(@ColorRes colorRes: Int) = requireContext().getColorCompat(colorRes)


/**
 * Helper method to get BitmapDescriptor from drawable
 * @return BitmapDescriptor
 * @author Dawar Malik.
 */
fun Drawable.getMarkerIconFromDrawable(): BitmapDescriptor {
    val canvas = Canvas()
    val bitmap = Bitmap.createBitmap(
        intrinsicWidth,
        intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    canvas.setBitmap(bitmap)
    setBounds(0, 0, intrinsicWidth, intrinsicHeight)
    draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}