package service;
import java.awt.image.BufferedImage;
import java.awt.image.Kernel;

import utils.FilterUtils;


public class Filters {

	public static BufferedImage blockFilter(BufferedImage src, int blockSize) {
		int width = src.getWidth();
		int height = src.getHeight();
		BufferedImage dst = FilterUtils.createCompatibleDestImage(src, null);
		int[] pixels = new int[blockSize * blockSize];
		for (int y = 0; y < height; y += blockSize) {
			for (int x = 0; x < width; x += blockSize) {
				int w = Math.min(blockSize, width - x);
				int h = Math.min(blockSize, height - y);
				int t = w * h;
				FilterUtils.getRGB(src, x, y, w, h, pixels);
				int r = 0, g = 0, b = 0;
				int argb;
				int i = 0;
				for (int by = 0; by < h; by++) {
					for (int bx = 0; bx < w; bx++) {
						argb = pixels[i];
						r += (argb >> 16) & 0xff;
						g += (argb >> 8) & 0xff;
						b += argb & 0xff;
						i++;
					}
				}
				argb = ((r / t) << 16) | ((g / t) << 8) | (b / t);
				i = 0;
				for (int by = 0; by < h; by++) {
					for (int bx = 0; bx < w; bx++) {
						pixels[i] = (pixels[i] & 0xff000000) | argb;
						i++;
					}
				}
				FilterUtils.setRGB(dst, x, y, w, h, pixels);
			}
		}
		return dst;
	}

	public static BufferedImage boxBlurFilter(BufferedImage src, int vRadius,
			int hRadius, boolean premultiplyAlpha, int iterations) {
		int width = src.getWidth();
		int height = src.getHeight();
		BufferedImage dst = FilterUtils.createCompatibleDestImage(src, null);
		int[] inPixels = new int[width * height];
		int[] outPixels = new int[width * height];
		FilterUtils.getRGB(src, 0, 0, width, height, inPixels);
		if (premultiplyAlpha) {
			FilterUtils.premultiply(inPixels, 0, inPixels.length);
		}
		for (int i = 0; i < iterations; i++) {
			FilterUtils.blur(inPixels, outPixels, width, height, hRadius);
			FilterUtils.blur(outPixels, inPixels, height, width, vRadius);
		}
		FilterUtils.blurFractional(inPixels, outPixels, width, height, hRadius);
		FilterUtils.blurFractional(outPixels, inPixels, height, width, vRadius);
		if (premultiplyAlpha) {
			FilterUtils.unpremultiply(inPixels, 0, inPixels.length);
		}
		FilterUtils.setRGB(dst, 0, 0, width, height, inPixels);
		return dst;
	}

	public static BufferedImage convolveFilter(BufferedImage src, int rows,
			int columns, float[] matrix, boolean premultiplyAlpha,
			boolean useAlpha, int edgeAction) {
		Kernel kernel = new Kernel(columns, rows, matrix);
		int width = src.getWidth();
		int height = src.getHeight();
		BufferedImage dst = FilterUtils.createCompatibleDestImage(src, null);
		int[] inPixels = new int[width * height];
		int[] outPixels = new int[width * height];
		FilterUtils.getRGB(src, 0, 0, width, height, inPixels);
		if (premultiplyAlpha) {
			FilterUtils.premultiply(inPixels, 0, inPixels.length);
		}
		FilterUtils.convolve(kernel, inPixels, outPixels, width, height,
				useAlpha, edgeAction);
		if (premultiplyAlpha) {
			FilterUtils.unpremultiply(outPixels, 0, outPixels.length);
		}
		FilterUtils.setRGB(dst, 0, 0, width, height, outPixels);
		return dst;
	}
}
