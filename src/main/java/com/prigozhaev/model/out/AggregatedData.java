package com.prigozhaev.model.out;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Aggregated data from an external system.
 * <p>
 * Data obtained by combining data from {@link com.prigozhaev.model.in.Source} (external system), where
 * {@code urlType} and {@code videoUrl} — obtained from {@link com.prigozhaev.model.in.SourceData},
 * {@code value} and {@code ttl} — obtained from {@link com.prigozhaev.model.in.TokenData}
 *
 * @author dprigozhaev on 01.02.2020
 * @see com.prigozhaev.model.in.Source
 * @see com.prigozhaev.model.in.SourceData
 * @see com.prigozhaev.model.in.TokenData
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