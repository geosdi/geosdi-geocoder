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
 * The Adress types. Please see
 * <a href="https://developers.google.com/maps/documentation/geocoding/#Types">Address
 * Types</a> for more detail.
 */
@XmlRootElement(name = "addressType")
public enum GCAddressType implements Serializable {

    /**
     * {@code STREET_ADDRESS} indicates a precise street address.
     */
    STREET_ADDRESS("street_address"),
    /**
     * {@code ROUTE} indicates a named route (such as "US 101").
     */
    ROUTE("route"),
    /**
     * {@code INTERSECTION} indicates a major intersection, usually of two major
     * roads.
     */
    INTERSECTION("intersection"),
    /**
     * {@code POLITICAL} indicates a political entity. Usually, this type
     * indicates a polygon of some civil administration.
     */
    POLITICAL("political"),
    /**
     * {@code COUNTRY} indicates the national political entity, and is typically
     * the highest order type returned by the Geocoder.
     */
    COUNTRY("country"),
    /**
     * {@code ADMINISTRATIVE_AREA_LEVEL_1} indicates a first-order civil entity
     * below the country level. Within the United States, these administrative
     * levels are states. Not all nations exhibit these administrative levels.
     */
    ADMINISTRATIVE_AREA_LEVEL_1("administrative_area_level_1"),
    /**
     * {@code ADMINISTRATIVE_AREA_LEVEL_2} indicates a second-order civil entity
     * below the country level. Within the United States, these administrative
     * levels are counties. Not all nations exhibit these administrative levels.
     */
    ADMINISTRATIVE_AREA_LEVEL_2("administrative_area_level_2"),
    /**
     * {@code ADMINISTRATIVE_AREA_LEVEL_3} indicates a third-order civil entity
     * below the country level. This type indicates a minor civil division. Not
     * all nations exhibit these administrative levels.
     */
    ADMINISTRATIVE_AREA_LEVEL_3("administrative_area_level_3"),
    /**
     * {@code ADMINISTRATIVE_AREA_LEVEL_4} indicates a fourth-order civil entity
     * below the country level. This type indicates a minor civil division. Not
     * all nations exhibit these administrative levels.
     */
    ADMINISTRATIVE_AREA_LEVEL_4("administrative_area_level_4"),
    /**
     * {@code ADMINISTRATIVE_AREA_LEVEL_5} indicates a fifth-order civil entity
     * below the country level. This type indicates a minor civil division. Not
     * all nations exhibit these administrative levels.
     */
    ADMINISTRATIVE_AREA_LEVEL_5("administrative_area_level_5"),
    /**
     * {@code COLLOQUIAL_AREA} indicates a commonly-used alternative name for
     * the entity.
     */
    COLLOQUIAL_AREA("colloquial_area"),
    /**
     * {@code LOCALITY} indicates an incorporated city or town political entity.
     */
    LOCALITY("locality"),
    /**
     * {@code SUBLOCALITY} indicates a first-order civil entity below a
     * locality. For some locations may receive one of the additional types:
     * sublocality_level_1 to sublocality_level_5. Each sublocality level is a
     * civil entity. Larger numbers indicate a smaller geographic area.
     */
    SUBLOCALITY("sublocality"),
    SUBLOCALITY_LEVEL_1("sublocality_level_1"),
    SUBLOCALITY_LEVEL_2("sublocality_level_2"),
    SUBLOCALITY_LEVEL_3("sublocality_level_3"),
    SUBLOCALITY_LEVEL_4("sublocality_level_4"),
    SUBLOCALITY_LEVEL_5("sublocality_level_5"),
    /**
     * {@code NEIGHBORHOOD} indicates a named neighborhood.
     */
    NEIGHBORHOOD("neighborhood"),
    /**
     * {@code PREMISE} indicates a named location, usually a building or
     * collection of buildings with a common name.
     */
    PREMISE("premise"),
    /**
     * {@code SUBPREMISE} indicates a first-order entity below a named location,
     * usually a singular building within a collection of buildings with a
     * common name
     */
    SUBPREMISE("subpremise"),
    /**
     * {@code POSTAL_CODE} indicates a postal code as used to address postal
     * mail within the country.
     */
    POSTAL_CODE("postal_code"),
    /**
     * {@code NATURAL_FEATURE} indicates a prominent natural feature.
     */
    NATURAL_FEATURE("natural_feature"),
    /**
     * {@code AIRPORT} indicates an airport.
     */
    AIRPORT("airport"),
    /**
     * {@code UNIVERSITY} indicates a university.
     */
    UNIVERSITY("university"),
    /**
     * {@code PARK} indicates a named park.
     */
    PARK("park"),
    /**
     * {@code POINT_OF_INTEREST} indicates a named point of interest. Typically,
     * these "POI"s are prominent local entities that don't easily fit in
     * another category, such as "Empire State Building" or "Statue of Liberty."
     */
    POINT_OF_INTEREST("point_of_interest"),
    /**
     * {@code ESTABLISHMENT} typically indicates a place that has not yet been
     * categorized.
     */
    ESTABLISHMENT("establishment"),
    /**
     * {@code BUS_STATION} indicates the location of a bus stop.
     */
    BUS_STATION("bus_station"),
    /**
     * {@code TRAIN_STATION} indicates the location of a train station.
     */
    TRAIN_STATION("train_station"),
    /**
     * {@code TRANSIT_STATION} indicates the location of a transit station.
     */
    TRANSIT_STATION("transit_station"),
    /**
     * Indicates an unknown address type returned by the server. The Java Client
     * for Google Maps Services should be updated to support the new value.
     */
    UNKNOWN("unknown");

    private static Logger log = Logger.getLogger(GCAddressType.class.getName());

    private String addressType;

    private GCAddressType() {
    }

    GCAddressType(String addressType) {
        this.addressType = addressType;
    }

    @Override
    public String toString() {
        return addressType;
    }

//    @Override
    public String toUrlValue() {
        if (this == UNKNOWN) {
            throw new UnsupportedOperationException("Shouldn't use AddressType.UNKNOWN in a request.");
        }
        return addressType;
    }

    public static GCAddressType lookup(String addressType) {
        if (addressType.equalsIgnoreCase(STREET_ADDRESS.toString())) {
            return STREET_ADDRESS;
        } else if (addressType.equalsIgnoreCase(ROUTE.toString())) {
            return ROUTE;
        } else if (addressType.equalsIgnoreCase(INTERSECTION.toString())) {
            return INTERSECTION;
        } else if (addressType.equalsIgnoreCase(POLITICAL.toString())) {
            return POLITICAL;
        } else if (addressType.equalsIgnoreCase(COUNTRY.toString())) {
            return COUNTRY;
        } else if (addressType.equalsIgnoreCase(ADMINISTRATIVE_AREA_LEVEL_1.toString())) {
            return ADMINISTRATIVE_AREA_LEVEL_1;
        } else if (addressType.equalsIgnoreCase(ADMINISTRATIVE_AREA_LEVEL_2.toString())) {
            return ADMINISTRATIVE_AREA_LEVEL_2;
        } else if (addressType.equalsIgnoreCase(ADMINISTRATIVE_AREA_LEVEL_3.toString())) {
            return ADMINISTRATIVE_AREA_LEVEL_3;
        } else if (addressType.equalsIgnoreCase(ADMINISTRATIVE_AREA_LEVEL_4.toString())) {
            return ADMINISTRATIVE_AREA_LEVEL_4;
        } else if (addressType.equalsIgnoreCase(ADMINISTRATIVE_AREA_LEVEL_5.toString())) {
            return ADMINISTRATIVE_AREA_LEVEL_5;
        } else if (addressType.equalsIgnoreCase(COLLOQUIAL_AREA.toString())) {
            return COLLOQUIAL_AREA;
        } else if (addressType.equalsIgnoreCase(LOCALITY.toString())) {
            return LOCALITY;
        } else if (addressType.equalsIgnoreCase(SUBLOCALITY.toString())) {
            return SUBLOCALITY;
        } else if (addressType.equalsIgnoreCase(NEIGHBORHOOD.toString())) {
            return NEIGHBORHOOD;
        } else if (addressType.equalsIgnoreCase(PREMISE.toString())) {
            return PREMISE;
        } else if (addressType.equalsIgnoreCase(SUBPREMISE.toString())) {
            return SUBPREMISE;
        } else if (addressType.equalsIgnoreCase(POSTAL_CODE.toString())) {
            return POSTAL_CODE;
        } else if (addressType.equalsIgnoreCase(NATURAL_FEATURE.toString())) {
            return NATURAL_FEATURE;
        } else if (addressType.equalsIgnoreCase(AIRPORT.toString())) {
            return AIRPORT;
        } else if (addressType.equalsIgnoreCase(UNIVERSITY.toString())) {
            return UNIVERSITY;
        } else if (addressType.equalsIgnoreCase(PARK.toString())) {
            return PARK;
        } else if (addressType.equalsIgnoreCase(POINT_OF_INTEREST.toString())) {
            return POINT_OF_INTEREST;
        } else if (addressType.equalsIgnoreCase(ESTABLISHMENT.toString())) {
            return ESTABLISHMENT;
        } else if (addressType.equalsIgnoreCase(BUS_STATION.toString())) {
            return BUS_STATION;
        } else if (addressType.equalsIgnoreCase(TRAIN_STATION.toString())) {
            return TRAIN_STATION;
        } else if (addressType.equalsIgnoreCase(TRANSIT_STATION.toString())) {
            return TRANSIT_STATION;
        } else if (addressType.equalsIgnoreCase(SUBLOCALITY_LEVEL_1.toString())) {
            return SUBLOCALITY_LEVEL_1;
        } else if (addressType.equalsIgnoreCase(SUBLOCALITY_LEVEL_2.toString())) {
            return SUBLOCALITY_LEVEL_2;
        } else if (addressType.equalsIgnoreCase(SUBLOCALITY_LEVEL_3.toString())) {
            return SUBLOCALITY_LEVEL_3;
        } else if (addressType.equalsIgnoreCase(SUBLOCALITY_LEVEL_4.toString())) {
            return SUBLOCALITY_LEVEL_4;
        } else if (addressType.equalsIgnoreCase(SUBLOCALITY_LEVEL_5.toString())) {
            return SUBLOCALITY_LEVEL_5;
        } else {
//            log.log(Level.WARNING, "Unknown address type '%s'", addressType);
            return UNKNOWN;
        }
    }

}
