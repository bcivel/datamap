package com.redoute.datamap.service;

import com.redoute.datamap.entity.Datamap;
import java.util.List;

/**
 *
 * @author bcivel
 */
public interface IDatamapService {

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
     * @param individualSearch search term on a dedicated column of the
     * resultSet
     * @return
     */
    List<Datamap> findDatamapListByCriteria(int start, int amount, String column, String dir, String searchTerm, String individualSearch);
    
    /**
     * 
     * @param name Key of the table
     * @param columnName Name of the column
     * @param value New value of the columnName
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
    List<String> findDistinctValuesfromColumn(String colName);
}
