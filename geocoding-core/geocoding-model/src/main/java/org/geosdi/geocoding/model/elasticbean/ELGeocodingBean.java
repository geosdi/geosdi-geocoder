/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geosdi.geocoding.model.elasticbean;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.geosdi.geocoding.model.GCGeocodingLatLng;

/**
 * @author Nazzareno Sileno - CNR IMAA geoSDI Group
 * @email nazzareno.sileno@geosdi.org
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "elGeocodingBean")
public class ELGeocodingBean implements Serializable {

    private static final long serialVersionUID = 7532841364725209171L;

    private Date acquisitionTime;

    private String id;

    private String language;

    private String formattedAddress;

    private GCGeocodingLatLng geometryLocation;

    private GCGeocodingLatLng boundsSouthwest;

    private GCGeocodingLatLng boundsNortheast;

    public ELGeocodingBean() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getAcquisitionTime() {
        return acquisitionTime;
    }

    public void setAcquisitionTime(Date acquisitionTime) {
        this.acquisitionTime = acquisitionTime;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public GCGeocodingLatLng getGeometryLocation() {
        return geometryLocation;
    }

    public void setGeometryLocation(GCGeocodingLatLng geometryLocation) {
        this.geometryLocation = geometryLocation;
    }

    public GCGeocodingLatLng getBoundsSouthwest() {
        return boundsSouthwest;
    }

    public void setBoundsSouthwest(GCGeocodingLatLng boundsSouthwest) {
        this.boundsSouthwest = boundsSouthwest;
    }

    public GCGeocodingLatLng getBoundsNortheast() {
        return boundsNortheast;
    }

    public void setBoundsNortheast(GCGeocodingLatLng boundsNortheast) {
        this.boundsNortheast = boundsNortheast;
    }

    @Override
    public String toString() {
        return "ELGeocodingBean{" + "acquisitionTime=" + acquisitionTime + ", id=" + id + ", language=" + language + ", formattedAddress=" + formattedAddress + ", geometryLocation=" + geometryLocation + ", boundsSouthwest=" + boundsSouthwest + ", boundsNortheast=" + boundsNortheast + '}';
    }

}
