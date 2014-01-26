/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiatus.rez;

import java.util.Collection;
import java.util.Locale;

import com.google.common.cache.CacheStats;

/**
 *
 * @author andrewregan
 */
public interface ResourceBundleStoreIF
{
	String getString( String inKey, Locale inLocale);
	Collection<String> getKeys( Locale inLocale);

	long cacheSize();
	CacheStats cacheStats();
	void invalidateCache();
	void invalidateCache( final Locale inLocale);
}