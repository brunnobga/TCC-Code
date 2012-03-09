package utils;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import service.Filters;


public class MetricUtils {
	
	public static final double SSIM_K1_FACTOR = 0.01;
	public static final double SSIM_K2_FACTOR = 0.03;
	
	public double calculateVariance(double[] values) {
		double variance = 0, mean = 0;
		mean = calculateMean(values);
		for (int i = 0; i < values.length; i++) {
			variance += Math.pow(values[i] - mean, 2);
		}
		return variance / (values.length);
	}

	public double calculateCoVariance(double[] values1, double[] values2,
			int length) {
		double coVariance = 0, mean1 = 0, mean2 = 0;
		mean1 = calculateMean(values1);
		mean2 = calculateMean(values2);
		for (int i = 0; i < length; i++) {
			coVariance += (Math.pow(values1[i] - mean1, 2))
					* (Math.pow(values2[i] - mean2, 2));
		}
		return coVariance / (length);
	}
	
	public double calculateMSE(ArrayList<String> sourcesPaths1,
			ArrayList<String> sourcesPaths2, int width, int height,
			int frameCount) {
		double mse = 0;
		for (int i = 0; i < frameCount; i++) {
			BufferedImage bf1, bf2;
			try {
				bf1 = ImageIO.read(new File(sourcesPaths1.get(i)));
				bf2 = ImageIO.read(new File(sourcesPaths2.get(i)));
			} catch (IOException e) {
				e.printStackTrace();
				return -1;
			}
			double[] source1Pixels = FilterUtils.convert8BitRGBToGrayScale(FilterUtils.getRGB(bf1, 0, 0, width, height, 
					new int[bf1.getWidth() * bf1.getHeight()]));
			double[] source2Pixels = FilterUtils.convert8BitRGBToGrayScale(FilterUtils.getRGB(bf2, 0, 0, width, height, 
					new int[bf1.getWidth() * bf2.getHeight()]));
			for (int j = 0; j < source1Pixels.length; j++) {
				mse += Math.pow(source1Pixels[j] - source2Pixels[j], 2);
			}
		}
		return mse / (frameCount * width * height);
	}

	public double calculateMean(double[] source) {
		double sum = 0;
		for (int i = 0; i < source.length; i++) {
			sum += source[i];
		}
		return sum / source.length;
	}

	public double calculatePSNR(double mse, int bits) {
		return 10 * (Math.log10(Math.pow((Math.pow(2, bits) - 1), 2) / (mse)));
	}

	public double calculateMSSIM(ArrayList<String> sourcesPaths1,
			ArrayList<String> sourcesPaths2, int width,
			int height, int bits, int frameCount) {
		double ssim = 0, meanSrc1, meanSrc2, varianceSrc1, varianceSrc2, covariance, c1, c2;
		c1 = Math.pow((SSIM_K1_FACTOR * (Math.pow(2, bits)) - 1), 2);
		c2 = Math.pow((SSIM_K2_FACTOR * (Math.pow(2, bits)) - 1), 2);
		for (int i = 0; i < frameCount; i++) {
			BufferedImage bf1, bf2;
			try {
				bf1 = ImageIO.read(new File(sourcesPaths1.get(i)));
				bf2 = ImageIO.read(new File(sourcesPaths2.get(i)));
			} catch (IOException e) {
				e.printStackTrace();
				return -1;
			}
			double[] source1Pixels = FilterUtils.convert8BitRGBToGrayScale(FilterUtils.getRGB(bf1, 0, 0, width, height, 
					new int[bf1.getWidth() * bf1.getHeight()]));
			double[] source2Pixels = FilterUtils.convert8BitRGBToGrayScale(FilterUtils.getRGB(bf2, 0, 0, width, height, 
					new int[bf2.getWidth() * bf2.getHeight()]));
			meanSrc1 = calculateMean(source1Pixels);
			meanSrc2 = calculateMean(source2Pixels);
			varianceSrc1 = calculateVariance(source1Pixels);
			varianceSrc2 = calculateVariance(source2Pixels);
			covariance = calculateCoVariance(source1Pixels, source2Pixels,
					source1Pixels.length);
			ssim += (2 * meanSrc1 * meanSrc2 + c1)
					* (2 * covariance + c2)
					/ ((Math.pow(meanSrc1, 2) + Math.pow(meanSrc2, 2) + c1) * (Math
							.pow(varianceSrc1, 2) + Math.pow(varianceSrc2, 2) + c2));
		}
		return ssim / (frameCount * width * height);
	}
}
