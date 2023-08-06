package io.directional.wine.controller


data class JSONData<T> (
    var success: Boolean = false,
    var data : T? = null
);