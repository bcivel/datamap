package com.redoute.datamap.service.impl;

import com.redoute.datamap.dao.impl.DatamapDAO;
import com.redoute.datamap.entity.Datamap;
import com.redoute.datamap.service.IDatamapService;
import com.redoute.datamap.service.IGraphGenerationService;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import org.jfree.data.general.DefaultPieDataset;
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
    @Autowired
    IGraphGenerationService graphGenerationService;

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

    @Override
    public List<String> findDistinctValuesfromColumn(String colName) {
        return datamapDao.findDistinctValuesfromColumn(colName);
    }

    @Override
    public List<Datamap> findDatamapListByColumnValue(String column, String value) {
        return datamapDao.findDatamapListByColumnValue(column, value);
    }

    @Override
    public BufferedImage dataImplementedByCriteria(String column, String value) {
        BufferedImage bi = null;
        List<Datamap> datamap = findDatamapListByColumnValue(column, value);
        String title = "Stream : "+ value;
        
        List<String> implemented = new ArrayList();
        List<String> nonimplemented = new ArrayList();
        
        for (Datamap data : datamap){
        if (data.getImplemented().equalsIgnoreCase("Y")){
        implemented.add("Y");
        }else{
        nonimplemented.add("N");
        }
        }
        
        DefaultPieDataset defaultpiedataset = new DefaultPieDataset();
            String legend = "\"Implemented\"";
            String dbl = String.valueOf(implemented.size()) + "D";
            defaultpiedataset.setValue(legend, new Double(dbl));
            legend = "\"Non Implemented\"";
            dbl = String.valueOf(nonimplemented.size()) + "D";
            defaultpiedataset.setValue(legend, new Double(dbl));


        bi = graphGenerationService.generatePieChart(defaultpiedataset, title, 2);

        return bi;
        
    }
}
