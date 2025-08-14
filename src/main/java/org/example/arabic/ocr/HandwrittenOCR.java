package org.example.arabic.ocr;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.example.arabic.model.HandwrittenCharacter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import javax.imageio.ImageIO;

public class HandwrittenOCR {

    private ITesseract tesseract;

    public HandwrittenOCR() {
        tesseract = new Tesseract();
        tesseract.setDatapath("./tessdata"); // path للـ tessdata folder
        tesseract.setLanguage("ara"); // Arabic language

        // إعدادات OCR مع استخدام الأرقام بدلاً من الثوابت المفقودة
        tesseract.setOcrEngineMode(1); // OEM_LSTM_ONLY
        tesseract.setPageSegMode(6);   // PSM_SINGLE_BLOCK
        tesseract.setVariable("preserve_interword_spaces", "1");
        tesseract.setVariable("textord_min_linesize", "2.5"); // للخط اليدوي
    }


    private BufferedImage enhanceImage(BufferedImage original) {
        int width = original.getWidth();
        int height = original.getHeight();

        // تحويل الصورة إلى رمادي
        BufferedImage gray = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2d = gray.createGraphics();
        g2d.drawImage(original, 0, 0, null);
        g2d.dispose();

        // تكبير الصورة 2x
        BufferedImage resized = new BufferedImage(width * 2, height * 2, BufferedImage.TYPE_BYTE_GRAY);
        g2d = resized.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.drawImage(gray, 0, 0, width * 2, height * 2, null);
        g2d.dispose();

        // تحسين التباين
        RescaleOp rescaleOp = new RescaleOp(1.5f, 0, null); // زيادة التباين
        BufferedImage contrasted = rescaleOp.filter(resized, null);

        return contrasted;
    }

    // تنظيف النص من الأخطاء الشائعة
    private String cleanText(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "NO_TEXT_DETECTED";
        }
        return text.trim()
                .replaceAll("\\s+", " ") // مسافات متعددة -> مسافة واحدة
                .replace("\"", "") // إزالة علامات اقتباس
                .replace("'", "") // إزالة أبوستروفي
                .replaceAll("[^\\u0600-\\u06FF\\u0750-\\u077F\\s0-9]", ""); // الاحتفاظ بالعربي والأرقام فقط
    }

    public String recognizeCharacter(String imagePath) {
        try {
            System.out.println("Processing image at: " + imagePath);
            File imageFile = new File(imagePath);
            BufferedImage image = ImageIO.read(imageFile);
            if (image == null) {
                System.out.println("Could not read image: " + imagePath);
                return "ERROR_READING_IMAGE";
            }
            System.out.println("Image loaded successfully! Size: " + image.getWidth() + "x" + image.getHeight());

            // تحسين الصورة
            BufferedImage enhancedImage = enhanceImage(image);

            // Perform Arabic OCR
            String result = tesseract.doOCR(enhancedImage);

            // تنظيف النص
            String cleanedResult = cleanText(result);
            System.out.println("Raw result: " + result);
            System.out.println("Cleaned result: " + cleanedResult);

            return cleanedResult.isEmpty() ? "NO_TEXT_DETECTED" : cleanedResult;

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
