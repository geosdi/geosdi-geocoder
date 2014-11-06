/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geosdi.geocoder.cache.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;

/**
 * @author Nazzareno Sileno - CNR IMAA geoSDI Group
 * @email nazzareno.sileno@geosdi.org
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Suggest implements Serializable {

    private static final long serialVersionUID = -6544365695106024398L;

    @JsonProperty("input")
    private List<String> input;

    public List<String> getInput() {
        return input;
    }

    public void setInput(List<String> input) {
        this.input = input;
    }

    public void addInput(String inputToAdd) {
        this.input.add(inputToAdd);
    }

    public String getInputArray() {
        StringBuilder result = new StringBuilder("{input:[");
        for (int i = 0; i < this.input.size();) {
            result.append(this.input.get(i));
            if (++i != this.input.size()) {
                result.append(',');
            }
        }
        result.append("]}");
        return result.toString();
    }

    @Override
    public String toString() {
        return "Suggest{" + "input=" + input + '}';
    }

}
