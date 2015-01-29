/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geosdi.geoplatform.services.config.server;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.RuntimeDelegate;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.provider.json.JSONProvider;
import org.apache.cxf.message.Message;
import org.geosdi.geoplatform.exception.rs.mapper.GPExceptionFaultMapper;
import org.geosdi.geoplatform.services.GeocoderRestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 *
 * @author Giuseppe La Scaleia - CNR IMAA geoSDI Group
 * @email giuseppe.lascaleia@geosdi.org
 */
@Configuration
class GeocoderRestServerConfig {

    private static final Logger logger = LoggerFactory.getLogger(
            GeocoderRestServerConfig.class);

    @Bean(initMethod = "create")
    @Required
    public static JAXRSServerFactoryBean geocoderRSServerFactory(@Qualifier(
            value = "geocoderRestService") GeocoderRestService geocoderRestService,
            @Qualifier(value = "geocoderJsonApplication") Application geocoderJsonApplication) {

        logger.debug("###########################Initializing REST Server");

        JAXRSServerFactoryBean factory = RuntimeDelegate.getInstance().createEndpoint(
                geocoderJsonApplication, JAXRSServerFactoryBean.class);
        factory.setServiceBean(geocoderRestService);
        factory.setAddress(factory.getAddress());

        factory.setProviders(Arrays.asList(
                new Object[]{new JSONProvider()}));
//                    new GPExceptionFaultMapper()}));

        Map<Object, Object> extensionMappings = new HashMap<>();
        extensionMappings.put("xml", MediaType.APPLICATION_XML);
        extensionMappings.put("json", MediaType.APPLICATION_JSON);

        factory.setExtensionMappings(extensionMappings);

        factory.setInInterceptors(Arrays.<Interceptor<? extends Message>>asList(
                new LoggingInInterceptor()));

        factory.setOutInterceptors(
                Arrays.<Interceptor<? extends Message>>asList(
                        new LoggingOutInterceptor()));

        return factory;
    }

}
