/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.redoute.datamap.dao.impl;

import com.redoute.datamap.dao.IPictureDAO;
import com.redoute.datamap.database.DatabaseSpring;
import com.redoute.datamap.entity.Picture;
import com.redoute.datamap.factory.IFactoryPicture;
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
public class PictureDAO implements IPictureDAO {

    /**
     * Description of the variable here.
     */
    @Autowired
    private DatabaseSpring databaseSpring;
    @Autowired
    private IFactoryPicture factoryPicture;

    @Override
    public Picture findPictureByKey(String id) {
        Picture result = null;
        final String query = "SELECT * FROM picture  WHERE id = ?";

        Connection connection = this.databaseSpring.connect();
        try {
            PreparedStatement preStat = connection.prepareStatement(query);
            try {
                preStat.setString(1, id);

                ResultSet resultSet = preStat.executeQuery();
                try {
                    if (resultSet.first()) {
                        result = loadPictureFromResultSet(resultSet);
                    }
                } catch (SQLException exception) {
                    Logger.log(PictureDAO.class.getName(), Level.ERROR, exception.toString());
                } finally {
                    resultSet.close();
                }
            } catch (SQLException exception) {
                Logger.log(PictureDAO.class.getName(), Level.ERROR, exception.toString());
            } finally {
                preStat.close();
            }
        } catch (SQLException exception) {
            Logger.log(PictureDAO.class.getName(), Level.ERROR, exception.toString());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                Logger.log(PictureDAO.class.getName(), Level.WARN, e.toString());
            }
        }
        
        return result;
    }

    @Override
    public void createPicture(Picture picture) {
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO picture (`id`,`page`,`picture`,`base64`) ");
        query.append("VALUES (0,?,?,?)");

        Connection connection = this.databaseSpring.connect();
        try {
            PreparedStatement preStat = connection.prepareStatement(query.toString());
            try {
                preStat.setString(1, picture.getPage());
                preStat.setString(2, picture.getPicture());
                preStat.setString(3, picture.getBase64());

                preStat.executeUpdate();

            } catch (SQLException exception) {
                Logger.log(PictureDAO.class.getName(), Level.ERROR, exception.toString());
            } finally {
                preStat.close();
            }
        } catch (SQLException exception) {
            Logger.log(PictureDAO.class.getName(), Level.ERROR, exception.toString());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                Logger.log(PictureDAO.class.getName(), Level.WARN, e.toString());
            }
        }
        
    }

    @Override
    public void deletePicture(Picture picture) {
        StringBuilder query = new StringBuilder();
        query.append("delete from picture where `id`=? ");

        Connection connection = this.databaseSpring.connect();
        try {
            PreparedStatement preStat = connection.prepareStatement(query.toString());
            try {
                preStat.setString(1, picture.getId().toString());

                preStat.executeUpdate();
                
            } catch (SQLException exception) {
                Logger.log(PictureDAO.class.getName(), Level.ERROR, exception.toString());
            } finally {
                preStat.close();
            }
        } catch (SQLException exception) {
            Logger.log(PictureDAO.class.getName(), Level.ERROR, exception.toString());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                Logger.log(PictureDAO.class.getName(), Level.WARN, e.toString());
            }
        }
    }

    @Override
    public List<Picture> findAllPicture() {
        List<Picture> list = null;
        final String query = "SELECT * FROM picture";

        Connection connection = this.databaseSpring.connect();
        try {
            PreparedStatement preStat = connection.prepareStatement(query);
            try {
                ResultSet resultSet = preStat.executeQuery();
                list = new ArrayList<Picture>();
                try {
                    while (resultSet.next()) {
                        list.add(this.loadPictureFromResultSet(resultSet));
                    }
                } catch (SQLException exception) {
                    Logger.log(PictureDAO.class.getName(), Level.ERROR, exception.toString());
                } finally {
                    resultSet.close();
                }
            } catch (SQLException exception) {
                Logger.log(PictureDAO.class.getName(), Level.ERROR, exception.toString());
            } finally {
                preStat.close();
            }
        } catch (SQLException exception) {
            Logger.log(PictureDAO.class.getName(), Level.ERROR, exception.toString());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                Logger.log(PictureDAO.class.getName(), Level.WARN, e.toString());
            }
        }
        return list;
    }

    @Override
    public List<Picture> findPictureListByCriteria(String individualSearch) {
        List<Picture> pictureList = new ArrayList<Picture>();
        StringBuilder gSearch = new StringBuilder();
        StringBuilder searchSQL = new StringBuilder();

        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM picture where 1=1 ");

        if (!individualSearch.equals("")) {
            searchSQL.append(individualSearch);
            } 

        query.append(searchSQL);
        
        Logger.log("test", Level.FATAL, query.toString());
        Connection connection = this.databaseSpring.connect();
        try {
            PreparedStatement preStat = connection.prepareStatement(query.toString());
            try {
                ResultSet resultSet = preStat.executeQuery();
                try {

                    while (resultSet.next()) {
                        pictureList.add(this.loadPictureFromResultSet(resultSet));
                    }

                } catch (SQLException exception) {
                    Logger.log(PictureDAO.class.getName(), Level.ERROR, exception.toString());
                } finally {
                    resultSet.close();
                }

            } catch (SQLException exception) {
                Logger.log(PictureDAO.class.getName(), Level.ERROR, exception.toString());
            } finally {
                preStat.close();
            }

        } catch (SQLException exception) {
            Logger.log(PictureDAO.class.getName(), Level.ERROR, exception.toString());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                Logger.log(PictureDAO.class.getName(), Level.ERROR, e.toString());
            }
        }

        return pictureList;
    }

    private Picture loadPictureFromResultSet(ResultSet resultSet) throws SQLException {
        Integer id = resultSet.getInt("id");
        String page = resultSet.getString("page");
        String picture = resultSet.getString("picture");
        String base64 = resultSet.getString("base64");

        return factoryPicture.create(id, page, picture, base64);
    }

    @Override
    public void updatePicture(String id, String columnName, String value) {
        boolean throwExcep = false;
        StringBuilder query = new StringBuilder();
        query.append("update picture set `");
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
                Logger.log(PictureDAO.class.getName(), Level.ERROR, exception.toString());
            } finally {
                preStat.close();
            }
        } catch (SQLException exception) {
            Logger.log(PictureDAO.class.getName(), Level.ERROR, exception.toString());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                Logger.log(PictureDAO.class.getName(), Level.WARN, e.toString());
            }
        }
        
    }

    @Override
    public Integer getNumberOfPicturePerCrtiteria(String searchTerm, String inds) {
        Integer result = 0;
        StringBuilder query = new StringBuilder();
        StringBuilder gSearch = new StringBuilder();
        String searchSQL = "";

        query.append("SELECT count(*) FROM picture");

        gSearch.append(" where (`id` like '%");
        gSearch.append(searchTerm);
        gSearch.append("%'");
        gSearch.append(" or `page` like '%");
        gSearch.append(searchTerm);
        gSearch.append("%'");
        gSearch.append(" or `base64` like '%");
        gSearch.append(searchTerm);
        gSearch.append("%'");
        gSearch.append(" or `picture` like '%");
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
                    Logger.log(PictureDAO.class.getName(), Level.ERROR, exception.toString());
                } finally {
                    resultSet.close();
                }

            } catch (SQLException exception) {
                Logger.log(PictureDAO.class.getName(), Level.ERROR, exception.toString());
            } finally {
                preStat.close();
            }

        } catch (SQLException exception) {
            Logger.log(PictureDAO.class.getName(), Level.ERROR, exception.toString());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                Logger.log(PictureDAO.class.getName(), Level.ERROR, e.toString());
            }
        }
        return result;

    }

    @Override
    public List<Picture> findPicturePerPages(String whereClause) {
        List<Picture> list = null;
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM picture where 1=1 ");
        query.append(whereClause);
        
        Connection connection = this.databaseSpring.connect();
        try {
            PreparedStatement preStat = connection.prepareStatement(query.toString());
            try {
                ResultSet resultSet = preStat.executeQuery();
                list = new ArrayList<Picture>();
                try {
                    while (resultSet.next()) {
                        list.add(this.loadPictureFromResultSet(resultSet));
                    }
                } catch (SQLException exception) {
                    Logger.log(PictureDAO.class.getName(), Level.ERROR, exception.toString());
                } finally {
                    resultSet.close();
                }
            } catch (SQLException exception) {
                Logger.log(PictureDAO.class.getName(), Level.ERROR, exception.toString());
            } finally {
                preStat.close();
            }
        } catch (SQLException exception) {
            Logger.log(PictureDAO.class.getName(), Level.ERROR, exception.toString());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                Logger.log(PictureDAO.class.getName(), Level.WARN, e.toString());
            }
        }
        return list;
    }
    
    @Override
    public List<String> findDistinctValuesfromColumn(String colName) {
        List<String> result = new ArrayList<String>();
        
        StringBuilder query = new StringBuilder();
        query.append("SELECT distinct ");
        query.append(colName);
        query.append(" FROM picture order by ");
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
                    Logger.log(PictureDAO.class.getName(), Level.ERROR, exception.toString());
                } finally {
                    resultSet.close();
                } 
         
            } catch (SQLException exception) {
                Logger.log(PictureDAO.class.getName(), Level.ERROR, exception.toString());
            } finally {
                preStat.close();
            }
        
        } catch (SQLException exception) {
            Logger.log(PictureDAO.class.getName(), Level.ERROR, exception.toString());
        } finally {
            try {
                if (connection != null) {
                    Logger.log(PictureDAO.class.getName(), Level.INFO, "Disconnecting to jdbc/qualityfollowup from findDistinctValuesfromParameter");
                    connection.close();
                }
            } catch (SQLException e) {
                Logger.log(PictureDAO.class.getName(), Level.WARN, e.toString());
            }
        }
        return result;  }
}

