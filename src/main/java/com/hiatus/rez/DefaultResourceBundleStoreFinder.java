/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiatus.rez;

/**
 *
 * @author andrewregan
 */
public class DefaultResourceBundleStoreFinder implements ResourceBundleStoreFinder
{
	private static Class<? extends ResourceBundleStoreIF>	s_InstanceClass;

	/*******************************************************************************
	*******************************************************************************/
	public ResourceBundleStoreIF find()
	{
		return LazyHolder.s_DefaultRBStore;
	}

	/*******************************************************************************
	*******************************************************************************/
	public static void setStoreType( final Class<? extends ResourceBundleStoreIF> inClazz)
	{
		s_InstanceClass = inClazz;
	}

	/*******************************************************************************
	*******************************************************************************/
	private static class LazyHolder
	{
		private static ResourceBundleStoreIF	s_DefaultRBStore;

		/*******************************************************************************
		*******************************************************************************/
		@SuppressWarnings("unused")
		public LazyHolder()
		{
			s_DefaultRBStore = null;
		}
	}
}
