ResourceBundleStore
===================

Managed, cached, hierarchical Locale-based ResourceBundles for Java.

Whether you load ResourceBundles from your application's property files, their serialized equivalents via JPA, or an alternative data store,
use a ResourceBundleStore to select the best-matching bundle for a passed-in Locale, and cache (using Google Guava's CacheBuilder)
the loaded data to minimise fetches.