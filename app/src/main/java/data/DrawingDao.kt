package data

import androidx.room.*

@Dao
interface DrawingDao {
    @Query("SELECT * FROM drawings")
   suspend fun getAllDrawings(): List<Drawing>

 @Query("SELECT * FROM drawings WHERE id = :drawingId")
 suspend fun getDrawingById(drawingId: Int): Drawing

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrawing(drawing: Drawing): Long

    @Update
    suspend fun updateDrawing(drawing: Drawing)

    @Query("DELETE FROM drawings WHERE id = :drawingId")
    suspend fun deleteDrawing(drawingId: Int)

    @Query("SELECT * FROM markers WHERE drawingId = :drawingId")
    suspend fun getMarkersForDrawing(drawingId: Int): List<Marker>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarker(marker: Marker): Long

    @Update
    suspend fun updateMarker(marker: Marker)

    @Query("DELETE FROM markers WHERE id = :markerId")
    suspend fun deleteMarker(markerId: Int)
}
