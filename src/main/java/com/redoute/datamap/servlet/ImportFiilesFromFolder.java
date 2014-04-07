/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.redoute.datamap.servlet;

import com.redoute.datamap.entity.Picture;
import com.redoute.datamap.service.impl.PictureService;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author bcivel
 */
public class ImportFiilesFromFolder extends HttpServlet {

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
        try {
            ApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
            PictureService pictureService = appContext.getBean(PictureService.class);

            PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.LINKS);
            String path = policy.sanitize(request.getParameter("path"));
           // C:\\Users\\bcivel\\Desktop\\datacerberus
            
            File folder = new File(path);
            File[] listOfFiles = folder.listFiles();

            for (File file : listOfFiles) {
                if (file.isFile()) {
                    BufferedImage img;
                    try {
                        Picture picture = new Picture();
                        String page = file.getName().split("_")[0];
                        picture.setPage(page);
                        
                        String name = "";
                        for (int a = 1 ; a<file.getName().split("_").length; a++){
                        name += file.getName().split("_")[a];
                        if (a != file.getName().split("_").length-1){
                        name += "_";
                        }
                        }
                        picture.setPicture(name);
                        
//                       img = ImageIO.read(file);
//                       ByteArrayOutputStream os = new ByteArrayOutputStream();
//                        OutputStream b64 = new Base64.OutputStream(os);
//                        ImageIO.write(img, "png", b64);
                        FileInputStream fileInputStream = new FileInputStream(file);
                        String  base64Format = new String(Base64.encodeBase64(IOUtils.toByteArray(fileInputStream)));
                        String pict = "data:image/jpeg;base64,"+base64Format;
                        picture.setBase64(pict);

                        
                        pictureService.createPicture(picture);
                        System.out.print("done : " + name);
                        fileInputStream.close();
//                        img.flush();
//                        os.close();
                    } catch (IOException ex) {
                       System.err.println(ex.getMessage());
                       ex.printStackTrace();
                       throw ex;
                    }
                    
                }
            }


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
