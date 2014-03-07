package com.redoute.datamap.service.impl;

import com.redoute.datamap.dao.impl.PictureDAO;
import com.redoute.datamap.entity.Picture;
import com.redoute.datamap.service.IPictureService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author bcivel
 */
@Service
public class PictureService implements IPictureService {

    @Autowired
    PictureDAO pictureDao;

    @Override
    public Picture findPictureByKey(String name) {
        return pictureDao.findPictureByKey(name);
    }

    @Override
    public void createPicture(Picture picture) {
        pictureDao.createPicture(picture);
    }

    @Override
    public void deletePicture(Picture picture) {
        pictureDao.deletePicture(picture);
    }

    @Override
    public List<Picture> findAllPicture() {
        return pictureDao.findAllPicture();
    }

    @Override
    public List<Picture> findPictureListByCriteria(int start, int amount, String column, String dir, String searchTerm, String individualSearch) {
        return pictureDao.findPictureListByCriteria(start, amount, column, dir, searchTerm, individualSearch);
    }

    @Override
    public void updatePicture(String name, String columnName, String value) {
        pictureDao.updatePicture(name, columnName, value);
    }

    @Override
    public Integer getNumberOfPicturePerCrtiteria(String searchTerm, String inds) {
        return pictureDao.getNumberOfPicturePerCrtiteria(searchTerm, inds);
    }

    @Override
    public List<Picture> findPicturePerPages(String[] pages) {
        if (pages!=null){
        StringBuilder clause = new StringBuilder();
        for (String page : pages){
        clause.append(" and page = '");
        clause.append(page);
        clause.append("' ");
        }
        return pictureDao.findPicturePerPages(clause.toString());
        } 
        return pictureDao.findAllPicture();
    }

    @Override
    public List<String> findDistinctValuesfromColumn(String colName) {
        return pictureDao.findDistinctValuesfromColumn(colName);
    }
}
