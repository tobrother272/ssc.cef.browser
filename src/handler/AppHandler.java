// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights
// reserved. Use of this source code is governed by a BSD-style license that
// can be found in the LICENSE file.
package handler;

import org.cef.CefApp;
import org.cef.CefApp.CefAppState;
import org.cef.browser.CefBrowser;
import org.cef.callback.CefCommandLine;
import org.cef.callback.CefSchemeHandlerFactory;
import org.cef.callback.CefSchemeRegistrar;
import org.cef.handler.CefAppHandlerAdapter;
import org.cef.handler.CefResourceHandler;
import org.cef.network.CefRequest;
import util.ToolSetting;

public class AppHandler extends CefAppHandlerAdapter {

    // We're registering our own schemes to demonstrate how to use
    // CefAppHandler.onRegisterCustomSchemes() in combination with
    // CefApp.registerSchemeHandlerFactory().
    public AppHandler(String[] args) {
        super(args);
    }

    @Override
    public void onBeforeCommandLineProcessing(String string, CefCommandLine ccl) {
        //ccl.appendSwitchWithValue("proxy-server", "http://107.181.187.120:24552");

//        ccl.appendArgument("--disable-blink-features=AutomationControlled");
//        ccl.appendSwitchWithValue("disable-blink-features", "AutomationControlled");
//        ccl.appendSwitchWithValue("--dom.webdriver.enabled", "false");
//        ccl.appendSwitchWithValue("--webdriver_enable_native_events", "false");
//        ccl.appendSwitchWithValue("--media.peerconnection.enabled", "false");
//        ccl.appendSwitchWithValue("--webdriver_enable_native_events", "false");
//        ccl.appendSwitchWithValue("--media.navigator.permission.disabled", "false");
//        ccl.appendSwitchWithValue("--network.proxy.share_proxy_settings", "false");
        ccl.appendArgument("--disable-web-security");
        ccl.appendArgument("--allow-running-insecure-content");
        ccl.appendSwitch("--disable-web-security");
        ccl.appendSwitch("--allow-running-insecure-content");

        if (ToolSetting.getInstance().proxyHost.length() != 0) {

            ccl.appendSwitchWithValue("proxy-server", "socks5://" + ToolSetting.getInstance().proxyHost + ":" + ToolSetting.getInstance().proxyPort);
            ccl.appendSwitchWithValue("proxy-server", "http://" + ToolSetting.getInstance().proxyHost + ":" + ToolSetting.getInstance().proxyPort);
            ccl.appendSwitchWithValue("proxy-server", ToolSetting.getInstance().proxyHost + ":" + ToolSetting.getInstance().proxyPort);

        }

    }

    // (1) First of all we have to register our custom schemes by implementing
    //     the method "onRegisterCustomSchemes. The scheme names are added by
    //     calling CefSchemeRegistrar.addCustomScheme.
    @Override
    public void onRegisterCustomSchemes(CefSchemeRegistrar registrar) {
        if (registrar.addCustomScheme(
                SearchSchemeHandler.scheme, true, false, false, false, true, false)) {
            System.out.println("Added scheme " + SearchSchemeHandler.scheme + "://");
        }
        if (registrar.addCustomScheme(
                ClientSchemeHandler.scheme, true, false, false, false, true, false)) {
            System.out.println("Added scheme " + ClientSchemeHandler.scheme + "://");
        }
    }

    // (2) At the next step we have to register a SchemeHandlerFactory which is
    //     called if an user enters our registered scheme.
    //
    //     This is done via the CefApp.registerSchemeHandlerFactory() method.
    //     A good place to call this function is from
    //     CefAppHandler.onContextInitialized().
    //
    //     The empty |domain_name| value will cause the factory to match all
    //     domain names. A set |domain_name| will only be valid for the entered
    //     domain.
    @Override
    public void onContextInitialized() {
        CefApp cefApp = CefApp.getInstance();
        cefApp.registerSchemeHandlerFactory(
                SearchSchemeHandler.scheme, SearchSchemeHandler.domain, new SchemeHandlerFactory());
        cefApp.registerSchemeHandlerFactory(
                ClientSchemeHandler.scheme, ClientSchemeHandler.domain, new SchemeHandlerFactory());
    }

    // (3) The SchemeHandlerFactory creates a new ResourceHandler instance for each
    //     request the user has send to the browser. The ResourceHandler is the
    //     responsible class to process and return the result of a received
    //     request.
    private class SchemeHandlerFactory implements CefSchemeHandlerFactory {

        @Override
        public CefResourceHandler create(
                CefBrowser browser, String schemeName, CefRequest request) {
            if (schemeName.equals(SearchSchemeHandler.scheme)) {
                return new SearchSchemeHandler(browser);
            } else if (schemeName.equals(ClientSchemeHandler.scheme)) {
                return new ClientSchemeHandler();
            }
            return null;
        }
    }

    @Override
    public void stateHasChanged(CefAppState state) {
        System.out.println("CefApp: " + state);
        if (state == CefAppState.TERMINATED) {
            System.exit(0);
        }
    }
}
