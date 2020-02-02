package com.prigozhaev.controller;

import com.prigozhaev.model.out.AggregatedData;
import com.prigozhaev.service.AggregationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for receiving and aggregating data from several services.
 *
 * @author dprigozhaev on 01.02.2020
 */

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
public class AggregationController {

    private final AggregationService aggregationService;

    /**
     * The method allows to return aggregated data from external systems.
     *
     * @return list of {@link AggregatedData}
     */
    @GetMapping("/getAggregatedData")
    public List<AggregatedData> getAggregatedData() throws InterruptedException {
        return aggregationService.getAggregatedData();
    }

}