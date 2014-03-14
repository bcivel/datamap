/* DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This file is part of Cerberus.
 *
 * Cerberus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Cerberus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Cerberus.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.redoute.datamap.dao;

import com.redoute.datamap.entity.Datamap;
import java.util.List;


/**
 * {Insert class description here}
 *
 * @author Tiago Bernardes
 * @version 1.0, 22/03/2013
 * @since 2.0.0
 */
public interface IDatamapDAO {

    Datamap findDatamapByKey(String name);
    
    /**
     *
     * @param datamap datamap to insert
     */
    void createDatamap(Datamap datamap);

    /**
     *
     * @param datamap datamap to delete
     */
    void deleteDatamap(Datamap datamap);

    /**
     *
     * @return All Datamap
     */
    List<Datamap> findAllDatamap();

    /**
     *
     * @param start first row of the resultSet
     * @param amount number of row of the resultSet
     * @param column order the resultSet by this column
     * @param dir Asc or desc, information for the order by command
     * @param searchTerm search term on all the column of the resultSet
     * @param individualSearch search term on a dedicated column of the resultSet
     * @return
     */
    List<Datamap> findDatamapListByCriteria(int start, int amount, String column, String dir, String searchTerm, String individualSearch);
    
    /**
     * 
     * @param name Key of the table
     * @param columnName Name of the column to update
     * @param value New value of the field columnName for the key name 
     */
    void updateDatamap(String name, String columnName, String value);
    
    /**
     * 
     * @param searchTerm words to be searched in every column (Exemple : article)
     * @param inds part of the script to add to where clause (Exemple : `type` = 'Article')
     * @return The number of records for these criterias
     */
    Integer getNumberOfDatamapPerCrtiteria(String searchTerm, String inds);
    
    /**
     * 
     * @param colName name of the column of the datamap table
     * @return List of distinct properties of this column
     */
    List<String> findDistinctValuesfromColumn (String colName);
    
     /**
     * 
     * @param column Name of the column
     * @param value value to put in the where clause
     * @return 
     */
    List<Datamap> findDatamapListByColumnValue(String column, String value);
    
    /**
     * 
     * @param criteria
     * @return 
     */
    boolean allImplementedByCriteria(String column, String value);
}


