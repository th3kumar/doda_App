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

The app follows the Model-View-ViewModel (MVVM) architecture pattern, which separates the app's logic into distinct layers:

- Model: Represents the data and business logic of the app, including the entities and database operations.
- View: Handles the UI and user interactions, displaying data from the ViewModel and forwarding user actions to it.
- ViewModel: Manages the state of the UI, retrieves and provides data to the View, and handles user interactions.

The architecture allows for better separation of concerns, testability, and maintainability of the codebase.

## Getting Started

To build and run the app locally, follow these steps:

1. Clone the repository: `git clone https://github.com/your-username/drawing-app.git`
2. Open the project in Android Studio.
3. Build and run the app on an emulator or a physical device.

## Contributing

Contributions to the project are welcome! If you have any ideas, suggestions, or bug reports, please open an issue or submit a pull request.

## License

This project is licensed under the [MIT License](LICENSE).


