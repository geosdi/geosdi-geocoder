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
 * The north east and south west points that delineate the outer bounds of a
 * map.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "geocodingBounds")
public class GCGeocodingBounds implements Serializable {

    private static final long serialVersionUID = -3892393432680733348L;

    public GCGeocodingLatLng northeast;
    public GCGeocodingLatLng southwest;

    public GCGeocodingBounds() {
    }

    @Override
    public String toString() {
        return "MapLiteGeocodingBounds{" + "northeast=" + northeast + ", southwest=" + southwest + '}';
    }

}
