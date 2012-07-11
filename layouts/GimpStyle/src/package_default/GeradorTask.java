/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package package_default;

import entity.Media;
import java.util.List;

/**
 *
 * @author caio
 */
public class GeradorTask {
    private Media video;
    private String artefato, raffleFile, newPath, newTitle;
    private List<String> parametros;
    
    public GeradorTask(Media video, String artefato, String raffleFile, List<String> parametros, String newPath, String newTitle){
	this.video = video;
        this.artefato = artefato;
        this.raffleFile = raffleFile;
        this.parametros = parametros;
	this.newPath = newPath;
        this.newTitle = newTitle;
    }

    public Media getVideo() {
	return this.video;
    }
    public String getNewPath(){
	return newPath;
    }
    public String getArtefato(){
        return this.artefato;
    }
    public String getRaffleFile(){
        return this.raffleFile;
    }
    public String getNewTitle(){
        return this.newTitle;
    }
    public List<String> getParams(){
        return this.parametros;
    }
}
