# Product Listing App

This Android app provides an intelligent and efficient way to list, search, and manage products. It utilizes a combination of Jetpack Compose, Firestore, and Room for a seamless user experience with optimal resource usage.

## Features
### Product Listing in LazyColumn:

- Displays all products in a scrollable list using Jetpack Compose’s LazyColumn.
- Each item in the list is clickable, allowing users to update the product’s details such as description and price.
  
   [Activity_file](https://github.com/Ace1032/StoreApp/blob/master/app/src/main/java/com/example/practice3/MainActivity.kt)

### Search Functionality:

- A search bar implemented with TextField allows users to search for products by name.
- The app performs intelligent search queries, even recognizing partial product names, to list all related items.

### Firestore Integration:

- Product data is fetched from Firestore, but only when needed to optimize network resource usage.
- The app intelligently queries Firestore to avoid unnecessary data fetching, reducing data usage and improving performance.

  [RemoteRepository](https://github.com/Ace1032/StoreApp/blob/master/app/src/main/java/com/example/practice3/RemoteRepository.kt)

### Local Storage with Room Database:

- The app uses a Room database to store product data locally.
- It fetches data from Firestore only when needed and relies on the local Room database for all other operations, ensuring efficiency and offline capabilities.

  [LocalDatabase](https://github.com/Ace1032/StoreApp/blob/master/app/src/main/java/com/example/practice3/LocalDatabase.kt)

### Add New Products:

- Users can add new products by clicking the "Add Item" button, which triggers an AlertDialog.
- New products can be entered directly into the dialog and are saved to the Firestore database.

### Edit Product Details:

- Clicking on any product in the list brings up an AlertDialog that allows users to update the product's description and price.

## Architecture
- ViewModel, LiveData, and StateFlow:

 - The app’s UI is powered by a ViewModel, using LiveData and StateFlow to manage UI data efficiently.
 - This architecture ensures that data flows smoothly from the backend to the UI, reacting to changes automatically.

   [viewModel](https://github.com/Ace1032/StoreApp/blob/master/app/src/main/java/com/example/practice3/myViewModelfactory.kt)

### Coroutines for Asynchronous Tasks:

- All background tasks, such as querying Firestore and updating the Room database, are handled asynchronously using Kotlin Coroutines, ensuring the UI remains responsive at all times.

### Singleton Classes for Databases:

 - Both the Firestore and Room databases are implemented as singleton classes at the application level, preventing memory leaks and ensuring consistent data access throughout the app.


## Technology Stack

- Kotlin & Jetpack Compose: For modern UI development.
- Firestore: Remote NoSQL database for product storage and querying.
- Room Database: Local database for offline storage and efficient querying.
- Coroutines: For managing asynchronous tasks and preventing UI blocking.
- ViewModel, LiveData & StateFlow: Ensuring a robust data flow architecture.
- AlertDialog: Used for both adding new products and editing existing product details.

## How It Works
- LazyColumn displays all products fetched from the local Room database. Each product item is clickable for editing.
- TextField at the top allows users to search for products. When the user types in the search field, it intelligently queries the Firestore database and lists matching products.
- Add Product Button opens an AlertDialog to input new product details (name, description, price) and adds it to Firestore.
- Optimized Network Usage: The app fetches product data from Firestore only when necessary and relies on the local Room database for faster, offline access.
- ViewModel Integration: The ViewModel manages all data and ensures that changes in the product list are reflected in the UI through LiveData and StateFlow.

## Installation
 - Clone the repository:
[git clone https://github.com/Ace1032/StoreApp.git]
- Open the project in Android Studio.
- Build and run the app on an emulator or a physical device.

## Future Improvements
- Implement pagination for the product list.
-  Add authentication to allow users to log in and manage their own products.
- Enhance error handling for network operations.
  
Feel free to modify this further as needed! Let me know if you'd like to add more details or make changes.
