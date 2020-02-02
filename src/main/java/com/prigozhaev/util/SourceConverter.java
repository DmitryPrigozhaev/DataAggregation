package com.prigozhaev.util;

import com.prigozhaev.model.out.AggregatedData;
import com.prigozhaev.model.in.Source;
import com.prigozhaev.model.in.SourceData;
import com.prigozhaev.model.in.TokenData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;

/**
 * Helper class containing logic for converting input from multiple sources into a single object.
 *
 * @author dprigozhaev on 01.02.2020
 */

public class SourceConverter implements Callable<AggregatedData> {

    private final Source source;

    /**
     * Create a new instance of {@link SourceConverter} with the given {@code Source}.
     *
     * @param source the source data to be converted
     */
    public SourceConverter(Source source) {
        this.source = source;
    }

    /**
     * Converts the source object and aggregates data from links to external resources.
     *
     * @return an aggregated object
     */
    @Override
    public AggregatedData call() {
        AggregatedData aggregatedData = new AggregatedData();

        ResponseEntity<SourceData> sourceDataResponse = new RestTemplate().getForEntity(source.getSourceDataUrl(), SourceData.class);
        ResponseEntity<TokenData> tokenDataResponse = new RestTemplate().getForEntity(source.getTokenDataUrl(), TokenData.class);

        aggregatedData.setId(source.getId());

        if (sourceDataResponse.getBody() != null) {
            aggregatedData.setUrlType(sourceDataResponse.getBody().getUrlType());
            aggregatedData.setVideoUrl(sourceDataResponse.getBody().getVideoUrl());
        }

        if (tokenDataResponse.getBody() != null) {
            aggregatedData.setValue(tokenDataResponse.getBody().getValue());
            aggregatedData.setTtl(tokenDataResponse.getBody().getTtl());
        }

        return aggregatedData;
    }

}