package com.idk.shit.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NanoVG;
import static org.lwjgl.nanovg.NanoVG.nvgBeginFrame;
import static org.lwjgl.nanovg.NanoVG.nvgEndFrame;
import static org.lwjgl.nanovg.NanoVG.nvgFillColor;
import static org.lwjgl.nanovg.NanoVG.nvgFontFace;
import static org.lwjgl.nanovg.NanoVG.nvgFontSize;
import static org.lwjgl.nanovg.NanoVG.nvgText;
import static org.lwjgl.nanovg.NanoVG.nvgTextBounds;

import com.idk.shit.Main;

public class TextRenderer {
    protected float screen_height=1000;
    protected float screen_width=650;
    private float[] color; 
    private float max_height;
    private float max_width;
    private long vg;
    private String fontName = "roboto"; // Имя шрифта
    float RATIO=(float)screen_width/(float)screen_height;
    public TextRenderer(long vg ) {
        this.vg=vg;
        loadFont();
    }
    private void loadFont() {
        InputStream fontStream = Main.class.getClassLoader().getResourceAsStream("fonts/Roboto_Condensed-MediumItalic.ttf");

        if (fontStream != null ) {
            try {
                Path tempFile = Files.createTempFile("roboto", ".ttf");
                File fontFile = tempFile.toFile();

                try (FileOutputStream outputStream = new FileOutputStream(fontFile)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fontStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }

                String fontPath = fontFile.getAbsolutePath();
//                String fontPath = "C:\\Users\\maksh\\Desktop\\python\\jkhasfjkadf\\src\\main\\resources\\fonts\\Roboto_Condensed-MediumItalic.ttf";

                int font = NanoVG.nvgCreateFont(this.vg, fontName, fontPath);
                if (font == -1) {
                    System.err.println("Не удалось загрузить шрифт!");
                } 

                fontFile.deleteOnExit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Шрифт не найден в ресурсах!");
        }
    }

    private float  calc( float max_width,float  max_height, String label){
        float fontSize = 60.0f; 
        float[] bounds = new float[4];

        while (true) {
            nvgFontSize(vg, fontSize);
            nvgTextBounds(vg, 0, 0, label, bounds);

            float textWidth = bounds[2] - bounds[0]; // Ширина текста
            float textHeight = bounds[3] - bounds[1]; // Высота текста

            if (textWidth <= max_width && textHeight <= max_height) {
                break; // Текст помещается в заданные размеры
            }

            fontSize -= 1.0f; // Уменьшаем размер шрифта

            if (fontSize <= 0) {
                throw new RuntimeException("Не удалось подобрать размер шрифта для текста: " + label);
            }
        }
        return fontSize;
    }

    public void update_text(String new_text, String label){
        label=new_text;
        calc(this.max_width, this.max_height, label);
    }

    public void drawText(float x, float y, String label,float[] color,  long vg, float max_height,float  max_width ) {
        x = (x+RATIO)/(2*RATIO)*screen_width;
        y =(1-(y+1)/2) *screen_height;
        max_height=max_height/2*screen_height;
        max_width=max_width/2*screen_width;
        nvgBeginFrame(vg, screen_width, screen_height, 1); // Начинаем фрейм
        float fontSize = calc( max_width, max_height, label );
        nvgFontSize(vg, fontSize);
         nvgFontFace(vg, fontName); // Используем загруженный шрифт

        NVGColor color1 = NVGColor.create();
        color1.r(color[0]);  // Красный
        color1.g(color[1]);  // Зеленый
        color1.b(color[2]);  // Синий
        color1.a(color.length > 3 ? color[3] : 1.0f);// прозрачность
        nvgFillColor(vg, color1);

        float[] bounds = new float[4];
        nvgTextBounds(vg, 0, 0, label, bounds);
        float textWidth = bounds[2] - bounds[0]; // Ширина текста
        float textHeight = bounds[3] - bounds[1]; // Высота текста


        nvgText(vg, x-textWidth/2, y+textHeight/3, label);
    
        nvgEndFrame(vg);
    }
}