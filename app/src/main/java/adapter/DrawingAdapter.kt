package adapter

import ViewModel.DrawingViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.doda.R
import data.Drawing

class DrawingAdapter(private val onItemClickListener: (Drawing) -> Unit) :
    ListAdapter<Drawing, DrawingViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Drawing>() {
            override fun areItemsTheSame(oldItem: Drawing, newItem: Drawing): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Drawing, newItem: Drawing): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrawingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_drawing, parent, false)
        return DrawingViewHolder(view)
    }

    override fun onBindViewHolder(holder: DrawingViewHolder, position: Int) {
        val drawing = getItem(position)
        holder.bind(drawing, onItemClickListener)
        holder.itemView.setOnClickListener {
            onItemClickListener(drawing)
        }
    }

    fun getDrawingAt(position: Int): Drawing {
        return getItem(position)
    }

    fun updateMarkerCount(drawingId: Int, markerCount: Int) {
        val position = currentList.indexOfFirst { it.id == drawingId }
        if (position != -1) {
            val drawing = currentList[position]
            drawing.markerCount = markerCount
            notifyItemChanged(position)
        }
    }
}
