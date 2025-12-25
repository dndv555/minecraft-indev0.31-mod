package util;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScreenshotManager {
    private static final int COOLDOWN_MS = 200; 
    private static final int MAX_HISTORY_SIZE = 20;
    private static final String SCREENSHOTS_DIR = "screenshots";
    private static boolean screenshotRequested = false;
    private static boolean wasF2Pressed = false;
    private static long lastScreenshotTime = 0;
    private static int screenshotCounter = 0;
    private static List<String> screenshotHistory = new ArrayList<>();
    private static File minecraftFolder = null;
    
    public static void update() {
        boolean isF2Pressed = Keyboard.isKeyDown(Keyboard.KEY_F2);
        long currentTime = System.currentTimeMillis();
        boolean canTakeScreenshot = (currentTime - lastScreenshotTime) > COOLDOWN_MS;
        
        if (isF2Pressed && !wasF2Pressed && canTakeScreenshot) {
            takeScreenshot();
        }
        wasF2Pressed = isF2Pressed;
    }
    public static String takeScreenshot() {
        try {
            File screenshotsDir = getScreenshotsFolder();
            int width = Display.getWidth();
            int height = Display.getHeight();
            ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);
            GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixelIndex = (x + (width * y)) * 4;
                    int r = buffer.get(pixelIndex) & 0xFF;
                    int g = buffer.get(pixelIndex + 1) & 0xFF;
                    int b = buffer.get(pixelIndex + 2) & 0xFF;
                    image.setRGB(x, height - 1 - y, (r << 16) | (g << 8) | b);
                }
            } 
            String filename = generateFilename();
            File outputFile = new File(screenshotsDir, filename);
            int counter = 1;
            String baseName = filename.substring(0, filename.lastIndexOf('.'));
            String extension = filename.substring(filename.lastIndexOf('.'));            
            while (outputFile.exists()) {
                filename = baseName + "_" + counter + extension;
                outputFile = new File(screenshotsDir, filename);
                counter++;
            }
            ImageIO.write(image, "PNG", outputFile);
            String filepath = outputFile.getAbsolutePath();
            screenshotHistory.add(filepath);
            lastScreenshotTime = System.currentTimeMillis();
            
            if (screenshotHistory.size() > MAX_HISTORY_SIZE) {
                screenshotHistory.remove(0);
            }
            
            System.out.println("Saved screenshot: " + filepath);
            return filepath;
            
        } catch (IOException e) {
            System.err.println("Error save screenshot: " + e.getMessage());
            return null;
    }
}
    public static String takeScreenshot(String customName) {
        try {
            File screenshotsDir = getScreenshotsFolder();
            int width = Display.getWidth();
            int height = Display.getHeight();
            ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);         
            GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);         
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);           
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixelIndex = (x + (width * y)) * 4;
                    int r = buffer.get(pixelIndex) & 0xFF;
                    int g = buffer.get(pixelIndex + 1) & 0xFF;
                    int b = buffer.get(pixelIndex + 2) & 0xFF;
                    image.setRGB(x, height - 1 - y, (r << 16) | (g << 8) | b);
                }
            }
            String filename = customName;
            if (!filename.toLowerCase().endsWith(".png")) {
                filename += ".png";
            }           
            File outputFile = new File(screenshotsDir, filename);
            int copyNumber = 1;
            while (outputFile.exists()) {
                String baseName = customName;
                if (baseName.toLowerCase().endsWith(".png")) {
                    baseName = baseName.substring(0, baseName.length() - 4);
                }
                filename = baseName + "_" + copyNumber + ".png";
                outputFile = new File(screenshotsDir, filename);
                copyNumber++;
            }
            ImageIO.write(image, "PNG", outputFile);
            String filepath = outputFile.getAbsolutePath();
            screenshotHistory.add(filepath);
            lastScreenshotTime = System.currentTimeMillis();
            
            if (screenshotHistory.size() > MAX_HISTORY_SIZE) {
                screenshotHistory.remove(0);
            }
            
            System.out.println("Saved screenshot: " + outputFile.getName());
            return filepath;
            
        } catch (IOException e) {
            System.err.println("Error save screenshot:  " + e.getMessage());
            return null;
        }
    }
    private static String generateFilename() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
        String timestamp = dateFormat.format(new Date());
        screenshotCounter++;
        if (screenshotCounter > 1) {
            return String.format("screenshot_%s_%d.png", timestamp, screenshotCounter);
        } else {
            return String.format("screenshot_%s.png", timestamp);
        }
    }
    private static void initMinecraftFolder() {
        if (minecraftFolder != null) {
            return;
        }      
        String os = System.getProperty("os.name").toLowerCase();
        String userHome = System.getProperty("user.home");
        
        if (os.contains("win")) {
            String appData = System.getenv("APPDATA");
            if (appData != null) {
                minecraftFolder = new File(appData, ".minecraft");
            } else {
                minecraftFolder = new File(userHome, "AppData/Roaming/.minecraft");
            }
        } else if (os.contains("mac")) {
            minecraftFolder = new File(userHome, "Library/Application Support/minecraft");
        } else {
            minecraftFolder = new File(userHome, ".minecraft");
        }
    }
    private static File getScreenshotsFolder() {
        initMinecraftFolder();
        File screenshotsFolder = new File(minecraftFolder, SCREENSHOTS_DIR);
        if (!screenshotsFolder.exists()) {
            if (!screenshotsFolder.mkdirs()) {
                System.err.println("Error create screenshots folder");
                screenshotsFolder = new File(SCREENSHOTS_DIR);
                if (!screenshotsFolder.exists()) {
                    screenshotsFolder.mkdirs();
                }
            }
        }
        
        return screenshotsFolder;
    }
    
    public static String getScreenshotsPath() {
        return getScreenshotsFolder().getAbsolutePath();
    }
    
    public static String getMinecraftPath() {
        initMinecraftFolder();
        return minecraftFolder.getAbsolutePath();
    }
 
    public static List<File> listScreenshots() {
        File screenshotsDir = getScreenshotsFolder();
        List<File> screenshots = new ArrayList<>();
        
        if (screenshotsDir.exists() && screenshotsDir.isDirectory()) {
            File[] files = screenshotsDir.listFiles((dir, name) -> 
                name.toLowerCase().endsWith(".png")
            );           
            if (files != null) {
                for (int i = files.length - 1; i >= 0; i--) {
                    screenshots.add(files[i]);
                }
            }
        }
        
        return screenshots;
    }
 
    public static File getLatestScreenshot() {
        List<File> screenshots = listScreenshots();
        return screenshots.isEmpty() ? null : screenshots.get(0);
    }

    public static List<String> getScreenshotHistory() {
        return new ArrayList<>(screenshotHistory);
    }

    public static void clearHistory() {
        screenshotHistory.clear();
    }
    
    public static void resetCounter() {
        screenshotCounter = 0;
    }

    public static int getScreenshotCount() {
        return screenshotCounter;
    }
    
    public static long getLastScreenshotTime() {
        return lastScreenshotTime;
    }
    
    public static boolean isFolderWritable() {
        File folder = getScreenshotsFolder();
        return folder.canWrite();
    }
}