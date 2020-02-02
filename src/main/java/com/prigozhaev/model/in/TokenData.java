package com.prigozhaev.model.in;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * POJO representing source data part from an external system.
 *
 * @author dprigozhaev on 01.02.2020
 * @see Source
 */

@Getter
@Setter
@NoArgsConstructor
public class TokenData {

    /**
     * Security token.
     */
    private String value;

    /**
     * Token lifetime.
     */
    private Long ttl;

}