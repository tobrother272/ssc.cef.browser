/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author PC
 */
public class SystemBootstrap {
    /**
     * Simple interface for how a library by name should be loaded.
     */
    static public interface Loader { public void loadLibrary(String libname); }

    /**
     * Default implementation is to call System.loadLibrary
     */
    static private Loader loader_ = new Loader() {
        @Override
        public void loadLibrary(String libname) {
            System.loadLibrary(libname);
        }
    };

    static public void setLoader(Loader loader) {
        if (loader == null) {
            throw new NullPointerException("Loader cannot be null");
        }
        loader_ = loader;
    }

    static public void loadLibrary(String libname) {
        loader_.loadLibrary(libname);
    }
}
