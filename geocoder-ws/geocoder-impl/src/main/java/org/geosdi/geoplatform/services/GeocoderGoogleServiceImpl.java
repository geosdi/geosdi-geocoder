/*
 *  geo-platform
 *  Rich webgis framework
 *  http://geo-platform.org
 * ====================================================================
 *
 * Copyright (C) 2008-2013 geoSDI Group (CNR IMAA - Potenza - ITALY).
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version. This program is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details. You should have received a copy of the GNU General
 * Public License along with this program. If not, see http://www.gnu.org/licenses/
 *
 * ====================================================================
 *
 * Linking this library statically or dynamically with other modules is
 * making a combined work based on this library. Thus, the terms and
 * conditions of the GNU General Public License cover the whole combination.
 *
 * As a special exception, the copyright holders of this library give you permission
 * to link this library with independent modules to produce an executable, regardless
 * of the license terms of these independent modules, and to copy and distribute
 * the resulting executable under terms of your choice, provided that you also meet,
 * for each linked independent module, the terms and conditions of the license of
 * that module. An independent module is a module which is not derived from or
 * based on this library. If you modify this library, you may extend this exception
 * to your version of the library, but you are not obligated to do so. If you do not
 * wish to do so, delete this exception statement from your version.
 *
 */
package org.geosdi.geoplatform.services;

import com.google.common.collect.Lists;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.AddressType;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.Geometry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.geosdi.geocoder.cache.GeocoderElCache;
import org.geosdi.geocoder.model.GCAddressComponent;
import org.geosdi.geocoder.model.GCAddressComponentType;
import org.geosdi.geocoder.model.GCAddressType;
import org.geosdi.geocoder.model.GCGeocodingBounds;
import org.geosdi.geocoder.model.GCGeocodingGeometry;
import org.geosdi.geocoder.model.elasticbean.ELGeocodingLatLon;
import org.geosdi.geocoder.model.GCGeocodingResult;
import org.geosdi.geocoder.model.GCLocationType;
import org.geosdi.geocoder.model.elasticbean.ELGeocodingBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GeocodingGoogle service delegate.
 *
 * @author Nazzareno Sileno - CNR IMAA geoSDI Group
 * @email nazzareno.sileno@geosdi.org
 */
class GeocoderGoogleServiceImpl {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private GeocoderElCache geocoderElCache;

    private GeoApiContext context = new GeoApiContext().
            setApiKey("AIzaSyAYwt6RZz7ATdEBel61EUABVww9l8gPaJE");

    public List<ELGeocodingBean> suggestGeocodingForAddress(String address) {
        return geocoderElCache.suggestForAddressResults(address);
    }

    public List<GCGeocodingResult> executeGeocoderByAddress(String queryAddress, String language) {
        GeocodingApiRequest req = GeocodingApi.newRequest(context).address(queryAddress).language(language);
        List<GCGeocodingResult> gcResults = null;
        List<GeocodingResult> geocodingResults = null;
        try {
            logger.debug("Executing geocode: ");
            GeocodingResult[] result = req.await();
            logger.debug(" GeocodingResult[] result: " + result);
            geocodingResults = Arrays.asList(result);
            logger.debug("Arrays.asList geocodingResults: " + geocodingResults);
            gcResults = this.convertGeocodingResult(result);
            logger.debug("this.convertGeocodingResult Executing geocode: " + gcResults);
            this.geocoderElCache.persistGeocodingResults(geocodingResults, language, queryAddress);
        } catch (Exception e) {
            logger.error("Error executing geocode: " + e.getCause().toString());
        }
        return gcResults;
    }

    private List<GCAddressType> converAddressComponentType(AddressType[] ats) {
        List<GCAddressType> mapLiteAT = new ArrayList<GCAddressType>(ats.length);
        for (AddressType at : ats) {
            mapLiteAT.add(GCAddressType.valueOf(at.name()));
        }
        return mapLiteAT;
    }

    private List<GCAddressComponentType> converAddressComponentType(AddressComponentType[] acts) {
        List<GCAddressComponentType> mapLiteACT = new ArrayList<GCAddressComponentType>(acts.length);
        for (AddressComponentType act : acts) {
            mapLiteACT.add(GCAddressComponentType.valueOf(act.name()));
        }
        return mapLiteACT;
    }

    private List<GCAddressComponent> converAddressComponent(AddressComponent[] acs) {
        List<GCAddressComponent> mapLiteAddressComponents = new ArrayList<GCAddressComponent>(acs.length);
        for (AddressComponent ac : acs) {
            GCAddressComponent mac = new GCAddressComponent();
            mac.longName = ac.longName;
            mac.shortName = ac.shortName;
            if (ac.types != null) {
                mac.types = this.converAddressComponentType(ac.types);
            }
            mapLiteAddressComponents.add(mac);
        }
        return mapLiteAddressComponents;
    }

    private GCGeocodingGeometry convertGeometry(Geometry geometry) {
        GCGeocodingGeometry mapLiteAddressGeometry = new GCGeocodingGeometry();
        if (geometry.bounds != null) {
            GCGeocodingBounds bounds = new GCGeocodingBounds();
            bounds.northeast = new ELGeocodingLatLon(geometry.bounds.northeast.lat,
                    geometry.bounds.northeast.lng);
            bounds.southwest = new ELGeocodingLatLon(geometry.bounds.southwest.lat,
                    geometry.bounds.southwest.lng);
            mapLiteAddressGeometry.bounds = bounds;
        }
        if (geometry.location != null) {
            ELGeocodingLatLon location = new ELGeocodingLatLon(geometry.location.lat,
                    geometry.location.lng);
            mapLiteAddressGeometry.location = location;
        }
        if (geometry.locationType != null) {
            mapLiteAddressGeometry.locationType = GCLocationType.valueOf(geometry.locationType.name());
        }
        if (geometry.viewport != null) {
            GCGeocodingBounds viewport = new GCGeocodingBounds();
            viewport.northeast = new ELGeocodingLatLon(geometry.viewport.northeast.lat,
                    geometry.viewport.northeast.lng);
            viewport.southwest = new ELGeocodingLatLon(geometry.viewport.southwest.lat,
                    geometry.viewport.southwest.lng);
            mapLiteAddressGeometry.viewport = viewport;
        }
        return mapLiteAddressGeometry;
    }

    private List<GCGeocodingResult> convertGeocodingResult(GeocodingResult[] results) {
        List<GCGeocodingResult> mapLiteGeocodingResults = Lists.<GCGeocodingResult>newArrayList();
        int count = 0;
        for (GeocodingResult result : results) {
            GCGeocodingResult mapLiteGeocodingResult = new GCGeocodingResult();
            if (result.addressComponents != null) {
                mapLiteGeocodingResult.addressComponents
                        = converAddressComponent(result.addressComponents);
            }
            mapLiteGeocodingResult.formattedAddress = result.formattedAddress;
            mapLiteGeocodingResult.geometry = this.convertGeometry(result.geometry);
            mapLiteGeocodingResult.partialMatch = result.partialMatch;
            logger.info("fore results: 5");
            if (result.postcodeLocalities != null) {
                mapLiteGeocodingResult.postcodeLocalities = Arrays.asList(result.postcodeLocalities);
            }
            logger.info("before if (result.types != null) {");
            if (result.types != null) {
                mapLiteGeocodingResult.types = converAddressComponentType(result.types);
            }
            logger.info("after if (result.types != null) {");
            mapLiteGeocodingResults.add(mapLiteGeocodingResult);
            if (++count == 5) {
                break;
            }
        }
        logger.info("after convertGeocodingResult");
        return mapLiteGeocodingResults;
    }

    public void setGeocoderElCache(GeocoderElCache geocoderElCache) {
        this.geocoderElCache = geocoderElCache;
    }

}
