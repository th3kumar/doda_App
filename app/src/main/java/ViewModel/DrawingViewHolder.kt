package ViewModel

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doda.R
import data.Drawing
import java.text.SimpleDateFormat
import java.util.*

class DrawingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val ivDrawing: ImageView = itemView.findViewById(R.id.imageView)
    private val tvTitle: TextView = itemView.findViewById(R.id.titleTextView)
    private val tvCreationTime: TextView = itemView.findViewById(R.id.createdTextView)
    private val tvMarkerCount: TextView = itemView.findViewById(R.id.markersTextView)

    fun bind(drawing: Drawing, onItemClickListener: (Drawing) -> Unit) {
        tvTitle.text = drawing.title

        val sdf = SimpleDateFormat("MMM dd, yyyy 'at' h:mm a", Locale.getDefault())
        val date = Date(drawing.creationTime)
        tvCreationTime.text = sdf.format(date)

        tvMarkerCount.text = "${drawing.markerCount} markers"

        Glide.with(itemView.context)
            .load(drawing.imageUri)
            .centerCrop()
            .into(ivDrawing)

        itemView.setOnClickListener { onItemClickListener(drawing) }
    }
}
