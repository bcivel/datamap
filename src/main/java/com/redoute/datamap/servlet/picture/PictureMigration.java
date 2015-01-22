package com.redoute.datamap.servlet.picture;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.redoute.datamap.dao.IPictureDAO;
import com.redoute.datamap.database.DatabaseSpring;
import com.redoute.datamap.entity.Picture;
import com.redoute.datamap.util.DAOUtil;

/**
 * Migration process from Picture only handled by database to Picture handled by
 * the file system
 * 
 * !! FOR MIGRATION ONLY. TO DELETE WHEN MIGRATION IS DONE !!
 * 
 * @author abourdon
 */
@Repository
public class PictureMigration extends HttpServlet {

	private static final Logger LOG = Logger.getLogger(PictureMigration.class);

	private static final long serialVersionUID = 1L;

	private DatabaseSpring databaseSpring;

	private IPictureDAO pictureDAO;

	private HttpServletResponse response;

	@Override
	public void init() throws ServletException {
		ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		pictureDAO = applicationContext.getBean(IPictureDAO.class);
		databaseSpring = applicationContext.getBean(DatabaseSpring.class);
	}

	/**
	 * !! FOR MIGRATION ONLY. TO DELETE WHEN MIGRATION IS DONE !!
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.response = response;
		processRequest();
	}

	private void processRequest() {
		sendMessage("Staring migration...");
		List<Picture> pictures = pictureDAO.findPicturePerClause(" AND `base64` IS NOT NULL AND `base64` <> '' ");
		if (pictures == null) {
			sendMessage("Error on picture retrieving. Exiting.");
			return;
		}
		for (Picture picture : pictures) {
			sendMessage("Processing " + picture + "... ");
			copyPictureToFS(picture);
			clearPictureBase64FromDatabase(picture);
			sendMessage("Done.");
		}
		sendMessage("Migration done.");
	}

	private void copyPictureToFS(Picture picture) {
		sendMessage("\tCopying base64 data to " + picture.getLocalPath() + "... ", false);
		pictureDAO.updatePicture(Integer.toString(picture.getId()), "base64", picture.getBase64());
		sendMessage("Done.");
	}

	private void clearPictureBase64FromDatabase(Picture picture) {
		sendMessage("\tClearing base64 data from database... ", false);
		Connection connection = databaseSpring.connect();
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement("UPDATE `picture` SET `base64`=? WHERE `id`=?");
			statement.setString(1, "");
			statement.setInt(2, picture.getId());
			statement.executeUpdate();
			sendMessage("Done.");
		} catch (SQLException e) {
			sendMessage("And error occurred: " + e.getMessage());
			try {
				e.printStackTrace(response.getWriter());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} finally {
			DAOUtil.closeResources(statement, connection);
		}
	}

	private void sendMessage(String message) {
		sendMessage(message, true);
	}

	private void sendMessage(String message, boolean newLine) {
		if (newLine) {
			message += "\n";
		}
		try {
			response.getWriter().write(message);
			response.getWriter().flush();
		} catch (IOException e) {
			LOG.error("Unable to send message " + message + " to the response");
		}
	}

}
