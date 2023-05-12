package data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DrawingRepository(private val drawingDao: DrawingDao) {
    suspend fun getAllDrawings(): List<Drawing> = withContext(Dispatchers.IO) {
        drawingDao.getAllDrawings()
    }

    suspend fun getDrawingById(drawingId: Int): Drawing = withContext(Dispatchers.IO) {
        drawingDao.getDrawingById(drawingId)
    }

    suspend fun insertDrawing(drawing: Drawing): Long = withContext(Dispatchers.IO) {
        drawingDao.insertDrawing(drawing)
    }

    suspend fun updateDrawing(drawing: Drawing) = withContext(Dispatchers.IO) {
        drawingDao.updateDrawing(drawing)
    }

    suspend fun deleteDrawing(drawingId: Int) = withContext(Dispatchers.IO) {
        drawingDao.deleteDrawing(drawingId)
    }

    suspend fun getMarkersForDrawing(drawingId: Int): List<Marker> = withContext(Dispatchers.IO) {
        drawingDao.getMarkersForDrawing(drawingId)
    }

    suspend fun insertMarker(marker: Marker): Long = withContext(Dispatchers.IO) {
        drawingDao.insertMarker(marker)
    }

    suspend fun updateMarker(marker: Marker) = withContext(Dispatchers.IO) {
        drawingDao.updateMarker(marker)
    }

    suspend fun deleteMarker(markerId: Int) = withContext(Dispatchers.IO) {
        drawingDao.deleteMarker(markerId)
    }

}

