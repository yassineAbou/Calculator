<div align="center"> 
 <img src="https://i.imgur.com/hVfvB0H.png" alt="Calculator" style="width: 100px; height: 100px; object-fit: contain; margin-right: 10px;">
  <h1 style="display: inline-block; margin: 0; vertical-align: middle;">Calculator</h1>
</div>

<p align="center">
  <a href="https://kotlinlang.org/">
    <img src="https://img.shields.io/badge/Kotlin-1.8.21-blue?logo=kotlin" alt="Kotlin version 1.8.21">
  </a>
  <a href="https://gradle.org/releases/">
    <img src="https://img.shields.io/badge/Gradle-8.0.2-white?logo=gradle" alt="Gradle version 8.0.2">
  </a>
  <a href="https://developer.android.com/studio">
    <img src="https://img.shields.io/badge/Android%20Studio-Flamingo-orange?logo=android-studio" alt="Android Studio Flamingo">
  </a>
  <a href="https://developer.android.com/studio/releases/platforms#7.0">
    <img src="https://img.shields.io/badge/Android%20min%20version-7-brightgreen?logo=android" alt="Android min version 7">
</a>
</p>

This app simplifies all the mathematical needs from basic to complex functions.
It allows the user to  save and reference his previous calculations at any time.
Plus, it can easily handle EMI calculations,  compare results, and share them with other apps


<p align="center">
  <a href="https://play.google.com/store/apps/details?id=com.yassineabou.calculator"><img alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" height="80px"/></a>
</p>

## Screenshots

<div style="display:flex; flex-wrap:wrap;">
  <img src="https://i.imgur.com/7WHkTHI.jpg" style="flex:1; margin:5px;" height="450">
  <img src="https://i.imgur.com/uSw9JmW.jpg" style="flex:1; margin:5px;" height="225">
  <img src="https://i.imgur.com/ZTUjq7h.jpg" style="flex:1; margin:5px;" height="450">
 <img src="https://i.imgur.com/wHxuU7S.jpg" style="flex:1; margin:5px;" height="450">
  <img src="https://i.imgur.com/jSxeAb0.jpg" style="flex:1; margin:5px;" height="450">
  <img src="https://i.imgur.com/eYofXob.jpg" style="flex:1; margin:5px;" height="450">
  <img src="https://i.imgur.com/fQHjr71.jpg" style="flex:1; margin:5px;" height="450">
  <img src="https://i.imgur.com/AXxQt2e.jpg" style="flex:1; margin:5px;" height="450">
</div>

## Architecture
The architecture of this application relies and complies with the following points below:
* A single-activity architecture, using the [Navigation Components](https://developer.android.com/guide/navigation) to manage fragment operations.
* Pattern [Model-View-ViewModel](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93viewmodel)(MVVM) which facilitates a separation of development of the graphical user interface.
* [Android architecture components](https://developer.android.com/topic/libraries/architecture/) which help to keep the application robust, testable, and maintainable.

<p align="center"><a><img src="https://raw.githubusercontent.com/mayokunthefirst/Instant-Weather/master/media/final-architecture.png" width="700"></a></p>

## Package Structure
```  
com.yassineabou.calculator  # Root Package
├── data                    # For handling all data operations
│   ├── local               # For handling data on a local level
│   ├── model               # Contains data classes/structures
│   └── repository          # For accessing and managing data
|  
├── di                      # Dependency Injection: Contains the dependency injection setup
│  
├── ui                      # UI Layer: Activities, Fragments, ViewModels
│   ├── calculator          # Contains logic and UI for the calculator
│   ├── emi                 # Contains logic and UI for the EMI calculator
│   └── MainActivity        # The main activity which hosts the other UI elements
|  
├── utils                   # Contains utility classes and Kotlin extensions
└── CalculatorApplication   # The application class


```  

## Built With 🧰

- [Baseline Profiles](https://developer.android.com/topic/performance/baselineprofiles/overview) (Performance optimization tool)
- [Navigation](https://developer.android.com/topic/libraries/architecture/navigation) (Fragment transitions)
- [Data Binding](https://developer.android.com/topic/libraries/data-binding) (Bind views)
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) (Store and manage UI-related data)
- [Kotlin Coroutine](https://github.com/Kotlin/kotlinx.coroutines) (Light-weight threads)
- [Hilt](https://dagger.dev/hilt/) (Dependency Injection for Android)
- [Room](https://developer.android.com/topic/libraries/architecture/room) (Abstraction layer over SQLite)
- [Sdp](https://github.com/intuit/sdp) (Scalable size unit)
- [Ssp](https://github.com/intuit/ssp) (Scalable size unit for texts)
-  [MxParser](https://github.com/mariuszgromada/MathParser.org-mXparser) (Math expression parser)

## Contribution
We welcome contributions to our project! Please follow these guidelines when submitting changes:

-   Report bugs and feature requests by creating an issue on our GitHub repository.
-   Contribute code changes by forking the repository and creating a new branch.
-   Ensure your code follows our coding conventions.
-   Improve our documentation by submitting changes as a pull request.

Thank you for your interest in contributing to our project!

## License
```
Copyright 2023 Yassine Abou 
  
Licensed under the Apache License, Version 2.0 (the "License");  
you may not use this file except in compliance with the License.  
You may obtain a copy of the License at  
  
    http://www.apache.org/licenses/LICENSE-2.0  
  
Unless required by applicable law or agreed to in writing, software  
distributed under the License is distributed on an "AS IS" BASIS,  
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  
See the License for the specific language governing permissions and  
limitations under the License.