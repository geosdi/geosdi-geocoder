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
 * The Geometry of a Geocoding Result.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "geocodingGeometry")
public class GCGeocodingGeometry implements Serializable {

    private static final long serialVersionUID = 5171352719154952686L;

    /**
     * {@code bounds} (optionally returned) stores the bounding box which can
     * fully contain the returned result. Note that these bounds may not match
     * the recommended viewport. (For example, San Francisco includes the
     * Farallon islands, which are technically part of the city, but probably
     * should not be returned in the viewport.)
     */
    public GCGeocodingBounds bounds;

    /**
     * {@code location} contains the geocoded {@code latitude,longitude} value.
     * For normal address lookups, this field is typically the most important.
     */
    public GCGeocodingLatLng location;

    /**
     * The level of certainty of this geocoding result.
     */
    public GCLocationType locationType;

    /**
     * {@code viewport} contains the recommended viewport for displaying the
     * returned result. Generally the viewport is used to frame a result when
     * displaying it to a user.
     */
    public GCGeocodingBounds viewport;

    public GCGeocodingGeometry() {
    }

}
