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

import java.util.List;
import org.geosdi.geocoder.cache.GeocoderElCache;
import org.geosdi.geoplatform.exception.ResourceNotFoundFault;
import org.geosdi.geocoder.model.GCGeocodingResult;
import org.geosdi.geocoder.model.elasticbean.ELGeocodingBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Web Service implementation of {@link GeocoderRestService} endpoint.
 *
 * @author Nazzareno Sileno - CNR IMAA geoSDI Group
 * @email nazzareno.sileno@geosdi.org
 */
@Service("geocoderRestService")
public class GeocoderRestServiceImpl implements GeocoderRestService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    // Delegate
    private final GeocoderGoogleServiceImpl geocoderGoogleServiceDelegate;

    private GeocoderElCache geocoderElCache;

    public void setGeocoderElCache(GeocoderElCache geocodingElCache) {
        this.geocoderElCache = geocodingElCache;
        this.geocoderGoogleServiceDelegate.setGeocoderElCache(geocodingElCache);
    }

    /**
     * Default constructor create each service delegate.
     */
    public GeocoderRestServiceImpl() {
        this.geocoderGoogleServiceDelegate = new GeocoderGoogleServiceImpl();
    }

    @Override
    public List<GCGeocodingResult> executeGeocodignByAddress(String address, String language) {
        return this.geocoderGoogleServiceDelegate.executeGeocoderByAddress(address, language);
    }

    @Override
    public List<ELGeocodingBean> suggestGeocodignByAddress(String address)
            throws ResourceNotFoundFault {
        return this.geocoderGoogleServiceDelegate.suggestGeocodingForAddress(address);
    }

}
