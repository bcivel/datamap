/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redoute.datamap.servlet;

import com.redoute.datamap.log.Logger;
import com.redoute.datamap.service.IDatamapLocationTypeService;
import com.redoute.datamap.service.IDatamapService;
import com.redoute.datamap.service.IPictureService;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;
import org.json.JSONArray;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author bcivel
 */
public class GetDistinctValueFromTableColumn extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.LINKS);
        String table = policy.sanitize(request.getParameter("table"));
        String colName = policy.sanitize(request.getParameter("colName"));

        ApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
        IDatamapService datamapService = appContext.getBean(IDatamapService.class);
        IPictureService pictureService = appContext.getBean(IPictureService.class);
        IDatamapLocationTypeService datamapLocationTypeService = appContext.getBean(IDatamapLocationTypeService.class);

        if (table != null && colName != null) {
            try {
                JSONArray valueList = new JSONArray();
                try {
                    if (table.equals("Datamap")) {
                        for (String value : datamapService.findDistinctValuesfromColumn(colName)) {
                            valueList.put(value);
                        }
                    }
                    if (table.equals("Picture")) {
                        for (String value : pictureService.findDistinctValuesfromColumn(colName)) {
                            valueList.put(value);
                        }
                    }
                    if (table.equals("DatamapLocationType")) {
                    	for (String value : datamapLocationTypeService.findDistinctValuesfromColumn(colName)) {
                            valueList.put(value);
                        }
                    }

                } catch (Exception ex) {
                    response.setContentType("text/html");
                    response.getWriter().print(ex);

                }
                response.setContentType("application/json");
                response.getWriter().print(valueList.toString());
            } catch (Exception e) {
                Logger.log(GetDistinctValueFromTableColumn.class.getName(), Level.FATAL, "" + e);
                response.setContentType("text/html");
                response.getWriter().print(e.getMessage());
            }
        }
    }
}
