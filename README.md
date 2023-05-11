<div align="center">
  <img src="https://drive.google.com/uc?export=view&id=1E69KrlktI_A5W2QVcp9f3M6hbHO6vaJy" alt="Calculator" style="width: 100px; height: 100px; object-fit: contain; margin-right: 10px;">
  <h1 style="display: inline-block; margin: 0; vertical-align: middle;">Calculator</h1>
</div>

<p align="center">
  <a href="https://kotlinlang.org/"><img src="https://img.shields.io/badge/Kotlin-v1.8.0-blue.svg" alt="Kotlin"></a>
  <a href="https://developer.android.com/about/versions/oreo/android-8.0"><img src="https://img.shields.io/badge/Min%20SDK-24-green.svg" alt="Minimum SDK Version"></a>
 <a href="https://gradle.org/releases/">
  <img src="https://img.shields.io/badge/Gradle-8-red.svg"
       alt="Gradle Version 8"
       style="border-radius: 3px; padding: 2px 6px; background-color: #FF0000; color: #fff;">
</a>
</p>

This app simplifies all the mathematical needs from basic to complex functions.
It allows the user to  save and reference his previous calculations at any time.
Plus, it can easily handle EMI calculations,  compare results, and share them with other apps

<p align="center">
  <a href="https://github.com/yassineAbou"><img alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" height="80px"/></a>
  <a href="https://github.com/yassineAbou"><img alt="Get it on F-Droid" src="https://f-droid.org/badge/get-it-on.png" height="80px"/></a>
</p>

## Screenshots
<div style="display:flex; flex-wrap:wrap;">
  <img src="https://drive.google.com/uc?export=view&id=1vl5EFw15Y93e3yQSfnDcKhwuuKTqY8UA" alt="Normal mode" style="flex:1; margin:5px;" height="450">
  <img src="https://drive.google.com/uc?export=view&id=1q0RvSDkcVkF7nTH7-RRnVoXHFnmdcLsr" alt="Scientific mode" style="flex:1;" height="225">
  <img src="https://drive.google.com/uc?export=view&id=1PCpXI92FGr9_BB1nTrFiHU7sMC6q2lbP" alt="History" style="flex:1;" height="450">
  <img src="https://drive.google.com/uc?export=view&id=1kGDjg9XyKcKJFjz8YrlMkUyT-GekNmsk" alt="Emi calculator" style="flex:1; margin:5px;" height="450">
  <img src="https://drive.google.com/uc?export=view&id=1AvrE-1smc4rDj1ztM7IAT8yXCKqJtNe3" alt="Emi calculation" style="flex:1; margin:5px;" height="450">
  <img src="https://drive.google.com/uc?export=view&id=11qs_glysq1UPWNA0VrBfAr8SNB8nWFqS" alt="Compare" style="flex:1; margin:5px;" height="450">
  <img src="https://drive.google.com/uc?export=view&id=1PMHNiVY9PZBTFEpjGBWx0SnziJk2DCCe" alt="Night theme 1" style="flex:1; margin:5px;" height="450">
  <img src="https://drive.google.com/uc?export=view&id=1TNbIG0QLEgU0j26N-r7L9yd0yxqNRAiu" alt="Night theme 2" style="flex:1; margin:5px;" height="450">
</div>


## Architecture
The architecture of this application relies and complies with the following points below:
* A single-activity architecture, using the [Navigation Components](https://developer.android.com/guide/navigation) to manage fragment operations.
* Pattern [Model-View-ViewModel](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93viewmodel)(MVVM) which facilitates a separation of development of the graphical user interface.
* [Android architecture components](https://developer.android.com/topic/libraries/architecture/) which help to keep the application robust, testable, and maintainable.

<p align="center"><a><img src="https://raw.githubusercontent.com/mayokunthefirst/Instant-Weather/master/media/final-architecture.png" width="700"></a></p>

## Libraries

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
