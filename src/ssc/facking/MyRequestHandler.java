/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.facking;

import org.cef.browser.CefBrowser;
import org.cef.callback.CefAuthCallback;
import org.cef.callback.CefRequestCallback;
import org.cef.handler.CefLoadHandler;
import org.cef.handler.CefRequestHandler;
import org.cef.handler.CefRequestHandlerAdapter;
import org.cef.handler.CefResourceHandler;
import org.cef.misc.BoolRef;
import org.cef.misc.StringRef;
import org.cef.network.CefRequest;
import org.cef.network.CefResponse;
import tests.detailed.handler.ResourceHandler;

/**
 *
 * @author PC
 */
public class MyRequestHandler extends CefRequestHandlerAdapter implements CefRequestHandler {
    
    @Override
    public boolean onBeforeBrowse(CefBrowser cb, CefRequest cr, boolean bln) {
        
        Facking.faking(cb);
       
        return false;
    }

    @Override
    public boolean onBeforeResourceLoad(CefBrowser cb, CefRequest cr) {
        //Facking.faking(cb);
        return false;
    }

    @Override
    public CefResourceHandler getResourceHandler(CefBrowser cb, CefRequest cr) {
        //Facking.faking(cb);
        if (cr.getURL().endsWith("foo.bar/")) {
            return new ResourceHandler();
        }
        if (cr.getURL().endsWith("seterror.test/")) {
        }
        return null;
    }

    @Override
    public void onResourceRedirect(CefBrowser cb, CefRequest cr, CefResponse cr1, StringRef sr) {
       Facking.faking(cb);
    }

    @Override
    public boolean getAuthCredentials(CefBrowser cb, boolean bln, String string, int i, String string1, String string2, CefAuthCallback cac) {
        Facking.faking(cb);
        return false;
    }

    @Override
    public boolean onQuotaRequest(CefBrowser cb, String string, long l, CefRequestCallback crc) {
        Facking.faking(cb);
        return false;
    }

    @Override
    public void onProtocolExecution(CefBrowser cb, String string, BoolRef br) {
        Facking.faking(cb);

    }

    @Override
    public boolean onCertificateError(CefBrowser cb, CefLoadHandler.ErrorCode ec, String string, CefRequestCallback crc) {
        Facking.faking(cb);
        return false;
    }

    @Override
    public void onPluginCrashed(CefBrowser cb, String string) {
        //Facking.faking(cb);

    }

    @Override
    public void onRenderProcessTerminated(CefBrowser cb, TerminationStatus ts) {
       Facking.faking(cb);

    }

}
