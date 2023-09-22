<h1 align="center">Cocktails</h1>



<p align="center">  
🍸 Cocktails is a application where you could find your favorite cocktails, their details and how to prepare them... Happy drinking🥂🥂
</p>
</br>

<p align="center">
<img src="https://github.com/J-cart/CockTails/assets/82452881/4b807657-27b3-439e-a834-8b1a832fc673" width="252"/>
<img src="https://github.com/J-cart/CockTails/assets/82452881/f35531cf-c8fc-487c-9ec0-5e1d8ddd4657" width="280"/>
<img src="https://github.com/J-cart/CockTails/assets/82452881/4a39de18-53ba-47da-805f-cf25c8e305f4" width="280"/>
</p>

## Download
 Coming Soon .....
<img src="/previews/preview.gif" align="right" width="320"/>

## Tech stack & Open-source libraries
- Minimum SDK level 24
- [Kotlin](https://kotlinlang.org/) based, [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous.
- Jetpack
  - Lifecycle: Observe Android lifecycles and handle UI states upon the lifecycle changes.
  - ViewModel: Manages UI-related data holder and lifecycle aware. Allows data to survive configuration changes such as screen rotations.
  - View Binding: Binds UI components in your layouts to data sources in your app using a declarative format rather than programmatically.
  - Room: Constructs Database by providing an abstraction layer over SQLite to allow fluent database access.
  - [Hilt](https://dagger.dev/hilt/): for dependency injection.
- Architecture
  - MVVM Architecture (View - View Binding - ViewModel - Model)
  - Repository Pattern
- [Retrofit2 & OkHttp3](https://github.com/square/retrofit): Construct the REST APIs and paging network data.
- [Moshi](https://github.com/square/moshi/): A modern JSON library for Kotlin and Java.
- [ksp](https://github.com/google/ksp): Kotlin Symbol Processing API.
- [Material-Components](https://github.com/material-components/material-components-android): Material design components for building ripple animation, and CardView.
- [Coil](https://github.com/coil-kt/coil): Loading images from network.
- **API** 
  - [Cocktails Api](https://www.thecocktaildb.com/api.php)

## Architecture
**Cocktails** is based on the MVVM architecture and the Repository pattern, which follows the [Google's official architecture guidance](https://developer.android.com/topic/architecture).

The overrall architecture of **Cocktails** is composed of three layers; the presentation layer, domain and the data layer. Each layer has dedicated components and they have each different responsibilities, as defined below:

**Cocktails** was built with [Guide to app architecture](https://developer.android.com/topic/architecture), so it would be a great sample to show how the architecture works in real-world projects.


### Architecture Overview

- Each layer follows [unidirectional event/data flow](https://developer.android.com/topic/architecture/ui-layer#udf); the UI layer emits user events to the data layer, and the data layer exposes data as a stream to other layers.
- The data layer is designed to work independently from other layers and must be pure, which means it doesn't have any dependencies on the other layers.

With this loosely coupled architecture, you can increase the reusability of components and scalability of your app.

### UI Layer

The UI layer consists of UI elements to configure screens that could interact with users and [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) that holds app states and restores data when configuration changes.
- UI elements observe the data flow via [View Binding](https://developer.android.com/topic/libraries/view-binding), which is the most essential part of the MVVM architecture. 
- With [View binding](https://developer.android.com/topic/libraries/view-binding), which is an Android DataBinding kit for notifying data changes.

### Data Layer

The data Layer consists of repositories, which include business logic, such as querying data from the local database and requesting remote data from the network and follows the [single source of truth](https://en.wikipedia.org/wiki/Single_source_of_truth) principle.<br>




