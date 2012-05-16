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
    private String artefato, raffleFile;
    private List<String> parametros;
    
    public GeradorTask(Media video, String artefato, String raffleFile, List<String> parametros){
	this.video = video;
        this.artefato = artefato;
        this.raffleFile = raffleFile;
        this.parametros = parametros;
    }
    public GeradorTask(Media video, String artefato, String raffleFile){
	this.video = video;
        this.artefato = artefato;
        this.raffleFile = raffleFile;
    }

    public Media getVideo() {
	return this.video;
    }
    public String getArtefato(){
        return this.artefato;
    }
    public String getRaffleFile(){
        return this.raffleFile;
    }
    public List<String> getParams(){
        return this.parametros;
    }
}
