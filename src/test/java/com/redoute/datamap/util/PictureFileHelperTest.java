package com.redoute.datamap.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;

import com.redoute.datamap.entity.Picture;
import com.redoute.datamap.factory.impl.FactoryPicture;
import com.redoute.datamap.service.IParameter;
import com.redoute.datamap.util.HTML5CanvasURLUtil.HTML5CanvasURLParsingException;

/**
 * {@link PictureFileHelper} test suite
 * 
 * @author abourdon
 */
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(locations = { "/applicationContextTest.xml" })
public class PictureFileHelperTest {

	private static final String CURRENT_DIRECTORY_PATH = PictureFileHelperTest.class.getResource("").getPath();
	private static final String TEMPORARY_DIRECTORY_PATH = System.getProperty("java.io.tmpdir");

	private static final int PICTURE_ID = 0;
	private static final String PICTURE_APPLICATION = "application";
	private static final String PICTURE_PAGE = "page";
	private static final String PICTURE_PICTURE = "picture";
	private static final String PICTURE_LOCAL_PATH = "application/page/picture/pic.png";
	private static final String PICTURE_IMAGE_TYPE = "png";
	private static final String PICTURE_CANVAS_URL = "data:image/png;base64,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";
	private static final String PICTURE_REPLACEMENT_CANVAS_URL = "data:image/png;base64,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";

	@Mock
	private IParameter parameter;

	@Mock
	private FactoryPicture factoryPicture;

	@InjectMocks
	private PictureFileHelper pictureFileHelper;

	private Picture picture;

	private File pictureFile;

	private void mockFactoryPicture(Picture picture) {
		final Picture newPicture = new Picture(picture);
		Mockito.when(factoryPicture.create(picture.getId(), picture.getApplication(), picture.getPage(), picture.getPicture(), picture.getBase64(), picture.getLocalPath()))
				.thenReturn(newPicture);
	}

	private void mockPicturePath(String picturePath) {
		Mockito.when(parameter.getPicturePath()).thenReturn(picturePath);
	}

	private Picture newPicture(String localPath) throws IOException {
		InputStream stream = PictureFileHelperTest.class.getResourceAsStream(localPath);
		String base64Data = HTML5CanvasURLUtil.encodeBase64Data(IOUtils.toByteArray(stream));
		String base64 = HTML5CanvasURLUtil.toCanvasURL(FilenameUtils.getExtension(localPath), base64Data);

		Picture picture = new Picture();
		picture.setId(PICTURE_ID);
		picture.setApplication(PICTURE_APPLICATION);
		picture.setPage(PICTURE_PAGE);
		picture.setPicture(PICTURE_PICTURE);
		picture.setBase64(base64);
		picture.setLocalPath(localPath);

		mockFactoryPicture(picture);

		return picture;
	}

	private File newPictureFile(Picture picture) {
		mockPicturePath(TEMPORARY_DIRECTORY_PATH);

		File pictureFile = new File(pictureFileHelper.getRealPath(picture.getLocalPath()));
		if (pictureFile.exists()) {
			pictureFile.delete();
		}
		return pictureFile;
	}

	@Before
	public void setup() throws IOException {
		picture = newPicture(PICTURE_LOCAL_PATH);
		pictureFile = newPictureFile(picture);
	}

	@Test
	public void testLoad() {
		mockPicturePath(CURRENT_DIRECTORY_PATH);

		Picture loadedPicture = pictureFileHelper.load(picture);
		Assert.assertNotSame(picture, loadedPicture);
		Assert.assertEquals(picture.getBase64(), loadedPicture.getBase64());
	}

	@Test
	public void testLoadWithBase64Ignorance() {
		mockPicturePath(CURRENT_DIRECTORY_PATH);

		picture.setBase64("!!base64!!");
		Picture loadedPicture = pictureFileHelper.load(picture);
		Assert.assertNotSame(picture, loadedPicture);
		Assert.assertThat(loadedPicture.getBase64(), CoreMatchers.not(CoreMatchers.equalTo(picture.getBase64())));
	}

	@Test
	public void testSave() {
		mockPicturePath(TEMPORARY_DIRECTORY_PATH);

		Assert.assertFalse(pictureFile.exists());
		Assert.assertTrue(pictureFileHelper.save(picture, true));
		Assert.assertTrue(pictureFile.exists());
	}

	@Test
	public void testSaveWithReplacement() throws IOException {
		Picture pictureReplacement = new Picture(picture);
		pictureReplacement.setBase64(PICTURE_REPLACEMENT_CANVAS_URL);
		mockPicturePath(TEMPORARY_DIRECTORY_PATH);
		mockFactoryPicture(pictureReplacement);

		Assert.assertTrue(pictureFileHelper.save(picture, true));
		Picture savedPicture = pictureFileHelper.load(picture);
		Assert.assertEquals(picture, savedPicture);

		picture.setBase64(PICTURE_REPLACEMENT_CANVAS_URL);
		Assert.assertTrue(pictureFileHelper.save(picture, true));
		savedPicture = pictureFileHelper.load(picture);
		Assert.assertEquals(picture, savedPicture);

		picture.setBase64(PICTURE_CANVAS_URL);
		Assert.assertFalse(pictureFileHelper.save(picture, false));
		savedPicture = pictureFileHelper.load(picture);
		Assert.assertThat(savedPicture, CoreMatchers.not(CoreMatchers.equalTo(picture)));
		picture.setBase64(PICTURE_REPLACEMENT_CANVAS_URL);
		Assert.assertEquals(picture, savedPicture);
	}

	@Test
	public void testDelete() {
		mockPicturePath(TEMPORARY_DIRECTORY_PATH);

		pictureFileHelper.save(picture, true);
		Picture loadedPicture = pictureFileHelper.load(picture);
		Assert.assertEquals(picture, loadedPicture);

		pictureFileHelper.delete(picture);
		picture.setBase64("");
		loadedPicture = pictureFileHelper.load(picture);
		Assert.assertEquals(picture, loadedPicture);
	}

	@Test
	public void testGetRealPath() {
		mockPicturePath(TEMPORARY_DIRECTORY_PATH);

		StringBuilder expected = new StringBuilder(TEMPORARY_DIRECTORY_PATH);
		expected.append(File.separator);
		expected.append(picture.getLocalPath());

		Assert.assertEquals(expected.toString(), pictureFileHelper.getRealPath(picture.getLocalPath()));
	}

	@Test
	public void testCreateLocalPath() throws HTML5CanvasURLParsingException {
		StringBuilder expected = new StringBuilder(PICTURE_APPLICATION);
		expected.append(File.separator);
		expected.append(PICTURE_PAGE);
		expected.append(File.separator);
		expected.append(PICTURE_PICTURE);
		expected.append(FilenameUtils.EXTENSION_SEPARATOR_STR);
		expected.append(PICTURE_IMAGE_TYPE);

		Assert.assertEquals(expected.toString(), pictureFileHelper.createLocalPath(picture));
	}

}
