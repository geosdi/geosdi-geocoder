/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geosdi.geocoder.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.elasticsearch.client.Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.model.GeocodingResult;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
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
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.indices.IndexAlreadyExistsException;
import org.elasticsearch.indices.IndexMissingException;
import org.elasticsearch.script.ScriptService;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.geosdi.geocoder.cache.model.LocationDoc;
import org.geosdi.geocoder.model.elasticbean.ELGeocodingBean;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.slf4j.LoggerFactory;

/**
 * @author Nazzareno Sileno - CNR IMAA geoSDI Group
 * @email nazzareno.sileno@geosdi.org
 */
public class GeocoderElCache implements InitializingBean {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private final static String GEOCODER_INDEX = "geosdi_geocoder";
    private final static String GEOCODER_TYPE = "geocoder_type";
    private final static String SUGGEST_FIELD = "suggest";
    private final static String SUGGEST_PAYLOAD_FIELD = "locationID";

    private String hostname;
    private int port;

//    Node node = nodeBuilder().client(true).node();
//    Client client = node.client();
    Client client;
    ObjectMapper mapper = new ObjectMapper();

    public GeocoderElCache() {
    }

    public GeocoderElCache(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void persistGeocodingResults(List<GeocodingResult> geocodingResults,
            String language, String originalQuery) {
        for (GeocodingResult result : geocodingResults) {
            logger.info("********** Looking for formatted address: " + result.formattedAddress);
            SearchResponse response = client.prepareSearch(GEOCODER_INDEX).
                    setTypes(GEOCODER_TYPE).setQuery(QueryBuilders.
                            termQuery("formattedAddress", result.formattedAddress))
                    .execute().actionGet();
            if (response != null && response.getHits().getTotalHits() > 0) {
                String responseJson = response.toString();
                int startSourceIndex = responseJson.indexOf("\"_source\":") + "\"_source\":".length();
                responseJson = responseJson.substring(startSourceIndex);
                logger.info("**********Response JSON _source: " + responseJson);

                LocationDoc locationDoc = null;
                try {
                    locationDoc = this.mapper.readValue(responseJson, LocationDoc.class);
                    logger.info("Location DOC mapped: " + locationDoc);
                } catch (IOException ex) {
                    logger.error("***************** Error parsing location doc: " + ex);
                }

                boolean foundToken = false;
                if (locationDoc != null && locationDoc.getSuggest() != null
                        && locationDoc.getSuggest().getInput() != null) {
                    for (String input : locationDoc.getSuggest().getInput()) {
                        logger.info("Looking into nex input field..: " + input);
                        if (input.toLowerCase().contains(originalQuery.toLowerCase())) {
                            logger.info("##### Input found!");
                            foundToken = true;
                            break;
                        }
                    }

                }

                if (!foundToken) {
                    logger.info("##### New data input not found!");
                    client.prepareUpdate(GEOCODER_INDEX, GEOCODER_TYPE, response.getHits().getAt(0).getId())
                            .addScriptParam("originalQuery", originalQuery)
                            .setScript("ctx._source.suggest.input += originalQuery", ScriptService.ScriptType.INLINE)
                            .setScriptLang("groovy")
                            //                            .setRefresh(true)
                            .execute().actionGet();
                }
            } else {
                try {
                    String _id = UUID.randomUUID().toString();
                    BulkRequestBuilder bulkRequest = client.prepareBulk();
                    bulkRequest.add(client.prepareIndex(GEOCODER_INDEX, GEOCODER_TYPE, _id)
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
                                    field("lat", result.geometry.bounds != null ? result.geometry.bounds.southwest.lat : result.geometry.viewport.southwest.lat).
                                    field("lon", result.geometry.bounds != null ? result.geometry.bounds.southwest.lng : result.geometry.viewport.southwest.lng).
                                    //field("lon", result.geometry.bounds.southwest.lng).
                                    endObject().
                                    startObject("boundsNortheast").
                                    field("lat", result.geometry.bounds != null ? result.geometry.bounds.northeast.lat : result.geometry.viewport.northeast.lat).
                                    field("lon", result.geometry.bounds != null ? result.geometry.bounds.northeast.lng : result.geometry.viewport.northeast.lng).
                                    endObject().
                                    startObject(SUGGEST_FIELD).
                                    array("input", result.formattedAddress, originalQuery).
                                    startObject("payload").
                                    field(SUGGEST_PAYLOAD_FIELD, _id).
                                    endObject().
                                    field("output", result.formattedAddress).
                                    endObject().
                                    endObject()
                            ));
                    BulkResponse bulkResponse = bulkRequest.execute().actionGet();
                    if (bulkResponse.hasFailures()) {
                        throw new IllegalArgumentException("Error writing geocoding results: "
                                + bulkResponse.buildFailureMessage());
                    }
                } catch (JsonProcessingException ex) {
                    logger.error(GeocoderElCache.class.getName() + "Error processing JSON: " + ex);
                } catch (IOException ex) {
                    logger.error(GeocoderElCache.class.getName() + "Error writing geocoding result: " + ex);
                }
            }
        }
    }

    public List<ELGeocodingBean> suggestForAddressResults(String address) {
        CompletionSuggestionBuilder compBuilder = new CompletionSuggestionBuilder("complete");
        compBuilder.text(address);
        compBuilder.field(SUGGEST_FIELD);

        SuggestRequestBuilder suggestRequestBuilder
                = client.prepareSuggest(GEOCODER_INDEX).addSuggestion(compBuilder);
        SuggestResponse suggestResponse = suggestRequestBuilder.execute().actionGet();

        Iterator<? extends CompletionSuggestion.Entry.Option> iterator
                = (Iterator<? extends CompletionSuggestion.Entry.Option>) suggestResponse.getSuggest().getSuggestion("complete").iterator()
                .next().getOptions().iterator();
        List<ELGeocodingBean> items = Lists.<ELGeocodingBean>newArrayList();
        while (iterator.hasNext()) {
            CompletionSuggestion.Entry.Option next = iterator.next();
            logger.debug("**** Results: " + next.getText().string());
            logger.debug("**** Payload: " + next.getPayloadAsMap().get(SUGGEST_PAYLOAD_FIELD));
            String _id = (String) next.getPayloadAsMap().get(SUGGEST_PAYLOAD_FIELD);
            GetResponse response = client.prepareGet(GEOCODER_INDEX, GEOCODER_TYPE, _id)
                    .execute()
                    .actionGet();
            if (response != null) {
                logger.debug("**** Response: " + response.getSourceAsString());
                try {
                    ELGeocodingBean geocodingBean = mapper.readValue(response.getSourceAsString(), ELGeocodingBean.class);
                    items.add(geocodingBean);
                } catch (IOException ex) {
                    logger.error(GeocoderElCache.class.getName() + "Error reading suggest: " + ex);
                }
            }
        }
        return items;
    }

    public void cleanUp() {
        client.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//        Node node = nodeBuilder().loadConfigSettings(true).client(true).node();
//        this.client = node.client();
        this.client = new TransportClient().addTransportAddress(new InetSocketTransportAddress(
                this.hostname, this.port));
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
                        //                        startObject("_id").
                        //                            field("path", "id")
                        //                        .endObject().
                        //                        startObject("_all").
                        //                            field("enabled", "true").
                        //                        endObject().
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
                        .startObject(SUGGEST_FIELD)
                        .field("type", "completion")
                        .field("preserve_separators", false)
                        .field("preserve_position_increments", false)
                        .field("payloads", true)
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
                    logger.warn("WARNING: Elasticsearch geocoding mapping not created");
                }
            } catch (IOException ex) {
                logger.error(GeocoderElCache.class.getName() + "Error writing mapping: " + ex);
            }

        } else {
            logger.info(GeocoderElCache.class.getName() + " The mapping already exists");
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

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
