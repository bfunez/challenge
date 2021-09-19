package com.example.challenge.utils

/**
 * sealed class to manage the status of a request
 * @param out T
 */
sealed class Resource<out T>{
    data class Success<out T>(val data : T) : Resource<T>()
    data class Failure(val errorMessage : String) : Resource<Nothing>()
}

/**
 * In line function to access result of the request when successful
 * @receiver Resource<T>
 * @param action Function1<T, Unit>
 * @return Resource<T>
 */
inline fun<T> Resource<T>.onSuccess( action : (T) -> Unit) : Resource<T>{
    if(this is Resource.Success) action(data)
    return this
}

/**
 * In line function to access show an error when not successful
 * @receiver Resource<T>
 * @param action Function1<String, Unit>
 */
inline fun<T> Resource<T>.onFailure( action : (String) -> Unit){
    if(this is Resource.Failure) action(errorMessage)
}
