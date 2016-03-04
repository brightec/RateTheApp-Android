# RateTheApp

RateTheApp allows users to rate your app prompting them for further action depending on the rating they gave.

<img src="images/ratetheapp.png" alt="RateTheApp" width="400"/>

```sh
 <uk.co.brightec.ratetheapp.RateTheApp
    xmlns:app="http://schemas.android.com/apk/res-auto"
    app:rateTheAppTitleText="Rate this app?"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    />
```

Depending on the user's rating, an AlertDialog prompts the user for further action.
- If a rating of 0, 1 or 2 stars is selected, the user is prompted whether they would like to email the app developer with any issues they might have.  RateTheApp will remain visible so the user can re-rate your app once their issues have been resolved.

<img src="images/demoapp-lowrating.png" alt="Low Rating" width="400"/>
- If a rating of 3, 4 or 5 is selected, the user is prompted whether they would like to also leave a rating on the Play Store.  After a positive rating, RateTheApp is removed from view so as not to distract the user from using your app.

<img src="images/demoapp-positiverating.png" alt="Positive Rating" width="400"/>

## Changing the appearance

#### Changing the title

The title text and appearance can be changed using the *rateTheAppTitleText* and *rateTheAppTitleTextAppearance* attributes.

<img src="images/demoapp-customisedtitle.png" alt="Changing the title" width="400"/>

```sh
 <uk.co.brightec.ratetheapp.RateTheApp
   xmlns:app="http://schemas.android.com/apk/res-auto"
   android:layout_width="wrap_content"
   android:layout_height="wrap_content"
   android:layout_gravity="center_horizontal"
   app:rateTheAppTitleText="@string/demo1_customised_title"
   app:rateTheAppTitleTextAppearance="@style/Demo.1.TextAppearance"
  />
```

###### Title text defined in strings.xml
```sh
    <string name="demo1_customised_title">Would you like to rate this app?</string>
```

###### Title style defined in styles.xml
```sh
    <style name="Demo.1.TextAppearance" parent="TextAppearance.AppCompat.Subhead">
        <item name="android:textColor">@color/colorPrimaryDark</item>
    </style>
```

#### Removing the title

The title can be removed altogether by setting a blank *rateTheAppTitleText* attribute.

<img src="images/demoapp-notitle.png" alt="No title" width="400"/>

```sh
 <uk.co.brightec.ratetheapp.RateTheApp
   xmlns:app="http://schemas.android.com/apk/res-auto"
   android:layout_width="wrap_content"
   android:layout_height="wrap_content"
   android:layout_gravity="center_horizontal"
   app:rateTheAppTitleText=""
  />
```

#### Changing the star colours

The colour of the stars can be changed using the *rateTheAppSelectedStarColor* and *rateTheAppUnselectedStarColor* attributes.

<img src="images/demoapp-customisedstars.png" alt="Changing the stars" width="400"/>

```sh
 <uk.co.brightec.ratetheapp.RateTheApp
   xmlns:app="http://schemas.android.com/apk/res-auto"
   android:layout_width="wrap_content"
   android:layout_height="wrap_content"
   android:layout_gravity="center_horizontal"
   app:rateTheAppSelectedStarColor="@color/colorPrimaryDark"
   app:rateTheAppUnselectedStarColor="@color/colorPrimary"
  />
```

###### Star colours defined in colors.xml
```sh
    <color name="colorPrimary">#2FBCED</color>
    <color name="colorPrimaryDark">#0091EA</color>
```

## Changing the behaviour

TODO

## Project Structure

This project is made up of 

- an *app folder*  - containing a demo app with code examples showing how to use and customise RateTheApp
- a *ratetheapp folder* - an Android Library containing the RateTheApp Android View

## Installation

Clone this repo and import the *ratetheapp* folder as a Library Module in your Android Studio project.

## Contributors

Feel free to use and improve this project - contributions are welcome.

If you find a problem, please create an issue here (alternatively, fix the code and create a pull request) ;-)

## License

A short snippet describing the license (MIT, Apache, etc.)
