package data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "drawings")
data class Drawing(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val imageUri: String,
    val creationTime: Long,
    var markerCount: Int
)

@Entity(tableName = "markers", foreignKeys = [ForeignKey(entity = Drawing::class, parentColumns = ["id"], childColumns = ["drawingId"], onDelete = ForeignKey.CASCADE)])
data class Marker(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val drawingId: Int,
    var title: String,
    val posX: Float,
    val posY: Float,
    val creationTime: Long
)

