/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.redoute.datamap.servlet.picture;

import com.redoute.datamap.entity.Picture;
import com.redoute.datamap.factory.IFactoryPicture;
import com.redoute.datamap.service.IPictureService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Level;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author bcivel
 */
public class AddPicture extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String page = "";
        String application = "";
        String pictureName = "";
        String screenshot = "";
        FileItem item = null;
        
        if (ServletFileUpload.isMultipartContent(request)) {
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);

            try {
            
                String fileName = null;
                List items = upload.parseRequest(request);
                List items2 = items;
                Iterator iterator = items.iterator();
                Iterator iterator2 = items2.iterator();
                File uploadedFile = null;
                String idNC = "";
                
                while (iterator.hasNext()) {
                    item = (FileItem) iterator.next();

                    if (item.isFormField()) {
                        String name = item.getFieldName();
                        if (name.equals("Page")) {
                            page = item.getString("UTF-8");
                            System.out.println(page);
                        }
                        if (name.equals("Application")) {
                            application = item.getString("UTF-8");
                            System.out.println(application);
                        }
                        if (name.equals("PictureName")) {
                            pictureName = item.getString("UTF-8");
                            System.out.println(pictureName);
                        }
                        if (name.equals("Screenshot")) {
                            screenshot = item.getString().split("<img src=\"")[1].split("\">")[0];
                            System.out.println(screenshot);
                            System.out.println(screenshot.length());
                        }
                    } 
                }
                
                
            ApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
            IPictureService pictService = appContext.getBean(IPictureService.class);
            IFactoryPicture factoryPicture = appContext.getBean(IFactoryPicture.class);
            
            Picture pict = factoryPicture.create(0, application, page, pictureName, screenshot);
        pictService.createPicture(pict);
        
        response.sendRedirect("Datamap.jsp");
            } catch (FileUploadException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
        
    }
}
