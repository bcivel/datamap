package com.redoute.datamap.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.redoute.datamap.entity.Picture;
import com.redoute.datamap.factory.IFactoryPicture;
import com.redoute.datamap.service.IParameter;
import com.redoute.datamap.util.HTML5CanvasURLUtil.HTML5CanvasURLParsingException;

/**
 * Helper to handle I/O operations on {@link Picture} with the file system
 * 
 * @author abourdon
 */
@Component
public final class PictureFileHelper {

	/** The associated {@link Logger} to this class */
	private static final Logger LOG = Logger.getLogger(PictureFileHelper.class);

	@Autowired
	private IParameter parameter;

	@Autowired
	private IFactoryPicture factoryPicture;

	/**
	 * Load the {@link Picture} from the file system
	 * 
	 * <p>
	 * Loading process is based on the value of {@link Picture#getLocalPath()}.
	 * If local path is undefined, then the same {@link Picture} is returned
	 * </p>
	 * 
	 * @param picture
	 *            the base {@link Picture} to load from the file system
	 * @return a new {@link Picture} with data retrieved from the file system,
	 *         or the same {@link Picture} if local path is not defined or an
	 *         error occurred during the loading process
	 */
	public Picture load(Picture picture) {
		// If the local path is empty then we cannot proceed
		if (DAOUtil.isEmpty(picture.getLocalPath())) {
			LOG.warn("Unable to load picture " + picture + " from a non-existing local path");
			return picture;
		}

		// Compute the real path and load it to the new picture
		String realPath = getRealPath(picture.getLocalPath());
		InputStream stream = null;
		try {
			stream = new FileInputStream(realPath);
			String base64Data = HTML5CanvasURLUtil.encodeBase64Data(IOUtils.toByteArray(stream));
			String extension = FilenameUtils.getExtension(realPath);
			String base64 = HTML5CanvasURLUtil.toCanvasURL(extension, base64Data);
			return factoryPicture.create(picture.getId(), picture.getApplication(), picture.getPage(), picture.getPicture(), base64, picture.getLocalPath());
		} catch (IOException e) {
			LOG.error("Unable to load canvas url from local path " + realPath, e);
		} finally {
			DAOUtil.closeResources(stream);
		}
		return picture;
	}

	/**
	 * Saves the given {@link Picture} on the file system
	 * 
	 * <p>
	 * Saving process is based on the {@link Picture#getLocalPath()}. If no
	 * local path is defined, then a new one is automatically generated
	 * </p>
	 * 
	 * @param picture
	 *            the {@link Picture} to save
	 * @param replaceExisting
	 *            if {@link Picture} can replace an older one on the file system
	 * @return <code>true</code> if the saving process succeed,
	 *         <code>false</code> otherwise
	 */
	public boolean save(Picture picture, boolean replaceExisting) {
		if (DAOUtil.isEmpty(picture.getBase64())) {
			LOG.warn("Unable to save empty picture " + picture);
			return false;
		}

		// Check if the local path has been defined
		if (DAOUtil.isEmpty(picture.getLocalPath())) {
			try {
				picture.setLocalPath(createLocalPath(picture));
			} catch (HTML5CanvasURLParsingException e) {
				LOG.error("Unable to save picture " + picture, e);
				return false;
			}
		}

		// Get the local file from the picture's local path
		File pictureFile = new File(getRealPath(picture.getLocalPath()));
		if (pictureFile.exists() && !replaceExisting) {
			LOG.error("Unable to save already existing picture " + picture);
			return false;
		}

		// Create missing directories if necessary
		File pictureDir = new File(FilenameUtils.getFullPath(pictureFile.getAbsolutePath()));
		pictureDir.mkdirs();

		// Save image to file
		OutputStream stream = null;
		try {
			byte[] image = HTML5CanvasURLUtil.decodeBase64Data(HTML5CanvasURLUtil.parseBase64Data(picture.getBase64()));
			stream = new BufferedOutputStream(new FileOutputStream(pictureFile));
			stream.write(image);
			return true;
		} catch (HTML5CanvasURLParsingException e) {
			LOG.error("Unable to save picture " + picture, e);
		} catch (IOException e) {
			LOG.error("Unable to save picture " + picture, e);
		} finally {
			DAOUtil.closeResources(stream);
		}

		return false;
	}

	/**
	 * Deletes the {@link Picture} from the file system
	 * 
	 * @param pic
	 *            the {@link Picture} to delete from the file system
	 * @return <code>true</code> if {@link Picture} has been deleted,
	 *         <code>false</code> otherwise
	 */
	public boolean delete(Picture pic) {
		return new File(getRealPath(pic.getLocalPath())).delete();
	}

	/**
	 * Gets the real path from the given local one
	 * 
	 * <p>
	 * Real path is calculated by concatenation between
	 * {@link IParameter#getPicturePath()} and the given local path
	 * </p>
	 * 
	 * @param localPath
	 *            the local path to get the real one
	 * @return the real path from the given local one
	 * @see IParameter#getPicturePath()
	 */
	public String getRealPath(String localPath) {
		StringBuilder realPath = new StringBuilder(parameter.getPicturePath());
		realPath.append(File.separator);
		realPath.append(localPath);
		return realPath.toString();
	}

	/**
	 * Creates a local path based on the given {@link Picture}'s information
	 * 
	 * @param picture
	 *            the base {@link Picture} to get information
	 * @return a local path of the given {@link Picture}
	 * @throws HTML5CanvasURLParsingException
	 *             if the given {@link Picture} has an invalid
	 *             <code>base64</code> field
	 */
	public String createLocalPath(Picture picture) throws HTML5CanvasURLParsingException {
		StringBuilder localPath = new StringBuilder(picture.getApplication());
		localPath.append(File.separator);
		localPath.append(picture.getPage());
		localPath.append(File.separator);
		localPath.append(picture.getPicture());
		localPath.append(FilenameUtils.EXTENSION_SEPARATOR_STR);
		localPath.append(HTML5CanvasURLUtil.parseImageType(picture.getBase64()));
		return localPath.toString();
	}

}
