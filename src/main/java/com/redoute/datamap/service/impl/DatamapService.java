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
        List<String> distinctValues = findDistinctValuesfromColumn("implemented");
        String title = "Stream : "+ value;
        
        List<String> implemented = new ArrayList();
        List<String> nonimplemented = new ArrayList();
        List<List<String>> valueList = new ArrayList();
        
        for (String valueDis : distinctValues){
        List<String> test = new ArrayList();
        test.add(valueDis);
        valueList.add(test);
        }
                
                
        for (Datamap data : datamap){
            for (int a = 0 ; a < valueList.size(); a++){
                if (data.getImplemented().equalsIgnoreCase(valueList.get(a).get(0))){
                valueList.get(a).add(valueList.get(a).get(0));
        }
        }
        }
        
        DefaultPieDataset defaultpiedataset = new DefaultPieDataset();
        for (List<String> titi : valueList){
            if (titi.size()-1!=0){
            String legend = "\""+titi.get(0) +"\"";
            String dbl = String.valueOf(titi.size()-1) + "D";
            defaultpiedataset.setValue(legend, new Double(dbl));
            }
        }
            


        bi = graphGenerationService.generatePieChart(defaultpiedataset, title, 2);

        return bi;
        
    }

    @Override
    public boolean allImplementedByCriteria(String column, String value) {
        return datamapDao.allImplementedByCriteria(column, value);
    }
}
