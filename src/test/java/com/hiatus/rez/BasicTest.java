/**
 *
 */
package com.hiatus.rez;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Locale;

import org.testng.annotations.Test;

import com.hiatus.rez.loader.ObjectArrayLoader;

/**
 * TODO
 *
 * @author andrewregan
 *
 */
public class BasicTest {

	private final static Locale	SWITZ_DE = new Locale("de", "CH");
	private final static Locale	SWITZ_DE_2 = new Locale("de", "CH", "Berne");

	@Test
	public void doTestPropertiesLoad1() {
		final ResourceBundleStore rbs = new ResourceBundleStore("Test1", Locale.UK);
		assertThat( rbs.getString( "Name", Locale.ENGLISH), is("John"));
		assertThat( rbs.getString( "Name", Locale.UK), is("John"));
		assertThat( rbs.getString( "Name", Locale.JAPANESE), is("John"));
	}

	@Test
	public void doTestPropertiesLoad2() {
		final ResourceBundleStore rbs = new ResourceBundleStore("Test2", Locale.UK);
		assertThat( rbs.getString( "Name", Locale.JAPANESE), is("ヒとし"));
		assertThat( rbs.getString( "Name", Locale.ENGLISH), nullValue());
		assertThat( rbs.getString( "Name", Locale.UK), nullValue());
	}

	@Test
	public void doInMemoryTest() {
		final ResourceBundleStore rbs = new ResourceBundleStore(Locale.UK);
		rbs.registerLoader( new ObjectArrayLoader( Locale.UK, new Object[][] {{"name", "Prince Andrew"}, {"year", "1976"}} ) );
		rbs.registerLoader( new ObjectArrayLoader( Locale.US, new Object[][] {{"name", "Andrew"}} ) );
		rbs.registerLoader( new ObjectArrayLoader( SWITZ_DE, new Object[][] { {"name", "Anders"}, {"year", "1981"} } ) );

		assertThat( rbs.getString( "name", Locale.US), is("Andrew"));
		assertThat( rbs.getString( "name", Locale.UK), is("Prince Andrew"));
		assertThat( rbs.getString( "name", Locale.GERMANY), is("Prince Andrew"));
		assertThat( rbs.getString( "name", SWITZ_DE), is("Anders"));
		assertThat( rbs.getString( "name", Locale.CHINESE), is("Prince Andrew"));

		testRBS(rbs);
		assertThat( rbs.cacheSize(), is(3L));
		assertThat( rbs.cacheStats().hitCount(), is(19L));
		assertThat( rbs.cacheStats().missCount(), is(15L));

		testRBS(rbs);
		assertThat( rbs.cacheSize(), is(3L));
		assertThat( rbs.cacheStats().hitCount(), is(31L));
		assertThat( rbs.cacheStats().missCount(), is(19L));

		rbs.invalidateCache();
		assertThat( rbs.cacheSize(), is(0L));
		testRBS(rbs);

		assertThat( rbs.getKeys( Locale.KOREAN ), hasItems("name"));
		assertThat( rbs.getKeys( Locale.UK ), hasItems("year", "name"));
	}

	private void testRBS( final ResourceBundleStore rbs) {
		assertThat( rbs.getString( "year", Locale.US), is("1976"));
		assertThat( rbs.getString( "year", Locale.UK), is("1976"));
		assertThat( rbs.getString( "year", Locale.GERMANY), is("1976"));
		assertThat( rbs.getString( "year", SWITZ_DE), is("1981"));
		assertThat( rbs.getString( "year", SWITZ_DE_2), is("1981"));
	}
}