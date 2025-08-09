package org.example.arabic.main;

import org.example.arabic.model.HandwrittenCharacter;
import org.example.arabic.ocr.HandwrittenOCR;
import org.example.arabic.service.MilvusService;
import java.io.File;

public class App {

    public static void main(String[] args) {
        System.out.println("Starting Arabic Handwritten Recognition App...");

        // Images folder path
        String imagesFolder = "src/main/resources/images/";

        // Check if folder exists
        File folder = new File(imagesFolder);
        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("Error: Folder not found: " + imagesFolder);
            return;
        }

        // Search for images in the folder
        File[] imageFiles = folder.listFiles((dir, name) -> {
            String lowercaseName = name.toLowerCase();
            return lowercaseName.endsWith(".jpg") ||
                    lowercaseName.endsWith(".jpeg") ||
                    lowercaseName.endsWith(".png") ||
                    lowercaseName.endsWith(".bmp") ||
                    lowercaseName.endsWith(".gif");
        });

        if (imageFiles == null || imageFiles.length == 0) {
            System.out.println("No images found in folder: " + imagesFolder);
            return;
        }

        System.out.println("Found " + imageFiles.length + " image(s)");
        System.out.println("Processing images...\n");

        // Create OCR object
        HandwrittenOCR ocr = new HandwrittenOCR();

        // Process each image
        for (int i = 0; i < imageFiles.length; i++) {
            File imageFile = imageFiles[i];
            String imagePath = imageFile.getAbsolutePath();

            System.out.println("Processing [" + (i+1) + "/" + imageFiles.length + "]: " + imageFile.getName());

            // Create handwritten character object
            HandwrittenCharacter hwChar = new HandwrittenCharacter("", imagePath);

            // Process the image
            ocr.processHandwrittenCharacter(hwChar);

            System.out.println("OCR Result: " + hwChar);
            System.out.println("─".repeat(50));
        }

        System.out.println("All images processed successfully!");

        // Create Milvus service and collection (if not exists)
        try {
            MilvusService milvusService = new MilvusService();
            milvusService.createCollection("handwritten_characters", 128);  // 128 is example for vector dimension
        } catch (Exception e) {
            System.out.println("Warning: Could not connect to Milvus (make sure Docker is running)");
        }

        // You can add other operations like data insertion, search, etc. later
    }
}