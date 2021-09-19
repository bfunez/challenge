package com.example.challenge.ui.map

/**
 * Handle view actions for [MapFragment]
 */
sealed class MapFragmentState{
    data class FindPlacesWithQuery( val query : String) : MapFragmentState()
}
