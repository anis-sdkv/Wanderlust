package com.wanderlust.data.services

import android.content.Context
import com.patloew.colocation.CoGeocoder
import com.wanderlust.domain.model.LocationAddress
import com.wanderlust.domain.services.GeocoderService
import java.util.Locale

class GeocoderServiceImpl(private val context: Context) : GeocoderService {

    suspend override fun decodeByCoordinates(lat: Double, lon: Double, language: String): LocationAddress {
        val coGeocoder = CoGeocoder.from(context)
        val addresses =
            coGeocoder.getAddressListFromLocation(latitude = lat, longitude = lon, locale = Locale(language))

        val address = addresses.first()
        val country = address.countryName
        val city = address.adminArea
        return LocationAddress(city, country)
    }
}
