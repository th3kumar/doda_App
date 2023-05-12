package activities

import ViewModel.DrawingViewModel
import ViewModel.DrawingViewModelFactory
import adapter.DrawingAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doda.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import data.Drawing
import data.DrawingDatabase
import data.DrawingRepository
import dialog.AddDrawingDialogFragment

class ProjectActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DrawingAdapter
    private lateinit var viewModel: DrawingViewModel
    private lateinit var emptyImageView: ImageView
    private lateinit var drawingList: List<Drawing>

    companion object {
        private const val TAG = "ProjectActivity"
        private const val REQUEST_DRAWING_DETAILS = 1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project)

        // Initialize emptyImageView
        emptyImageView = findViewById(R.id.emptyImageView)


        // Initialize database
        val database = DrawingDatabase.getDatabase(this)

        // Initialize repository and ViewModel
        val repository = DrawingRepository(database.drawingDao())
        viewModel = ViewModelProvider(this, DrawingViewModelFactory(repository)).get(DrawingViewModel::class.java)


        // Initialize RecyclerView and adapter
        recyclerView = findViewById(R.id.drawingsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = DrawingAdapter { drawing ->
            // Handle click on a drawing item
            // For example, navigate to a drawing details activity
            val intent = Intent(this, DrawingDetailsActivity::class.java)
            intent.putExtra("drawing_id", drawing.id)
            startActivityForResult(intent, REQUEST_DRAWING_DETAILS)
        }
        recyclerView.adapter = adapter


        // Add a divider between RecyclerView items
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)

        // Add a button to create a new drawing
        val fab: FloatingActionButton = findViewById(R.id.addDrawingButton)
        fab.setOnClickListener { createNewDrawing() }

        // Call getAllDrawings() to retrieve data for RecyclerView
        viewModel.getAllDrawings()



        viewModel.drawings.observe(this) { drawings ->
            if (drawings.isEmpty()) {
                emptyImageView.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                emptyImageView.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
            adapter.submitList(drawings)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_DRAWING_DETAILS && resultCode == RESULT_OK) {
            val updatedMarkerCount = data?.getIntExtra("marker_count", -1) ?: -1
            val drawingId = data?.getIntExtra("drawing_id", -1) ?: -1
            if (updatedMarkerCount != -1 && drawingId != -1) {
                adapter.updateMarkerCount(drawingId, updatedMarkerCount)
            }
        }
    }



    private fun createNewDrawing() {
        Log.d(TAG, "createNewDrawing() called")
        val fragmentManager = supportFragmentManager
        val addDrawingDialogFragment = AddDrawingDialogFragment()
        addDrawingDialogFragment.show(fragmentManager, "AddDrawingDialogFragment")
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove the observer to prevent memory leaks
        viewModel.drawings.removeObservers(this)
    }


}
