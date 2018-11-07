# Google News
A sample android app that leverages the [News API](https://newsapi.org) to desplays a list of **News** about **Google** sourced from **USA Today**. It also allows you to read/favorite news from the list. See the [News API documentation](https://newsapi.org/docs/endpoints/everything) in case you need more info.

The app is composed of one screen. This screen displays a list of News, in which, each New is described by ites title, description and cover photo. Users allowed to favorite/unfavorite any New. Also they can filter the list so that it containing only those favorite News only.

**News List**

![Imgur](http://funkyimg.com/i/2MUts.png)

## Overview

The app does the following:

1. Get a list of News using [News API](https://newsapi.org/v2/everything).
2. Check if any item in the list is in user's favorite list. 
3. Display the list of News with their cover iamges and details.
4. Show ProgressBar before each network request.
5. Use a view intent to open the News url in browser.

To achieve this, there are three different components in this app:

1. `ApiClient` & `ApiService` & `NewsService` - Responsible for executing the API requests and retrieving the JSON.
2. `MySharedPreference` - Responsible for saving/retrieving data stored in Prefrences.
3. `RVAdapter` - Responsible for mapping each `New` to a particular view layout.

## Running the tests
The app designed using **MVP** architectural pattern, facilitate automated unit testing.

The app tests five different components:

1. `Get data from API sevice test` by mocking the request and check every thing is clear.
2. `Get data from API sevice throwing Exception test` by mocking the request to IOException and check if app handling exceptions.
3. `Sorting By Date test` testing the rule of soring func.
4. `Remove Dupicates test` testing the rule of removing duplicated items.
5. `set up dividers test` testing the rule of adding dividers between items in the list.

## Libraries
Entirely written in [Java](https://www.java.com/en/).
This app leverages two third-party libraries:

* [Gson](https://github.com/google/gson) - Java library that can be used to convert Java Objects into their JSON representation.
* [Retrofit](http://square.github.io/retrofit/) - Type-safe HTTP client for Android and Java by Square.
* [RxJava](https://github.com/ReactiveX/RxJava) - Java VM implementation of [Reactive Extensions](http://reactivex.io/): a library for composing asynchronous and event-based programs by using observable sequences.
* [Okhttp 3](https://github.com/square/okhttp) - An HTTP+HTTP/2 client for Android and Java applications.
* [Picasso](http://square.github.io/picasso/) - Library for image downloading and caching.
* [Mockito](https://github.com/mockito/mockito) - framework for unit tests written in Java. 
