package com.hiatus.rez;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheStats;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hiatus.rez.loader.BundleLoaderIF;

public class ResourceBundleStore implements ResourceBundleStoreIF
{
    private static Object _locale_locker = new byte[0];
    private static Object _put_locker = new byte[0];	// 7 May 2002

    // The top level hash containing the second-level hashes for each Locale. The top-level key is the Locale object...

    private final Cache<Locale,Map<String,String>> localeDataHash = CacheBuilder.newBuilder().recordStats().build();

    private final String baseName;
    private final Set<BundleLoaderIF> loaders = Sets.newHashSet();
    private final Locale defaultLocale;

    public ResourceBundleStore() {
        this( "", Locale.getDefault());
    }

    public ResourceBundleStore( final Locale inDefaultLocale) {
        this( "", inDefaultLocale);
    }

    public ResourceBundleStore( final String inBaseName, final Locale inDefaultLocale) {
        baseName = Strings.nullToEmpty(inBaseName);
        defaultLocale = checkNotNull(inDefaultLocale);

        getBestMatchedLocale(defaultLocale);	// 4 June 2002. Load default Locale now
    }

    @Override
    public long cacheSize() {
        return localeDataHash.size();
    }

    @Override
    public void registerLoader( final BundleLoaderIF loader) {
        loaders.add(loader);
    }

    @Override
    public String getString( final String inKey, final Locale inLocale) {
        final Locale theLoadedLocale = getBestMatchedLocale( checkNotNull(inLocale) );  // Try to load "gd-GB" ... 7 March 2002

        final Map<String,String> theLocaleData = localeDataHash.getIfPresent(theLoadedLocale);
        if ( theLocaleData == null) {  // This happens when...?
            return null;
        }

        ////////////////////////////////////////////////////////////

        String theResult = theLocaleData.get(inKey);

        if ( theResult != null) {
            return theResult;
        }
        else if ( theLoadedLocale.getCountry() != null && !theLoadedLocale.getCountry().isEmpty()) {  // no string in "gd-GB", try in "gd" ... 7 March 2002
            final Locale theParentLocale = new Locale( theLoadedLocale.getLanguage(), "");
            final Locale theLoadedParent = checkLocale(theParentLocale);  // try to load "gd" (duh!) ... 7 March 2002

            theResult = ( localeDataHash.getIfPresent(theLoadedParent)).get(inKey);

            if ( theResult != null) {
                return theResult;
            }
        }

        ////////////////////////////////////////////////////////////

        final Map<String,String> theMap = localeDataHash.getIfPresent(defaultLocale);  // use default locale ... 8 May 2002
        return ( theMap != null) ? theMap.get(inKey) : "";
    }

    /**
     * Check to see if the PropertyResourceBundle for this Locale has been loaded.
     * @param inLocale
     * @return
     */
    private Locale checkLocale( final Locale inLocale) {
        final Set<Locale> alreadyUsed = Sets.newHashSet();
        Locale localeToTry = inLocale;

        synchronized (_locale_locker) {
            while (true) {
                if (localeDataHash.getIfPresent(localeToTry) != null) {
                    return localeToTry;
                }
                else
                {
                    try {
                        cacheBundleLocaleIfNecessary(localeToTry);
                        return localeToTry;
                    }
                    catch (final MissingResourceException mre) {
                        alreadyUsed.add(localeToTry);

                        if ( localeToTry.getVariant() != null && !localeToTry.getVariant().isEmpty()) {
                            localeToTry = new Locale( localeToTry.getLanguage(), localeToTry.getCountry());
                            if (alreadyUsed.contains(localeToTry)) {
                                continue;
                            }
                        }
                        else if ( localeToTry.getCountry() != null && !localeToTry.getCountry().isEmpty()) {
                            localeToTry = new Locale( localeToTry.getLanguage() );
                            if (alreadyUsed.contains(localeToTry)) {
                                continue;
                            }
                        }
                        else if (!localeToTry.equals(defaultLocale)) {
                            localeToTry = defaultLocale;
                            if (alreadyUsed.contains(localeToTry)) {
                                return null;
                            }
                        }
                        else {
                            return null;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void invalidateCache() {
        synchronized (_put_locker) {
            localeDataHash.invalidateAll();
        }
    }

    @Override
    public void invalidateCache( final Locale inLocale) {
        synchronized (_put_locker) {
            localeDataHash.invalidate(inLocale);
        }
    }

    private void cacheBundleLocaleIfNecessary( final Locale inLocale) throws MissingResourceException {
        for ( final BundleLoaderIF eachLoader : loaders) {
            final ResourceBundle result = eachLoader.apply(inLocale);
            if ( result != null) {
                cacheFoundBundleLocaleIfNecessary( result, inLocale);
                return;
            }
        }

        cacheFoundBundleLocaleIfNecessary( ResourceBundle.getBundle( baseName, inLocale), inLocale);
    }

    private void cacheFoundBundleLocaleIfNecessary( final ResourceBundle inBundle, final Locale inLocale) {
        if (localeDataHash.getIfPresent(inLocale) == null) {

            final Map<String,String> temp = Maps.newHashMap();

            final Enumeration<String> theKeys = inBundle.getKeys();
            while (theKeys.hasMoreElements()) {
                final String strKey = theKeys.nextElement();
                temp.put( strKey, inBundle.getString(strKey));
            }

            localeDataHash.put( inLocale, temp);	// Now add the newly-created hash to our top-level hash.
        }
    }

    private Locale getBestMatchedLocale( final Locale inLocale) {
        final Locale theLoadedLocale = checkLocale(inLocale);
        return ( theLoadedLocale != null) ? theLoadedLocale : this.defaultLocale;
    }

    @Override
    public CacheStats cacheStats() {
        return localeDataHash.stats();
    }

    @Override
    public Collection<String> getKeys( final Locale inLocale) {
        final Locale theLoadedLocale = getBestMatchedLocale(inLocale);
        final Map<String,String> thisLocalesMap = localeDataHash.getIfPresent(theLoadedLocale);

        if ( thisLocalesMap != null) {
            return thisLocalesMap.keySet();
        }

        return Collections.emptySet();
    }
}