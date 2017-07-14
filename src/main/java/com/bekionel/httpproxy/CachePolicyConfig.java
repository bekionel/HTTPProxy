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
    private static String[] cacheObjectTypes = {
        "jpg",
        "jpeg",
        "png",
        "img"
    };
    public static boolean objectEligibleForCaching(String extension){
        return Arrays.asList(cacheObjectTypes).contains(extension);
    }

}
