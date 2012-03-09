package utils;

import java.util.Locale;
import java.util.ResourceBundle;

public class ErrorUtils {
	
	public final static int ERR_NONE = 0;
	public final static int ERR_UNKNOWN = -1;
	public final static int ERR_XUGGLER_GPL = -2;
	public final static int ERR_FILE_IO = -3;
	public final static int ERR_XUGGLER_VIDEO_STREAM_OPEN = -4;
	public final static int ERR_XUGGLER_VIDEO_DECODER_OPEN = -5;
	public final static int ERR_XUGGLER_VIDEO_COLORSPACE = -6;
	public final static int ERR_XUGGLER_VIDEO_DECODER_DECODE = -7;
	public final static int ERR_XUGGLER_VIDEO_RESAMPLER_RESAMPLE = -8;
	public final static int ERR_XUGGLER_VIDEO_DECODER_BGR24 = -9;
	public final static int ERR_FILTER_TEMP_FILE = -10;
	public final static int ERR_MERAPI_SEND_MESSAGE = -11;
	public final static int ERR_HIBERNATE_QUERY_LIST = -12;
	public final static int ERR_HIBERNATE_QUERY_REMOVE = -13;
	public final static int ERR_HIBERNATE_QUERY_SAVE = -14;
	
	public static String getErrorMsg(int code) {
		return getErrorMsg(Locale.getDefault(), code);
	}
	
	public static String getErrorMsg(Locale l, int code) {
		ResourceBundle bundle = ResourceBundle.getBundle("locale/Resources", l);
		switch(code) {
			case ERR_NONE :
				return bundle.getString("err.none");
			case ERR_XUGGLER_GPL :
				return bundle.getString("err.xuggler.gpl");
			case ERR_FILE_IO :
				return bundle.getString("err.file.io");
			case ERR_XUGGLER_VIDEO_STREAM_OPEN :
				return bundle.getString("err.xuggler.video.stream.open");
			case ERR_XUGGLER_VIDEO_DECODER_OPEN :
				return bundle.getString("err.xuggler.video.decoder.open");
			case ERR_XUGGLER_VIDEO_COLORSPACE :
				return bundle.getString("err.xuggler.video.colorspace");
			case ERR_XUGGLER_VIDEO_DECODER_DECODE :
				return bundle.getString("err.xuggler.video.decoder.decode");
			case ERR_XUGGLER_VIDEO_RESAMPLER_RESAMPLE:
				return bundle.getString("err.xuggler.video.resampler.resample");
			case ERR_XUGGLER_VIDEO_DECODER_BGR24 :
				return bundle.getString("err.xuggler.video.decoder.bgr24");
			case ERR_FILTER_TEMP_FILE :
				return bundle.getString("err.xuggler.filter.tempfile");
			case ERR_HIBERNATE_QUERY_LIST :
				return bundle.getString("err.hibernate.query.list");
			case ERR_HIBERNATE_QUERY_REMOVE :
				return bundle.getString("err.hibernate.query.remove");
			case ERR_HIBERNATE_QUERY_SAVE :
				return bundle.getString("err.hibernate.query.save");
			case ERR_MERAPI_SEND_MESSAGE :
				return bundle.getString("err.merapi.send.message");
			default:
				return bundle.getString("err.unknown");
		}
	}
}
