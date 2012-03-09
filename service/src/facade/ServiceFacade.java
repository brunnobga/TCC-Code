package facade;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import service.Filters;
import service.VideoService;
import utils.ErrorUtils;
import utils.MetricUtils;

import com.xuggle.xuggler.IStreamCoder;
import communication.Connection;
import communication.Operations;
import communication.Protocol;

/**
 * 
 * @author evandro.souza
 */
public class ServiceFacade {

	public ServiceFacade() {
	}

	public int blockFilterVideo(String sourceVideoPath, String destVideoPath,
			int blockSize) {
		int status = 0;
		VideoService vs = new VideoService();
		status = vs.extractVideoFrames(sourceVideoPath, "bmp");
		if(status == ErrorUtils.ERR_NONE) {
			ArrayList<String> frames = vs.getVideoFrames();
			int i = 0;
			for (String aux : frames) {
				BufferedImage bf;
				try {
					bf = ImageIO.read(new File(aux));
					bf = Filters.blockFilter(bf, blockSize);
					File file = File.createTempFile("frame", ".bmp");
					ImageIO.write(bf, "bmp", file);
					frames.set(i++, file.getAbsolutePath());
				} catch (IOException e) {
					status = ErrorUtils.ERR_FILTER_TEMP_FILE;
					e.printStackTrace();
				}
			}
			IStreamCoder coder = vs.getVideoCoder(sourceVideoPath);
			status = vs.buildVideoFromImages(destVideoPath, coder.getCodecID(),
					coder.getFrameRate(), coder.getWidth(), coder.getHeight(),
					frames);
		}
		return status;
	}
	
	public int boxBlurFilterVideo(String sourceVideoPath, String destVideoPath,
			int vRadius, int hRadius, boolean premultiplyAlpha, int iterations) {
		int status = 0;
		VideoService vs = new VideoService();
		status = vs.extractVideoFrames(sourceVideoPath, "jpg");
		if(status == ErrorUtils.ERR_NONE) {
			ArrayList<String> frames = vs.getVideoFrames();
			int i = 0;
			for (String aux : frames) {
				BufferedImage bf;
				try {
					bf = ImageIO.read(new File(aux));
					bf = Filters.boxBlurFilter(bf, vRadius, hRadius, premultiplyAlpha, iterations);
					File file = File.createTempFile("frame", ".jpg");
					ImageIO.write(bf, "jpg", file);
					frames.set(i++, file.getAbsolutePath());
				} catch (IOException e) {
					status = ErrorUtils.ERR_FILTER_TEMP_FILE;
					e.printStackTrace();
				}
			}
			IStreamCoder coder = vs.getVideoCoder(sourceVideoPath);
			status = vs.buildVideoFromImages(destVideoPath, coder.getCodecID(),
					coder.getFrameRate(), coder.getWidth(), coder.getHeight(),
					frames);
		}
		return status;
	}
	
	public int convolveFilterVideo(String sourceVideoPath, String destVideoPath,
			int rows, int columns, float[] matrix, boolean premultiplyAlpha, boolean useAlpha, int edgeAction) {
		int status = 0;
		VideoService vs = new VideoService();
		status = vs.extractVideoFrames(sourceVideoPath, "jpg");
		if(status == ErrorUtils.ERR_NONE) {
			ArrayList<String> frames = vs.getVideoFrames();
			int i = 0;
			for (String aux : frames) {
				BufferedImage bf;
				try {
					bf = ImageIO.read(new File(aux));
					bf = Filters.convolveFilter(bf, rows,
							columns, matrix, premultiplyAlpha,
							useAlpha, edgeAction);
					File file = File.createTempFile("frame", ".jpg");
					ImageIO.write(bf, "jpg", file);
					frames.set(i++, file.getAbsolutePath());
				} catch (IOException e) {
					status = ErrorUtils.ERR_FILTER_TEMP_FILE;
					e.printStackTrace();
				}
			}
			IStreamCoder coder = vs.getVideoCoder(sourceVideoPath);
			status = vs.buildVideoFromImages(destVideoPath, coder.getCodecID(),
					coder.getFrameRate(), coder.getWidth(), coder.getHeight(),
					frames);
		}
		return status;
	}
	
	public double calculateMSEFromVideo(String referenceVideoPath, String videoPath, 
			int width, int height) {
		double value = -1;
		int status = 0;
		VideoService vs = new VideoService(), vs2 = new VideoService();
		status = vs.extractVideoFrames(referenceVideoPath, "jpg");
		if(status == ErrorUtils.ERR_NONE) {
			status = vs2.extractVideoFrames(videoPath, "jpg");
			if(status == ErrorUtils.ERR_NONE) {
				MetricUtils mu = new MetricUtils();
				value = mu.calculateMSE(vs.getVideoFrames(), vs2.getVideoFrames(),
						width, height, vs.getVideoFrames().size());
			}
		}
		return value;
	}
	
	public double calculatePSNRFromMSE(double mse, int bits) {
		return new MetricUtils().calculatePSNR(mse, bits);
	}
	
	public double calculateMSSIMFromVideo(String referenceVideoPath, String videoPath, int bits,
			int width, int height) {
		double value = -1;
		int status = 0;
		VideoService vs = new VideoService(), vs2 = new VideoService();
		status = vs.extractVideoFrames(referenceVideoPath, "jpg");
		if(status == ErrorUtils.ERR_NONE) {
			status = vs2.extractVideoFrames(videoPath, "jpg");
			if(status == ErrorUtils.ERR_NONE) {
				MetricUtils mu = new MetricUtils();
				value = mu.calculateMSSIM(vs.getVideoFrames(), vs2.getVideoFrames(),
						width, height, bits, vs.getVideoFrames().size());
			}
		}
		return value;
	}
	
	public int defineMetricOnDevice(Connection c, int value) {
		return new Operations().defineMetric(c, value);
    }
	
	public int[][] getReceivedRatesFromDevice(Connection c) {
		return new Operations().getReceivedRates(c);
	}
    
    public int enableRateOnDevice(Connection c) {
    	return new Operations().enableRate(c);
    }
    
    public int disableRateOnDevice(Connection c) {
    	return new Operations().disableRate(c);
    }
}
