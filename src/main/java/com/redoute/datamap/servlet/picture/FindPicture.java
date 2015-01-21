/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.redoute.datamap.servlet.picture;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.redoute.datamap.entity.Picture;
import com.redoute.datamap.service.IPictureService;
import com.redoute.datamap.util.HTML5CanvasURLUtil;
import com.redoute.datamap.util.HTML5CanvasURLUtil.HTML5CanvasURLParsingException;
import com.redoute.datamap.util.IndividualSearchUtil;
import com.redoute.datamap.util.StringUtil;

/**
 *
 * @author bcivel
 */
public class FindPicture extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger.getLogger(FindPicture.class);

	public static enum ParameterKey {
		id, Application, Page, Picture;
	}

	public static final String JSON_PICTURE_KEY = "picture";

	private HttpServletResponse response;

	private IPictureService pictureService;

	@Override
	public void init() throws ServletException {
		ApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
		pictureService = appContext.getBean(IPictureService.class);
	}

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 *
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.response = response;

		PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.LINKS);

		// Case of calling servlet only with the ParameterKey#id
		String id = policy.sanitize(request.getParameter(ParameterKey.id.name()));
		if (!StringUtil.isEmpty(id)) {
			processId(id);
		}

		// Case of calling servlet with ParameterKey#Application,
		// ParameterKey#Page and ParameterKey#Picture
		String application = policy.sanitize(request.getParameter(ParameterKey.Application.name()));
		String page = policy.sanitize(request.getParameter(ParameterKey.Page.name()));
		String picture = policy.sanitize(request.getParameter(ParameterKey.Picture.name()));
		if (!(StringUtil.isEmpty(application) && StringUtil.isEmpty(page) && StringUtil.isEmpty(picture))) {
			processApplicationPagePicture(application, page, picture);
		}
	}

	private void processId(String id) throws IOException {
		Picture picture = pictureService.findPictureByKey(id);
		if (picture == null) {
			return;
		}

		try {
			JSONObject jsonResponse = new JSONObject();
			JSONArray row = new JSONArray();
			row.put(picture.getId()).put(picture.getPage()).put(picture.getPicture()).put(picture.getBase64());
			jsonResponse.put(JSON_PICTURE_KEY, row);
			response.setContentType("application/json");
			response.getWriter().print(jsonResponse.toString());
		} catch (JSONException e) {
			LOG.error("Unable to create JSON object", e);
		}
	}

	private void processApplicationPagePicture(final String application, final String page, final String picture) throws IOException {
		@SuppressWarnings("serial")
		String individualSearch = IndividualSearchUtil.constructIndividualSearch(new HashMap<String, String>() {
			{
				put(ParameterKey.Application.name(), application);
				put(ParameterKey.Page.name(), page);
				put(ParameterKey.Picture.name(), picture);
			}
		});
		List<Picture> pictures = pictureService.findPictureListByCriteria(individualSearch, "");
		if (pictures == null || pictures.isEmpty()) {
			return;
		}

		// In case of multiple pictures, takes the first one by default
		Picture pic = pictures.get(0);
		try {
			response.setContentType(String.format("image/%s", HTML5CanvasURLUtil.parseImageType(pic.getBase64())));
			IOUtils.write(HTML5CanvasURLUtil.decodeBase64Data(HTML5CanvasURLUtil.parseBase64Data(pic.getBase64())), response.getOutputStream());
		} catch (HTML5CanvasURLParsingException e) {
			throw new IOException(e);
		}
	}

	// <editor-fold defaultstate="collapsed"
	// desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP <code>GET</code> method.
	 *
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 *
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 *
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>

}
