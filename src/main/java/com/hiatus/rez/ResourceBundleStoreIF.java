/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiatus.rez;

import java.util.Locale;
import java.util.ResourceBundle;

import com.google.common.cache.CacheStats;

/**
 *
 * @author andrewregan
 */
public interface ResourceBundleStoreIF
{
	String getString( String inKey, Locale inLocale);
	ResourceBundle getBundle( Locale inLocale);

	long cacheSize();
	CacheStats cacheStats();
	void invalidateCache();
	void invalidateCache( final Locale inLocale);
}