package com.prigozhaev.model.out;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Aggregated data from an external system.
 *
 * @author dprigozhaev on 01.02.2020
 */

@Getter
@Setter
@NoArgsConstructor
public class AggregatedData {

    private Long id;
    private String urlType;
    private String videoUrl;
    private String value;
    private Long ttl;

}