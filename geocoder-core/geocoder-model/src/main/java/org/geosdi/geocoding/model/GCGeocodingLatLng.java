/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geosdi.geocoding.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Nazzareno Sileno - CNR IMAA geoSDI Group
 * @email nazzareno.sileno@geosdi.org
 */
/**
 * The latitude of this location.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "geocodingLatLng")
public class GCGeocodingLatLng implements Serializable {

    private static final long serialVersionUID = -7867555074767651501L;

    public double lat;

    /**
     * The longitude of this location.
     */
    public double lng;

    public GCGeocodingLatLng() {
    }

    /**
     * Construct a location with a latitude longitude pair.
     */
    public GCGeocodingLatLng(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

//    @Override
    public String toUrlValue() {
        // Enforce Locale to English for double to string conversion
//        return String.format(Locale.ENGLISH, "%f,%f", lat, lng);
        return "";
    }

    @Override
    public String toString() {
        return "GCGeocodingLatLng{" + "lat=" + lat + ", lng=" + lng + '}';
    }

}
