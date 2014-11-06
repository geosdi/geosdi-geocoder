/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geosdi.geocoder.model.elasticbean;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Nazzareno Sileno - CNR IMAA geoSDI Group
 * @email nazzareno.sileno@geosdi.org
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "elGeocodingBean")
public class ELGeocodingBean implements Serializable {

    private static final long serialVersionUID = 7532841364725209171L;

    private Date acquisitionTime;

//    private String _id;
    private String language;

    private String formattedAddress;

    private ELGeocodingLatLon geometryLocation;

    private ELGeocodingLatLon boundsSouthwest;

    private ELGeocodingLatLon boundsNortheast;

    public ELGeocodingBean() {
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

    public ELGeocodingLatLon getGeometryLocation() {
        return geometryLocation;
    }

    public void setGeometryLocation(ELGeocodingLatLon geometryLocation) {
        this.geometryLocation = geometryLocation;
    }

    public ELGeocodingLatLon getBoundsSouthwest() {
        return boundsSouthwest;
    }

    public void setBoundsSouthwest(ELGeocodingLatLon boundsSouthwest) {
        this.boundsSouthwest = boundsSouthwest;
    }

    public ELGeocodingLatLon getBoundsNortheast() {
        return boundsNortheast;
    }

    public void setBoundsNortheast(ELGeocodingLatLon boundsNortheast) {
        this.boundsNortheast = boundsNortheast;
    }

    @Override
    public String toString() {
        return "ELGeocodingBean{" + "acquisitionTime=" + acquisitionTime + ", language=" + language + ", formattedAddress=" + formattedAddress + ", geometryLocation=" + geometryLocation + ", boundsSouthwest=" + boundsSouthwest + ", boundsNortheast=" + boundsNortheast + '}';
    }

}
