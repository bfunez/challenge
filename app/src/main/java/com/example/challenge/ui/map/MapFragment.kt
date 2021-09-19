package com.example.challenge.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.challenge.R
import com.example.challenge.databinding.MapFragmentBinding
import com.example.challenge.ui.base.BaseFragment
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.*

class MapFragment : BaseFragment<MapFragmentBinding>(
    R.layout.map_fragment
), OnMapReadyCallback {
    companion object {
        const val MY_LOCATION_ZOOM = 16.0F
        const val DEFAULT_ZOOM = 1.0F
        const val LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION
    }

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var placesClient: PlacesClient
    private val viewModel = MapViewModel()
    private lateinit var userLocation : LatLng
    private lateinit var dialog : Dialog
    private val permissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            when(it){
                true -> {
                    startLocationsActions()
                }
                false ->{
                    showInformativeDialog(R.string.permissions_denied_label, R.string.permission_denied_message, R.string.ok_label){}
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        placesClient = Places.createClient(requireActivity())
        initFusedLocationService()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, ::onViewEvent)
    }

    /**
     * Check if the location permissions is granted
     * @return Boolean
     */
    private fun checkIfLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireActivity(),
            LOCATION_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun startLocationsActions(){
        setupLocationListener()
        if (checkIfLocationEnabled()) {
            println("permission called 3")
            googleMap.isMyLocationEnabled = true
            setUserInitialLocation()
        } else {
            showLocationServicesDisableDialog()
        }
    }

    @SuppressLint("MissingPermission")
    private fun grantLocationPermission() {
        println("permission called 1")
        if (checkIfLocationPermissionGranted()) {
            println("permission 2")
            startLocationsActions()
        } else {
            permissionRequest.launch(LOCATION_PERMISSION)
        }

    }

    override fun onResume() {
        super.onResume()
        //if a dialog is visible when resumed then hide it
        if(::dialog.isInitialized){
            dialog.dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        if(::googleMap.isInitialized) {
            grantLocationPermission()
        }
    }

    override fun onPause() {
        super.onPause()
        if (::locationCallback.isInitialized) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }


    @SuppressLint("MissingPermission")
    private fun initFusedLocationService() {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationProviderClient.lastLocation

    }


    /**
     * Function to initialize the map View
     */
    private fun setupMapFragment() {
        mapFragment = SupportMapFragment()
        childFragmentManager.beginTransaction().add(viewBinding.mapView.id, mapFragment).commit()
        mapFragment.getMapAsync(this)
    }

    /**
     * actions to execute right after setting bindings
     */
    override fun onInitDataBinding() {
        setupMapFragment()
        viewBinding.viewModel = viewModel
    }

    /**
     * Override to setup the Map, also requesting location permissions
     * @param map GoogleMap
     */
    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        println("call this function")
        grantLocationPermission()
    }

    /**
     * setup the fusedclient location callback
     */
    @SuppressLint("MissingPermission")
    private fun setupLocationListener() {
        locationCallback = object : LocationCallback() {}
        val locationRequest = LocationRequest.create().apply {
            interval = 2000
            fastestInterval = 2000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            isWaitForAccurateLocation = true
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }

    /**
     * function to ask fusedLocationProviderClient to
     * get the users location and send it to setUpUserLocationMarker
     */
    @SuppressLint("MissingPermission")
    private fun setUserInitialLocation() {
        fusedLocationProviderClient.lastLocation.addOnCompleteListener { location ->
            if (location.isSuccessful && location.result != null) {
                println("set user initial location")
                setUpUserLocationMarker(location.result)
            } else {
                println("set user initial location false")
                setUserInitialLocation()
            }

        }

    }

    /**
     * Function to setup the marker and zoom to user initial location
     * @param location Location
     */
    private fun setUpUserLocationMarker(location: Location) {
        googleMap.apply {
            googleMap.clear()
            userLocation = LatLng(location.latitude, location.longitude)
            addMarker(
                MarkerOptions().position(userLocation)
                    .title(requireContext().getString(R.string.you_are_here))
                    .draggable(false)
            )
            moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, MY_LOCATION_ZOOM))
        }
    }

    /**
     * Function to check if location services are enabled
     * @return Boolean
     */
    private fun checkIfLocationEnabled(): Boolean {
        val locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    /**
     * Function to show a Dialog if the user has location services disabled
     */
    private fun showLocationServicesDisableDialog() {
        showInformativeDialog(R.string.enable_location_services,R.string.enable_location_services_message,R.string.enable_now){
            requireContext().startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }

    /**
     * function to show a message with params to:
     * @param title Int show the title
     * @param message Int show the message
     * @param positiveButtonLabel Int the ok button label
     * @param action Function0<Unit> an action to happen when the user clicks ok
     */
    private fun showInformativeDialog(title : Int, message : Int , positiveButtonLabel : Int, action : () -> Unit ) {
        dialog = AlertDialog.Builder(requireContext())
            .setTitle(requireContext().getString(title))
            .setCancelable(false)
            .setMessage(requireContext().getString(message))
            .setPositiveButton(requireContext().getString(positiveButtonLabel)) { dialog, _ ->
                action()
                dialog.dismiss()
            }
            .create()
        dialog.show()
    }

    /**
     * Function to show a Dialog if no places are found
     */
    private fun showNoPlacesFoundError() {
        showInformativeDialog(R.string.no_place_found, R.string.no_place_found_message, R.string.ok_label){}
    }

    /**
     * Function to show a Dialog if query is empty
     */
    private fun showEmptyQueryError() {
        showInformativeDialog(R.string.error_query_empty, R.string.empty_query_message, R.string.ok_label){}
    }

    /**
     * Function to look for locations predictions
     * inspired on the developers documentation from google
     * locking places to Costa Rica USA and Honduras
     * @see (https://developers.google.com/maps/documentation/places/android-sdk/autocomplete?hl=es_419#maps_places_programmatic_place_predictions-kotlin)
     */
    private fun findPlacesWithText(query: String) {
        googleMap.clear() // clear all markers from the map
        val token = AutocompleteSessionToken.newInstance()

        val origin  = if (::userLocation.isInitialized) userLocation else null
        val request = FindAutocompletePredictionsRequest.builder()
            .setTypeFilter(TypeFilter.ESTABLISHMENT) // Searching by place name (ESTABLISHMENT), can be changed to ADDRESS
            .setSessionToken(token)
            .setQuery(query)
            .setOrigin(origin)
            .setCountries("US", "CR", "HN") //add more if needed
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                if(response.autocompletePredictions.size > 0) {
                    for (prediction in response.autocompletePredictions) {
                        findPlaceDetails(prediction.placeId)
                    }
                }else{
                    showNoPlacesFoundError()
                }
            }
            .addOnFailureListener {
                showNoPlacesFoundError()
            }
        viewModel.stopLoading()

    }

    /**
     * Function to find place details and draw a marker on the map
     * from doc https://developers.google.com/maps/documentation/places/android-sdk/place-details?hl=es_419#maps_places_get_place_by_id-kotlin
     * @param placeId String
     */
    private fun findPlaceDetails(placeId : String) {

        val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)


        val request = FetchPlaceRequest.newInstance(placeId, placeFields)

        placesClient.fetchPlace(request)
            .addOnSuccessListener { response: FetchPlaceResponse ->
                val place = response.place

                googleMap.apply {
                    place.latLng?.let { latLong ->
                        addMarker(
                            MarkerOptions().position(latLong)
                                .title(place.name ?: "")
                                .draggable(false)
                        )
                        moveCamera(CameraUpdateFactory.newLatLngZoom(latLong, DEFAULT_ZOOM))
                    }
                }
            }
    }

    /**
     * Function to observe the changes on the viewModel state [MapFragmentState]
     * @param viewEvent MapFragmentState
     */
    private fun onViewEvent(viewEvent: MapFragmentState) =
        when (viewEvent) {
            is MapFragmentState.FindPlacesWithQuery -> {
                if(viewEvent.query.isEmpty()){
                    showEmptyQueryError()
                }else {
                    findPlacesWithText(viewEvent.query)
                }
            }
        }


}