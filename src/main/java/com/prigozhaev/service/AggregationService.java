package com.prigozhaev.service;

import com.prigozhaev.model.in.Source;
import com.prigozhaev.model.in.SourceData;
import com.prigozhaev.model.in.TokenData;
import com.prigozhaev.model.out.AggregatedData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Service for receiving and aggregating data from several services.
 * <p>
 * It is highly recommended not to use parallel streams for any long operations (getting data from the database,
 * network connections), since all parallel streams work with one fork/join pool and such long operations can
 * stop all parallel streams in the JVM due to lack of available threads in the pool, i.e. parallel streams should
 * be used only for short operations where the count goes for milliseconds, but not for those where the count can
 * go for seconds and minutes.
 * <p>
 * Therefore, to solve the problem, we use ExecutorService.
 *
 * @author dprigozhaev on 01.02.2020
 */

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
public class AggregationService {

    private final DataService dataService;

    /**
     * The main method for obtaining aggregated data from several external systems.
     *
     * @return aggregated data collected from external systems
     */
    public List<AggregatedData> getAggregatedData() {

        final List<Source> sources = dataService.getSource();
        final List<AggregatedData> aggregatedData = new ArrayList<>(sources.size());

        final ExecutorService executor = Executors.newCachedThreadPool();
        final ExecutorCompletionService<AggregatedData> completionService = new ExecutorCompletionService<>(executor);

        sources.forEach(source -> completionService.submit(() -> convertData(source)));

        executor.shutdown();

        // Unfortunately ExecutorCompletionService doesn't tell you how many Future objects are still
        // there waiting so you must remember how many times to call take() (why not use forEach).
        for (int i = 0; i < sources.size(); i++) {
            try {
                final Future<AggregatedData> future = completionService.take();
                final AggregatedData data = future.get();
                aggregatedData.add(data);
            } catch (InterruptedException | ExecutionException e) {
                log.error(e.getMessage(), e);
            }
        }

        return aggregatedData;
    }

    /**
     * Converts the source object and aggregates data from links to external resources.
     *
     * @param source the source data to be converted
     * @return an aggregated object
     */
    private AggregatedData convertData(Source source) {
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