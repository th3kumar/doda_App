# Drawing App



This is a drawing app that allows users to create and manage drawings. Users can create new drawings, add markers to the drawings, and view details of each drawing.

## Features

- Create new drawings with a custom title and image
- Add markers to the drawings, specifying the marker position and title
- View the list of drawings with their respective marker counts
- Click on a drawing to view its details, including the markers
- Update the marker count of a drawing from the details screen

## Technologies Used

- Kotlin: Programming language used for the app development
- Android Jetpack: Collection of Android libraries and tools for app development
  - ViewModel: Architecture component for managing and handling UI-related data
  - LiveData: Observable data holder used for communication between components
  - Room: Database library for data persistence
  - RecyclerView: Component for displaying a scrollable list of items
- SQLite: Database used for storing the drawings and markers data
- Glide: Image loading library for loading and displaying images efficiently
- Material Design Components: UI components and design guidelines for a modern and intuitive user experience

## Architecture
The app follows the Model-View-ViewModel (MVVM) architecture pattern. The key components of the architecture are:

#Model
-The data layer of the app that includes entities, data access objects (DAOs), and the database.

-Entities: The data models that represent the core data structures in the app. In this project, we have two entities: Drawing and Marker.

```kotlin
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


## Getting Started

To build and run the app locally, follow these steps:

1. Clone the repository: `git clone https://github.com/your-username/drawing-app.git`
2. Open the project in Android Studio.
3. Build and run the app on an emulator or a physical device.

## Contributing

Contributions to the project are welcome! If you have any ideas, suggestions, or bug reports, please open an issue or submit a pull request.

## License

This project is licensed under the [MIT License](LICENSE).


