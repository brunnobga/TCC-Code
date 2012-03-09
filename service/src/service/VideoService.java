package service;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.ICodec.ID;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IRational;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.IVideoResampler;
import com.xuggle.xuggler.Utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import utils.ErrorUtils;

/**
 * 
 * @author evandro.souza
 */
public class VideoService extends MediaListenerAdapter {

	private ArrayList<String> videoFrames;

	public VideoService() {
		videoFrames = new ArrayList<String>();
	}

	public int extractVideoFrames(String filePath, String outputFormat) {
		videoFrames.clear();
		// make sure that we can actually convert video pixel formats
		if (!IVideoResampler.isSupported(IVideoResampler.Feature.FEATURE_COLORSPACECONVERSION)) {
			return ErrorUtils.ERR_XUGGLER_GPL;
		}
		// create a Xuggler container object
		IContainer container = IContainer.make();
		// open up the container
		if (container.open(filePath, IContainer.Type.READ, null) < 0)
			return ErrorUtils.ERR_FILE_IO;
		// query how many streams the call to open found
		int numStreams = container.getNumStreams();
		// and iterate through the streams to find the first video stream
		int videoStreamId = -1;
		IStreamCoder videoCoder = null;
		for (int i = 0; i < numStreams; i++) {
			// find the stream object
			IStream stream = container.getStream(i);
			// get the pre-configured decoder that can decode this stream;
			IStreamCoder coder = stream.getStreamCoder();
			if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
				videoStreamId = i;
				videoCoder = coder;
				break;
			}
		}
		if (videoStreamId == -1)
			return ErrorUtils.ERR_XUGGLER_VIDEO_STREAM_OPEN;
		// Now we have found the video stream in this file. Let's open up our
		// decoder so it can do work
		if (videoCoder.open() < 0) {
			return ErrorUtils.ERR_XUGGLER_VIDEO_DECODER_OPEN;
		}
		IVideoResampler resampler = null;
		if (videoCoder.getPixelType() != IPixelFormat.Type.BGR24) {
			// if this stream is not in BGR24, we're going to need to
			// convert it. The VideoResampler does that for us.
			resampler = IVideoResampler.make(videoCoder.getWidth(),
					videoCoder.getHeight(), IPixelFormat.Type.BGR24,
					videoCoder.getWidth(), videoCoder.getHeight(),
					videoCoder.getPixelType());
			if (resampler == null) {
				return ErrorUtils.ERR_XUGGLER_VIDEO_COLORSPACE;
			}
		}
		// Now, we start walking through the container looking at each packet.
		IPacket packet = IPacket.make();
		while (container.readNextPacket(packet) >= 0) {
			// Now we have a packet, let's see if it belongs to our video strea
			if (packet.getStreamIndex() == videoStreamId) {
				// We allocate a new picture to get the data out of Xuggle
				IVideoPicture picture = IVideoPicture.make(
						videoCoder.getPixelType(), videoCoder.getWidth(),
						videoCoder.getHeight());
				int offset = 0;
				while (offset < packet.getSize()) {
					// Now, we decode the video, checking for any errors.
					int bytesDecoded = videoCoder.decodeVideo(picture, packet,
							offset);
					if (bytesDecoded < 0) {
						return ErrorUtils.ERR_XUGGLER_VIDEO_DECODER_DECODE;
					}
					offset += bytesDecoded;
					// Some decoders will consume data in a packet, but will not
					// be able to construct a full video picture yet. Therefore
					// you should always check if you got a complete picture from the decode.
					if (picture.isComplete()) {
						IVideoPicture newPic = picture;
						// If the resampler is not null, it means we didn't get
						// the video in BGR24 format and need to convert it into BGR24 format.
						if (resampler != null) {
							// we must resample
							newPic = IVideoPicture.make(
									resampler.getOutputPixelFormat(),
										picture.getWidth(), picture.getHeight());
							if (resampler.resample(newPic, picture) < 0) {
								return ErrorUtils.ERR_XUGGLER_VIDEO_RESAMPLER_RESAMPLE;
							}
						}
						if (newPic.getPixelType() != IPixelFormat.Type.BGR24) {
							return ErrorUtils.ERR_XUGGLER_VIDEO_DECODER_BGR24;
						}
						// convert the BGR24 to an Java buffered image
						@SuppressWarnings("deprecation")
						BufferedImage javaImage = Utils.videoPictureToImage(newPic);
						File file;
						try {
							file = File.createTempFile("frame", outputFormat);
							ImageIO.write(javaImage, outputFormat, file);
							videoFrames.add(file.getCanonicalPath());
						} catch (IOException e) {
							return ErrorUtils.ERR_FILE_IO;
						}
					}
				}
			} else { // This packet isn't part of our video stream, so we just silently drop it.
				do {
				} while (false);
			}
		}
		if (videoCoder != null) {
			videoCoder.close();
			videoCoder = null;
		}
		if (container != null) {
			container.close();
			container = null;
		}
		return ErrorUtils.ERR_NONE;
	}

	public int buildVideoFromImages(String outputFilename, ID codecId,
			IRational frameRate, int width, int height,
			ArrayList<String> frames) {
		// let's make a IMediaWriter to write the file.
		final IMediaWriter writer = ToolFactory.makeWriter(outputFilename);
		// We tell it we're going to add one video stream, with id 0, at position 0, and that it will have a fixed frame rate of FRAME_RATE.
		writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_MPEG4, width, height);
		long startTime = System.nanoTime();
		for (int i = 0; i < frames.size(); i++) {
			// encode the image to stream #0
			try {
				writer.encodeVideo(0, ImageIO.read(new File(frames.get(i))), System.nanoTime() - startTime,
						TimeUnit.NANOSECONDS);
			} catch (IOException e1) {
				return ErrorUtils.ERR_FILE_IO;
			}
			try {
				Thread.sleep((long) (1000d / frameRate.getDouble()));
			} catch (InterruptedException e) {
			}
		}
		while (writer.isOpen()) {
			try {
				Thread.sleep(10);
				writer.close();
			} catch (InterruptedException e) {
			}
		}
		return ErrorUtils.ERR_NONE;
	}

	public ArrayList<String> getVideoFrames() {
		return videoFrames;
	}
	
	public IStreamCoder getVideoCoder(String filename) {
		IContainer container = IContainer.make();
		// we attempt to open up the container
		int result = container.open(filename, IContainer.Type.READ, null);
		// check if the operation was successful
		if (result < 0)
			return null;
		// query how many streams the call to open found
		int numStreams = container.getNumStreams();
		// iterate through the streams to print their meta data
		for (int i = 0; i < numStreams; i++) {
			// find the stream object
			IStream stream = container.getStream(i);
			// get the pre-configured decoder that can decode this stream;
			IStreamCoder coder = stream.getStreamCoder();
			if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
				return coder;
			}
		}
		while (container.isOpened()) {
			try {
				Thread.sleep(10);
				container.close();
			} catch (InterruptedException e) {
			}
		}
		return null;
	}
}
