/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.redoute.datamap.servlet.datamap;

import com.redoute.datamap.entity.Datamap;
import com.redoute.datamap.factory.IFactoryDatamap;
import com.redoute.datamap.service.IDatamapService;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author bcivel
 */
public class CreateDatamap extends HttpServlet {

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
        String stream = policy.sanitize(request.getParameter("stream"));
        String application = policy.sanitize(request.getParameter("application"));
        String page = policy.sanitize(request.getParameter("page"));
        String locationType = policy.sanitize(request.getParameter("locationType"));
        String locationValue = policy.sanitize(request.getParameter("locationValue"));
        String implemented = policy.sanitize(request.getParameter("implemented"));
        String zone = policy.sanitize(request.getParameter("zone"));
        String picture = policy.sanitize(request.getParameter("picture"));
        String comment = policy.sanitize(request.getParameter("comment"));
        
        ApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
        IDatamapService datamapService = appContext.getBean(IDatamapService.class);
        IFactoryDatamap factoryDatamap = appContext.getBean(IFactoryDatamap.class);
        
        Datamap datamap = factoryDatamap.create(0, stream, application,  page, locationType, locationValue, implemented , zone, picture, comment);
        datamapService.createDatamap(datamap);
            
        response.sendRedirect("Datamap.jsp");
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
