package com.prigozhaev.service;

import com.prigozhaev.model.in.Source;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Service for obtaining source data for subsequent aggregation.
 * <p>
 * It would be good practice to create an interface and several implementations,
 * however, for this task, one class is enough.
 *
 * @author dprigozhaev on 02.02.2020
 */

@Service
public class DataService {

    @Value("${source.data.link}")
    private String sourceData;

    /**
     * Method for obtaining source data.
     *
     * @return list of {@link Source}
     */
    List<Source> getSource() {
        ResponseEntity<Source[]> responseEntity = new RestTemplate().getForEntity(sourceData, Source[].class);
        return new ArrayList<>(Arrays.asList(Objects.requireNonNull(responseEntity.getBody())));
    }

}