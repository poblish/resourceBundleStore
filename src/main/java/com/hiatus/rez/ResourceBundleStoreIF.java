/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiatus.rez;

import java.util.Locale;

/**
 *
 * @author andrewregan
 */
public interface ResourceBundleStoreIF
{
	String getString( String inKey, Locale inLocale);
}