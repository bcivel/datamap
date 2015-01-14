package com.redoute.datamap.servlet.datamap;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.redoute.datamap.entity.Datamap;
import com.redoute.datamap.service.IDatamapService;
import com.redoute.datamap.util.IndividualSearchUtil;

/**
 * Find locations from available datamaps.
 * 
 * <h2>Input parameters</h2>
 * <p>
 * This {@link HttpServlet} accepts the following parameters:
 * </p>
 * <ul>
 * <li>Application: The application name to find location</li>
 * <li>Page: The page to find location</li>
 * <li>Zone: The zone to find location</li>
 * </ul>
 * 
 * <h2>Output parameters</h2>
 * <p>
 * This {@link HttpServlet} returns a {@link JSONObject} with the following
 * parameters:
 * </p>
 * <ul>
 * <li>Locations: Array of locations that satisfy the given input parameters</li>
 * </ul>
 * 
 * @author abourdon
 */
public class FindLocations extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/** The associated {@link Logger} to this class */
	private static final Logger LOG = Logger.getLogger(FindLocations.class);

	/** Set of available parameter keys */
	public static enum ParameterKey {
		Application, Page, Picture, Zone
	}

	/** Set of available response keys */
	public static enum ResponseKey {
		Locations
	}

	/** Access to the {@link IDatamapService} service */
	private IDatamapService datamapService;

	@Override
	public void init() throws ServletException {
		ApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
		datamapService = appContext.getBean(IDatamapService.class);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Processes the request regardless the way to reach this
	 * {@link HttpServlet} (GET or POST).
	 * 
	 * @param request
	 *            the associated request
	 * @param response
	 *            the associated response
	 * @throws ServletException
	 *             if an internal servlet error occurred
	 * @throws IOException
	 *             if an I/O error occurred
	 */
	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Get parameters from the request
		PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.LINKS);
		final String application = policy.sanitize(request.getParameter(ParameterKey.Application.name()));
		final String page = policy.sanitize(request.getParameter(ParameterKey.Page.name()));
		final String picture = policy.sanitize(request.getParameter(ParameterKey.Picture.name()));
		final String zone = policy.sanitize(request.getParameter(ParameterKey.Zone.name()));

		// Find available datamaps according to the given parameters
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(ParameterKey.Application.name(), application);
		parameters.put(ParameterKey.Page.name(), page);
		parameters.put(ParameterKey.Picture.name(), picture);
		parameters.put(ParameterKey.Zone.name(), zone);
		String individualSearch = IndividualSearchUtil.constructIndividualSearch(parameters);
		List<Datamap> datamaps = datamapService.findDatamapListByCriteria(-1, -1, ParameterKey.Application.name(), "ASC", "", individualSearch);

		// Construct the response according to the datamaps
		JSONArray locations = new JSONArray();
		for (Datamap datamap : datamaps) {
			locations.put(datamap.getLocation());
		}
		JSONObject jsonResponse = new JSONObject();
		try {
			jsonResponse.put(ResponseKey.Locations.name(), locations);
		} catch (JSONException e) {
			LOG.error("Unable to construct JSON response for locations " + locations.toString(), e);
			throw new ServletException(e);
		}
		response.setContentType("application/json");
		response.getWriter().print(jsonResponse.toString());
	}

}
