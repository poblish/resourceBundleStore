/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiatus.rez;

import java.util.Collection;
import java.util.Locale;

import com.google.common.cache.CacheStats;
import com.hiatus.rez.loader.BundleLoaderIF;

/**
 *
 * @author andrewregan
 */
public interface ResourceBundleStoreIF
{
	void registerLoader( final BundleLoaderIF loader);

	String getString( String inKey, Locale inLocale);
	Collection<String> getKeys( Locale inLocale);

	long cacheSize();
	CacheStats cacheStats();
	void invalidateCache();
	void invalidateCache( final Locale inLocale);
}