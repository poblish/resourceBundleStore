package com.hiatus.rez.loader;

import java.util.Locale;
import java.util.ResourceBundle;

import com.google.common.base.Function;

public interface BundleLoaderIF extends Function<Locale,ResourceBundle> {

}
