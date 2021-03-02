/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.File;
import java.io.FileReader;
import java.util.Random;
import org.cef.CefSettings;
import org.cef.OS;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * @author PC
 */
public class ToolSetting {

    public static ToolSetting instance;
    public String fakeingFile;
    public String lastUrl;
    
    public int socketPort = 0;
    public String localIp = "192.168.1.9";
    public String proxyHost = "";
    public int proxyPort;
    public int x;
    public int y;
    public int w = 1200;
    public int h = 800;
    public JSONArray plugins;
    public int canvas_r;
    public int canvas_g;
    public int canvas_b;
    public String account;
    public String osCpu;
    public String platform = "Win32";
    public boolean fake = true;
    public String simplePath = OS.isLinux() ? "/" : "\\";

    public String getLastUrl() {
        return lastUrl;
    }

    public void setLastUrl(String lastUrl) {
        this.lastUrl = lastUrl;
    }
    
    public Boolean willFake(String Url){
        try {
            if(Url.equals(lastUrl)){
               return false;
            }
            lastUrl=Url;
        } catch (Exception e) {
        }
        return true;
    }
    
    
    public String profile = "";
    public String userAgent = "";
    public String appVersion = "";

    public String appName = "Netscape";
    public String product = "Gecko";
    public String vendorSub = "Gecko";
    public int availTop = 0;
    public int colorDepth = 24;
    public int pixelDepth = 24;
    public int availWidth = 1364;
    public int outerWidth = 1364;
    public int deviceMemory = 8;
    public int maxTouchPoints = 0;
    public int availLeft = 0;
    public int hardwareConcurrency = 16;
    public int height = 768;
    public int outerHeight = 728;
    public int availHeight = 728;
    public int width = 1364;
    public String appCodeName = "Mozilla";
    public double devicePixelRatio = 1;
    public String productSub = "20030107";
    public String vendor = "Google Inc.";
    public JSONArray mimes;
    public JSONArray font_offset;
    public int arrFonts[];
    public float canvasNoiseR;
    public float canvasNoiseG;
    public float canvasNoiseB;
    public float canvasNoiseA;
    public float audioContext_1;
    public float audioContext_2;
    public float audioContext_3;
    public float audioContext_4;
    private CefSettings cefSettings;
    public int openGL_maxVertexTextureImageUnits;
    public String openGL_renderer;
    public int openGL_greenBits;
    public int openGL_maxAnisotropy;
    public String openGL_shadingLanguage;
    public int openGL_maxVertexUniformVectors;
    public int openGL_stencilBits;
    public int openGL_blueBits;
    public int openGL_maxTextureSize;
    public int openGL_maxCubeMapTextureSize;
    public int openGL_maxVaryingVectors;
    public int openGL_maxRenderBufferSize;
    public String openGL_version;
    public int openGL_alphaBits;
    public String openGL_vendor;
    public int openGL_maxCombinedTextureImageUnits;
    public String openGL_unmaskedVendor;
    public int openGL_maxFragmentUniformVectors;
    public int openGL_maxVertexAttribs;
    public String openGL_unmaskedRenderer;
    public int openGL_maxTextureImageUnits;
    public JSONArray openGL_exts;
    public int openGL_depthBits;
    public int openGL_redBits;
    public int webgl_else;
    public float webglNoise;
    public float webgl_3386;

    public CefSettings getCefSettings() {
        return cefSettings;
    }

    public void setCefSettings(CefSettings cefSettings) {
        this.cefSettings = cefSettings;
    }

    public String getFakeingFile() {
        return fakeingFile;
    }

    public void setFakeingFile(String fakeingFile) {
        this.fakeingFile = fakeingFile;
    }

    public static ToolSetting getInstance() {
        if (instance == null) {
            instance = new ToolSetting();
        }
        return instance;
    }

    public ToolSetting() {
        boolean devMode = true;
        if (devMode) {
            userAgent = "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36";
            appVersion = "5.0 (Windows NT 10.0; Win64; x64; rv:77.0) Gecko/20100101 Firefox/77.0";
            profile = "C:\\PROFILES\\winartbestbeast";
        }
        cefSettings = new CefSettings();
        cefSettings.windowless_rendering_enabled = false;
        //cefSettings.resources_dir_path = System.getProperty("user.dir") + "\\dll";
        cefSettings.locales_dir_path = System.getProperty("user.dir") + "\\dll\\locales";
        cefSettings.background_color = cefSettings.new ColorType(100, 255, 242, 211);

    }

    public void initRandomFake() {
        String jsonOject = "{"
                + "\"localIP\":\"192.168.1.9\","
                + "\"canvasNoiseR\":" + (Math.ceil(new Random().nextFloat() * 10) / 10) + ","
                + "\"canvasNoiseG\":" + (Math.ceil(new Random().nextFloat() * 10) / 10) + ","
                + "\"canvasNoiseB\":" + (Math.ceil(new Random().nextFloat() * 10) / 10) + ","
                + "\"canvasNoiseA\":" + (Math.ceil(new Random().nextFloat() * 10) / 10) + ","
                + "\"webglNoise\":" + (Math.ceil(new Random().nextFloat() * 10) / 10) + ","
                + "\"webgl_35661\":\"[128, 192, 256]\","
                + "\"webgl_else\":" + (new Random().nextInt(4) + 1) + ","
                + "\"webgl_36349\":" + ((new Random().nextInt(2) + 1) * 4096) + ","
                + "\"webgl_34930\":" + ((new Random().nextInt(3) + 1) * 16) + ","
                + "\"webgl_34076\":" + ((new Random().nextInt(2) + 1) * 16384) + ","
                + "\"webgl_3413\":" + (new Random().nextInt(4) + 1) + ","
                + "\"webgl_3386\":" + ((new Random().nextInt(3) + 1) * 8192) + ","
                + "\"font_offset\":[" + ((new Random().nextInt(4)) - 2) + "," + ((new Random().nextInt(4)) - 2) + "," + ((new Random().nextInt(4)) - 2) + "," + ((new Random().nextInt(4)) - 2) + "," + ((new Random().nextInt(4)) - 2) + "," + ((new Random().nextInt(4)) - 2) + "," + ((new Random().nextInt(4)) - 2) + "," + ((new Random().nextInt(4)) - 2) + "],"
                + "\"audioContext_1\":" + (Math.ceil(new Random().nextFloat() * 10000) / 10000) + ","
                + "\"audioContext_2\":" + (Math.ceil(new Random().nextFloat() * 10000) / 10000) + ","
                + "\"audioContext_3\":" + (Math.ceil(new Random().nextFloat() * 10000) / 10000) + ","
                + "\"audioContext_4\":" + (Math.ceil(new Random().nextFloat() * 10000) / 10000) + "}";
        if (!new File(profile + simplePath + "settingF.json").exists()) {
            MyFileUtils.createFile(profile + simplePath + "settingF.json");
            MyFileUtils.writeStringToFile(jsonOject, profile + simplePath + "settingF.json");
        }
        try {
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(new FileReader(profile + simplePath + "settingF.json"));
            if (obj != null) {
                canvasNoiseR = Float.parseFloat(obj.get("canvasNoiseR").toString());
                canvasNoiseG = Float.parseFloat(obj.get("canvasNoiseG").toString());
                canvasNoiseB = Float.parseFloat(obj.get("canvasNoiseB").toString());
                canvasNoiseA = Float.parseFloat(obj.get("canvasNoiseA").toString());
                audioContext_1 = Float.parseFloat(obj.get("audioContext_1").toString());
                audioContext_2 = Float.parseFloat(obj.get("audioContext_2").toString());
                audioContext_3 = Float.parseFloat(obj.get("audioContext_3").toString());
                audioContext_4 = Float.parseFloat(obj.get("audioContext_4").toString());
                webgl_3386 = Float.parseFloat(obj.get("webgl_3386").toString());
                webgl_else = Integer.parseInt(obj.get("webgl_else").toString());
                font_offset = (JSONArray) obj.get("font_offset");
                webglNoise = Float.parseFloat(obj.get("webglNoise").toString());
            }
        } catch (Exception e) {
        }

    }

    public void init() {
        initRandomFake();
        fakeingFile = profile + simplePath + "setting.json";
        cefSettings.cache_path = profile;
        cefSettings.locale = profile;
        cefSettings.user_agent = userAgent;
        System.out.println("USERAGENT " + userAgent);
        //
        //cefSettings.user_agent = "Mozilla/5.0 (iPad; CPU OS 11_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/11.0 Tablet/15E148 Safari/604.1";
        //cefSettings.remote_debugging_port=12345;
        cefSettings.persist_session_cookies = true;
        JSONParser parser = new JSONParser();

        if (new File(fakeingFile).exists()) {
            FileReader reader=null;
            try {
                reader = new FileReader(fakeingFile);
                JSONObject obj = (JSONObject) parser.parse(reader);
                if (obj != null) {
                    plugins = (JSONArray) obj.get("plugins");
                    mimes = (JSONArray) obj.get("mimes");
                    if (userAgent.length() == 0) {
                        System.out.println("cefSettings.user_agent " + userAgent);
                    }
                    appVersion = userAgent.replace("Mozilla/", "");
                    bindAttr((JSONObject) obj.get("attr"));
                    bindOpenGL((JSONObject) obj.get("webgl_properties"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally{
                try {
                    reader.close();
                } catch (Exception e) {
                }
            }
        }

    }

    public void bindOpenGL(JSONObject attr) {
        openGL_maxVertexTextureImageUnits = Integer.parseInt(attr.get("maxVertexTextureImageUnits").toString());
        openGL_renderer = attr.get("renderer").toString();
        openGL_greenBits = Integer.parseInt(attr.get("greenBits").toString());
        openGL_maxAnisotropy = Integer.parseInt(attr.get("maxAnisotropy").toString().length() == 0 ? "8" : attr.get("maxAnisotropy").toString());
        openGL_shadingLanguage = attr.get("shadingLanguage").toString();
        openGL_maxVertexUniformVectors = Integer.parseInt(attr.get("maxVertexUniformVectors").toString());
        openGL_stencilBits = Integer.parseInt(attr.get("stencilBits").toString());
        openGL_blueBits = Integer.parseInt(attr.get("blueBits").toString());
        openGL_maxTextureSize = Integer.parseInt(attr.get("maxTextureSize").toString());
        openGL_maxCubeMapTextureSize = Integer.parseInt(attr.get("maxCubeMapTextureSize").toString());
        openGL_maxVaryingVectors = Integer.parseInt(attr.get("maxVaryingVectors").toString());
        openGL_maxRenderBufferSize = Integer.parseInt(attr.get("maxRenderBufferSize").toString());
        openGL_version = attr.get("version").toString();
        openGL_alphaBits = Integer.parseInt(attr.get("alphaBits").toString());
        openGL_vendor = attr.get("vendor").toString();
        openGL_maxCombinedTextureImageUnits = Integer.parseInt(attr.get("maxCombinedTextureImageUnits").toString());
        openGL_unmaskedVendor = attr.get("unmaskedVendor").toString();
        openGL_maxFragmentUniformVectors = Integer.parseInt(attr.get("maxFragmentUniformVectors").toString());
        openGL_maxVertexAttribs = Integer.parseInt(attr.get("maxVertexAttribs").toString());
        openGL_unmaskedRenderer = attr.get("unmaskedRenderer").toString();
        openGL_maxTextureImageUnits = Integer.parseInt(attr.get("maxTextureImageUnits").toString());
        openGL_depthBits = Integer.parseInt(attr.get("depthBits").toString());
        openGL_redBits = Integer.parseInt(attr.get("redBits").toString());
        openGL_exts = StringUtil.getJSONArrayStringBySplit(attr.get("extensions").toString(), ",");
    }

    public void bindAttr(JSONObject attr) {
        //appVersion = attr.get("navigator.appVersion").toString();
        appName = attr.get("navigator.appName").toString();
        product = attr.get("navigator.product").toString();
        vendorSub = attr.get("navigator.vendorSub").toString();
        availTop = Integer.parseInt(attr.get("screen.availTop").toString());
        platform = attr.get("navigator.platform").toString();
        colorDepth = Integer.parseInt(attr.get("screen.colorDepth").toString());
        pixelDepth = Integer.parseInt(attr.get("screen.pixelDepth").toString());
        availWidth = Integer.parseInt(attr.get("screen.availWidth").toString());
        outerWidth = Integer.parseInt(attr.get("outerWidth").toString());
        //deviceMemory = Integer.parseInt(attr.get("deviceMemory").toString());
        deviceMemory = 8;
        //userAgent = attr.get("navigator.userAgent").toString();
        maxTouchPoints = Integer.parseInt(attr.get("maxTouchPoints").toString());
        availLeft = Integer.parseInt(attr.get("screen.availLeft").toString());
        hardwareConcurrency = Integer.parseInt(attr.get("hardwareConcurrency").toString());
        height = Integer.parseInt(attr.get("screen.height").toString());
        outerHeight = Integer.parseInt(attr.get("outerHeight").toString());
        availHeight = Integer.parseInt(attr.get("screen.availHeight").toString());
        width = Integer.parseInt(attr.get("screen.width").toString());
        appCodeName = attr.get("navigator.appCodeName").toString();
        devicePixelRatio = Double.parseDouble(attr.get("window.devicePixelRatio").toString());
        productSub = attr.get("navigator.productSub").toString();
        vendor = attr.get("navigator.vendor").toString();
    }

}
