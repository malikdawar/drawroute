RouteDrawer
===========

RouteDrawer wraps Google Directions API (https://developers.google.com/maps/documentation/directions/) using RxJava for Android so developers can download, parse and draw path on the map in very fast and flexible way (For now only JSON support).

[![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0) 
The library contains two main parts.

 - RouteApi
    is responsible for sending request to Google's Direction API and handling the response

 - DrawerApi
    is responsible for drawing the path on the map



USAGE
===========
First we have to download the path. For this we need to provide two points (start and end) and travel mode.


```Kotlin
interface RouteApi {
	fun getJsonDirections(
		start: LatLng,
		end: LatLng,
		mode: TravelMode,
		apiKey: String
	): Observable<String?>?
}

```

Where travel mode can be:

```Kotlin
enum class TravelMode {
	DRIVING, WALKING, BICYCLING, TRANSIT
}

```

As you can see the above method returns Observable and our response is a String.
So far so good, we downloaded the route but what the hell - response as String, I don't want to parse it on my own.

With RxJava and some transformations nothing more easily.

Have a look:

```Kotlin
val routeRest = RouteRest()
	routeRest.getJsonDirections(
		source, destination, //starting and ending point
		TravelMode.DRIVING, //Travel mode
		"Your api key" //google maps API from GCP, make sure google directions are enabled
	)
		?.observeOn(AndroidSchedulers.mainThread())
		?.map { s -> RouteJsonParser<Routes>().parse(s, Routes::class.java) }
		?.subscribe { r -> routeDrawer.drawPath(r) }
		
```

The most important part here is

```Kotlin
 .map { 
	s -> RouteJsonParser<Routes>().parse(s, Routes::class.java) 
 }
 
```

For more details about 'map' operator can be find here - https://github.com/ReactiveX/RxJava/wiki/Transforming-Observables#map
In short, we parse our response to Routes object, so now we can go to draw the path on the map.


Here we have to use DrawerApi which for now provides one method:
```Kotlin
fun drawPath(Routes routes);
```
(for now it forces to use Routes object).

We are almost there but before we invoke draw method we have to build our drawer using RouteDrawerBuilder.
It allows us to customize a little bit the path and the markers. It requires to get GoogleMap(!) and if we want we can provide
```
- marker icon
- path width
- path color
- marker alpha
```

This can look as

```Kotlin
    val routeDrawer = RouteDrawer.RouteDrawerBuilder(googleMap)
            .withColor(context!!.getColorCompat(R.color.colorPrimary))
            .withWidth(13)
            .withAlpha(0.6f)
            .withMarkerIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
            .build()
```


And taking all together:

```Kotlin

class YourFragment : Fragment(), OnMapReadyCallback {

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
	
		//check if map has been initialized
        googleMap?.run {
            //Called the drawRouteOnMap method to draw the polyline/route on google maps
			
            //creation of polyline with attributes
        val routeDrawer = RouteDrawer.RouteDrawerBuilder(this)
            .withColor(context!!.getColorCompat(R.color.colorPrimary))
            .withWidth(13)
            .withAlpha(0.6f)
            .withMarkerIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
            .build()

        //API call to get the path points from google
        val routeRest = RouteRest()
        routeRest.getJsonDirections(
            source, destination, //starting and ending point
            TravelMode.DRIVING, //Travel mode
            "Your api key" //google maps API from GCP, make sure google directions are enabled
        )
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.map { s -> RouteJsonParser<Routes>().parse(s, Routes::class.java) }
            ?.subscribe { r -> routeDrawer.drawPath(r) }
        }
    }
}
```

Developed By
------------
Malik Dawar - malidawar332@gmail.com

License
----------


Copyright 2020 Malik Dawar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```





