# ILoveZappos: Cryptocurrency Price Tracker

*Disclaimer: This project is solely used to apply for an internship position at Zappos and is not for commercialization or for-profit purposes.*

## Summary

* Created an android cryptocurrency tracker that is based on data retrieved from Bitstamp.net’s API.
* Displayed a transaction history using an interactive line graph that shows how price for BTC/USD changed in the past 24 hours.
* Used recycler view to display Order Books (Bids and Asks) and JobService to handle price alerts or notifications.
* Managed REST requests using Retrofit and implemented ReactiveX with observer/observables for async data streams.
* *Biggest challenge:* Finishing the application in 2 days total while learning new libraries.
* *Utilized:* Java, Android Studio, JavaRx/AndroidRx, Material Design, Retrofit, mpAndroidChart, Bitstamp.net’s API.


## Table of Contents

<!--ts-->
   * [Summary](#summary)
   * [Table of Contents](#table-of-contents)
   * [Screenshots](#ss)
   * [Process](#process)
      * [Transaction History](#transaction-history)
      * [Order Book](#order-book)
      * [Price Alert](#price-alert)
   * [Installation](#installation)
   * [Dependencies](#dependencies)
<!--te-->

## Screenshots

**Splash Screen**

[![](https://i.imgur.com/GwPV1yo.png)](#)

**Transaction History**

The chart will display the price fluctuations of BTC/USD within the past 24 hours.<br><br>
![Landing Page](/docs/screenshots/chart.jpg?raw=true)<br>

Changing to horizontal view, and toggling the grid and values for each of the breakpoints when the price changes.<br><br>
![Landing Page](/docs/screenshots/chart-toggle.jpg?raw=true)<br>

**Order Book**

Splits the screen in half to display the sorted list of bids and asks. It could be updated in real time but for demostration purposes it is not (Would cause a lot of flickering and activity)<br><br>
![Search Page](/docs/screenshots/book-order.jpg?raw=true)<br>

**Price Alert**

A price alert can be set so that when a price is below the selected amount, it will trigger a notification. By pressing the cancel button, it will reset the notification that was set (Could use a more understandable button label like 'Reset')<br><br>
![Search Page Bottom Pagination](/docs/screenshots/notification.jpg?raw=true)<br>

## Process

The three major requirements for the project were:

1. Transaction History
2. Order Book
3. Price Alert

Each of the requirements used a different Fragment with the purpose of modularizing the different functionalities of the application. This also meant that each Fragment has its own lifecycle. If the number of requirements increase considerably it might be more feasible to consider other alternatives.

The design used for the UI was based on Material Design guidelines and trying to keep consistency throughout the entire application.

### Transaction History

The goal was to have a line graph that would show the price history over time. Since the API used (Bitstamp) limited the number of data to 24 hours or less, the decision was to use the largest possible range and allow the user to zoom into a more detailed view. The library used for the transaction history is called MPAndroidChart, which has its pros and cons. The pros is that the library contains most of the essential features needed to graph a line bar. The most notable con is how limiting some of the customization features can be, for example there is a limit to how much the chart lines can be smoothed for a better visualization of data. Additionally, it does not manage very well large quantities of data or points in the X axis, and it will not adjust automatically the amount of data according to the level of zoom (granularity).

### Order Book

The order book uses 2 tables displayed on RecyclerViews and shows the most recent information retrieved from the book order API from Bitstamp. It doesn't automatically refresh the data as it could become very hectic for the user as all the transactions move extremely quick.

### Price Alert

The price alert for the user can be set by inputting the desired value on the text field. After setting up the alert, the application can be closed and the notification will be triggered regardless. This is thanks to using Android's JobService, which is a class that handles asynchronous requests that were scheduled. 

## Installation

Click [here](/docs/CryptocurrencyTracker.apk?raw=true) to download the APK

## Dependencies

Minimum SDK Version: 26

* Retrofit 2.7.1
* RxJava 3.0.0
* RxAndroid 2.1.1
* RecyclerView
* MPAndroidChart 3.1.0
* Bitstamp API (https://www.bitstamp.net/api/)

