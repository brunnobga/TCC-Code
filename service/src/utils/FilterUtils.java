package utils;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

public class FilterUtils {
	
	public static final double RGBTOGRAY_CONVERT_RED_FACTOR = 0.2989;
	public static final double RGBTOGRAY_CONVERT_GREEN_FACTOR = 0.5870;
	public static final double RGBTOGRAY_CONVERT_BLUE_FACTOR = 0.1140;
	/**
	 * Treat pixels off the edge as zero.
	 */
	public final static int CONVOLVE_ZERO_EDGES = 0;

	/**
	 * Clamp pixels off the edge to the nearest edge.
	 */
	public final static int CONVOLVE_CLAMP_EDGES = 1;

	/**
	 * Wrap pixels off the edge to the opposite edge.
	 */
	public final static int CONVOLVE_WRAP_EDGES = 2;

	public static boolean saveImageToDisk(File f, BufferedImage src,
			String format) throws IOException {
		ImageOutputStream ios = ImageIO.createImageOutputStream(f);
		return ImageIO.write(src, format, ios);
	}

	public static double[] convert8BitRGBToGrayScale(int rgbSource[]) {
		double[] grayScaleValues = new double[rgbSource.length];
		for (int i = 0; i < rgbSource.length; i++) {
			grayScaleValues[i] = RGBTOGRAY_CONVERT_RED_FACTOR
					* ((rgbSource[i] >> 16) & 0xff)
					+ RGBTOGRAY_CONVERT_GREEN_FACTOR
					* ((rgbSource[i] >> 8) & 0xff)
					+ RGBTOGRAY_CONVERT_BLUE_FACTOR * (rgbSource[i] & 0xff);
		}
		return grayScaleValues;
	}

	public static BufferedImage createCompatibleDestImage(BufferedImage src,
			ColorModel dstCM) {
		if (dstCM == null)
			dstCM = src.getColorModel();
		return new BufferedImage(dstCM, dstCM.createCompatibleWritableRaster(
				src.getWidth(), src.getHeight()), dstCM.isAlphaPremultiplied(),
				null);
	}

	/**
	 * A convenience method for getting ARGB pixels from an image. This tries to
	 * avoid the performance penalty of BufferedImage.getRGB unmanaging the
	 * image.
	 * 
	 * @param image
	 *            a BufferedImage object
	 * @param x
	 *            the left edge of the pixel block
	 * @param y
	 *            the right edge of the pixel block
	 * @param width
	 *            the width of the pixel arry
	 * @param height
	 *            the height of the pixel arry
	 * @param pixels
	 *            the array to hold the returned pixels. May be null.
	 * @return the pixels
	 * @see #setRGB
	 */
	public static int[] getRGB(BufferedImage image, int x, int y, int width,
			int height, int[] pixels) {
		int type = image.getType();
		if (type == BufferedImage.TYPE_INT_ARGB
				|| type == BufferedImage.TYPE_INT_RGB) {
			return (int[]) image.getRaster().getDataElements(x, y, width,
					height, pixels);
		}
		return image.getRGB(x, y, width, height, pixels, 0, width);
	}

	/**
	 * A convenience method for setting ARGB pixels in an image. This tries to
	 * avoid the performance penalty of BufferedImage.setRGB unmanaging the
	 * image.
	 * 
	 * @param image
	 *            a BufferedImage object
	 * @param x
	 *            the left edge of the pixel block
	 * @param y
	 *            the right edge of the pixel block
	 * @param width
	 *            the width of the pixel array
	 * @param height
	 *            the height of the pixel array
	 * @param pixels
	 *            the array of pixels to set
	 * @see #getRGB
	 */
	public static void setRGB(BufferedImage image, int x, int y, int width,
			int height, int[] pixels) {
		int type = image.getType();
		if (type == BufferedImage.TYPE_INT_ARGB
				|| type == BufferedImage.TYPE_INT_RGB) {
			image.getRaster().setDataElements(x, y, width, height, pixels);
		} else {
			image.setRGB(x, y, width, height, pixels, 0, width);
		}
	}

	/**
	 * Premultiply a block of pixels
	 */
	public static void premultiply(int[] p, int offset, int length) {
		length += offset;
		for (int i = offset; i < length; i++) {
			int rgb = p[i];
			int a = (rgb >> 24) & 0xff;
			int r = (rgb >> 16) & 0xff;
			int g = (rgb >> 8) & 0xff;
			int b = rgb & 0xff;
			float f = a * (1.0f / 255.0f);
			r *= f;
			g *= f;
			b *= f;
			p[i] = (a << 24) | (r << 16) | (g << 8) | b;
		}
	}

	/**
	 * Unpremultiply a block of pixels
	 */
	public static void unpremultiply(int[] p, int offset, int length) {
		length += offset;
		for (int i = offset; i < length; i++) {
			int rgb = p[i];
			int a = (rgb >> 24) & 0xff;
			int r = (rgb >> 16) & 0xff;
			int g = (rgb >> 8) & 0xff;
			int b = rgb & 0xff;
			if (a != 0 && a != 255) {
				float f = 255.0f / a;
				r *= f;
				g *= f;
				b *= f;
				if (r > 255) {
					r = 255;
				}
				if (g > 255) {
					g = 255;
				}
				if (b > 255) {
					b = 255;
				}
				p[i] = (a << 24) | (r << 16) | (g << 8) | b;
			}
		}
	}

	/**
	 * Blur and transpose a block of ARGB pixels.
	 * 
	 * @param in
	 *            the input pixels
	 * @param out
	 *            the output pixels
	 * @param width
	 *            the width of the pixel array
	 * @param height
	 *            the height of the pixel array
	 * @param radius
	 *            the radius of blur
	 */
	public static void blur(int[] in, int[] out, int width, int height,
			float radius) {
		int widthMinus1 = width - 1;
		int r = (int) radius;
		int tableSize = 2 * r + 1;
		int divide[] = new int[256 * tableSize];
		for (int i = 0; i < 256 * tableSize; i++) {
			divide[i] = i / tableSize;
		}
		int inIndex = 0;
		for (int y = 0; y < height; y++) {
			int outIndex = y;
			int ta = 0, tr = 0, tg = 0, tb = 0;
			for (int i = -r; i <= r; i++) {
				int rgb = in[inIndex + clamp(i, 0, width - 1)];
				ta += (rgb >> 24) & 0xff;
				tr += (rgb >> 16) & 0xff;
				tg += (rgb >> 8) & 0xff;
				tb += rgb & 0xff;
			}
			for (int x = 0; x < width; x++) {
				out[outIndex] = (divide[ta] << 24) | (divide[tr] << 16)
						| (divide[tg] << 8) | divide[tb];
				int i1 = x + r + 1;
				if (i1 > widthMinus1) {
					i1 = widthMinus1;
				}
				int i2 = x - r;
				if (i2 < 0) {
					i2 = 0;
				}
				int rgb1 = in[inIndex + i1];
				int rgb2 = in[inIndex + i2];
				ta += ((rgb1 >> 24) & 0xff) - ((rgb2 >> 24) & 0xff);
				tr += ((rgb1 & 0xff0000) - (rgb2 & 0xff0000)) >> 16;
				tg += ((rgb1 & 0xff00) - (rgb2 & 0xff00)) >> 8;
				tb += (rgb1 & 0xff) - (rgb2 & 0xff);
				outIndex += height;
			}
			inIndex += width;
		}
	}

	public static void blurFractional(int[] in, int[] out, int width,
			int height, float radius) {
		radius -= (int) radius;
		float f = 1.0f / (1 + 2 * radius);
		int inIndex = 0;
		for (int y = 0; y < height; y++) {
			int outIndex = y;
			out[outIndex] = in[0];
			outIndex += height;
			for (int x = 1; x < width - 1; x++) {
				int i = inIndex + x;
				int rgb1 = in[i - 1];
				int rgb2 = in[i];
				int rgb3 = in[i + 1];
				int a1 = (rgb1 >> 24) & 0xff;
				int r1 = (rgb1 >> 16) & 0xff;
				int g1 = (rgb1 >> 8) & 0xff;
				int b1 = rgb1 & 0xff;
				int a2 = (rgb2 >> 24) & 0xff;
				int r2 = (rgb2 >> 16) & 0xff;
				int g2 = (rgb2 >> 8) & 0xff;
				int b2 = rgb2 & 0xff;
				int a3 = (rgb3 >> 24) & 0xff;
				int r3 = (rgb3 >> 16) & 0xff;
				int g3 = (rgb3 >> 8) & 0xff;
				int b3 = rgb3 & 0xff;
				a1 = a2 + (int) ((a1 + a3) * radius);
				r1 = r2 + (int) ((r1 + r3) * radius);
				g1 = g2 + (int) ((g1 + g3) * radius);
				b1 = b2 + (int) ((b1 + b3) * radius);
				a1 *= f;
				r1 *= f;
				g1 *= f;
				b1 *= f;
				out[outIndex] = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1;
				outIndex += height;
			}
			out[outIndex] = in[width - 1];
			inIndex += width;
		}
	}

	/**
	 * Convolve a block of pixels.
	 * 
	 * @param kernel
	 *            the kernel
	 * @param inPixels
	 *            the input pixels
	 * @param outPixels
	 *            the output pixels
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 * @param alpha
	 *            include alpha channel
	 * @param edgeAction
	 *            what to do at the edges
	 */
	public static void convolve(Kernel kernel, int[] inPixels, int[] outPixels,
			int width, int height, boolean alpha, int edgeAction) {
		if (kernel.getHeight() == 1) {
			convolveH(kernel, inPixels, outPixels, width, height, alpha,
					edgeAction);
		} else if (kernel.getWidth() == 1) {
			convolveV(kernel, inPixels, outPixels, width, height, alpha,
					edgeAction);
		} else {
			convolveHV(kernel, inPixels, outPixels, width, height, alpha,
					edgeAction);
		}
	}

	/**
	 * Convolve with a 2D kernel.
	 * 
	 * @param kernel
	 *            the kernel
	 * @param inPixels
	 *            the input pixels
	 * @param outPixels
	 *            the output pixels
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 * @param alpha
	 *            include alpha channel
	 * @param edgeAction
	 *            what to do at the edges
	 */
	public static void convolveHV(Kernel kernel, int[] inPixels,
			int[] outPixels, int width, int height, boolean alpha,
			int edgeAction) {
		int index = 0;
		float[] matrix = kernel.getKernelData(null);
		int rows = kernel.getHeight();
		int cols = kernel.getWidth();
		int rows2 = rows / 2;
		int cols2 = cols / 2;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				float r = 0, g = 0, b = 0, a = 0;
				for (int row = -rows2; row <= rows2; row++) {
					int iy = y + row;
					int ioffset;
					if (0 <= iy && iy < height) {
						ioffset = iy * width;
					} else if (edgeAction == CONVOLVE_CLAMP_EDGES) {
						ioffset = y * width;
					} else if (edgeAction == CONVOLVE_WRAP_EDGES) {
						ioffset = ((iy + height) % height) * width;
					} else {
						continue;
					}
					int moffset = cols * (row + rows2) + cols2;
					for (int col = -cols2; col <= cols2; col++) {
						float f = matrix[moffset + col];
						if (f != 0) {
							int ix = x + col;
							if (!(0 <= ix && ix < width)) {
								if (edgeAction == CONVOLVE_CLAMP_EDGES) {
									ix = x;
								} else if (edgeAction == CONVOLVE_WRAP_EDGES) {
									ix = (x + width) % width;
								} else {
									continue;
								}
							}
							int rgb = inPixels[ioffset + ix];
							a += f * ((rgb >> 24) & 0xff);
							r += f * ((rgb >> 16) & 0xff);
							g += f * ((rgb >> 8) & 0xff);
							b += f * (rgb & 0xff);
						}
					}
				}
				int ia = alpha ? clamp((int) (a + 0.5)) : 0xff;
				int ir = clamp((int) (r + 0.5));
				int ig = clamp((int) (g + 0.5));
				int ib = clamp((int) (b + 0.5));
				outPixels[index++] = (ia << 24) | (ir << 16) | (ig << 8) | ib;
			}
		}
	}

	/**
	 * Convolve with a kernel consisting of one row.
	 * 
	 * @param kernel
	 *            the kernel
	 * @param inPixels
	 *            the input pixels
	 * @param outPixels
	 *            the output pixels
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 * @param alpha
	 *            include alpha channel
	 * @param edgeAction
	 *            what to do at the edges
	 */
	public static void convolveH(Kernel kernel, int[] inPixels,
			int[] outPixels, int width, int height, boolean alpha,
			int edgeAction) {
		int index = 0;
		float[] matrix = kernel.getKernelData(null);
		int cols = kernel.getWidth();
		int cols2 = cols / 2;
		for (int y = 0; y < height; y++) {
			int ioffset = y * width;
			for (int x = 0; x < width; x++) {
				float r = 0, g = 0, b = 0, a = 0;
				int moffset = cols2;
				for (int col = -cols2; col <= cols2; col++) {
					float f = matrix[moffset + col];
					if (f != 0) {
						int ix = x + col;
						if (ix < 0) {
							if (edgeAction == CONVOLVE_CLAMP_EDGES) {
								ix = 0;
							} else if (edgeAction == CONVOLVE_WRAP_EDGES) {
								ix = (x + width) % width;
							}
						} else if (ix >= width) {
							if (edgeAction == CONVOLVE_CLAMP_EDGES) {
								ix = width - 1;
							} else if (edgeAction == CONVOLVE_WRAP_EDGES) {
								ix = (x + width) % width;
							}
						}
						int rgb = inPixels[ioffset + ix];
						a += f * ((rgb >> 24) & 0xff);
						r += f * ((rgb >> 16) & 0xff);
						g += f * ((rgb >> 8) & 0xff);
						b += f * (rgb & 0xff);
					}
				}
				int ia = alpha ? clamp((int) (a + 0.5)) : 0xff;
				int ir = clamp((int) (r + 0.5));
				int ig = clamp((int) (g + 0.5));
				int ib = clamp((int) (b + 0.5));
				outPixels[index++] = (ia << 24) | (ir << 16) | (ig << 8) | ib;
			}
		}
	}

	/**
	 * Convolve with a kernel consisting of one column.
	 * 
	 * @param kernel
	 *            the kernel
	 * @param inPixels
	 *            the input pixels
	 * @param outPixels
	 *            the output pixels
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 * @param alpha
	 *            include alpha channel
	 * @param edgeAction
	 *            what to do at the edges
	 */
	public static void convolveV(Kernel kernel, int[] inPixels,
			int[] outPixels, int width, int height, boolean alpha,
			int edgeAction) {
		int index = 0;
		float[] matrix = kernel.getKernelData(null);
		int rows = kernel.getHeight();
		int rows2 = rows / 2;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				float r = 0, g = 0, b = 0, a = 0;
				for (int row = -rows2; row <= rows2; row++) {
					int iy = y + row;
					int ioffset;
					if (iy < 0) {
						if (edgeAction == CONVOLVE_CLAMP_EDGES) {
							ioffset = 0;
						} else if (edgeAction == CONVOLVE_WRAP_EDGES) {
							ioffset = ((y + height) % height) * width;
						} else {
							ioffset = iy * width;
						}
					} else if (iy >= height) {
						if (edgeAction == CONVOLVE_CLAMP_EDGES) {
							ioffset = (height - 1) * width;
						} else if (edgeAction == CONVOLVE_WRAP_EDGES) {
							ioffset = ((y + height) % height) * width;
						} else {
							ioffset = iy * width;
						}
					} else {
						ioffset = iy * width;
					}
					float f = matrix[row + rows2];
					if (f != 0) {
						int rgb = inPixels[ioffset + x];
						a += f * ((rgb >> 24) & 0xff);
						r += f * ((rgb >> 16) & 0xff);
						g += f * ((rgb >> 8) & 0xff);
						b += f * (rgb & 0xff);
					}
				}
				int ia = alpha ? clamp((int) (a + 0.5)) : 0xff;
				int ir = clamp((int) (r + 0.5));
				int ig = clamp((int) (g + 0.5));
				int ib = clamp((int) (b + 0.5));
				outPixels[index++] = (ia << 24) | (ir << 16) | (ig << 8) | ib;
			}
		}
	}
	
	/**
	 * Clamp a value to an interval.
	 * @param a the lower clamp threshold
	 * @param b the upper clamp threshold
	 * @param x the input parameter
	 * @return the clamped value
	 */
	public static int clamp(int x, int a, int b) {
		return (x < a) ? a : (x > b) ? b : x;
	}
	
	/**
	 * Clamp a value to the range 0..255
	 */
	public static int clamp(int c) {
		if (c < 0)
			return 0;
		if (c > 255)
			return 255;
		return c;
	}
	
	public static String buildMediaDstPath(String srcPath, String append) {
		String dstPath = "";
		if(srcPath != null && srcPath.length() > 0) {
			String[] nameAux = srcPath.split("\\.");
			if(nameAux != null && nameAux.length > 0) {
				for(int i = 0; i < nameAux.length -1; i++) {
					dstPath = nameAux[i];
				}
				dstPath += "@" + append + ".mp4";
			}
		}
		return dstPath;
	}
}