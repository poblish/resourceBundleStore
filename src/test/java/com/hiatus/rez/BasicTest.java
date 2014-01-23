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

	@Test
	public void doTest() {
		ResourceBundleStore x = new ResourceBundleStore("test", Locale.UK);
		x.registerFinder( new Function<Locale,ResourceBundle>() {
			public final ResourceBundle apply( Locale input) {
				if ( input.equals( Locale.UK )) {
					return new ListResourceBundle() {

						@Override
						protected Object[][] getContents() {
							return new Object[][] { {"name", "Prince Andrew"} };
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
							return new Object[][] { {"name", "Anders"} };
						}};
				}
				return null;
			}
		} );

		assertThat( x.getString( "name", Locale.US), is("Andrew"));
		assertThat( x.getString( "name", Locale.UK), is("Prince Andrew"));
		assertThat( x.getString( "name", Locale.GERMANY), is("Prince Andrew"));
		assertThat( x.getString( "name", SWITZ_DE), is("Anders"));
		assertThat( x.getStringArray("poo", Locale.UK), is( new String[]{"Anders"} ));
	}
}
