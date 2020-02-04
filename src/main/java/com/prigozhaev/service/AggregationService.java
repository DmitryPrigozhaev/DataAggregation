package com.prigozhaev.service;

import com.prigozhaev.model.in.Source;
import com.prigozhaev.model.in.SourceData;
import com.prigozhaev.model.in.TokenData;
import com.prigozhaev.model.out.AggregatedData;
import com.prigozhaev.service.data.DataService;
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
 * About solution:
 * <p>
 * It is highly recommended not to use parallel streams for any long operations (getting data from the database,
 * network connections), since all parallel streams work with one fork/join pool and such long operations can
 * stop all parallel streams in the JVM due to lack of available threads in the pool, i.e. parallel streams should
 * be used only for short operations where the count goes for milliseconds, but not for those where the count can
 * go for seconds and minutes.
 * <p>
 * Therefore, to solve the problem, we use ExecutorService.
 * <p>
 * The implementation {@code Executors.newCachedThreadPool()} used is due to the assumption that
 * tasks of data aggregation in threads will complete quickly.
 * <p>
 * But for a heavily loaded system, such a choice is likely to be a bad decision,
 * and it is better to use {@code Executor.newFixedThreadPool} (based on materials Joshua Bloch).
 * <p>
 * In any case, it is necessary to conduct a performance study.
 *
 * @author dprigozhaev on 01.02.2020
 */

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
public class AggregationService {

    private final DataService dataService;

    private final RestTemplate restTemplate;

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

        sources.forEach(source -> completionService.submit(() -> aggregateDataFrom(source)));

        executor.shutdown();

        for (int i = 0; i < sources.size(); i++) {
            try {
                final Future<AggregatedData> future = completionService.take();
                final AggregatedData data = future.get();
                aggregatedData.add(data);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                log.error(e.getCause().getMessage());
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
    private AggregatedData aggregateDataFrom(Source source) {
        AggregatedData aggregatedData = new AggregatedData();

        ResponseEntity<SourceData> sourceDataResponse = restTemplate.getForEntity(source.getSourceDataUrl(), SourceData.class);
        ResponseEntity<TokenData> tokenDataResponse = restTemplate.getForEntity(source.getTokenDataUrl(), TokenData.class);

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