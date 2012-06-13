/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package package_default;

import java.awt.Dimension;

/**
 *
 * @author caio
 */
public class Util {
    private static String defaultVideosDirectory = System.getProperty("user.dir");
    private static String defaultOutputDirectory = System.getProperty("user.dir");
    private static String userDirectory = System.getProperty("user.dir");
    private static boolean backscreen = true;
    
    public static Dimension getScreenSize(){
        java.awt.Toolkit toolkit =  java.awt.Toolkit.getDefaultToolkit ();
        Dimension dim = toolkit.getScreenSize();
        return dim;
    }
    public static void setDefaultVideosDirectory(String folder){
        defaultVideosDirectory = folder;
        defaultOutputDirectory = folder;
    }
    public static void setDefaultOutputDirectory(String folder){
        defaultOutputDirectory = folder;
    }
    public static void setUserDirectory(String folder){
        userDirectory = folder;
    }
    public static void setSessionBackscreenVisible(boolean b){
        backscreen = b;
    }
    public static boolean isSessionBackscreenVisible(){
        return backscreen;
    }
    public static String getDefaultVideosDirectory(){
        return defaultVideosDirectory;
    }
    public static String getDefaultOutputDirectory(){
        return defaultOutputDirectory;
    }
    public static String getUserDirectory(){
        return userDirectory;
    }
}
