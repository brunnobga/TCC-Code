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
    private static String raffleDirectory = System.getProperty("user.dir");
    private static boolean backscreen = true;
    private static String operatingSystem;
    private static String fileSeparator;
    private static String toolsDirectory;
    private static String loggedUser;
    
    public static Dimension getScreenSize(){
        java.awt.Toolkit toolkit =  java.awt.Toolkit.getDefaultToolkit ();
        Dimension dim = toolkit.getScreenSize();
        return dim;
    }
    public static void setDefaultVideosDirectory(String folder){
        defaultVideosDirectory = folder;
    }
    public static void setDefaultOutputDirectory(String folder){
        defaultOutputDirectory = folder;
    }
    public static void setUserDirectory(String folder){
        userDirectory = folder;
    }
    public static void setDefaultRaffleDirectory(String folder){
        raffleDirectory = folder;
    }
    public static void setSessionBackscreenVisible(boolean b){
        backscreen = b;
    }
    public static void setOperatingSystem(String os){
        operatingSystem = os;
    }
    public static void setFileSeparator(String fSeparator){
        fileSeparator = fSeparator;
    }
    public static void setDefaultToolsDirectory(String folder){
        toolsDirectory = folder;
    }
    public static void setLoggedUser(String login){
        loggedUser = login;
    }
    public static boolean isSessionBackscreenVisible(){
        return backscreen;
    }
    public static String getLoggedUser(){
        return loggedUser;
    }
    public static String getDefaultToolsDirectory(){
        return toolsDirectory;
    }
    public static String getOperatingSystem(){
        return operatingSystem;
    }
    public static String getFileSeparator(){
        return fileSeparator;
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
    public static String getDefaultRaffleDirectory(){
        return raffleDirectory;
    }
}
