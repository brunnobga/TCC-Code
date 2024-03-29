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
    private static String userDirectory;
    private static String fileSeparator;
    private static String defaultVideosDirectory;
    private static String defaultOutputDirectory;
    private static String raffleDirectory;
    private static String toolsDirectory;
    private static boolean backscreen;
    private static String operatingSystem;
    private static String loggedUser;
    private static int titleDelay;
    private static int endDelay;
    private static int intervalDelay;
    private static int evalDelay;
    
    public static void init(){
        fileSeparator = System.getProperty("file.separator");
        operatingSystem = System.getProperty("os.name");
        userDirectory = fileSeparator + "media" + fileSeparator + "hd" + fileSeparator + "UT" + fileSeparator + "SASQV2";
        defaultVideosDirectory = userDirectory + fileSeparator + "videodb"; // criar pasta VideoDB
        defaultOutputDirectory = userDirectory + fileSeparator + "videodb"; // criar pasta VideoOutput
        raffleDirectory = userDirectory + fileSeparator + "raffles"; // criar pasta RaffleFiles
        toolsDirectory = userDirectory + fileSeparator + "tools"; // criar pasta Tools + mplayer
        backscreen = true;
        titleDelay = 2000;
        endDelay = 10000;
        intervalDelay = 3000;
        evalDelay = 7000;
    }
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
    public static void setSessionDelayTitle(int delay){
        titleDelay = delay;
    }
    public static void setSessionDelayEnd(int delay){
        endDelay = delay;
    }
    public static void setSessionDelayInterval(int delay){
        intervalDelay = delay;
    }
    public static void setSessionDelayEvaluation(int delay){
        evalDelay = delay;
    }
    public static boolean isSessionBackscreenVisible(){
        return backscreen;
    }
    public static int getSessionDelayEvaluation(){
        return evalDelay;
    }
    public static int getSessionDelayInterval(){
        return intervalDelay;
    }
    public static int getSessionDelayEnd(){
        return endDelay;
    }
    public static int getSessionDelayTitle(){
        return titleDelay;
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
