/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bekionel.httpproxy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;


/**
 *
 * @author Socrates
 */
@WebServlet(name = "Proxy", urlPatterns = {"/"})
public class Proxy extends HttpServlet {
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */

    private static final Logger proxyLogger = Logger.getLogger("Proxy Submodule");

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
        boolean eligibleForCaching = false;
        proxyLogger.debug("Entering doGet");
        String fullRequestUrl = "http://"+request.getHeader("Host")+request.getRequestURI();
        String cacheLookupKey = request.getHeader("Host")+request.getRequestURI();
        String hostAddr = request.getHeader("Host");
        CloseableHttpClient httpclient = HttpClients.createDefault();
    try {
        HttpHost target = new HttpHost(hostAddr);
            String requestString = request.getRequestURI();
            int dotIndex = requestString.lastIndexOf(".");
            if (dotIndex > 0){
            String extension = requestString.substring(dotIndex + 1);
            proxyLogger.debug("Object extension: "+ extension);
            if (CachePolicyConfig.objectEligibleForCaching(extension)){
                eligibleForCaching = true;
                proxyLogger.debug("Request string "+requestString+"\n Eligible for caching: "+ eligibleForCaching);
                }
            }
        //Request to server starts here
        HttpGet req = new HttpGet(request.getRequestURI());
        proxyLogger.debug("Executing request " + req.toString() + " to " + target);
        CloseableHttpResponse resp = httpclient.execute(target, req);
        proxyLogger.debug("Servlet request path "+ request.getRequestURI());

        //Request sent, reading body from InputStream
        //TODO: Change transparent copy policy of InputStream to OutputStream.
        //      Add policy for storing caching eligible object to cache based
        //      on CachePolicyConfig.objectEligibleForCaching result.
        try {
            //Header[] headz = resp.getAllHeaders();
            //System.out.println("Printing headerz fo " +req.getRequestLine()+ " to "+ target);
            /*for (Header hd : headz){
               System.out.println(hd.toString());
            }*/
            //System.out.println("----------------------------------------");
            //System.out.println(resp.getStatusLine());
            HttpEntity respEntity = resp.getEntity();
            OutputStream out = response.getOutputStream();
            InputStream in = respEntity.getContent();
            /*Caching logic starts here:
              First check with eligibleForCaching if the object is of a type that can be cached.
              Then check if the object already exists in cache with the existsInCache method.
              If it exists, fetch it with fetchFromCache and write it to output stream,
              else save it in cache and also write it to output stream.
            */
            if (eligibleForCaching){
               if (CacheIndex.existsInCache(cacheLookupKey)){
                   File fetchedFile = CacheIndex.fetchFromCache(cacheLookupKey);
                   FileUtils.copyFile(fetchedFile, out);
                   proxyLogger.debug("Object fetched from cache and copied to output stream");
               }
               else{
                   FileOutputStream fos = new FileOutputStream(CacheIndex.addToCache(cacheLookupKey));
                   byte[] buffer = new byte[1024];
                   int len = in.read(buffer);
                   while (len != -1 ) {
                       out.write(buffer, 0, len);
                       fos.write(buffer, 0, len);
                       len = in.read(buffer);
                   }
                   proxyLogger.debug("Object copied to cache and copied to output stream");
               }
            }
            else{
                IOUtils.copy(in,out);
            }
            in.close();
            out.close();
        } finally {
            resp.close();
        }
    } finally {
        httpclient.close();
    }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet requestnu
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        //processRequest(request, response);
//    }

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
