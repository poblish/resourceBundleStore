/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiatus.rez;

import java.util.Locale;
import java.util.Map;

/**
 *
 * @author andrewregan
 */
public interface ResourceBundleStoreIF
{
	public void clear();
	public String getString( String inKey, Locale inLocale);
//	public Map getUnmodifiableLocaleResourceMap( Locale inLocale);
	public String[] getStringArray( String inKeyPrefix, Locale inLocale);
}