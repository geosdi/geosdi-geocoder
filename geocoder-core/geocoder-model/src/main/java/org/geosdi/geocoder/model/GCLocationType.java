/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geosdi.geocoder.model;

import java.io.Serializable;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Nazzareno Sileno - CNR IMAA geoSDI Group
 * @email nazzareno.sileno@geosdi.org
 */
/**
 * Location types for a reverse geocoding request. Please see
 * <a href="https://developers.google.com/maps/documentation/geocoding/#ReverseGeocoding">for
 * more detail</a>.
 */
@XmlRootElement(name = "locationType")
public enum GCLocationType implements Serializable {

    /**
     * {@code ROOFTOP} restricts the results to addresses for which we have
     * location information accurate down to street address precision.
     */
    ROOFTOP,
    /**
     * {@code RANGE_INTERPOLATED} restricts the results to those that reflect an
     * approximation (usually on a road) interpolated between two precise points
     * (such as intersections). An interpolated range generally indicates that
     * rooftop geocodes are unavailable for a street address.
     */
    RANGE_INTERPOLATED,
    /**
     * {@code GEOMETRIC_CENTER} restricts the results to geometric centers of a
     * location such as a polyline (for example, a street) or polygon (region).
     */
    GEOMETRIC_CENTER,
    /**
     * {@code APPROXIMATE} restricts the results to those that are characterized
     * as approximate.
     */
    APPROXIMATE,
    /**
     * Indicates an unknown location type returned by the server. The Java
     * Client for Google Maps Services should be updated to support the new
     * value.
     */
    UNKNOWN;

    private static Logger log = Logger.getLogger(GCLocationType.class.getName());

//    @Override
    public String toUrlValue() {
        if (this == UNKNOWN) {
            throw new UnsupportedOperationException("Shouldn't use LocationType.UNKNOWN in a request.");
        }
        return toString();
    }

    public GCLocationType lookup(String locationType) {
        if (locationType.equalsIgnoreCase(ROOFTOP.toString())) {
            return ROOFTOP;
        } else if (locationType.equalsIgnoreCase(RANGE_INTERPOLATED.toString())) {
            return RANGE_INTERPOLATED;
        } else if (locationType.equalsIgnoreCase(GEOMETRIC_CENTER.toString())) {
            return GEOMETRIC_CENTER;
        } else if (locationType.equalsIgnoreCase(APPROXIMATE.toString())) {
            return APPROXIMATE;
        } else {
//            log.log(Level.WARNING, "Unknown location type '%s'", locationType);
            return UNKNOWN;
        }
    }
}
