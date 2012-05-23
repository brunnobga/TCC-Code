/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package package_default;

/**
 *
 * @author caio
 */
public class Util {
    private static String defaultVideosDirectory = System.getProperty("user.dir");
    private static String defaultOutputDirectory = System.getProperty("user.dir");
    private static String userDirectory = System.getProperty("user.dir");
    
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
