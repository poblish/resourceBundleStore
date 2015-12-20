package com.hiatus.rez.loader;

import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.ResourceBundle;

public class ObjectArrayLoader implements BundleLoaderIF {

    private final Locale locale;
    private final Object[][] objVals;

    public ObjectArrayLoader( final Locale inLocale, final Object[][] inVals)  {
        locale = inLocale;
        objVals = inVals;
    }

    public final ResourceBundle apply( final Locale input) {
        if (input.equals(locale)) {
            return new ListResourceBundle() {

                @Override
                protected Object[][] getContents() {
                    return objVals;
                }};
        }
        return null;
    }
}
