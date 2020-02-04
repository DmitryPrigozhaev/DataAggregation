package com.prigozhaev.service.data;

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
 * Service for obtaining source data for subsequent aggregation
 * from a remote server.
 *
 * @author dprigozhaev on 02.02.2020
 */

@Service
public class DataServiceImpl implements DataService {

    @Value("${source.data.link}")
    private String sourceDataLink;

    /**
     * Method for obtaining source data.
     *
     * @return list of {@link Source} from external service
     */
    @Override
    public List<Source> getSource() {
        ResponseEntity<Source[]> responseEntity = new RestTemplate().getForEntity(sourceDataLink, Source[].class);
        return new ArrayList<>(Arrays.asList(Objects.requireNonNull(responseEntity.getBody())));
    }

}