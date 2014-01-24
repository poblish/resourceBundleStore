/**
 *
 */
package com.hiatus.rez;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.ResourceBundle;

import org.testng.annotations.Test;

import com.google.common.base.Function;

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
	public void doTest() {
		final ResourceBundleStore rbs = new ResourceBundleStore("test", Locale.UK);
		rbs.registerFinder( new Function<Locale,ResourceBundle>() {
			public final ResourceBundle apply( final Locale input) {
				if ( input.equals( Locale.UK )) {
					return new ListResourceBundle() {

						@Override
						protected Object[][] getContents() {
							return new Object[][] { {"name", "Prince Andrew"}, {"year", "1976"} };
						}};
				}
				else if ( input.equals( Locale.US )) {
					return new ListResourceBundle() {

						@Override
						protected Object[][] getContents() {
							return new Object[][] { {"name", "Andrew"} };
						}};
				}
				else if ( input.equals(SWITZ_DE)) {
					return new ListResourceBundle() {

						@Override
						protected Object[][] getContents() {
							return new Object[][] { {"name", "Anders"}, {"year", "1981"} };
						}};
				}
				return null;
			}
		} );

		assertThat( rbs.getString( "name", Locale.US), is("Andrew"));
		assertThat( rbs.getString( "name", Locale.UK), is("Prince Andrew"));
		assertThat( rbs.getString( "name", Locale.GERMANY), is("Prince Andrew"));
		assertThat( rbs.getString( "name", SWITZ_DE), is("Anders"));

		assertThat( rbs.getString( "year", Locale.US), is("1976"));
		assertThat( rbs.getString( "year", Locale.UK), is("1976"));
		assertThat( rbs.getString( "year", Locale.GERMANY), is("1976"));
		assertThat( rbs.getString( "year", SWITZ_DE), is("1981"));
		assertThat( rbs.getString( "year", SWITZ_DE_2), is("1981"));

		assertThat( rbs.cacheSize(), is(3L));
	}
}