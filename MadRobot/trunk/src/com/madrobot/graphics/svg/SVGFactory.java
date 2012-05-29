package com.madrobot.graphics.svg;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.StringTokenizer;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.Picture;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.Log;

import com.madrobot.di.XMLUtils;
import com.madrobot.graphics.ColorUtils;
import com.madrobot.graphics.GraphicsUtils;
import com.madrobot.io.CopyInputStream;

/**
 * Entry point for parsing SVG files for Android. Use one of the various static
 * methods for parsing SVGs by resource, asset or input stream. Optionally, a
 * single color can be searched and replaced in the SVG while parsing. You can
 * also parse an svg path directly.
 * 
 * @see #getSVGFromResource(android.content.res.Resources, int)
 * @see #getSVGFromAsset(android.content.res.AssetManager, String)
 * @see #getSVGFromString(String)
 * @see #getSVGFromInputStream(java.io.InputStream)
 * @see #parsePath(String)
 * 
 */
public class SVGFactory {

	
	/**
	 * Parse SVG data from an input stream.
	 * 
	 * @param svgData
	 *            the input stream, with SVG XML data in UTF-8 character
	 *            encoding.
	 * @return the parsed SVG.
	 * @throws SVGParseException
	 *             if there is an error while parsing.
	 */
	public static SVG getSVGFromInputStream(InputStream svgData)
			throws SVGParseException {
		return SVGFactory.parse(svgData, 0, 0, false);
	}

	/**
	 * Parse SVG data from a string.
	 * 
	 * @param svgData
	 *            the string containing SVG XML data.
	 * @return the parsed SVG.
	 * @throws SVGParseException
	 *             if there is an error while parsing.
	 */
	public static SVG getSVGFromString(String svgData) throws SVGParseException {
		return getSVGFromByteArray(svgData.getBytes());
	}

	public static SVG getSVGFromByteArray(byte[] svgData) throws SVGParseException {
		return SVGFactory.parse(new ByteArrayInputStream(svgData), 0,
				0, false);
	}
	/**
	 * Parse SVG data from an Android application resource.
	 * <p>
	 * This is a blocking operation and should be done preferably on a non-UI
	 * thread.
	 * </p>
	 * 
	 * @param resources
	 *            the Android context resources.
	 * @param resId
	 *            the ID of the raw resource SVG.
	 * @return the parsed SVG.
	 * @throws SVGParseException
	 *             if there is an error while parsing.
	 */
	public static SVG getSVGFromResource(Resources resources, int resId)
			throws SVGParseException {
		return SVGFactory.parse(resources.openRawResource(resId), 0, 0, false);
	}

	/**
	 * Parse SVG data from an Android application asset.
	 * <p>
	 * This is a blocking operation and should be done preferably on a non-UI
	 * thread.
	 * </p>
	 * 
	 * @param assetMngr
	 *            the Android asset manager.
	 * @param svgPath
	 *            the path to the SVG file in the application's assets.
	 * @return the parsed SVG.
	 * @throws SVGParseException
	 *             if there is an error while parsing.
	 * @throws IOException
	 *             if there was a problem reading the file.
	 */
	public static SVG getSVGFromAsset(AssetManager assetMngr, String svgPath)
			throws SVGParseException, IOException {
		InputStream inputStream = assetMngr.open(svgPath);
		SVG svg = getSVGFromInputStream(inputStream);
		inputStream.close();
		return svg;
	}

	/**
	 * Parse SVG data from an input stream, replacing a single color with
	 * another color.
	 * <p>
	 * This is a blocking operation and should be done preferably on a non-UI
	 * thread.
	 * </p>
	 * 
	 * @param svgData
	 *            the input stream, with SVG XML data in UTF-8 character
	 *            encoding.
	 * @param searchColor
	 *            the color in the SVG to replace.
	 * @param replaceColor
	 *            the color with which to replace the search color.
	 * @return the parsed SVG.
	 * @throws SVGParseException
	 *             if there is an error while parsing.
	 */
	public static SVG getSVGFromInputStream(InputStream svgData,
			int searchColor, int replaceColor) throws SVGParseException {
		return SVGFactory.parse(svgData, searchColor, replaceColor, false);
	}

	/**
	 * Parse SVG data from a string.
	 * <p>
	 * This is a blocking operation and should be done preferably on a non-UI
	 * thread.
	 * </p>
	 * 
	 * @param svgData
	 *            the string containing SVG XML data.
	 * @param searchColor
	 *            the color in the SVG to replace.
	 * @param replaceColor
	 *            the color with which to replace the search color.
	 * @return the parsed SVG.
	 * @throws SVGParseException
	 *             if there is an error while parsing.
	 */
	public static SVG getSVGFromString(String svgData, int searchColor,
			int replaceColor) throws SVGParseException {
		return SVGFactory.parse(new ByteArrayInputStream(svgData.getBytes()),
				searchColor, replaceColor, false);
	}

	/**
	 * Parse SVG data from an Android application resource.
	 * <p>
	 * This is a blocking operation and should be done preferably on a non-UI
	 * thread.
	 * </p>
	 * 
	 * @param resources
	 *            the Android context
	 * @param resId
	 *            the ID of the raw resource SVG.
	 * @param searchColor
	 *            the color in the SVG to replace.
	 * @param replaceColor
	 *            the color with which to replace the search color.
	 * @return the parsed SVG.
	 * @throws SVGParseException
	 *             if there is an error while parsing.
	 */
	public static SVG getSVGFromResource(Resources resources, int resId,
			int searchColor, int replaceColor) throws SVGParseException {
		return SVGFactory.parse(resources.openRawResource(resId), searchColor,
				replaceColor, false);
	}

	/**
	 * Parse SVG data from an Android application asset.
	 * <p>
	 * This is a blocking operation and should be done preferably on a non-UI
	 * thread.
	 * </p>
	 * 
	 * @param assetMngr
	 *            the Android asset manager.
	 * @param svgPath
	 *            the path to the SVG file in the application's assets.
	 * @param searchColor
	 *            the color in the SVG to replace.
	 * @param replaceColor
	 *            the color with which to replace the search color.
	 * @return the parsed SVG.
	 * @throws SVGParseException
	 *             if there is an error while parsing.
	 * @throws IOException
	 *             if there was a problem reading the file.
	 */
	public static SVG getSVGFromAsset(AssetManager assetMngr, String svgPath,
			int searchColor, int replaceColor) throws SVGParseException,
			IOException {
		InputStream inputStream = assetMngr.open(svgPath);
		SVG svg = getSVGFromInputStream(inputStream, searchColor, replaceColor);
		inputStream.close();
		return svg;
	}

	private static SVG parse(InputStream in, Integer searchColor,
			Integer replaceColor, boolean whiteMode) throws SVGParseException {
		// Util.debug("Parsing SVG...");
		SVGHandler svgHandler = null;
		try {
			// long start = System.currentTimeMillis();
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			final Picture picture = new Picture();
			svgHandler = new SVGHandler(picture,20);
			svgHandler.setColorSwap(searchColor, replaceColor);
			svgHandler.setWhiteMode(whiteMode);

			CopyInputStream cin = new CopyInputStream(in);

			IDHandler idHandler = new IDHandler();
			xr.setContentHandler(idHandler);
			xr.parse(new InputSource(cin.getCopy()));
			svgHandler.idXml = idHandler.idXml;

			xr.setContentHandler(svgHandler);
			xr.parse(new InputSource(cin.getCopy()));
			// Util.debug("Parsing complete in " + (System.currentTimeMillis() -
			// start) + " millis.");
			SVG result = new SVG(picture, svgHandler.bounds);
			// Skip bounds if it was an empty pic
			if (!Float.isInfinite(svgHandler.limits.top)) {
				result.setLimits(svgHandler.limits);
			}
			return result;
		} catch (Exception e) {
			throw new SVGParseException(e);
		}
	}

	
	}