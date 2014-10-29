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
import java.util.Arrays;
import java.util.List;
import org.geosdi.geocoding.cache.GeocodingElCache;
import org.geosdi.geocoding.model.GCAddressComponent;
import org.geosdi.geocoding.model.GCAddressComponentType;
import org.geosdi.geocoding.model.GCAddressType;
import org.geosdi.geocoding.model.GCGeocodingBounds;
import org.geosdi.geocoding.model.GCGeocodingGeometry;
import org.geosdi.geocoding.model.GCGeocodingLatLng;
import org.geosdi.geocoding.model.GCGeocodingResult;
import org.geosdi.geocoding.model.GCLocationType;
import org.geosdi.geocoding.model.elasticbean.ELGeocodingBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GeocodingGoogle service delegate.
 *
 * @author Nazzareno Sileno - CNR IMAA geoSDI Group
 * @email nazzareno.sileno@geosdi.org
 */
class GeocodingGoogleServiceImpl {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private GeocodingElCache geocodingElCache;

    private GeoApiContext context = new GeoApiContext().
            setApiKey("AIzaSyAYwt6RZz7ATdEBel61EUABVww9l8gPaJE");

    public List<ELGeocodingBean> suggestGeocodingForAddress(String address, String language) {
        return geocodingElCache.suggestForAddressResults(address);
    }

    public List<GCGeocodingResult> executeGeocodignByAddress(String address, String language) {
        GeocodingApiRequest req = GeocodingApi.newRequest(context).address(address).language(language);
        List<GCGeocodingResult> gcResults = null;
        List<GeocodingResult> geocodingResults = null;
        try {
            GeocodingResult[] result = req.await();
            geocodingResults = Arrays.asList(result);
            gcResults = this.convertGeoCodingresult(result);
        } catch (Exception e) {
            logger.error("Error executing geocode: " + e);
        }
        this.geocodingElCache.persistGeocodingResults(geocodingResults, language);
        return gcResults;
    }

    private GCAddressType[] converAddressComponentType(AddressType[] ats) {
        GCAddressType[] mapLiteAT = new GCAddressType[ats.length];
        int i = 0;
        for (AddressType at : ats) {
            mapLiteAT[i] = GCAddressType.valueOf(at.name());
        }
        return mapLiteAT;
    }

    private GCAddressComponentType[] converAddressComponentType(AddressComponentType[] acts) {
        GCAddressComponentType[] mapLiteACT = new GCAddressComponentType[acts.length];
        int i = 0;
        for (AddressComponentType act : acts) {
            mapLiteACT[i] = GCAddressComponentType.valueOf(act.name());
        }
        return mapLiteACT;
    }

    private GCAddressComponent[] converAddressComponent(AddressComponent[] acs) {
        GCAddressComponent[] mapLiteAddressComponents = new GCAddressComponent[acs.length];
        int i = 0;
        for (AddressComponent ac : acs) {
            GCAddressComponent mac = new GCAddressComponent();
            mac.longName = ac.longName;
            mac.shortName = ac.shortName;
            mac.types = this.converAddressComponentType(ac.types);
            mapLiteAddressComponents[i] = mac;
        }
        return mapLiteAddressComponents;
    }

    private GCGeocodingGeometry convertGeometry(Geometry geometry) {
        GCGeocodingGeometry mapLiteAddressGeometry = new GCGeocodingGeometry();
        GCGeocodingBounds bounds = new GCGeocodingBounds();
        bounds.northeast = new GCGeocodingLatLng(geometry.bounds.northeast.lat,
                geometry.bounds.northeast.lng);
        bounds.southwest = new GCGeocodingLatLng(geometry.bounds.southwest.lat,
                geometry.bounds.southwest.lng);
        mapLiteAddressGeometry.bounds = bounds;
        GCGeocodingLatLng location = new GCGeocodingLatLng(geometry.location.lat,
                geometry.location.lng);
        mapLiteAddressGeometry.location = location;
        mapLiteAddressGeometry.locationType = GCLocationType.valueOf(geometry.locationType.name());
        GCGeocodingBounds viewport = new GCGeocodingBounds();
        viewport.northeast = new GCGeocodingLatLng(geometry.viewport.northeast.lat,
                geometry.viewport.northeast.lng);
        viewport.southwest = new GCGeocodingLatLng(geometry.viewport.southwest.lat,
                geometry.viewport.southwest.lng);
        mapLiteAddressGeometry.viewport = viewport;
        return mapLiteAddressGeometry;
    }

    private List<GCGeocodingResult> convertGeoCodingresult(GeocodingResult[] results) {
        List<GCGeocodingResult> mapLiteGeocodingResults = Lists.<GCGeocodingResult>newArrayList();
        GCGeocodingResult mapLiteGeocodingResult;
        int count = 0;
        for (GeocodingResult result : results) {
            mapLiteGeocodingResult = new GCGeocodingResult();
            mapLiteGeocodingResult.addressComponents
                    = converAddressComponent(result.addressComponents);
            mapLiteGeocodingResult.formattedAddress = result.formattedAddress;
            mapLiteGeocodingResult.geometry = this.convertGeometry(result.geometry);
            mapLiteGeocodingResult.partialMatch = result.partialMatch;
            mapLiteGeocodingResult.postcodeLocalities = result.postcodeLocalities;
            mapLiteGeocodingResult.types = converAddressComponentType(result.types);
            mapLiteGeocodingResults.add(mapLiteGeocodingResult);
            if (++count == 5) {
                break;
            }
        }
        return mapLiteGeocodingResults;
    }

    public void setGeocodingElCache(GeocodingElCache geocodingElCache) {
        this.geocodingElCache = geocodingElCache;
    }

}
