/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.redoute.datamap.servlet.datamap;

import com.redoute.datamap.entity.Datamap;
import com.redoute.datamap.log.Logger;
import com.redoute.datamap.service.IDatamapService;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Level;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author bcivel
 */
public class FindAllDatamap extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.LINKS);
        
        try {
            String echo = policy.sanitize(request.getParameter("sEcho"));
            String sStart = policy.sanitize(request.getParameter("iDisplayStart"));
            String sAmount = policy.sanitize(request.getParameter("iDisplayLength"));
            String sCol = policy.sanitize(request.getParameter("iSortCol_0"));
            String sdir = policy.sanitize(request.getParameter("sSortDir_0"));
            String dir = "asc";
            String[] cols = { "Id", "Stream", "Page","DataCerberus","Picture","Xpath", "Implemented"};

            JSONObject result = new JSONObject();
            JSONArray array = new JSONArray();
            int amount = 10;
            int start = 0;
            int col = 0;

            String id = "";
            String[] stream = null;
            String[] page = null;
            String datacerberus = "";
            String implemented = "";
            String picture = "";
            String xpath = "";

            if (request.getParameterValues("page") != null) {
            page = request.getParameterValues("page");
            }
            if (request.getParameterValues("stream") != null) {
            stream = request.getParameterValues("stream");
            }
            
            id = policy.sanitize(request.getParameter("sSearch_0"));
            datacerberus = policy.sanitize(request.getParameter("sSearch_3"));
            picture = policy.sanitize(request.getParameter("sSearch_4"));
            xpath = policy.sanitize(request.getParameter("sSearch_5"));
            implemented = policy.sanitize(request.getParameter("sSearch_6"));

            List<String> sArray = new ArrayList<String>();
            if (!id.equals("")) {
                String sId = " `Id` like '%" + id + "%'";
                sArray.add(sId);
            }
            if (page != null) {
            String spage = " (";
            for (int a = 0; a < page.length - 1; a++) {
                spage += " `page` like '%" + page[a] + "%' or";
            }
            spage += " `page` like '%" + page[page.length - 1] + "%') ";
            sArray.add(spage);
            }
            if (stream != null) {
            String sstream = " (";
            for (int a = 0; a < stream.length - 1; a++) {
                sstream += " stream like '%" + stream[a] + "%' or";
            }
            sstream += " stream like '%" + stream[stream.length - 1] + "%') ";
            sArray.add(sstream);
        }
            if (!datacerberus.equals("")) {
                String sDatacerberus = " `datacerberus` like '%" + datacerberus + "%'";
                sArray.add(sDatacerberus);
            }
            if (!implemented.equals("")) {
                String sImplemented = " `implemented` like '%" + implemented + "%'";
                sArray.add(sImplemented);
            }
            if (!picture.equals("")) {
                String spicture = " `picture` like '%" + picture + "%'";
                sArray.add(spicture);
            }
            if (!xpath.equals("")) {
                String sxpath = " `xpath` like '%" + xpath + "%'";
                sArray.add(sxpath);
            }

            StringBuilder individualSearch = new StringBuilder();
            if (sArray.size() == 1) {
                individualSearch.append(sArray.get(0));
            } else if (sArray.size() > 1) {
                for (int i = 0; i < sArray.size() - 1; i++) {
                    individualSearch.append(sArray.get(i));
                    individualSearch.append(" and ");
                }
                individualSearch.append(sArray.get(sArray.size() - 1));
            }

            if (sStart != null) {
                start = Integer.parseInt(sStart);
                if (start < 0) {
                    start = 0;
                }
            }
            if (sAmount != null) {
                amount = Integer.parseInt(sAmount);
                if (amount < 10 || amount > 100) {
                    amount = 10;
                }
            }

            if (sCol != null) {
                col = Integer.parseInt(sCol);
                if (col < 0 || col > 5) {
                    col = 0;
                }
            }
            if (sdir != null) {
                if (!sdir.equals("asc")) {
                    dir = "desc";
                }
            }
            String colName = cols[col];

            String searchTerm = "";
            if (!request.getParameter("sSearch").equals("")) {
                searchTerm = request.getParameter("sSearch");
            }

            String inds = String.valueOf(individualSearch);

            JSONArray data = new JSONArray(); //data that will be shown in the table

            ApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
            IDatamapService datamapService = appContext.getBean(IDatamapService.class);

            List<Datamap> datamapList = datamapService.findDatamapListByCriteria(start, amount, colName, dir, searchTerm, inds);

            JSONObject jsonResponse = new JSONObject();

            for (Datamap datamap : datamapList) {
                JSONArray row = new JSONArray();
                row.put(datamap.getId())
                        .put(datamap.getStream())
                        .put(datamap.getPage())
                        .put(datamap.getDatacerberus())
                        .put(datamap.getPicture())
                        .put(datamap.getXpath())
                        .put(datamap.getImplemented());

                data.put(row);
            }
            
            Integer numberOfTotalRows = datamapService.getNumberOfDatamapPerCrtiteria(searchTerm, inds);
            
            jsonResponse.put("aaData", data);
            jsonResponse.put("sEcho", echo);
            jsonResponse.put("iTotalRecords", numberOfTotalRows);
            jsonResponse.put("iDisplayLength", data.length());
            jsonResponse.put("iTotalDisplayRecords", numberOfTotalRows);
            

            response.setContentType("application/json");
            response.getWriter().print(jsonResponse.toString());
        } catch (JSONException ex) {
            Logger.log(FindAllDatamap.class.getName(), Level.FATAL, ex.toString());
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
