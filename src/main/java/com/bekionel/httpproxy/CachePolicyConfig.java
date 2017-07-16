/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bekionel.httpproxy;

import java.util.Arrays;

/**
 *
 * @author Socrates
 */
public class CachePolicyConfig {

    //List containing the extensions which are eligible for caching
    private static String[] cacheObjectTypes = {
        "jpg",
        "jpeg",
        "png",
        "img"
    };

     /**
     * Check whether an object is eligible for caching.
     * This method is going to be used in order to determine if an 
     * object is eligible for caching based on the extension. 
     * cacheObjectTypes list is the one that is going to be utilized
     * in order to make the check. If the extension exists on the list then
     * this method will return true.
     * @param extension Request object extension.
     * @return True if the object is cacheable based on the extension.
     */
    public static boolean objectEligibleForCaching(String extension){
        return Arrays.asList(cacheObjectTypes).contains(extension);
    }

}
