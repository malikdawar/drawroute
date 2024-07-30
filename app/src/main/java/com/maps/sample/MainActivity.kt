package com.maps.sample

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        replaceFragmentSafely(RouteFragment())
    }

    //Method to load the fragment
    private fun replaceFragmentSafely(
        fragment: Fragment,
        tag: String = fragment.javaClass.name,
        @IdRes containerViewId: Int = R.id.container
    ) {
        val ft = supportFragmentManager
            .beginTransaction()
            .replace(containerViewId, fragment, tag)
        ft.commit()
    }
}