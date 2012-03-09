
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import service.Filters;
import service.VideoService;
import utils.FilterUtils;

public class TestMain {

    /**
     * Takes a media container (file) as the first argument, opens it and
     *  writes some of it's video frames to PNG image files in the
     *  temporary directory.
     *
     * @param args must contain one string which represents a filename
     */
    public static void main(String[] args) throws IOException {
        String filename = "C:/Users/EvandrO/Desktop/TCC Stuffs/TCC/ImagemTeste.JPG";
        File file = new File(filename);
        BufferedImage bf = ImageIO.read(new File(filename));
        
        //bf = Filters.boxBlurFilter(bf, 5, 5, true, 5);
        
        
        float[] matrix = new float[9];
        for(int i = 0; i < matrix.length; i++) {
        	if(i == 5)
        		matrix[i] = 0.2f;
        	else
        		matrix[i] = 0.1f;
        }
        bf = Filters.convolveFilter(bf, 3, 3, matrix, true, true, FilterUtils.CONVOLVE_WRAP_EDGES);
        
        //bf = Filters.boxBlurFilter(bf, 2, 2, true, 1);
        ImageIO.write(bf, "JPG", new File("C:/Users/EvandrO/Desktop/TCC Stuffs/TCC/ImagemTesteDEG.JPG"));
        
        
    }
    
}
