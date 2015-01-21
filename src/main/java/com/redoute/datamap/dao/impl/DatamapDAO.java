/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.redoute.datamap.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.redoute.datamap.dao.IDatamapDAO;
import com.redoute.datamap.database.DatabaseSpring;
import com.redoute.datamap.entity.Datamap;
import com.redoute.datamap.factory.IFactoryDatamap;
import com.redoute.datamap.log.Logger;
import com.redoute.datamap.util.DAOUtil;

/**
 * {Insert class description here}
 *
 * @author bcivel
 */
@Repository
public class DatamapDAO implements IDatamapDAO {

	/** Associated {@link org.apache.log4j.Logger} to this class */
	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DatamapDAO.class);
	
    /**
     * Description of the variable here.
     */
    @Autowired
    private DatabaseSpring databaseSpring;
    @Autowired
    private IFactoryDatamap factoryDatamap;

    @Override
    public Datamap findDatamapByKey(String id) {
        Datamap result = null;
        final String query = "SELECT * FROM datamap  WHERE id = ?";

        Connection connection = this.databaseSpring.connect();
        try {
            PreparedStatement preStat = connection.prepareStatement(query);
            try {
                preStat.setString(1, id);

                ResultSet resultSet = preStat.executeQuery();
                try {
                    if (resultSet.first()) {
                        result = loadDatamapFromResultSet(resultSet);
                    }
                } catch (SQLException exception) {
                    Logger.log(DatamapDAO.class.getName(), Level.ERROR, exception.toString());
                } finally {
                    resultSet.close();
                }
            } catch (SQLException exception) {
                Logger.log(DatamapDAO.class.getName(), Level.ERROR, exception.toString());
            } finally {
                preStat.close();
            }
        } catch (SQLException exception) {
            Logger.log(DatamapDAO.class.getName(), Level.ERROR, exception.toString());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                Logger.log(DatamapDAO.class.getName(), Level.WARN, e.toString());
            }
        }
        
        return result;
    }

    @Override
    public void createDatamap(Datamap datamap) {
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO datamap (`id`,`stream`,`application`,`page`,`locationType`,`locationValue`,`implemented`, `zone`, `picture`, `comment`) ");
        query.append("VALUES (0,?,?,?,?,?,?,?,?,?)");

        Connection connection = this.databaseSpring.connect();
        try {
            PreparedStatement preStat = connection.prepareStatement(query.toString());
            try {
                preStat.setString(1, datamap.getStream());
                preStat.setString(2, datamap.getApplication());
                preStat.setString(3, datamap.getPage());
                preStat.setString(4, datamap.getLocationType());
                preStat.setString(5, datamap.getLocationValue());
                preStat.setString(6, datamap.getImplemented());
                preStat.setString(7, datamap.getZone());
                preStat.setString(8, datamap.getPicture());
                preStat.setString(9, datamap.getComment());

                preStat.executeUpdate();

            } catch (SQLException exception) {
                Logger.log(DatamapDAO.class.getName(), Level.ERROR, exception.toString());
            } finally {
                preStat.close();
            }
        } catch (SQLException exception) {
            Logger.log(DatamapDAO.class.getName(), Level.ERROR, exception.toString());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                Logger.log(DatamapDAO.class.getName(), Level.WARN, e.toString());
            }
        }
        
    }

    @Override
    public void deleteDatamap(Datamap datamap) {
        StringBuilder query = new StringBuilder();
        query.append("delete from datamap where `id`=? ");

        Connection connection = this.databaseSpring.connect();
        try {
            PreparedStatement preStat = connection.prepareStatement(query.toString());
            try {
                preStat.setString(1, datamap.getId().toString());

                preStat.executeUpdate();
                
            } catch (SQLException exception) {
                Logger.log(DatamapDAO.class.getName(), Level.ERROR, exception.toString());
            } finally {
                preStat.close();
            }
        } catch (SQLException exception) {
            Logger.log(DatamapDAO.class.getName(), Level.ERROR, exception.toString());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                Logger.log(DatamapDAO.class.getName(), Level.WARN, e.toString());
            }
        }
    }

    @Override
    public List<Datamap> findAllDatamap() {
        List<Datamap> list = null;
        final String query = "SELECT * FROM datamap";

        Connection connection = this.databaseSpring.connect();
        try {
            PreparedStatement preStat = connection.prepareStatement(query);
            try {
                ResultSet resultSet = preStat.executeQuery();
                list = new ArrayList<Datamap>();
                try {
                    while (resultSet.next()) {
                        list.add(this.loadDatamapFromResultSet(resultSet));
                    }
                } catch (SQLException exception) {
                    Logger.log(DatamapDAO.class.getName(), Level.ERROR, exception.toString());
                } finally {
                    resultSet.close();
                }
            } catch (SQLException exception) {
                Logger.log(DatamapDAO.class.getName(), Level.ERROR, exception.toString());
            } finally {
                preStat.close();
            }
        } catch (SQLException exception) {
            Logger.log(DatamapDAO.class.getName(), Level.ERROR, exception.toString());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                Logger.log(DatamapDAO.class.getName(), Level.WARN, e.toString());
            }
        }
        return list;
    }

    @Override
	public List<Datamap> findDatamapListByCriteria(int start, int amount, String column, String dir, String searchTerm, String individualSearch) {
		List<Datamap> sqlLibraryList = new ArrayList<Datamap>();
		StringBuilder gSearch = new StringBuilder();
		StringBuilder searchSQL = new StringBuilder();

		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM datamap ");

		gSearch.append(" where (`id` like '%");
		gSearch.append(searchTerm);
		gSearch.append("%'");
		gSearch.append(" or `page` like '%");
		gSearch.append(searchTerm);
		gSearch.append("%'");
		gSearch.append(" or `application` like '%");
		gSearch.append(searchTerm);
		gSearch.append("%'");
		gSearch.append(" or `stream` like '%");
		gSearch.append(searchTerm);
		gSearch.append("%'");
		gSearch.append(" or `implemented` like '%");
		gSearch.append(searchTerm);
		gSearch.append("%'");
		gSearch.append(" or `zone` like '%");
		gSearch.append(searchTerm);
		gSearch.append("%'");
		gSearch.append(" or `picture` like '%");
		gSearch.append(searchTerm);
		gSearch.append("%'");
		gSearch.append(" or `comment` like '%");
		gSearch.append(searchTerm);
		gSearch.append("%'");
		gSearch.append(" or `locationType` like '%");
		gSearch.append(searchTerm);
		gSearch.append("%'");
		gSearch.append(" or `locationValue` like '%");
		gSearch.append(searchTerm);
		gSearch.append("%')");

		if (!searchTerm.equals("") && !individualSearch.equals("")) {
			searchSQL.append(gSearch.toString());
			searchSQL.append(" and ");
			searchSQL.append(individualSearch);
		} else if (!individualSearch.equals("")) {
			searchSQL.append(" where ");
			searchSQL.append(individualSearch);
		} else if (!searchTerm.equals("")) {
			searchSQL.append(gSearch.toString());
		}

		query.append(searchSQL);
		query.append(" order by `");
		query.append(column);
		query.append("` ");
		query.append(dir);

		if (start >= 0 && amount > 0) {
			query.append(" limit ");
			query.append(start);
			query.append(" , ");
			query.append(amount);
		}

		Connection connection = null;
		PreparedStatement preStat = null;
		ResultSet resultSet = null;
		try {
			connection = this.databaseSpring.connect();
			preStat = connection.prepareStatement(query.toString());
			resultSet = preStat.executeQuery();
			while (resultSet.next()) {
				sqlLibraryList.add(this.loadDatamapFromResultSet(resultSet));
			}
		} catch (SQLException e) {
			LOG.error("Unable to findDatamapListByCriteria", e);
		} finally {
			DAOUtil.closeResources(resultSet, preStat, connection);
		}
		
		return sqlLibraryList;
	}

    private Datamap loadDatamapFromResultSet(ResultSet resultSet) throws SQLException {
        Integer id = resultSet.getInt("id");
        String stream = resultSet.getString("stream");
        String application = resultSet.getString("application");
        String page = resultSet.getString("page");
        String locationType = resultSet.getString("locationType");
        String locationValue = resultSet.getString("locationValue");
        String implemented = resultSet.getString("implemented");
        String zone = resultSet.getString("zone");
        String picture = resultSet.getString("picture");
        String comment = resultSet.getString("comment");

        return factoryDatamap.create(id, stream, application, page, locationType, locationValue, implemented, zone, picture, comment);
    }

    @Override
    public void updateDatamap(String id, String columnName, String value) {
        boolean throwExcep = false;
        StringBuilder query = new StringBuilder();
        query.append("update datamap set `");
        query.append(columnName);
        query.append("`=? where `id`=? ");

        Connection connection = this.databaseSpring.connect();
        try {
            PreparedStatement preStat = connection.prepareStatement(query.toString());
            try {
                preStat.setString(1, value);
                preStat.setString(2, id);

                preStat.executeUpdate();
                throwExcep = false;

            } catch (SQLException exception) {
                Logger.log(DatamapDAO.class.getName(), Level.ERROR, exception.toString());
            } finally {
                preStat.close();
            }
        } catch (SQLException exception) {
            Logger.log(DatamapDAO.class.getName(), Level.ERROR, exception.toString());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                Logger.log(DatamapDAO.class.getName(), Level.WARN, e.toString());
            }
        }
        
    }

    @Override
    public Integer getNumberOfDatamapPerCrtiteria(String searchTerm, String inds) {
        Integer result = 0;
        StringBuilder query = new StringBuilder();
        StringBuilder gSearch = new StringBuilder();
        String searchSQL = "";

        query.append("SELECT count(*) FROM datamap");

        gSearch.append(" where (`id` like '%");
        gSearch.append(searchTerm);
        gSearch.append("%'");
        gSearch.append(" or `page` like '%");
        gSearch.append(searchTerm);
        gSearch.append("%'");
        gSearch.append(" or `application` like '%");
        gSearch.append(searchTerm);
        gSearch.append("%'");
        gSearch.append(" or `stream` like '%");
        gSearch.append(searchTerm);
        gSearch.append("%'");
        gSearch.append(" or `implemented` like '%");
        gSearch.append(searchTerm);
        gSearch.append("%'");
        gSearch.append(" or `zone` like '%");
        gSearch.append(searchTerm);
        gSearch.append("%'");
        gSearch.append(" or `picture` like '%");
        gSearch.append(searchTerm);
        gSearch.append("%'");
        gSearch.append(" or `comment` like '%");
        gSearch.append(searchTerm);
        gSearch.append("%'");
        gSearch.append(" or `locationTYpe` like '%");
        gSearch.append(searchTerm);
        gSearch.append("%'");
        gSearch.append(" or `locationValue` like '%");
        gSearch.append(searchTerm);
        gSearch.append("%')");

        if (!searchTerm.equals("") && !inds.equals("")) {
            searchSQL = gSearch.toString() + " and " + inds;
        } else if (!inds.equals("")) {
            searchSQL = " where " + inds;
        } else if (!searchTerm.equals("")) {
            searchSQL = gSearch.toString();
        }

        query.append(searchSQL);

        Connection connection = this.databaseSpring.connect();
        try {
            PreparedStatement preStat = connection.prepareStatement(query.toString());
            try {
                ResultSet resultSet = preStat.executeQuery();
                try {

                    if (resultSet.first()) {
                        result = resultSet.getInt(1);
                    }

                } catch (SQLException exception) {
                    Logger.log(DatamapDAO.class.getName(), Level.ERROR, exception.toString());
                } finally {
                    resultSet.close();
                }

            } catch (SQLException exception) {
                Logger.log(DatamapDAO.class.getName(), Level.ERROR, exception.toString());
            } finally {
                preStat.close();
            }

        } catch (SQLException exception) {
            Logger.log(DatamapDAO.class.getName(), Level.ERROR, exception.toString());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                Logger.log(DatamapDAO.class.getName(), Level.ERROR, e.toString());
            }
        }
        return result;

    }
    
    @Override
    public List<String> findDistinctValuesfromColumn(String colName) {
        List<String> result = new ArrayList<String>();
        
        StringBuilder query = new StringBuilder();
        query.append("SELECT distinct ");
        query.append(colName);
        query.append(" FROM datamap order by ");
        query.append(colName);
        query.append(" asc");
        
        Connection connection = this.databaseSpring.connect();
        try {
            PreparedStatement preStat = connection.prepareStatement(query.toString());
            try {
                ResultSet resultSet = preStat.executeQuery();
                try {
                 
                    while (resultSet.next()) {
                        result.add(resultSet.getString(1)== null ? "" : resultSet.getString(1) );
                    }

            resultSet.close();
                } catch (SQLException exception) {
                    Logger.log(DatamapDAO.class.getName(), Level.ERROR, exception.toString());
                } finally {
                    resultSet.close();
                } 
         
            } catch (SQLException exception) {
                Logger.log(DatamapDAO.class.getName(), Level.ERROR, exception.toString());
            } finally {
                preStat.close();
            }
        
        } catch (SQLException exception) {
            Logger.log(DatamapDAO.class.getName(), Level.ERROR, exception.toString());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                Logger.log(DatamapDAO.class.getName(), Level.WARN, e.toString());
            }
        }
        return result;  }

    @Override
    public List<Datamap> findDatamapListByColumnValue(String column, String value) {
        List<Datamap> result = new ArrayList<Datamap>();
        
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM datamap where `");
        query.append(column);
        query.append("` = ?");
        
        Connection connection = this.databaseSpring.connect();
        try {
            PreparedStatement preStat = connection.prepareStatement(query.toString());
            try {
                preStat.setString(1, value);
                ResultSet resultSet = preStat.executeQuery();
                try {
                        while (resultSet.next()) {
                            result.add(this.loadDatamapFromResultSet(resultSet));
                        }

            resultSet.close();
                } catch (SQLException exception) {
                    Logger.log(DatamapDAO.class.getName(), Level.ERROR, exception.toString());
                } finally {
                    resultSet.close();
                } 
         
            } catch (SQLException exception) {
                Logger.log(DatamapDAO.class.getName(), Level.ERROR, exception.toString());
            } finally {
                preStat.close();
            }
        
        } catch (SQLException exception) {
            Logger.log(DatamapDAO.class.getName(), Level.ERROR, exception.toString());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                Logger.log(DatamapDAO.class.getName(), Level.WARN, e.toString());
            }
        }
        return result;
    }

    @Override
    public boolean allImplementedByCriteria(String column, String value) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT count(*) as val FROM datamap where `");
        query.append(column);
        query.append("` = ? and implemented!='Y'");
        
        Connection connection = this.databaseSpring.connect();
        try {
            PreparedStatement preStat = connection.prepareStatement(query.toString());
            try {
                preStat.setString(1, value);
                ResultSet resultSet = preStat.executeQuery();
                try {
                        if (resultSet.next()) {
                            if (resultSet.getString("val").equals("0")){
                            return true;
                            }
                            
                        }

            resultSet.close();
                } catch (SQLException exception) {
                    Logger.log(DatamapDAO.class.getName(), Level.ERROR, exception.toString());
                } finally {
                    resultSet.close();
                } 
         
            } catch (SQLException exception) {
                Logger.log(DatamapDAO.class.getName(), Level.ERROR, exception.toString());
            } finally {
                preStat.close();
            }
        
        } catch (SQLException exception) {
            Logger.log(DatamapDAO.class.getName(), Level.ERROR, exception.toString());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                Logger.log(DatamapDAO.class.getName(), Level.WARN, e.toString());
            }
        }
        return false;
    }
    

}

