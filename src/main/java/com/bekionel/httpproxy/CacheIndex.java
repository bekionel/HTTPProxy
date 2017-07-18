/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bekionel.httpproxy;

import java.io.File;
import java.util.HashMap;

/**
 *
 * @author Socrates
 */
public class CacheIndex {
    
    private static final String cachePath = "/tmp/httpProxyCache/";
    private static HashMap<String,String> cacheIndex = new HashMap<>();
    
    public static boolean existsInCache (String key){
        return (cacheIndex.containsKey(key)) ;
    }
    
    public static String addToCache (String key){
        cacheIndex.put(key, cachePath + key);
        return cachePath + key;
    }
    
    public static File fetchFromCache (String key){
        File fetchFile = new File(cacheIndex.get(key));
        return fetchFile;
    }
}
