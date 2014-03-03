/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.redoute.datamap.dao.impl;

import com.redoute.datamap.dao.IDatamapDAO;
import com.redoute.datamap.database.DatabaseSpring;
import com.redoute.datamap.entity.Datamap;
import com.redoute.datamap.factory.IFactoryDatamap;
import com.redoute.datamap.log.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * {Insert class description here}
 *
 * @author bcivel
 */
@Repository
public class DatamapDAO implements IDatamapDAO {

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
        query.append("INSERT INTO datamap (`id`,`stream`,`page`,`datacerberus`,`implemented`, `xpath`, `picture`) ");
        query.append("VALUES (0,?,?,?,?,?,?)");

        Connection connection = this.databaseSpring.connect();
        try {
            PreparedStatement preStat = connection.prepareStatement(query.toString());
            try {
                preStat.setString(1, datamap.getStream());
                preStat.setString(2, datamap.getPage());
                preStat.setString(3, datamap.getDatacerberus());
                preStat.setString(4, datamap.getImplemented());
                preStat.setString(5, datamap.getXpath());
                preStat.setString(6, datamap.getPicture());

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
        final String query = "SELECT * FROM Datamap";

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
        gSearch.append(" or `stream` like '%");
        gSearch.append(searchTerm);
        gSearch.append("%'");
        gSearch.append(" or `implemented` like '%");
        gSearch.append(searchTerm);
        gSearch.append("%'");
        gSearch.append(" or `xpath` like '%");
        gSearch.append(searchTerm);
        gSearch.append("%'");
        gSearch.append(" or `picture` like '%");
        gSearch.append(searchTerm);
        gSearch.append("%'");
        gSearch.append(" or `datacerberus` like '%");
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
        query.append("order by `");
        query.append(column);
        query.append("` ");
        query.append(dir);
        query.append(" limit ");
        query.append(start);
        query.append(" , ");
        query.append(amount);

        Datamap sqlLibrary;

        Logger.log(DatamapDAO.class.getName(), Level.ERROR, query.toString());
        Connection connection = this.databaseSpring.connect();
        try {
            PreparedStatement preStat = connection.prepareStatement(query.toString());
            try {
                ResultSet resultSet = preStat.executeQuery();
                try {
                    while (resultSet.next()) {
                        sqlLibraryList.add(this.loadDatamapFromResultSet(resultSet));
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

        return sqlLibraryList;
    }

    private Datamap loadDatamapFromResultSet(ResultSet resultSet) throws SQLException {
        Integer id = resultSet.getInt("id");
        String stream = resultSet.getString("stream");
        String page = resultSet.getString("page");
        String datacerberus = resultSet.getString("datacerberus");
        String implemented = resultSet.getString("implemented");
        String xpath = resultSet.getString("xpath");
        String picture = resultSet.getString("picture");

        return factoryDatamap.create(id, stream, page, datacerberus, implemented,xpath,picture);
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
        gSearch.append(" or `stream` like '%");
        gSearch.append(searchTerm);
        gSearch.append("%'");
        gSearch.append(" or `implemented` like '%");
        gSearch.append(searchTerm);
        gSearch.append("%'");
        gSearch.append(" or `xpath` like '%");
        gSearch.append(searchTerm);
        gSearch.append("%'");
        gSearch.append(" or `picture` like '%");
        gSearch.append(searchTerm);
        gSearch.append("%'");
        gSearch.append(" or `datacerberus` like '%");
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
}

