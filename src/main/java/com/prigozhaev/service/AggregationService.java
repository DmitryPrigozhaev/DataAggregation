package com.prigozhaev.service;

import com.prigozhaev.model.out.AggregatedData;
import com.prigozhaev.model.in.Source;
import com.prigozhaev.util.SourceConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
public class AggregationService {

    @Value("${source.data.link}")
    private String sourceData;

    /**
     * @return aggregated data collected from external systems
     */
    public List<AggregatedData> getAggregatedData() throws InterruptedException {

        final List<AggregatedData> aggregatedData = new ArrayList<>();
        final List<Source> sources = getSource();

        final ExecutorService executor = Executors.newCachedThreadPool();
        final ExecutorCompletionService<AggregatedData> completionService = new ExecutorCompletionService<>(executor);

        sources.forEach(source -> completionService.submit(new SourceConverter(source)));

        executor.shutdown();

        // Unfortunately ExecutorCompletionService doesn't tell you how many Future objects are still
        // there waiting so you must remember how many times to call take() (why not use forEach).
        for (int i = 0; i < sources.size(); i++) {
            final Future<AggregatedData> future = completionService.take();
            try {
                final AggregatedData data = future.get();
                aggregatedData.add(data);
            } catch (ExecutionException e) {
                System.out.println("temp: error");
            }
        }

        return aggregatedData;
    }

    // TODO: think what to do about it
    private List<Source> getSource() {
        ResponseEntity<Source[]> responseEntity = new RestTemplate().getForEntity(sourceData, Source[].class);
        return new ArrayList<>(Arrays.asList(Objects.requireNonNull(responseEntity.getBody())));
    }

}