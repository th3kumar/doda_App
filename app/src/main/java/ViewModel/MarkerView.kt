package ViewModel

import android.content.Context
import android.view.Gravity
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import com.example.doda.R

class MarkerView(context: Context) : AppCompatImageView(context) {
    init {
        // Set the marker image resource or background drawable
        setImageResource(R.drawable.marker_icon) // Replace with your marker image resource

        // Set the marker view properties
        val markerSize = resources.getDimensionPixelSize(R.dimen.marker_size)
        val layoutParams = FrameLayout.LayoutParams(markerSize, markerSize)
        layoutParams.gravity = Gravity.CENTER
        setLayoutParams(layoutParams)

        // Add any additional customization or styling for the marker view
        // ...
    }
}
