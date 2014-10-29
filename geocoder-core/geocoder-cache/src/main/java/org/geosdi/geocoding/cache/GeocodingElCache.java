/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geosdi.geocoding.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.elasticsearch.client.Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.model.GeocodingResult;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.suggest.SuggestRequestBuilder;
import org.elasticsearch.action.suggest.SuggestResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.indices.IndexAlreadyExistsException;
import org.elasticsearch.indices.IndexMissingException;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.geosdi.geocoding.model.elasticbean.ELGeocodingBean;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.slf4j.LoggerFactory;

/**
 * @author Nazzareno Sileno - CNR IMAA geoSDI Group
 * @email nazzareno.sileno@geosdi.org
 */
public class GeocodingElCache implements InitializingBean {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private final static String GEOCODER_INDEX = "geosdi_geocoder";
    private final static String GEOCODER_TYPE = "geocoder_type";

//    Node node = nodeBuilder().client(true).node();
//    Client client = node.client();
    Client client;
    ObjectMapper mapper = new ObjectMapper();

    public GeocodingElCache() {
    }

    public void persistGeocodingResults(List<GeocodingResult> geocodingResults, String language) {
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        for (GeocodingResult result : geocodingResults) {
            try {
//                String json = mapper.writeValueAsString(result);
                bulkRequest.add(client.prepareIndex(GEOCODER_INDEX, GEOCODER_TYPE)
                        .setSource(XContentFactory.jsonBuilder().
                                startObject().
                                field("acquisitionTime", new Date()).
                                field("language", language).
                                field("formattedAddress", result.formattedAddress).
                                startObject("geometryLocation").
                                field("lat", result.geometry.location.lat).
                                field("lon", result.geometry.location.lng).
                                endObject().
                                startObject("boundsSouthwest").
                                field("lat", result.geometry.bounds.southwest.lat).
                                field("lon", result.geometry.bounds.southwest.lng).
                                endObject().
                                startObject("boundsNortheast").
                                field("lat", result.geometry.bounds.northeast.lat).
                                field("lon", result.geometry.bounds.northeast.lng).
                                endObject().
                                endObject()
                        ));
//                        .setSource(json));
//                        .execute()
//                        .actionGet());
                client.prepareSuggest(GEOCODER_INDEX).addSuggestion(
                        new CompletionSuggestionBuilder("suggestion").field("formattedAddress")
                        .text(result.formattedAddress).size(1)).execute().actionGet();
            } catch (JsonProcessingException ex) {
                logger.error(GeocodingElCache.class.getName() + "Error processing JSON: " + ex);
            } catch (IOException ex) {
                logger.error(GeocodingElCache.class.getName() + "Error writing geocoding result: " + ex);
            }
        }
        BulkResponse bulkResponse = bulkRequest.execute().actionGet();
        if (bulkResponse.hasFailures()) {
            throw new IllegalArgumentException("Error writing geocoding results: "
                    + bulkResponse.buildFailureMessage());
        }
    }

    public List<ELGeocodingBean> suggestForAddressResults(String address) {
        CompletionSuggestionBuilder compBuilder = new CompletionSuggestionBuilder("complete");
        compBuilder.text(address);
        compBuilder.field("suggest");

        SuggestRequestBuilder suggestRequestBuilder
                = client.prepareSuggest(GEOCODER_INDEX).addSuggestion(compBuilder);

//        SearchResponse searchResponse = client.prepareSearch(GEOCODING_INDEX)
//                .setTypes("completion")
//                .setQuery(QueryBuilders.matchAllQuery())
//                .addSuggestion(compBuilder)
//                .execute().actionGet();
//        CompletionSuggestion compSuggestion = searchResponse.getSuggest().getSuggestion("complete");
        SuggestResponse suggestResponse = suggestRequestBuilder.execute().actionGet();

        Iterator<? extends Suggest.Suggestion.Entry.Option> iterator
                = suggestResponse.getSuggest().getSuggestion("complete").iterator().next().getOptions().iterator();

        List<ELGeocodingBean> items = Lists.<ELGeocodingBean>newArrayList();
        while (iterator.hasNext()) {
            Suggest.Suggestion.Entry.Option next = iterator.next();
            System.out.println("Results: " + next.getText().string());
            ObjectMapper mapper = new ObjectMapper();
            try {
                ELGeocodingBean geocodingBean = mapper.readValue(next.getText().string(), ELGeocodingBean.class);
                items.add(geocodingBean);
//            items.add(new SuggestionResponse(next.getText().string()));
            } catch (IOException ex) {
                logger.error(GeocodingElCache.class.getName() + "Error reading suggest: " + ex);
            }
        }
        return items;
    }

    public void cleanUp() {
        client.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
//        Node node = nodeBuilder().loadConfigSettings(true).client(true).node();
//        this.client = node.client();
        this.client = new TransportClient().addTransportAddress(new InetSocketTransportAddress(
                "localhost", 9300));
        CreateIndexRequestBuilder cirb = client.admin().indices().prepareCreate(GEOCODER_INDEX);
        try {
//        !client.admin().indices().prepareExists(GEOCODER_INDEX).execute().actionGet().isExists()
            cirb.execute().actionGet();
        } catch (IndexAlreadyExistsException e) {
            logger.warn("Warning the elasticsearch index: " + GEOCODER_INDEX
                    + " not created, already exists?");
        }
        if (!this.mappingAlreadyExists()) {
            try {
                XContentBuilder builder = XContentFactory.jsonBuilder()
                        .startObject()
                            .startObject(GEOCODER_TYPE).
                                field("dynamic", "strict").
//                                startObject("_id").
//                                    field("path", "id")
//                                .endObject().
//                                startObject("_all").
//                                    field("enabled", "true").
//                                endObject().
                                startObject("properties").
                                    startObject("acquisitionTime").
                                        field("type", "date").
                                        field("store", "yes").
                                        field("index", "not_analyzed").
                                    endObject().
                                    startObject("language").
                                        field("type", "string").
                                        field("store", "yes").
                                        field("index", "not_analyzed").
                                    endObject().
                                    startObject("formattedAddress").
                                        field("type", "string").
                                        field("store", "yes").
                                        field("index", "analyzed").
                                    endObject().
                                    startObject("geometryLocation").
                                        field("type", "geo_point").
                                        field("store", "yes").
                                        field("lat_lon", true).
                                        field("index", "not_analyzed").
                                    endObject().
                                    startObject("boundsSouthwest").
                                        field("type", "geo_point").
                                        field("store", "yes").
                                        field("lat_lon", true).
                                        field("index", "not_analyzed").
                                    endObject().
                                    startObject("boundsNortheast").
                                        field("type", "geo_point").
                                        field("store", "yes").
                                        field("lat_lon", true).
                                        field("index", "not_analyzed").
                                    endObject()
                                    .startObject("suggest")
                                        .field("type", "completion")
                                        .field("preserve_separators", false)
                                        .field("preserve_position_increments", false)
                                    .endObject().
                                endObject().
                            endObject()
                        .endObject();
                PutMappingResponse response = client.admin().
                        indices().
                        preparePutMapping(GEOCODER_INDEX).
                        setType(GEOCODER_TYPE).
                        setSource(builder).
                        execute().
                        actionGet();
                if (response.isAcknowledged()) {
                    // Type and Mapping created!
                    logger.info("Elasticsearch geocoding mapping created");
                } else {
                    logger.info("WARNING: Elasticsearch geocoding mapping not created");
                }
            } catch (IOException ex) {
                logger.error(GeocodingElCache.class.getName() + "Error writing mapping: " + ex);
            }

        } else {
            logger.error(GeocodingElCache.class.getName() + " Cannot create mapping");
        }
    }

    private boolean mappingAlreadyExists() {
        boolean result = false;
        try {
            ClusterState cs = client.admin().
                    cluster().
                    prepareState().
                    setIndices(GEOCODER_INDEX).
                    execute().
                    actionGet().
                    getState();

            IndexMetaData imd = cs.getMetaData().index(GEOCODER_INDEX);

            MappingMetaData mdd = imd.mapping(GEOCODER_TYPE);
            if (mdd != null) {
                result = true;
                logger.info("Mapping as JSON string:" + mdd.source());
            }
        } catch (IndexMissingException e) {
            logger.error("Warning the elasticsearch index: " + GEOCODER_INDEX
                    + " does not exists!");
        }
        return result;
    }
}
