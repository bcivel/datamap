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
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.redoute.datamap.dao.IPictureDAO;
import com.redoute.datamap.database.DatabaseSpring;
import com.redoute.datamap.entity.Picture;
import com.redoute.datamap.factory.IFactoryPicture;
import com.redoute.datamap.log.Logger;
import com.redoute.datamap.util.DAOUtil;
import com.redoute.datamap.util.HTML5CanvasURLUtil.HTML5CanvasURLParsingException;
import com.redoute.datamap.util.PictureFileHelper;

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
    @Autowired
    private PictureFileHelper pictureFileHelper;

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
        query.append("INSERT INTO picture (`id`,`application`,`page`,`picture`) ");
        query.append("VALUES (0,?,?,?)");

        Connection connection = this.databaseSpring.connect();
        try {
            PreparedStatement preStat = connection.prepareStatement(query.toString());
            try {
                preStat.setString(1, picture.getApplication());
                preStat.setString(2, picture.getPage());
                preStat.setString(3, picture.getPicture());

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
        
        pictureFileHelper.save(picture, false);
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
        
        pictureFileHelper.save(picture, false);
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
    public List<Picture> findPictureListByCriteria(String individualSearch, String joinedSearch) {
        List<Picture> pictureList = new ArrayList<Picture>();
        StringBuilder searchSQL = new StringBuilder();
        StringBuilder searchSQL2 = new StringBuilder();

        StringBuilder query = new StringBuilder();
        query.append("SELECT p.id, p.application, p.page, p.picture, '' as base64, p.localpath FROM picture p ");
        
        if (!joinedSearch.equals("")){
        query.append(" join datamap d on p.page=d.page and p.picture=d.picture and p.application=d.application");
        }
        
        query.append(" where 1=1 ");
        
        if (!joinedSearch.equals("")){
        searchSQL.append(joinedSearch);
        }

        if (!individualSearch.equals("")) {
            searchSQL.append(individualSearch);
            } 

        query.append(searchSQL2);
        query.append(searchSQL);
        
        query.append(" group by p.page,p.picture ");
        
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
		String application = resultSet.getString("application");
		String page = resultSet.getString("page");
		String picture = resultSet.getString("picture");
		String base64 = resultSet.getString("base64");
		String localPath = resultSet.getString("localpath");
		Picture pic = factoryPicture.create(id, application, page, picture, base64, localPath);
		return DAOUtil.isEmpty(localPath) ? pic : pictureFileHelper.load(pic);
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
        
		if (columnName.equals("base64")) {
			Picture pic = findPictureByKey(id);
			pic.setBase64(value);
			try {
				if (DAOUtil.isEmpty(pic.getLocalPath())) {
					pic.setLocalPath(pictureFileHelper.createLocalPath(pic));
					updatePicture(id, "localpath", pic.getLocalPath());
				}
				pictureFileHelper.save(pic, true);
			} catch (HTML5CanvasURLParsingException e) {
				Log.error("Unable to update picture " + pic, e);
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
        gSearch.append(" or `application` like '%");
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

