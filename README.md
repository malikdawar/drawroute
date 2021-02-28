DrawRoute

[![Android Arsenal]( https://img.shields.io/badge/Android%20Arsenal-drawroute-green.svg?style=flat )]( https://android-arsenal.com/details/1/8158 )
[![](https://jitpack.io/v/malikdawar/drawroute.svg)](https://jitpack.io/#malikdawar/drawroute)
![GitHub pull requests](https://img.shields.io/github/issues-pr/malikdawar/drawroute)


DrawRoute wraps Google Directions API (https://developers.google.com/maps/documentation/directions/) using RxAndroid for Android. Now developers can easily Draw route on maps, can get the Estimated time of arrival and the Google suggested distance between two locations in a very easy and flexible. 
Note: You need to generate an API key at Google cloud platform also don't forget to enable Directions API. Enjoy!

read more on [medium](https://medium.com/better-programming/introducing-drawroute-a-kotlin-library-for-drawing-routes-on-google-maps-for-android-5e6cc99d58f6)




How to add (gradle)
===========
If you are using gradle:
Step1: Add it in your root build.gradle at the end of repositories

```xml
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

Step2: Add the dependency
```xml
dependencies {
	implementation 'com.github.malikdawar:drawroute:1.4'
}
```
Otherwise you have to use library directly in your project.

USAGE
===========
First we have to download the path. For this we need to provide two points (start and end) and travel mode.


```Kotlin
	val source = LatLng(31.490127, 74.316971) //starting point (LatLng)
        val destination = LatLng(31.474316, 74.316112) // ending point (LatLng)

        googleMap?.run {
            moveCameraOnMap(latLng = source) // if you want to zoom the map to any point

            //Called the drawRouteOnMap extension to draw the polyline/route on google maps
           drawRouteOnMap(
                getString(R.string.google_map_api_key), //your API key
                source = source, // Source from where you want to draw path
                destination = destination, // destination to where you want to draw path
                context = context!! //Activity context
            )
        }

```

If you want more control

```Kotlin
	fun GoogleMap.drawRouteOnMap(
	    mapsApiKey: String,  // YOur API key
	    context: Context, //App context
	    source: LatLng, //Source, from where you want to draw path
	    destination: LatLng,  //Destination, to where you want to draw path
	    color: Int = context.getColorCompat(R.color.pathColor),  //color, path/route/polygon color, specify the color if you want some other color other then default one
	    markers: Boolean = true, //If you want markers on source and destination, by default it is true
	    boundMarkers: Boolean = true, //If you want to bound the markers(start and end points) in screen with padding, by default it is true 
	    polygonWidth: Int = 13, // route/path width, by default it is 13
	    travelMode: TravelMode = TravelMode.DRIVING //Travel mode, by default it is DRIVING
	)

```

```Kotlin
enum class TravelMode {
    DRIVING, WALKING, BICYCLING, TRANSIT
}

```

Route Estimations
=================

(Kotlin)

In your Activity/Fragmant

```kotlin 

class YourFragment : Fragment(), OnMapReadyCallback {

	
	//if you only want the Estimates (Distance & Time of arrival)
        getTravelEstimations(
            mapsApiKey = getString(R.string.google_map_api_key),
            source = source,
            destination = destination
        ) { estimates ->
            estimates?.let {
                //Google Estimated time of arrival
                Log.d(TAG, "ETA: ${it.duration?.text}, ${it.duration?.value}")
                //Google suggested path distance
                Log.d(TAG, "Distance: ${it.distance?.text}, ${it.distance?.text}")

            } ?: Log.e(TAG, "Nothing found")
        }


	//if you only want to draw a route on maps and also need the ETAs
        //Called the drawRouteOnMap extension to draw the polyline/route on google maps
            drawRouteOnMap(
                getString(R.string.google_map_api_key),
                source = source,
                destination = destination,
                context = context!!,
            ) { estimates ->
                estimates?.let {
                    //Google Estimated time of arrival
                    Log.d(TAG, "ETA: ${it.duration?.text}, ${it.duration?.value}")
                    //Google suggested path distance
                    Log.d(TAG, "Distance: ${it.distance?.text}, ${it.distance?.text}")

                } ?: Log.e(TAG, "Nothing found")
            }
  ```  

And taking all together:

(Kotlin)

```Kotlin

class RouteFragment : Fragment(), OnMapReadyCallback {

    private var googleMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map_route, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //initialized google maps
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap?) {
        this.googleMap = p0

        val source = LatLng(31.490127, 74.316971) //starting point (LatLng)
        val destination = LatLng(31.474316, 74.316112) // ending point (LatLng)

        //if you only want the Estimates (Distance & Time of arrival)
        getTravelEstimations(
            mapsApiKey = getString(R.string.google_map_api_key),
            source = source,
            destination = destination
        ) { estimates ->
            estimates?.let {
                //Google Estimated time of arrival
                Log.d(TAG, "ETA: ${it.duration?.text}, ${it.duration?.value}")
                //Google suggested path distance
                Log.d(TAG, "Distance: ${it.distance?.text}, ${it.distance?.text}")

            } ?: Log.e(TAG, "Nothing found")
        }


        googleMap?.run {
            //if you want to move the map on specific location
            moveCameraOnMap(latLng = source)

            //if you want to drop a marker of maps, call it
            drawMarker(location = source, context = requireContext(), title = "test marker")

            //if you only want to draw a route on maps
            //Called the drawRouteOnMap extension to draw the polyline/route on google maps
            drawRouteOnMap(
                getString(R.string.google_map_api_key),
                source = source,
                destination = destination,
                context = context!!
            )

            //if you only want to draw a route on maps and also need the ETAs
            //Called the drawRouteOnMap extension to draw the polyline/route on google maps
            drawRouteOnMap(
                getString(R.string.google_map_api_key),
                source = source,
                destination = destination,
                context = context!!,
            ) { estimates ->
                estimates?.let {
                    //Google Estimated time of arrival
                    Log.d(TAG, "ETA: ${it.duration?.text}, ${it.duration?.value}")
                    //Google suggested path distance
                    Log.d(TAG, "Distance: ${it.distance?.text}, ${it.distance?.text}")

                } ?: Log.e(TAG, "Nothing found")
            }
        }
    }

    companion object {
        var TAG = RouteFragment::javaClass.name
    }
}

```

(Java)
```Java

private final OnMapReadyCallback callback = googleMap -> {

        LatLng source = new LatLng(31.490127, 74.316971); //starting point (LatLng)
        LatLng destination = new LatLng(31.474316, 74.316112); // ending point (LatLng)

        //zoom/move cam on map ready
        MapExtensionKt.moveCameraOnMap(googleMap, 16, true, source);

        //draw route on map
       //If want to draw route on maps and also required the Estimates else just pass the null as a param for lambda
        MapExtensionKt.drawRouteOnMap(googleMap,
                getString(R.string.google_map_api_key),
                getContext(),
                source,
                destination,
                getActivity().getColor(R.color.pathColor),
                true, true, 13, TravelMode.DRIVING, null /*deprecated*/,
                //call the lambda if you need the estimates
                (estimates -> {
                    //Estimated time of arrival
                    Log.d("estimatedTimeOfArrival", "withUnit " + estimates.getDuration().getText());
                    Log.d("estimatedTimeOfArrival", "InMilliSec " + estimates.getDuration().getValue());

                    //Google suggested path distance
                    Log.d("GoogleSuggestedDistance", "withUnit " + estimates.getDistance().getText());
                    Log.d("GoogleSuggestedDistance", "InMilliSec " + estimates.getDistance().getValue());
                    return null;
                }));
	
    };


    For more please refer to the RouteFragment in sample app. Enjoy :)
```



Screen Shot
![image](screenshot/map.jpeg)

Developed By
------------
Malik Dawar - malikdawar332@gmail.com

[Mobin Munir](https://github.com/mmobin789)
