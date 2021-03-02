package util;

import java.io.*;
import java.lang.reflect.Field;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Cef natives file extract helper
 */
public final class CefNativeLibFileExtractor {

    private static String[] localeFiles = new String[]{"am.pak", "ar.pak", "bg.pak", "bn.pak", "ca.pak", "cs.pak",
            "da.pak", "de.pak", "el.pak", "en-GB.pak", "en-US.pak", "es-419.pak", "es.pak", "et.pak", "fa.pak", "fi.pak",
            "fil.pak", "fr.pak", "gu.pak", "he.pak", "hi.pak", "hr.pak", "hu.pak", "id.pak", "it.pak", "ja.pak",
            "kn.pak", "ko.pak", "lt.pak", "lv.pak", "ml.pak", "mr.pak", "ms.pak", "nb.pak", "nl.pak", "pl.pak",
            "pt-BR.pak", "pt-PT.pak", "ro.pak", "ru.pak", "sk.pak", "sl.pak", "sr.pak", "sv.pak", "sw.pak", "ta.pak",
            "te.pak", "th.pak", "tr.pak", "uk.pak", "vi.pak", "zh-CN.pak", "zh-TW.pak"};

    private static String[] cefFiles = new String[]{
            "cef.pak",
            "cef_100_percent.pak",
            "cef_200_percent.pak",
            "cef_extensions.pak",
            "chrome_elf.dll",
            "d3dcompiler_43.dll",
            "d3dcompiler_47.dll",
            "devtools_resources.pak",
            "icudtl.dat",
            "jcef.dll",
            "jcef.exp",
            "jcef.lib",
            "jcef_helper.exe",
            "libcef.dll",
            "libEGL.dll",
            "libGLESv2.dll",
            "natives_blob.bin",
            "snapshot_blob.bin",
            "v8_context_snapshot.bin",
    };

    /**
     * exract native and resource files to temp dir and set to
     * "java.library.path"
     *
     * @throws Exception
     */
    public static void extractNativeAndResource() throws Exception {
        // create temp dir first
  
        File tempDir = new File(System.getProperty("user.dir") + "\\dll");
        if (!tempDir.exists() && !tempDir.mkdir()) return;

        // localesDir
        File localesDir = new File(tempDir.getPath() + File.separator + "locales");
        if (!localesDir.exists() && !localesDir.mkdirs()) return;

        // extract cef files
        for (String cefFileName : cefFiles) {
            File cefFile = new File(tempDir.getPath() + File.separator + cefFileName);
            if (cefFile.exists()) {
                try (FileInputStream ofis = new FileInputStream(cefFile);
                     InputStream nfis = CefNativeLibFileExtractor.class.getResourceAsStream("/" + cefFileName)) {
                    if (equalMD5(ofis, nfis))
                        continue;
                }
                cefFile.delete();
                cefFile.createNewFile();
            } else {
                cefFile.createNewFile();
            }
            try (InputStream is = CefNativeLibFileExtractor.class.getResourceAsStream("/" + cefFileName); FileOutputStream fos = new FileOutputStream(cefFile)) {

                byte[] buf = new byte[4096];
                int n;
                while ((n = is.read(buf)) > 0) {
                    fos.write(buf, 0, n);
                }
            }
        }

        // extrac locale files
        for (String localFileName : localeFiles) {
            File localeFile = new File(localesDir.getPath() + File.separator + localFileName);
            if (localeFile.exists()) {
                try (FileInputStream ofis = new FileInputStream(localeFile);
                     InputStream nfis = CefNativeLibFileExtractor.class.getResourceAsStream("/locales/" + localFileName)) {
                    if (equalMD5(ofis, nfis))
                        continue;
                }
                localeFile.delete();
                localeFile.createNewFile();
            } else {
                localeFile.createNewFile();
            }
            try (InputStream is = CefNativeLibFileExtractor.class.getResourceAsStream("/locales/" + localFileName);
                 FileOutputStream fos = new FileOutputStream(localeFile)) {
                int byteCount = 0;

                byte[] bytes = new byte[1024];

                while ((byteCount = is.read(bytes)) != -1) {
                    fos.write(bytes, 0, byteCount);
                }
            }
        }

        //addLibraryDir(tempDir.getPath());
        addLibraryDir(System.getProperty("user.dir") + "\\dll");
        
        
        
    }

    private static void addLibraryDir(String libraryPath) throws Exception {
        Field userPathsField = ClassLoader.class.getDeclaredField("usr_paths");
        userPathsField.setAccessible(true);
        String[] paths = (String[]) userPathsField.get(null);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < paths.length; i++) {
            if (libraryPath.equals(paths[i])) {
                continue;
            }
            sb.append(paths[i]).append(File.pathSeparatorChar);
        }
        sb.append(libraryPath);
        System.setProperty("java.library.path", sb.toString());
        final Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
        sysPathsField.setAccessible(true);
        sysPathsField.set(null, null);
    }

    public static String byteArrayToHex(byte[] byteArray) {
        StringBuilder hex = new StringBuilder();
        for (int n = 0; n < byteArray.length; n++) {
            String stmp = (Integer.toHexString(byteArray[n] & 0XFF));
            if (stmp.length() == 1)
                hex.append('0');
            hex.append(stmp);
        }
        return hex.toString();
    }

    public static String streamMD5(InputStream is) throws IOException {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            try (DigestInputStream digestInputStream = new DigestInputStream(is, messageDigest)) {
                byte[] buffer = new byte[4 * 1024];
                while (digestInputStream.read(buffer) > 0) ;
                messageDigest = digestInputStream.getMessageDigest();
                byte[] resultByteArray = messageDigest.digest();
                return byteArrayToHex(resultByteArray);
            }
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static boolean equalMD5(InputStream is0, InputStream is1) {
        try {
            String md50 = streamMD5(is0);
            String md51 = streamMD5(is1);
            if (md50 == null || md51 == null) return false;
            return md50.equals(md51);
        } catch (Exception e) {
            return false;
        }
    }
}