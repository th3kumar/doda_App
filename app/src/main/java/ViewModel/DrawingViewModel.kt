package ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.Drawing
import data.DrawingRepository
import data.Marker
import kotlinx.coroutines.launch

class DrawingViewModel(private val repository: DrawingRepository) : ViewModel() {

    private val _drawings = MutableLiveData<List<Drawing>>()
    val drawings: LiveData<List<Drawing>> = _drawings

    private val _markers = MutableLiveData<List<Marker>>()
    val markers: LiveData<List<Marker>> = _markers

    private val _drawing = MutableLiveData<Drawing>()
    val drawing: LiveData<Drawing> = _drawing

    fun getAllDrawings() {
        viewModelScope.launch {
            try {
                val result = repository.getAllDrawings()
                _drawings.postValue(result)
                println("DrawingViewModel: Fetched all drawings successfully")
            } catch (e: Exception) {
                println("DrawingViewModel: Error fetching drawings: ${e.message}")
            }
        }
    }

    fun insertDrawing(drawing: Drawing) {
        viewModelScope.launch {
            try {
                repository.insertDrawing(drawing)
                println("DrawingViewModel: Drawing inserted successfully")
                getAllDrawings() // Fetch the updated list of drawings
            } catch (e: Exception) {
                println("DrawingViewModel: Error inserting drawing: ${e.message}")
            }
        }
    }

    fun updateDrawing(drawing: Drawing) {
        viewModelScope.launch {
            try {
                repository.updateDrawing(drawing)
                println("DrawingViewModel: Drawing updated successfully")
            } catch (e: Exception) {
                println("DrawingViewModel: Error updating drawing: ${e.message}")
            }
        }
    }

    fun deleteDrawing(drawingId: Int) {
        viewModelScope.launch {
            try {
                repository.deleteDrawing(drawingId)
                println("DrawingViewModel: Drawing deleted successfully")
            } catch (e: Exception) {
                println("DrawingViewModel: Error deleting drawing: ${e.message}")
            }
        }
    }

    fun getMarkersForDrawing(drawingId: Int) {
        viewModelScope.launch {
            try {
                val result = repository.getMarkersForDrawing(drawingId)
                _markers.value = result
                println("DrawingViewModel: Fetched markers for drawing successfully")
            } catch (e: Exception) {
                println("DrawingViewModel: Error fetching markers for drawing: ${e.message}")
            }
        }
    }

    fun insertMarker(marker: Marker) {
        viewModelScope.launch {
            try {
                repository.insertMarker(marker)
                println("DrawingViewModel: Marker inserted successfully")
            } catch (e: Exception) {
                println("DrawingViewModel: Error inserting marker: ${e.message}")
            }
        }
    }

    fun updateMarker(marker: Marker) {
        viewModelScope.launch {
            try {
                repository.updateMarker(marker)
                println("DrawingViewModel: Marker updated successfully")
            } catch (e: Exception) {
                println("DrawingViewModel: Error updating marker: ${e.message}")
            }
        }
    }

    fun deleteMarker(markerId: Int) {
        viewModelScope.launch {
            try {
                repository.deleteMarker(markerId)
                println("DrawingViewModel: Marker deleted successfully")
            } catch (e: Exception) {
                println("DrawingViewModel: Error deleting marker: ${e.message}")
            }
        }
    }

    fun getDrawingById(drawingId: Int) {
        viewModelScope.launch {
            try {
                val result = repository.getDrawingById(drawingId)
                _drawing.value = result
                println("DrawingViewModel: Fetched drawing by ID successfully")
            } catch (e: Exception) {
                println("DrawingViewModel: Error fetching drawing by ID: ${e.message}")
            }
        }
    }

    fun saveMarker(marker: Marker) {
        viewModelScope.launch {
            try {
                repository.insertMarker(marker)
                println("DrawingViewModel: Marker inserted successfully")
            } catch (e: Exception) {
                println("DrawingViewModel: Error inserting marker: ${e.message}")
            }
        }
    }

    fun getAllMarkers(drawingId: Int) {
        viewModelScope.launch {
            try {
                val result = repository.getMarkersForDrawing(drawingId)
                _markers.value = result
                println("DrawingViewModel: Fetched markers for drawing successfully")
            } catch (e: Exception) {
                println("DrawingViewModel: Error fetching markers for drawing: ${e.message}")
            }
        }
    }
}


