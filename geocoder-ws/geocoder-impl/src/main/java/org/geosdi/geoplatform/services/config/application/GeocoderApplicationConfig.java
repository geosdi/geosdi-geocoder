/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geosdi.geoplatform.services.config.application;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 *
 * @author Giuseppe La Scaleia - CNR IMAA geoSDI Group
 * @email giuseppe.lascaleia@geosdi.org
 */
@Configuration
class GeocoderApplicationConfig {

    @Bean
    public Application geocoderJsonApplication() {
        return new GeocoderJsonApplication();
    }

    @ApplicationPath(value = "/json")
    final class GeocoderJsonApplication extends Application {
    }

}
