package com.prigozhaev.service.data;

import com.prigozhaev.model.in.Source;

import java.util.List;

/**
 * Interface for the possibility of using polymorphism to obtain data from different sources.
 *
 * @author dprigozhaev on 04.02.2020
 */

public interface DataService {

    /**
     * Method for obtaining source data.
     *
     * @return list of {@link Source}
     */
    List<Source> getSource();

}