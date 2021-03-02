/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
 * @author inet
 */
public class MyFileUtils {
    public static long folderSize(File directory) {
        long length = 0;
        try {
            for (File file : directory.listFiles()) {
                if (file.isFile()) {
                    length += file.length();
                } else {
                    length += folderSize(file);
                }
            }
        } catch (Exception e) {
        }
        return length;
    }
    public static boolean deleteFolder(File file) {
        try {
            File[] contents = file.listFiles();
            if (contents != null) {
                for (File f : contents) {
                    deleteFolder(f);
                }
            }
            file.delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getFileExtension(String file) {
        try {
            return file.substring(file.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }

    public static String getFileName(String file) {
        try {
            return file.substring(file.lastIndexOf("\\") + 1, file.lastIndexOf("."));
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean checkDDrive() {
        try {
            File checkDFile = new File("D:\\");
            if (checkDFile.exists()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static List<File> sortByName(File[] files) {
        List<File> results = new ArrayList<>();
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                int n1 = extractNumber(o1.getName());
                int n2 = extractNumber(o2.getName());
                return n1 - n2;
            }

            private int extractNumber(String name) {
                int i = 0;
                try {
                    int e = name.lastIndexOf('.');
                    String number = name.substring(0, e);
                    i = Integer.parseInt(number);
                } catch (Exception e) {
                    i = 0; // if filename does not match the format
                    // then default to 0
                }
                return i;
            }
        });

        for (File f : files) {
            results.add(f);
        }
        return results;
    }

    public static boolean checkFreeDrive(String drive) {
        try {
            File file = new File(drive + ":");
            if (file.getUsableSpace() / 1024 / 1024 / 1024 < 5) {
                return false;
            }
        } catch (Exception e) {
        }
        return true;
    }

    public static boolean sortFilesByDate(File[] files) {
        try {
            Arrays.sort(files, new Comparator() {
                public int compare(Object o1, Object o2) {
                    if (((File) o1).lastModified() < ((File) o2).lastModified()) {
                        return -1;
                    } else if (((File) o1).lastModified() > ((File) o2).lastModified()) {
                        return +1;
                    } else {
                        return 0;
                    }
                }
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public static void copy(File sourceLocation, File targetLocation) throws IOException {
        //&&!sourceLocation.getName().contains("firefox") khong bit de lam gi
        if (sourceLocation.isDirectory() && !sourceLocation.getName().contains("cache2")) {
            copyDirectory(sourceLocation, targetLocation);
        } else {
            copyFile(sourceLocation, targetLocation);
        }
    }


    public static void copyDirectory(File source, File target) {
        try {
            if (!target.exists()) {
                //System.out.println("tạo "+target.getAbsolutePath());
                target.mkdir();
            }
            for (String f : source.list()) {
                copy(new File(source, f), new File(target, f));
            }
        } catch (Exception e) {
        }
    }



    public static boolean copyFile(File source, File target) {
        try (
                InputStream in = new FileInputStream(source);
                OutputStream out = new FileOutputStream(target)) {
            byte[] buf = new byte[1024];
            int length;
            while ((length = in.read(buf)) > 0) {
                out.write(buf, 0, length);
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

   
    public static boolean deleteFile(String fileurl) {
        try {
            File file = new File(fileurl);
            if (!file.exists()) {
                return true;
            }
            if (file.delete()) {
                return true;
                //System.out.println(file.getName() + " is deleted!");
            } else {
                return false;
                //System.out.println("Delete operation is failed.");
            }
        } catch (Exception e) {
            System.err.println("delete fail :" + e.getMessage());
            return false;
        }
    }

    public static int countLines(File input) throws IOException {
        try (InputStream is = new FileInputStream(input)) {
            int count = 1;
            for (int aChar = 0; aChar != -1; aChar = is.read()) {
                count += aChar == '\n' ? 1 : 0;
            }
            return count;
        } catch (Exception ex) {
            return 0;
        }
    }

    public static int getStringPixelWidth(String str, int fontSize) {
        BufferedImage image = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        try {
            FontMetrics metrics = g.getFontMetrics(new Font("Arial Unicode MS", Font.TRUETYPE_FONT, fontSize));
            return metrics.stringWidth(str);
        } catch (Exception e) {
        } finally {
            g.dispose();
        }
        return 0;
    }


    public static boolean writeStringToFileUTF8(String content, String path) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(path), "UTF-8"));
            writer.write(content);
            writer.newLine();
            writer.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean writeStringToFileUTF8OnlyLine(String content, String path) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path, true));
            writer.write(content);
            writer.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean writeStringToFile(String content, String path) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path, true));
            writer.write(content);
            writer.newLine();
            writer.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static ObservableList<String> getListFileNameFromFolder(String path) {
        ObservableList<String> optionLanguage = FXCollections.observableArrayList();
        File folder = new File(path);
        for (File file : folder.listFiles()) {
            optionLanguage.add(file.getName());
        }
        return optionLanguage;
    }

    public static boolean checkCreateAdmin(String path) {
        try {
            File file = new File("c:\\newfile.txt");
            if (file.createNewFile()) {
                System.out.println("File is created!");
            } else {
                System.out.println("File already exists.");
            }
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    public static boolean createFile(String path) {
        try {
            File file = new File(path);
            if (file.createNewFile()) {
                System.out.println("File is created!");
            } else {
                System.out.println("File already exists.");
            }
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    public static void createFolder(String uri) {
        File theDir = new File(uri);
        // if the directory does not exist, create it
        if (!theDir.exists()) {
            boolean result = false;
            try {
                theDir.mkdir();
                result = true;
            } catch (SecurityException se) {
                se.printStackTrace();
            }
            if (result) {
                //System.out.println("DIR created");  
            }
        }
    }

    public static String getStringFromFile(File filePath) {
        String result = "";
        try {
            //BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "Cp1252"));      
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                result = result + line.trim();
            }
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getStringFromFile(File filePath, String spaceChar) {
        String result = "";
        try {
            //BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "Cp1252"));      
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                result = result + line.trim() + spaceChar;
            }
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getStringFromFile(String filePath, String spaceChar) {
        String result = "";
        try {
            //BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "Cp1252"));      
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                result = result + line + spaceChar;
            }
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<String> getListStringFromFile(String filePath) {
        ArrayList<String> result = new ArrayList<>();
        try {
            //BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "Cp1252"));      
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                result.add(line);
            }
            br.close();

        } catch (IOException e) {
            //e.printStackTrace();
        }
        return result;
    }


    public static boolean copyFileUsingStream(File source, File dest) {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(MyFileUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(MyFileUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
