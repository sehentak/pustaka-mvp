package com.sehentak.base.model

import com.google.gson.annotations.SerializedName

/**
 * Information about the current device info
 *
 * @author  Angger Prasetyo
 * @since   2019 Aug 9
 */
 
data class LocationMdl(

    /**
     * Get the estimated horizontal accuracy of this location, radial in meters.
     * Does not indicate the accuracy of bearing, velocity or altitude
     *
     * @return  Float - If this location does not have a horizontal accuracy,
     *          then 0.0 is returned.
     * @see     android.location.Location.getAccuracy
     */
    @SerializedName("accuracy")
    var accuracy: Float? = null,

    /**
     * If the location came from a mock provider.
     * Build version must be higher than JELLY BEAN MR2 (18)
     *
     * @since   Android version 18
     * @return  Boolean - true if this Location came from a mock provider,
     *          false otherwise
     * @see     android.location.Location.isFromMockProvider
     */
    @SerializedName("fromMockProvider")
    var isFromMockProvider: Boolean? = null,

    /**
     * Get the latitude, in degrees.
     *
     * @return  Double - generate a valid latitude
     * @see     android.location.Location.getLatitude
     */
    @SerializedName("latitude")
    var latitude: Double? = null,

    /**
     * Get the longitude, in degrees.
     *
     * @return  Double - generate a valid longitude
     * @see     android.location.Location.getLongitude
     */
    @SerializedName("longitude")
    var longitude: Double? = null,

    @SerializedName("address")
    var address: String? = null
)