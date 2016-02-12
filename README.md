# RateTheApp

RateTheApp is a custom Android View which allows users to rate your app.

```sh
 <uk.co.brightec.ratetheapp.RateTheApp
    xmlns:app="http://schemas.android.com/apk/res-auto"
    app:rateTheAppTitleText="How would you rate this demo?"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_gravity="bottom"
    android:paddingBottom="16dp"
    />
```

RateTheApp is customisable but out-of-the-box displays a 5-star rating control and depending on what rating the user gives, does one of two things:
- If a rating of 0, 1 or 2 stars is selected, the user is prompted whether they would like to email the app developer with any issues they might have.  
- If a rating of 3, 4 or 5 is selected, the user is prompted whether they would like to also leave a rating on the Play Store.

## Project Structure

This project is made up of 
- a *ratetheapp folder* - an Android Library containing the custom View and
- an *app folder* a demo app showing what the RateTheApp looks like and how you can use it and customise it.

## Customising

```sh
 <uk.co.brightec.ratetheapp.RateTheApp
   xmlns:app="http://schemas.android.com/apk/res-auto"
   app:rateTheAppName="customisedTitleWidget"
   android:layout_width="wrap_content"
   android:layout_height="wrap_content"
   android:layout_gravity="center_horizontal"
   app:rateTheAppTitleText="@string/demo1_customised_title"
   app:rateTheAppTitleTextAppearance="@style/Demo.1.TextAppearance"
   app:rateTheAppSelectedStarColor="@color/colorPrimaryDark"
   app:rateTheAppUnselectedStarColor="@color/colorPrimary"
  />
```

## Installation

Clone this repo and add to your Android Studio project as a Library Module.

## Contributors

Feel free to use and improve this project - contributions are welcome.

If you find a problem, please create an issue here (alternatively, fix the code and create a pull request) ;-)

## License

A short snippet describing the license (MIT, Apache, etc.)
