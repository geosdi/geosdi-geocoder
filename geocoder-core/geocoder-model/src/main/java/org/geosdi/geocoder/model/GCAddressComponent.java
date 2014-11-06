/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geosdi.geocoder.model;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Nazzareno Sileno - CNR IMAA geoSDI Group
 * @email nazzareno.sileno@geosdi.org
 */
/**
 * The parts of an address.
 *
 * <p>
 * See
 * <a href="https://developers.google.com/maps/documentation/geocoding/">here
 * for more detail</a>.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "addressComponent")
public class GCAddressComponent implements Serializable {

    private static final long serialVersionUID = 9221131208418408104L;

    public GCAddressComponent() {
    }

    /**
     * {@code longName} is the full text description or name of the address
     * component as returned by the Geocoder.
     */
    public String longName;

    /**
     * {@code shortName} is an abbreviated textual name for the address
     * component, if available. For example, an address component for the state
     * of Alaska may have a longName of "Alaska" and a shortName of "AK" using
     * the 2-letter postal abbreviation.
     */
    public String shortName;

    /**
     * This indicates the type of each part of the address. Examples include
     * street number or country.
     */
    @XmlElementWrapper(name = "typeList")
    @XmlElement(name = "type")
    public List<GCAddressComponentType> types;

    @Override
    public String toString() {
//        String typestring = new String();
//        for (GCAddressComponentType t : types) {
//            typestring += "\n" + t.name();
//        }
        return "GCAddressComponent{" + "longName=" + longName
                + ", shortName=" + shortName + ", types=" + types + '}';
    }

}
