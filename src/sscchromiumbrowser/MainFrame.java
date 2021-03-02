/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sscchromiumbrowser;

import handler.MessageRouterHandler;
import java.awt.BorderLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.lang.reflect.Field;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.cef.CefApp;
import org.cef.CefApp.CefVersion;
import org.cef.CefClient;
import org.cef.OS;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefMessageRouter;
import org.cef.browser.CefRequestContext;
import org.cef.handler.CefDisplayHandlerAdapter;
import org.cef.handler.CefLoadHandlerAdapter;
import org.cef.handler.CefRequestContextHandlerAdapter;
import org.cef.network.CefCookieManager;
import ssc.facking.Facking;
import ssc.facking.MyRequestHandler;
import tests.detailed.dialog.DownloadDialog;
import tests.detailed.handler.AppHandler;
import tests.detailed.handler.ContextMenuHandler;
import tests.detailed.handler.DragHandler;
import tests.detailed.handler.GeolocationHandler;
import tests.detailed.handler.JSDialogHandler;
import tests.detailed.handler.KeyboardHandler;
import tests.detailed.ui.ControlPanel;
import tests.detailed.ui.MenuBar;
import tests.detailed.ui.StatusPanel;
import util.SystemBootstrap;
import util.TaskReadThread;
import util.ToolSetting;

/**
 *
 * @author simplesolution.co
 */
public class MainFrame extends JFrame {

    private static final long serialVersionUID = -2295538706810864538L;

    public static void main(String[] args) {

        System.setProperty("java.library.path", System.getProperty("user.dir")
                + File.separator + "dll" + ";"
                + System.getProperty("user.dir") + File.separator + "dll" + File.separator + "cef.pak");
        try {
            Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
            fieldSysPath.setAccessible(true);
            fieldSysPath.set(null, null);
            if (OS.isWindows()) {
                SystemBootstrap.loadLibrary("jawt");
                SystemBootstrap.loadLibrary("chrome_elf");
                SystemBootstrap.loadLibrary("libcef");
                SystemBootstrap.loadLibrary("jcef");
            } else if (OS.isLinux()) {
                SystemBootstrap.loadLibrary("cef");
            }
        } catch (Exception e) {
        }

        boolean osrEnabledArg = OS.isLinux();
        boolean transparentPaintingEnabledArg = false;
        String cookiePath = null;
        /*
        for (String arg : args) {
            arg = arg.toLowerCase();
            if (!OS.isLinux() && arg.equals("--off-screen-rendering-enabled")) {
                osrEnabledArg = true;
            } else if (arg.equals("--transparent-painting-enabled")) {
                transparentPaintingEnabledArg = true;
            } else if (arg.startsWith("--cookie-path=")) {
                cookiePath = arg.substring("--cookie-path=".length());
                File testPath = new File(cookiePath);
                if (!testPath.isDirectory() || !testPath.canWrite()) {
                    System.out.println("Can't use " + cookiePath
                            + " as cookie directory. Check if it exists and if it is writable");
                    cookiePath = null;
                } else {
                    System.out.println("Storing cookies in " + cookiePath);
                }
            }
        }
         */
        List<String> arryAry = new ArrayList<>();

        arryAry.addAll(Arrays.asList(args));
        for (String string : arryAry) {
            if (string.startsWith("port:")) {
                ToolSetting.getInstance().socketPort = Integer.parseInt(string.replaceAll("port:", ""));
            } else if (string.startsWith("profile:")) {
                ToolSetting.getInstance().profile = string.replaceAll("profile:", "");
            } else if (string.startsWith("proxy:")) {
                ToolSetting.getInstance().proxyHost = string.replaceAll("proxy:", "").split(":")[0];
                ToolSetting.getInstance().proxyPort = Integer.parseInt(string.replaceAll("proxy:", "").split(":")[0]);
            } else if (string.startsWith("useragent:")) {
                ToolSetting.getInstance().userAgent = string.replaceAll("useragent:", "");
            } else if (string.startsWith("account:")) {
                ToolSetting.getInstance().account = string.replaceAll("account:", "");
            } else if (string.startsWith("w:")) {
                ToolSetting.getInstance().w = Integer.parseInt(string.replaceAll("w:", ""));
            } else if (string.startsWith("h:")) {
                ToolSetting.getInstance().h = Integer.parseInt(string.replaceAll("h:", ""));
            } else if (string.startsWith("fake:")) {
                ToolSetting.getInstance().fake = Boolean.parseBoolean(string.replaceAll("fake:", ""));
            }
        }
        ToolSetting.getInstance().init();

        //cookiePath = ToolSetting.getInstance().profile;
        final MainFrame frame
                = new MainFrame(osrEnabledArg, transparentPaintingEnabledArg, cookiePath, args);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                CefApp.getInstance().dispose();
                frame.dispose();
            }
        });
        frame.setSize(ToolSetting.getInstance().w, ToolSetting.getInstance().h);
        frame.setVisible(true);
    }

    private final CefClient client_;
    private String errorMsg_ = "";
    private final CefBrowser browser_;
    private ControlPanel control_pane_;
    private StatusPanel status_panel_;
    private final CefCookieManager cookieManager_;
    private boolean loading;

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }
    Label messageLabel;

    public MainFrame(boolean osrEnabled, boolean transparentPaintingEnabled, String cookiePath,
            String[] args) {
        // 1) CefApp is the entry point for JCEF. You can pass
        //    application arguments to it, if you want to handle any
        //    chromium or CEF related switches/attributes in
        //    the native world.

        /*
        options.add_argument('--ignore-ssl-errors=yes')
        options.add_argument('--ignore-certificate-errors')
        args[0]="--disable-blink-features=AutomationControlled";
        args[1]="--disable-extensions";
        args[2]="--incognito";
        args[3]="--disable-plugins-discovery";
        args[4]="--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.150 Safari/537.36";
        args[5]="--disable-blink-features=AutomationControlled";
        args[6]="--disable-blink-features=AutomationControlled";
        
                        /**
                  ".\Worker\Worker.exe" 
                  * 
                  *  --proxytunneling --skipframes --unique-process-id=1amfxmkz --profile --extensions --no-sandbox --lang=en-US --log-file="C:\Users\PC\AppData\Roaming\BrowserAutomationStudio\apps\23.2.2\Worker\debug.log" --log-severity=disable --remote-debugging-port=11860 --disable-features=MimeHandlerViewInCrossProcessFrame --high-dpi-support --disable-site-isolation-trials --disable-gpu --disable-gpu-compositing --disable-gpu-shader-disk-cache --ignore-certificate-errors --enable-widevine-cdm --enable-blink-features=WebBluetooth,Badging,InstalledApp,WakeLock,Notifications,WebAnimationsAPI,AOMPhase1 en 0 1 1 prof/S6PzeRAd  oobwwihbrm none 12896 record
         */
        args = new String[19];
        args[0] = "--useflash";
        args[1] = "--skipframes";
        args[2] = "--unique-process-id=1amfxmkz";
        args[3] = "--profile";
        args[4] = "--extensions";
        args[5] = "--no-sandbox";
        args[6] = "--lang=en-US ";
        args[7] = "--log-severity=disable";
        args[8] = "--disable-features=MimeHandlerViewInCrossProcessFrame";
        args[9] = "--high-dpi-support";
        args[10] = "--disable-site-isolation-trials";
        args[11] = "--disable-gpu";
        args[12] = "--disable-gpu-compositing";
        args[13] = "--disable-gpu-shader-disk-cache";
        args[14] = "--ignore-certificate-errors";
        args[15] = "--enable-widevine-cdm";
        args[16] = "--enable-blink-features=WebBluetooth,Badging,InstalledApp,WakeLock,Notifications,WebAnimationsAPI,AOMPhase1 en 0 1 1 prof/S6PzeRAd";
        args[17] = "--disable-web-security";
        args[18] = "--allow-running-insecure-content";

        CefApp myApp = CefApp.getInstance(args, ToolSetting.getInstance().getCefSettings());
        CefVersion version = myApp.getVersion();
        System.out.println("Using:\n" + version);

        //    We're registering our own AppHandler because we want to
        //    add an own schemes (search:// and client://) and its corresponding
        //    protocol handlers. So if you enter "search:something on the web", your
        //    search request "something on the web" is forwarded to www.google.com
        CefApp.addAppHandler(new AppHandler(args));

        //    By calling the method createClient() the native part
        //    of JCEF/CEF will be initialized and an  instance of
        //    CefClient will be created. You can create one to many
        //    instances of CefClient.
        client_ = myApp.createClient();

        // 2) You have the ability to pass different handlers to your
        //    instance of CefClient. Each handler is responsible to
        //    deal with different informations (e.g. keyboard input).
        //
        //    For each handler (with more than one method) adapter
        //    classes exists. So you don't need to override methods
        //    you're not interested in.
        DownloadDialog downloadDialog = new DownloadDialog(this);
        client_.addContextMenuHandler(new ContextMenuHandler(this));
        client_.addDownloadHandler(downloadDialog);
        client_.addDragHandler(new DragHandler());
        client_.addGeolocationHandler(new GeolocationHandler(this));
        client_.addJSDialogHandler(new JSDialogHandler());
        client_.addKeyboardHandler(new KeyboardHandler());
        client_.addRequestHandler(new MyRequestHandler());

        //    Beside the normal handler instances, we're registering a MessageRouter
        //    as well. That gives us the opportunity to reply to JavaScript method
        //    calls (JavaScript binding). We're using the default configuration, so
        //    that the JavaScript binding methods "cefQuery" and "cefQueryCancel"
        //    are used.
        // 2.1) We're overriding CefDisplayHandler as nested anonymous class
        //      to update our address-field, the title of the panel as well
        //      as for updating the status-bar on the bottom of the browser
        client_.addDisplayHandler(new CefDisplayHandlerAdapter() {
            @Override
            public void onAddressChange(CefBrowser browser, String url) {
                control_pane_.setAddress(browser, url);
            }

            @Override
            public void onTitleChange(CefBrowser browser, String title) {
                setTitle(title);
            }

            @Override
            public void onStatusMessage(CefBrowser browser, String value) {
                status_panel_.setStatusText(value);
            }
        });
        client_.addLoadHandler(new CefLoadHandlerAdapter() {
            @Override
            public void onLoadingStateChange(CefBrowser browser, boolean isLoading,
                    boolean canGoBack, boolean canGoForward) {
                control_pane_.update(browser, isLoading, canGoBack, canGoForward);
                status_panel_.setIsInProgress(isLoading);
                setLoading(isLoading);
                if (!isLoading && !errorMsg_.isEmpty()) {
                    browser.loadString(errorMsg_, control_pane_.getAddress());
                    errorMsg_ = "";
                }
            }

            @Override
            public void onLoadEnd(CefBrowser cb, int i, int i1) {
                Facking.faking(cb);

            }

            @Override
            public void onLoadError(CefBrowser browser, int frameIdentifer, ErrorCode errorCode,
                    String errorText, String failedUrl) {
                if (errorCode != ErrorCode.ERR_NONE && errorCode != ErrorCode.ERR_ABORTED) {
                    errorMsg_ = "<html><head>";
                    errorMsg_ += "<title>Error while loading</title>";
                    errorMsg_ += "</head><body>";
                    errorMsg_ += "<h1>" + errorCode + "</h1>";
                    errorMsg_ += "<h3>Failed to load " + failedUrl + "</h3>";
                    errorMsg_ += "<p>" + (errorText == null ? "" : errorText) + "</p>";
                    errorMsg_ += "</body></html>";
                    browser.stopLoad();
                }
            }
        });

        // 3) Before we can display any content, we require an instance of
        //    CefBrowser itself by calling createBrowser() on the CefClient.
        //    You can create one to many browser instances per CefClient.
        //
        //    If the user has specified the application parameter "--cookie-path="
        //    we provide our own cookie manager which persists cookies in a directory.'
        CefRequestContext requestContext = null;

        if (cookiePath != null) {
            cookieManager_ = CefCookieManager.createManager(cookiePath, false);
            requestContext = CefRequestContext.createContext(new CefRequestContextHandlerAdapter() {
                @Override
                public CefCookieManager getCookieManager() {
                    return cookieManager_;
                }
            });
        } else {
            cookieManager_ = CefCookieManager.getGlobalManager();
        }

        browser_ = client_.createBrowser("https://www.youtube.com/", false, false, requestContext);
        messageLabel = new Label("......................");

        JButton aa = new JButton("......................");

        aa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("xxxxxxxxx");
                browser_.executeJavaScript("window.cefQuery({request:'#1614686286284#jsvalue='+document.evaluate(\"//iframe[contains(@src,'/includes/commerce/authenticate')]\", document, null, 7, null).snapshotItem(0).getAttribute(\"src\")})", browser_.getURL(), 0);
            }
        });
        //    Last but not least we're setting up the UI for this example implementation.
        getContentPane().add(createContentPanel(), BorderLayout.CENTER);
        getContentPane().add(aa, BorderLayout.PAGE_START);
        MenuBar menuBar = new MenuBar(this, browser_, control_pane_, downloadDialog, cookieManager_);
        menuBar.addBookmark("Canvas checking", "https://browserleaks.com/canvas");
        menuBar.addBookmark("Audio Test", "https://music.apple.com/us/listen-now?at=1000l4QJ&ct=402&itscg=10000&itsct=402x");
        menuBar.addBookmark("Youtube", "https://www.youtube.com/");
        menuBar.addBookmark("Test Mp3", "https://www.nhaccuatui.com/bai-hat/dong-doi-dan-truong.NelwjxRBA6g4.html");
        menuBar.addBookmark("Test YTB Video", "https://www.youtube.com/watch?v=LTByTPP_NxY");
        menuBar.addBookmark("F.vision", "http://f.vision");
        menuBar.addBookmark("amiunique", "https://amiunique.org/fp");
        setJMenuBar(menuBar);

        if (ToolSetting.getInstance().socketPort != 0) {
            try {
                // Create a socket to connect to the server
                socket = new Socket("127.0.0.1", ToolSetting.getInstance().socketPort);
                task = new TaskReadThread(socket, browser_, messageLabel, this);
                Thread thread = new Thread(task);
                thread.start();
                CefMessageRouter msgRouter = CefMessageRouter.create();
                msgRouter.addHandler(new MessageRouterHandler(task), true);
                //msgRouter.addHandler(new MessageRouterHandlerEx(client_), false);
                client_.addMessageRouter(msgRouter);

            } catch (Exception ex) {

            }
        } else {
            CefMessageRouter msgRouter = CefMessageRouter.create();
            msgRouter.addHandler(new MessageRouterHandler(task), true);
            //msgRouter.addHandler(new MessageRouterHandlerEx(client_), false);
            client_.addMessageRouter(msgRouter);
        }
    }
    Socket socket;
    TaskReadThread task;

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        control_pane_ = new ControlPanel(browser_);

        status_panel_ = new StatusPanel();
        contentPanel.add(control_pane_, BorderLayout.NORTH);

        // 4) By calling getUIComponen() on the CefBrowser instance, we receive
        //    an displayable UI component which we can add to our application.
        contentPanel.add(browser_.getUIComponent(), BorderLayout.CENTER);

        contentPanel.add(status_panel_, BorderLayout.SOUTH);

        return contentPanel;
    }
}
