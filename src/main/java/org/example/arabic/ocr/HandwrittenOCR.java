package org.example.arabic.ocr;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.example.arabic.model.HandwrittenCharacter;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class HandwrittenOCR {

    private ITesseract tesseract;

    public HandwrittenOCR() {
        tesseract = new Tesseract();
        tesseract.setDatapath("./tessdata"); // path للـ tessdata folder
        tesseract.setLanguage("ara"); // Arabic language
    }

    public String recognizeCharacter(String imagePath) {
        try {
            System.out.println("Processing image at: " + imagePath);

            // Read the image
            File imageFile = new File(imagePath);
            BufferedImage image = ImageIO.read(imageFile);

            if (image == null) {
                System.out.println("Could not read image: " + imagePath);
                return "ERROR_READING_IMAGE";
            }

            System.out.println("Image loaded successfully! Size: " + image.getWidth() + "x" + image.getHeight());

            // Perform Arabic OCR
            String result = tesseract.doOCR(image);

            // Clean up the result
            result = result.trim().replaceAll("\\s+", " ");

            return result.isEmpty() ? "NO_TEXT_DETECTED" : result;

        } catch (Exception e) {
            System.err.println("OCR Error: " + e.getMessage());
            return "OCR_ERROR";
        }
    }

    public void processHandwrittenCharacter(HandwrittenCharacter hwChar) {
        String recognizedText = recognizeCharacter(hwChar.getImagePath());
        hwChar.setCharacter(recognizedText);
        System.out.println("Recognized text: " + recognizedText);
    }
}