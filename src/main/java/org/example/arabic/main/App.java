package org.example.arabic.main;

import org.example.arabic.model.HandwrittenCharacter;
import org.example.arabic.ocr.HandwrittenOCR;
import org.example.arabic.service.MilvusService;

public class App {

    public static void main(String[] args) {
        System.out.println("Starting Arabic Handwritten Recognition App...");

        // إنشاء كائن يمثل حرف مكتوب بخط اليد مع مسار صورة تجريبية
        HandwrittenCharacter hwChar = new HandwrittenCharacter("", "src/main/resources/images/test.jpeg");


        // إنشاء كائن الـ OCR ومعالجة الحرف
        HandwrittenOCR ocr = new HandwrittenOCR();
        ocr.processHandwrittenCharacter(hwChar);

        System.out.println("OCR Result: " + hwChar);

        // إنشاء خدمة Milvus وإنشاء Collection (لو مش موجود)
        MilvusService milvusService = new MilvusService();
        milvusService.createCollection("handwritten_characters", 128);  // 128 هنا هو مثال لـ vector dimension

        // ممكن تضيف عمليات تانية زي إدخال بيانات، بحث، الخ لاحقاً
    }
}