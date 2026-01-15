package com.example.HCOData.utils;

import com.example.HCOData.constant.DocumentsPathConstants;
import com.example.HCOData.constant.Patterns;
import com.example.HCOData.model.Image;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

@Slf4j
public class ImageUtil {
	
	public static byte[] createChecksum(File file) throws Exception {
	    FileInputStream fis = new FileInputStream(file);
	    byte[] buffer = new byte[1024];
	    MessageDigest complete = MessageDigest.getInstance("MD5");
	    int numRead;
	    do {
	        numRead = fis.read(buffer);
	        if (numRead > 0) {
	            complete.update(buffer, 0, numRead);
	        }
	    } while (numRead != -1);
	    fis.close();
	    return complete.digest();
	}

	public static String convertImageToBase64(File image) throws IOException {
		byte[] fileContent = FileUtils.readFileToByteArray(image);
		String encodedString = Base64.getEncoder().encodeToString(fileContent);
		return encodedString;

	}

	public static String convertStringFromAccToReg(String chainCharacter) {
		String[] listOfChainCharacter = chainCharacter.split("");
		List<String> listPfChainCharacterArray = Arrays.asList(listOfChainCharacter);
		for (String str : listOfChainCharacter) {
			if (Arrays.asList(Patterns.ARRAY_ACC_CHARS).contains(str)) {
				listPfChainCharacterArray.set(listPfChainCharacterArray.indexOf(str),
						Arrays.asList(Patterns.ARRAY_REG_CHARS)
								.get(Arrays.asList(Patterns.ARRAY_ACC_CHARS).indexOf(str)));
			}
		}
		return ConcatStrings(listPfChainCharacterArray);

	}
	public static File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
		File file = new File(System.getProperty("java.io.tmpdir") + File.separator + multipartFile.getOriginalFilename());
		try (FileOutputStream fos = new FileOutputStream(file)) {
			fos.write(multipartFile.getBytes());
		}

		return file;
	}

	public static String getValidPath(String windowsPath, String linuxPath) {
		File windwsFile = new File(windowsPath);
		File linuxFile = new File(linuxPath);

		if (windwsFile.exists()) {
			return windowsPath;
		} else if (linuxFile.exists()) {
			return linuxPath;
		} else {
			return linuxPath;
		}
	}


	public static String ConcatStrings(List<String> listOfStrings) {
		StringBuilder result = new StringBuilder();
		for (String str : listOfStrings) {
			result.append(str);
		}
		return result.toString();
	}

	public static String getTypeImage(MultipartFile multipart) {
		String fileName = multipart.getOriginalFilename();
		if (fileName == null) {
			return ".jpg";
		}
		int lastIndexOf = fileName.lastIndexOf('.');
		if (lastIndexOf == -1) {
			return ".jpg";
		}
		return fileName.substring(lastIndexOf + 1);
	}
	public static int getNumberPagesPdf(MultipartFile multipart) {
		String originalFileName = multipart.getOriginalFilename();
		if (originalFileName != null && originalFileName.toLowerCase().endsWith(".pdf")) {
			try (PDDocument doc = PDDocument.load(multipart.getInputStream())) {
				int pageCount = doc.getNumberOfPages();
				return pageCount;
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Error reading PDF file : {} ", e.getMessage());
			}
		} else {
			return 1;
		}
		return 0;
	}

	public static String getMD5Checksum(File file) throws Exception {
	    byte[] b = createChecksum(file);
	    StringBuilder result = new StringBuilder();
	    for (byte value : b) {
	        result.append(Integer.toString((value & 0xff) + 0x100, 16).substring(1));
	    }
	    return result.toString();
	}

	public static File multipartToFile(MultipartFile multipart, String imageType) throws IOException {
		String originalFileName = multipart.getOriginalFilename();

		if (originalFileName != null && originalFileName.toLowerCase().endsWith(".pdf")) {
			String newFileName = originalFileName.replace(".pdf", ".jpg");
			File jpgFile = new File(DocumentsPathConstants.FIRST_DESTINATION + "/" + newFileName);

			try (PDDocument document = PDDocument.load(multipart.getInputStream())) {
				PDFRenderer pdfRenderer = new PDFRenderer(document);
				int pageCount = document.getNumberOfPages();

				if (pageCount > 1) {
					BufferedImage mergedImage = mergePdfPagesToImage(pdfRenderer, pageCount);
					ImageIO.write(mergedImage, "jpg", jpgFile);
				} else {
					BufferedImage image = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);
					ImageIO.write(image, "jpg", jpgFile);
				}
			}

			return jpgFile;
		} else {
			String newFileName;
			if (originalFileName != null && originalFileName.toLowerCase().endsWith(".jpg")) {
				newFileName = originalFileName.replace(".jpg", "." + imageType);
			} else {
				newFileName = originalFileName;
			}

			File convFile = new File(DocumentsPathConstants.FIRST_DESTINATION + "/" + newFileName);
			multipart.transferTo(convFile);
			return convFile;
		}
	}

	private static BufferedImage mergePdfPagesToImage(PDFRenderer pdfRenderer, int pageCount) throws IOException {
		int dpi = 300;
		BufferedImage firstPage = pdfRenderer.renderImageWithDPI(0, dpi, ImageType.RGB);
		int singlePageWidth = firstPage.getWidth();
		int singlePageHeight = firstPage.getHeight();

		int finalImageHeight = singlePageHeight * pageCount;
		int finalImageWidth = singlePageWidth;

		BufferedImage finalImage = new BufferedImage(finalImageWidth, finalImageHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = finalImage.createGraphics();
		g2d.setBackground(Color.WHITE);
		g2d.clearRect(0, 0, finalImageWidth, finalImageHeight);

		for (int i = 0; i < pageCount; i++) {
			BufferedImage pageImage = pdfRenderer.renderImageWithDPI(i, dpi, ImageType.RGB);
			g2d.drawImage(pageImage, 0, i * singlePageHeight, null);
		}
		g2d.dispose();

		return finalImage;
	}

	public static String decode(File whatFile) throws Exception {
		String tmpFinalResult = null;
		try {
			Map<DecodeHintType, Object> whatHints = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);
			whatHints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
			whatHints.put(DecodeHintType.POSSIBLE_FORMATS, EnumSet.allOf(BarcodeFormat.class));
			whatHints.put(DecodeHintType.PURE_BARCODE, Boolean.FALSE);

			if (whatFile == null || whatFile.getName().trim().isEmpty())
				throw new IllegalArgumentException("File not found, or invalid file name.");
			BufferedImage tmpBfrImage;
			try {
				tmpBfrImage = ImageIO.read(whatFile);
			} catch (IOException tmpIoe) {
				throw new Exception(tmpIoe.getMessage());
			}
			if (tmpBfrImage == null)
				throw new IllegalArgumentException("Could not decode image.");
			LuminanceSource tmpSource = new BufferedImageLuminanceSource(tmpBfrImage);
			BinaryBitmap tmpBitmap = new BinaryBitmap(new HybridBinarizer(tmpSource));
			MultiFormatReader tmpBarcodeReader = new MultiFormatReader();
			Result tmpResult;

			try {
				if (whatHints != null && !whatHints.isEmpty())
					tmpResult = tmpBarcodeReader.decode(tmpBitmap, whatHints);
				else
					tmpResult = tmpBarcodeReader.decode(tmpBitmap);
				tmpFinalResult = String.valueOf(tmpResult.getText());
			} catch (NotFoundException notFoundException) {
				System.out.println("No barcode found in the provided image.");
				tmpFinalResult = null;
			}


		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception error :", e.getMessage());
		}

		return tmpFinalResult;
	}

	public static void sendImageToFolder(File imageFile, String destinationFolder) {
		if (!imageFile.exists()) {
			System.out.println("The source image file does not exist.");
			return;
		}

		Path destinationPath = Paths.get(destinationFolder);
		if (!Files.exists(destinationPath)) {
			try {
				Files.createDirectories(destinationPath);
			} catch (IOException e) {
				System.out.println("Could not create the destination folder: " + e.getMessage());
				return;
			}
		}
		Path destinationFilePath = destinationPath.resolve(imageFile.getName());
		try {
			Files.copy(imageFile.toPath(), destinationFilePath);
			System.out.println("Image successfully sent to folder: " + destinationFilePath.toString());
		} catch (IOException e) {
			System.out.println("Error while sending the image to the folder: " + e.getMessage());
		}
	}

	public static void generateMD5(File imageFile, Image image) {
		try {
			File file = new File(DocumentsPathConstants.SECOND_DESTINATION + "/" + imageFile.getName());
			String imageToMD5 = ImageUtil.getMD5Checksum(file);
			image.setImageMD5(imageToMD5);
		} catch (Exception e) {
			log.error("Error while generating MD5 checksum: {}", e.getMessage(), e);
		}
	}

}
