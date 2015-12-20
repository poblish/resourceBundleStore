package com.hiatus.rez;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.hiatus.rez.loader.BundleLoaderIF;

public class JpaLoader implements BundleLoaderIF {

	@Inject EntityManager em;

	public JpaLoader() {
	}

	public final ResourceBundle apply( final Locale input) {
		final ResourceBundleEntity entity = em.find( ResourceBundleEntity.class, input);
		return ( entity != null) ? entity.getBundle() : null;
	}
}