package com.maps.route.extensions

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

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