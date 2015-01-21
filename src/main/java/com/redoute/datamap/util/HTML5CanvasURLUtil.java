package com.redoute.datamap.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;

/**
 * Set of utility methods to handle HTML5 Canvas URL
 * 
 * @author abourdon
 * @see http://www.w3schools.com/html/html5_canvas.asp
 */
public final class HTML5CanvasURLUtil {

	/**
	 * Common exception when a canvas URL parsing error occurred.
	 * 
	 * @author abourdon
	 */
	public static class HTML5CanvasURLParsingException extends Exception {

		private static final long serialVersionUID = 1L;

		public HTML5CanvasURLParsingException(String message) {
			super(message);
		}

	}

	/** Pattern to get image type from canvas URL */
	private static final Pattern IMAGE_TYPE_PATTERN = Pattern.compile("^data:image/([^;]+).*");

	/** Pattern of the entire canvas URL header */
	private static final Pattern BASE64_DATA_PATTERN = Pattern.compile("^data:image/[^;]+;base64,(.*)$");

	/**
	 * Parses the image type from the given canvas URL
	 * 
	 * @param canvasURL
	 *            the canvas URL to parse
	 * @return the image type from the given canvas URL
	 * @throws HTML5CanvasURLParsingException
	 *             if the given canvas URL is malformed
	 */
	public static String parseImageType(String canvasURL) throws HTML5CanvasURLParsingException {
		Matcher imageTypeMatcher = IMAGE_TYPE_PATTERN.matcher(canvasURL);
		if (!imageTypeMatcher.find()) {
			throw new HTML5CanvasURLParsingException("Invalid canvas URL " + canvasURL);
		}
		return imageTypeMatcher.group(1);
	}

	/**
	 * Parses the base64 data from the given canvas URL
	 * 
	 * @param canvasURL
	 *            the canvas URL to parse
	 * @return the base64 data from the given canvas URL
	 * @throws HTML5CanvasURLParsingException
	 *             if the given canvas URL is malformed
	 */
	public static String parseBase64Data(String canvasURL) throws HTML5CanvasURLParsingException {
		Matcher base64DataMatcher = BASE64_DATA_PATTERN.matcher(canvasURL);
		if (!base64DataMatcher.find()) {
			throw new HTML5CanvasURLParsingException("Invalid canvas URL " + canvasURL);
		}
		return base64DataMatcher.group(1);
	}

	/**
	 * Creates a new canvas URL based on the given image type and base64 data
	 * 
	 * @param base64Data
	 * @param imageType
	 * @return a new canvas URL based on the given image type and base64 data
	 */
	public static String toCanvasURL(String imageType, String base64Data) {
		return String.format("data:image/%s;base64,%s", imageType, base64Data);
	}

	/**
	 * Decodes the given base64 {@link String} data
	 * 
	 * @param base64Data
	 *            the base64 {@link String} data to decode
	 * @return the decoded value of the given base64 {@link String} data
	 * @see Base64#decodeBase64(byte[])
	 */
	public static byte[] decodeBase64Data(String base64Data) {
		return Base64.decodeBase64(base64Data.getBytes());
	}

	/**
	 * Encodes the given base64 data into a base64 {@link String} one
	 * 
	 * @param base64
	 *            the base64 data to encode
	 * @return the encoded value of the given base64 data
	 * @see Base64#encodeBase64(byte[])
	 */
	public static String encodeBase64Data(byte[] base64) {
		return new String(Base64.encodeBase64(base64));
	}

	/**
	 * Utility class then private constructor
	 */
	private HTML5CanvasURLUtil() {
	}

}
