package com.redoute.datamap.service.impl;

import com.redoute.datamap.dao.impl.DatamapDAO;
import com.redoute.datamap.entity.Datamap;
import com.redoute.datamap.service.IDatamapService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author bcivel
 */
@Service
public class DatamapService implements IDatamapService {

    @Autowired
    DatamapDAO datamapDao;

    @Override
    public Datamap findDatamapByKey(String name) {
        return datamapDao.findDatamapByKey(name);
    }

    @Override
    public void createDatamap(Datamap datamap) {
        datamapDao.createDatamap(datamap);
    }

    @Override
    public void deleteDatamap(Datamap datamap) {
        datamapDao.deleteDatamap(datamap);
    }

    @Override
    public List<Datamap> findAllDatamap() {
        return datamapDao.findAllDatamap();
    }

    @Override
    public List<Datamap> findDatamapListByCriteria(int start, int amount, String column, String dir, String searchTerm, String individualSearch) {
        return datamapDao.findDatamapListByCriteria(start, amount, column, dir, searchTerm, individualSearch);
    }

    @Override
    public void updateDatamap(String name, String columnName, String value) {
        datamapDao.updateDatamap(name, columnName, value);
    }

    @Override
    public Integer getNumberOfDatamapPerCrtiteria(String searchTerm, String inds) {
        return datamapDao.getNumberOfDatamapPerCrtiteria(searchTerm, inds);
    }
}
