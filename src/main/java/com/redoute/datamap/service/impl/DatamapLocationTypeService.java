package com.redoute.datamap.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.redoute.datamap.dao.IDatamapLocationTypeDAO;
import com.redoute.datamap.service.IDatamapLocationTypeService;

@Service
public class DatamapLocationTypeService implements IDatamapLocationTypeService {

	@Autowired
	private IDatamapLocationTypeDAO datamapLocationTypeDAO;

	@Override
	public List<String> findDistinctValuesfromColumn(String colName) {
		return datamapLocationTypeDAO.findDistinctValuesfromColumn(colName);
	}

}
