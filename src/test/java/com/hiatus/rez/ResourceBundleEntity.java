package com.hiatus.rez;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ResourceBundle;

import javax.persistence.Entity;

@Entity
public class ResourceBundleEntity {
	private final ResourceBundle bundle;

	public ResourceBundleEntity( final ResourceBundle rb) {
		this.bundle = checkNotNull(rb);
	}

	public ResourceBundle getBundle() {
		return bundle;
	}
}