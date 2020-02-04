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

    /**
     * Camera ID.
     */
    private Long id;

    /**
     * Type of link to the video stream.
     * Possible values: "LIVE", "ARCHIVE".
     */
    private String urlType;

    /**
     * Video stream link.
     */
    private String videoUrl;

    /**
     * Security token.
     */
    private String value;

    /**
     * Token lifetime.
     */
    private Long ttl;

}