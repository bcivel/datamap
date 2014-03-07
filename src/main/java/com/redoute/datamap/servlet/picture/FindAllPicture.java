/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.redoute.datamap.servlet.picture;

import com.redoute.datamap.entity.Picture;
import com.redoute.datamap.log.Logger;
import com.redoute.datamap.service.IPictureService;
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
public class FindAllPicture extends HttpServlet {

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
            String page[] = null;
            String picture[] = null;

            if (request.getParameterValues("page") != null) {
            page = request.getParameterValues("page");
            }
            if (request.getParameterValues("picture") != null) {
            picture = request.getParameterValues("picture");
            }
            List<String> sArray = new ArrayList<String>();
            if (page != null) {
            String spage = " (";
            for (int a = 0; a < page.length - 1; a++) {
                spage += " page like '%" + page[a] + "%' or";
            }
            spage += " page like '%" + page[page.length - 1] + "%') ";
            sArray.add(spage);
        }
            if (picture != null) {
            String spicture = " (";
            for (int a = 0; a < picture.length - 1; a++) {
                spicture += " picture like '%" + picture[a] + "%' or";
            }
            spicture += " picture like '%" + picture[picture.length - 1] + "%') ";
            sArray.add(spicture);
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

            String inds = String.valueOf(individualSearch);

            JSONArray data = new JSONArray(); //data that will be shown in the table

            ApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
            IPictureService pictureService = appContext.getBean(IPictureService.class);

            List<Picture> datamapList = pictureService.findPictureListByCriteria(inds);

            JSONObject jsonResponse = new JSONObject();

            for (Picture datamap : datamapList) {
                JSONArray row = new JSONArray();
                row.put(datamap.getId())
                        .put(datamap.getPage())
                        .put(datamap.getPicture())
                        .put(datamap.getBase64());

                data.put(row);
            }
            
            jsonResponse.put("aaData", data);
            
            response.setContentType("application/json");
            response.getWriter().print(jsonResponse.toString());
        } catch (JSONException ex) {
            Logger.log(FindAllPicture.class.getName(), Level.FATAL, ex.toString());
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
