/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bekionel.httpproxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


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
        String fullRequestUrl = "http://"+request.getHeader("Host")+request.getContextPath()+"/";
        String hostAddr = request.getHeader("Host");
     CloseableHttpClient httpclient = HttpClients.createDefault();
    try {
        HttpHost target = new HttpHost(hostAddr);
        HttpHost proxy = new HttpHost("127.0.0.1", 8080, "http");
        System.out.println(hostAddr+"\n"+fullRequestUrl+"\n");

        RequestConfig config = RequestConfig.custom()
                .build();
        HttpGet req = new HttpGet(request.getContextPath()+"/");
        req.setConfig(config);

        System.out.println("Executing request " + req.getRequestLine() + " to " + target + " via " + proxy);

        CloseableHttpResponse resp = httpclient.execute(target, req);
        try {
            Header[] headz = resp.getAllHeaders();
             System.out.println("Printing headerz fo " +req.getRequestLine()+ "to "+ target);
            for (Header hd : headz){
               System.out.println(hd.toString());
            }
            System.out.println("----------------------------------------");
            System.out.println(resp.getStatusLine());
            HttpEntity respEntity = resp.getEntity();
            OutputStream out = response.getOutputStream();
            InputStream in = respEntity.getContent();
            IOUtils.copy(in,out);
            in.close();
            out.close();
//            buffer
//            inputStream.
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
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //processRequest(request, response);
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
    
    
    
    public CloseableHttpResponse sendGet(HttpGet httpGet) throws IOException {
        
                CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(httpGet);
                return response;
	}

}
