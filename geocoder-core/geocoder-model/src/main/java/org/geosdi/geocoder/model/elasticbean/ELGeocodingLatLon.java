/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geosdi.geocoder.model.elasticbean;

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
@XmlRootElement(name = "geocodingLatLon")
public class ELGeocodingLatLon implements Serializable {

    private static final long serialVersionUID = -7867555074767651501L;

    private double lat;

    /**
     * The longitude of this location.
     */
    private double lon;

    public ELGeocodingLatLon() {
    }

    /**
     * Construct a location with a latitude longitude pair.
     */
    public ELGeocodingLatLon(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "GCGeocodingLatLng{" + "lat=" + lat + ", lon=" + lon + '}';
    }

}
