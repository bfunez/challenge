package com.example.challenge.ui.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.challenge.utils.SingleLiveEvent

/**
 *Handle UI related data for [MapFragment]
 * @property isLoading MutableLiveData<(Boolean..Boolean?)> handle loading state
 * @property state SingleLiveEvent<MapFragmentState>  handle fragment events
 * @property query MutableLiveData<(String..String?)> two way binding with the query textInput
 */
class MapViewModel : ViewModel() {
    val isLoading = MutableLiveData(false)
    val state =  SingleLiveEvent<MapFragmentState>()
    val query = MutableLiveData("")

    fun findPlacesPredictions(query : String){
        startLoading()
        state.postValue(MapFragmentState.FindPlacesWithQuery(query))
    }

    fun stopLoading(){
        isLoading.postValue(false)
    }


    fun startLoading(){
        isLoading.postValue(false)
    }
}