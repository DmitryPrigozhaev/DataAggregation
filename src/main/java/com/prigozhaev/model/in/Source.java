package com.prigozhaev.model.in;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * POJO representing data from an external system.
 * <p>
 * The full data structure is as follows:
 * <pre>
 *     Source {
 *         id
 *         sourceData {
 *             urlType
 *             videoUrl
 *         }
 *         tokenData {
 *             value
 *             ttl
 *         }
 *     }
 * </pre>
 *
 * @author dprigozhaev on 01.02.2020
 * @see SourceData
 * @see TokenData
 */

@Getter
@Setter
@NoArgsConstructor
public class Source {

    /**
     * Camera ID.
     */
    private Long id;

    /**
     * Link to get source data.
     *
     * @see SourceData
     */
    private String sourceDataUrl;

    /**
     * Link for receiving security tokens on the camera.
     *
     * @see TokenData
     */
    private String tokenDataUrl;

}