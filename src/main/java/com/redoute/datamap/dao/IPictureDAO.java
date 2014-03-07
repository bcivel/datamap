package com.redoute.datamap.dao;

import com.redoute.datamap.entity.Picture;
import java.util.List;


/**
 * {Insert class description here}
 *
 * @author Tiago Bernardes
 * @version 1.0, 22/03/2013
 * @since 2.0.0
 */
public interface IPictureDAO {

    Picture findPictureByKey(String name);
    
    /**
     *
     * @param picture picture to insert
     */
    void createPicture(Picture picture);

    /**
     *
     * @param picture picture to delete
     */
    void deletePicture(Picture picture);

    /**
     *
     * @return All Picture
     */
    List<Picture> findAllPicture();

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
    List<Picture> findPictureListByCriteria(int start, int amount, String column, String dir, String searchTerm, String individualSearch);
    
    /**
     * 
     * @param name Key of the table
     * @param columnName Name of the column to update
     * @param value New value of the field columnName for the key name 
     */
    void updatePicture(String name, String columnName, String value);
    
    /**
     * 
     * @param searchTerm words to be searched in every column (Exemple : article)
     * @param inds part of the script to add to where clause (Exemple : `type` = 'Article')
     * @return The number of records for these criterias
     */
    Integer getNumberOfPicturePerCrtiteria(String searchTerm, String inds);
    
    /**
     * 
     * @param whereClause
     * @return 
     */
    List<Picture> findPicturePerPages(String whereClause);
    
    /**
     * 
     * @param colName name of the column of the datamap table
     * @return List of distinct properties of this column
     */
    List<String> findDistinctValuesfromColumn (String colName);
}


