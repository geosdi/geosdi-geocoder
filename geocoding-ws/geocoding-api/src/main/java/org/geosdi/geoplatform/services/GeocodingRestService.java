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
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.geosdi.geoplatform.exception.ResourceNotFoundFault;
import org.geosdi.geocoding.model.GCGeocodingResult;
import org.geosdi.geocoding.model.elasticbean.ELGeocodingBean;

/**
 * Public interface to define the service operations mapped via REST using CXT
 * framework.
 *
 * @author Nazzareno Sileno - CNR IMAA geoSDI Group
 * @email nazzareno.sileno@geosdi.org
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public interface GeocodingRestService {

    static final String GEOCODING_PATH = "/geocoding/";

//    @GET
//    @Path(GEOCODING_PATH + "{idDevice}/{alias}/{x}/{y}/{elevation}/{accuracy}/{speed}/{compass}/{status}/{uuid}")
//    @Consumes(MediaType.APPLICATION_JSON)
//    long updateDeviceLocation(@PathParam("idDevice") String idDevice,
//            @PathParam("alias") String alias,
//            @PathParam("x") double x,
//            @PathParam("y") double y,
//            @PathParam("elevation") double elevation,
//            @PathParam("accuracy") double accuracy,
//            @PathParam("speed") double speed,
//            @PathParam("compass") double compass,
//            @PathParam("status") String status,
//            @PathParam("uuid") String uuid)
//            throws ResourceNotFoundFault;
    @GET
    @Path(GEOCODING_PATH + "execute/{address}/{language}")
    @Consumes(MediaType.APPLICATION_JSON)
    List<GCGeocodingResult> executeGeocodignByAddress(@PathParam("address") String address,
            @PathParam("language") String language) throws ResourceNotFoundFault;

    @GET
    @Path(GEOCODING_PATH + "suggest/{address}/{language}")
    @Consumes(MediaType.APPLICATION_JSON)
    List<ELGeocodingBean> suggestGeocodignByAddress(@PathParam("address") String address,
            @PathParam("language") String language) throws ResourceNotFoundFault;
}
