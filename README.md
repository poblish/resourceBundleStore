ResourceBundleStore
===================

Managed, cached, hierarchical `Locale`-based `ResourceBundle`s for Java.

Whether you load `ResourceBundle`s from your application's property files, their serialized equivalents via JPA, or an alternative data store,
use a `ResourceBundleStore` to select the best-matching bundle for a passed-in `Locale`, and cache (using Google Guava's `CacheBuilder`)
the loaded data to minimise fetches.

```java
@Test
public void inMemoryTest() {
    final ResourceBundleStore rbs = new ResourceBundleStore( /* Default */ Locale.UK);
    rbs.registerLoader( new ObjectArrayLoader( Locale.UK, new Object[][] {{"name", "Prince Andrew"}} ) );
    rbs.registerLoader( new ObjectArrayLoader( Locale.US, new Object[][] {{"name", "Andrew"}} ) );
    rbs.registerLoader( new ObjectArrayLoader( SWITZ_DE,  new Object[][] {{"name", "Anders"}} ) );

    assertThat( rbs.getString( "name", Locale.UK), is("Prince Andrew"));
    assertThat( rbs.getString( "name", Locale.US), is("Andrew"));
    assertThat( rbs.getString( "name", Locale.GERMANY), is("Prince Andrew"));
    assertThat( rbs.getString( "name", SWITZ_DE), is("Anders"));
    assertThat( rbs.getString( "name", Locale.CHINESE), is("Prince Andrew"));
}

@Inject JpaLoader jpaLoader;

@Test
public void jpaTest() {
    final ResourceBundleStore rbs = new ResourceBundleStore( /* Default */ Locale.UK);
    rbs.registerLoader(jpaLoader);

    assertThat( rbs.getString( "name", Locale.US), is("Andrew"));
    assertThat( rbs.getKeys( Locale.US ), hasItems("name", ...));

    rbs.invalidateCache( Locale.CHINESE );
    rbs.invalidateCache();
    assertThat( rbs.cacheSize(), is(0L));
}
```