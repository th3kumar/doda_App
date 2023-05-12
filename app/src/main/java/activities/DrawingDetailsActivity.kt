package activities

import ViewModel.DrawingViewModel
import ViewModel.DrawingViewModelFactory
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.doda.R
import data.Drawing
import data.DrawingDatabase
import data.DrawingRepository
import data.Marker
import java.io.FileNotFoundException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class DrawingDetailsActivity : AppCompatActivity() {
    private var drawingId: Int = 0
    private lateinit var drawing: Drawing

    private val REQUEST_PERMISSION_CODE = 123

    private lateinit var viewModel: DrawingViewModel // Declare the ViewModel as a member variable

    private lateinit var imageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var createdTextView: TextView
    private lateinit var markersTextView: TextView

    private lateinit var gestureDetector: GestureDetector
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var scaleFactor = 1.0f

    private val markers = mutableListOf<Marker>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawing_details)

        // Get the drawing ID from the intent extras
        drawingId = intent.getIntExtra("drawing_id", -1)

        // Initialize views
        imageView = findViewById(R.id.drawingimageView)
        titleTextView = findViewById(R.id.drawingtitleTextView)
        createdTextView = findViewById(R.id.drawingcreatedTextView)
        markersTextView = findViewById(R.id.drawingmarkersTextView)

        // Initialize the ScaleGestureDetector
        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())

        // Check if the app has permission to access external storage
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Request the permission from the user
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERMISSION_CODE)
        } else {
            // The permission is already granted, proceed with accessing the image file
            loadImageFromFile()
        }

        // Set the touch listener for the image view
        imageView.setOnTouchListener { _, event ->
            onTouchEvent(event)
        }
        // Fetch the drawing details from the database using the ID
        val database = DrawingDatabase.getDatabase(this)
        val repository = DrawingRepository(database.drawingDao())
        // Assign the ViewModel to the member variable
        viewModel = ViewModelProvider(this, DrawingViewModelFactory(repository)).get(DrawingViewModel::class.java)
        viewModel.getDrawingById(drawingId)
        viewModel.drawing.observe(this) { drawing ->
            if (drawing != null) {
                displayDrawingDetails(drawing)
                // Fetch markers for the drawing
                viewModel.getMarkersForDrawing(drawing.id)
            }
        }

        // Observe the markers LiveData
        viewModel.markers.observe(this) { markers ->
            if (markers != null) {
                this.markers.addAll(markers)
                Log.d("DrawingDetailsActivity", "Number of markers: ${markers.size}")
                updateImageViewWithMarkers()
            }
        }

        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                val x = e.x
                val y = e.y

                val imageViewRect = Rect()
                imageView.getGlobalVisibleRect(imageViewRect)

                if (imageViewRect.contains(x.toInt(), y.toInt())) {
                    val marker = Marker(0, drawingId, "", x, y, System.currentTimeMillis())
                    openMarkerTitleInputDialog(viewModel, marker)
                    return true
                }

                return false
            }
        })

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted, proceed with accessing the image file
                loadImageFromFile()
            } else {
                // Permission is denied, handle it accordingly (e.g., display an error message)
            }
        }
    }


    private fun loadImageFromFile() {
        if (::drawing.isInitialized) {
            Glide.with(this)
                .load(drawing.imageUri)
                .addListener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        displayErrorMessage("Failed to load image")
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        updateImageViewWithMarkers()
                        return false
                    }
                })
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        imageView.setImageDrawable(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // Clear any placeholder if needed
                    }
                })
        }
    }



    private fun openMarkerTitleInputDialog(viewModel: DrawingViewModel,marker: Marker) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.layout_dialog_title_input, null)
        val titleEditText = dialogView.findViewById<EditText>(R.id.titleEditText)

        val dialogBuilder = AlertDialog.Builder(this)
            //.setTitle("Enter Marker Title")
            .setView(dialogView)
            .setPositiveButton("Save") { dialog, which ->
                val title = titleEditText.text.toString()
                marker.title = title
                markers.add(marker)

                // Save the marker in the database
                viewModel.insertMarker(marker)


                // Update the marker count for the drawing
                drawing.markerCount = markers.size

                // Update the drawing in the database
                viewModel.updateDrawing(drawing)

                // Call a method to update the image view with marker overlays
                updateImageViewWithMarkers()

                // Create a result intent and pass the updated marker count and drawing ID
                val resultIntent = Intent()
                resultIntent.putExtra("marker_count", drawing.markerCount)
                resultIntent.putExtra("drawing_id", drawingId)

                // Set the result and finish the activity
                setResult(Activity.RESULT_OK, resultIntent)
               //finish()
            }
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }



    private fun updateImageViewWithMarkers() {
        val imageUri = Uri.parse(drawing.imageUri)

        try {
            val contentResolver = contentResolver
            val inputStream = contentResolver.openInputStream(imageUri)

            if (inputStream != null) {
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()

                if (bitmap != null) {
                    val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)

                    val canvas = Canvas(mutableBitmap)
                    val paint = Paint().apply {
                        color = Color.YELLOW
                        style = Paint.Style.FILL
                    }

                    for (marker in markers) {
                        canvas.drawCircle(marker.posX, marker.posY, 30f, paint)
                    }
                    runOnUiThread {
                        imageView.setImageBitmap(mutableBitmap)
                    }
                } else {
                    // Handle the case where the bitmap is null
                    displayErrorMessage("Failed to load image")
                    // Display an error message or take appropriate action
                }
            } else {
                // Handle the case where inputStream is null
                displayErrorMessage("Failed to load image")
                // Display an error message or take appropriate action
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            // Handle the file not found exception
            displayErrorMessage("Failed to load image")
            // Display an error message or take appropriate action
        } catch (e: IOException) {
            e.printStackTrace()
            // Handle the IOException
            displayErrorMessage("Failed to load image")
            // Display an error message or take appropriate action
        }
    }


    private fun displayErrorMessage(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // Pass the touch event to the ScaleGestureDetector
        scaleGestureDetector.onTouchEvent(event)
        if (::gestureDetector.isInitialized) {
            gestureDetector.onTouchEvent(event)
        } else {
            return false
        }
        return true
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= detector.scaleFactor

            // Limit the scale factor to a minimum and maximum value if desired
            // scaleFactor = scaleFactor.coerceIn(MIN_SCALE_FACTOR, MAX_SCALE_FACTOR)

            // Apply the scale transformation to the ImageView
            imageView.scaleX = scaleFactor
           imageView.scaleY = scaleFactor

            return true
        }
    }

    private fun displayDrawingDetails(drawing: Drawing) {
        // Display the drawing details in the views
        titleTextView.text = drawing.title
        val sdf = SimpleDateFormat("MMM dd, yyyy 'at' h:mm a", Locale.getDefault())
        val date = Date(drawing.creationTime)
        createdTextView.text = sdf.format(date)
        markersTextView.text = "${drawing.markerCount} markers"

        // Save the drawing object to use it later in the updateImageViewWithMarkers method
        this.drawing = drawing
        // Log the imageUri value
        Log.d("DrawingDetailsActivity", "Image URI: ${drawing.imageUri}")

        Glide.with(this)
            .load(drawing.imageUri)
            .centerCrop()
            .into(imageView)
    }
}


