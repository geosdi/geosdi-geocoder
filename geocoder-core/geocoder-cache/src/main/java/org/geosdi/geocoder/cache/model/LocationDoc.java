/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geosdi.geocoder.cache.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

/**
 * @author Nazzareno Sileno - CNR IMAA geoSDI Group
 * @email nazzareno.sileno@geosdi.org
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationDoc implements Serializable {

    private static final long serialVersionUID = 8215654725217521565L;

    @JsonProperty("suggest")
    private Suggest suggest;

    public Suggest getSuggest() {
        return suggest;
    }

    public void setSuggest(Suggest suggest) {
        this.suggest = suggest;
    }

    @Override
    public String toString() {
        return "LocationDoc{" + "suggest=" + suggest + '}';
    }

}
