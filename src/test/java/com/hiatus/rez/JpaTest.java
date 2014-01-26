/**
 *
 */
package com.hiatus.rez;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ListResourceBundle;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;

/**
 * TODO
 *
 * @author andrewregan
 *
 */
public class JpaTest {

	@Inject JpaLoader jpaLoader;

	@BeforeClass
	public void init() {
        ObjectGraph.create( new TestModule() ).inject(this);
	}

	private final static Locale	SWITZ_DE = new Locale("de", "CH");
	private final static Locale	SWITZ_DE_2 = new Locale("de", "CH", "Berne");

	@Test
	public void testJpaLoad() {
		final ResourceBundleStore rbs = new ResourceBundleStore("", Locale.UK);
		rbs.registerLoader(jpaLoader);

		assertThat( rbs.getString( "name", Locale.US), is("Andrew"));
		assertThat( rbs.getString( "name", Locale.UK), is("Prince Andrew"));
		assertThat( rbs.getString( "name", Locale.GERMANY), is("Prince Andrew"));
		assertThat( rbs.getString( "name", SWITZ_DE), is("Anders"));
		assertThat( rbs.getString( "name", Locale.CHINESE), is("Prince Andrew"));

		assertThat( rbs.getString( "year", Locale.US), is("1976"));
		assertThat( rbs.getString( "year", Locale.UK), is("1976"));
		assertThat( rbs.getString( "year", Locale.GERMANY), is("1976"));
		assertThat( rbs.getString( "year", SWITZ_DE), is("1981"));
		assertThat( rbs.getString( "year", SWITZ_DE_2), is("1981"));
	}

    @Module( injects={ JpaTest.class, JpaLoader.class})
    static class TestModule {

    	@Provides
    	@Singleton
    	EntityManager provideEntityManager() {
    		final EntityManager em = mock( EntityManager.class );
    		when( em.find( ResourceBundleEntity.class, Locale.UK)).thenReturn( new ResourceBundleEntity( new ListResourceBundle() {

				@Override
				protected Object[][] getContents() {
					return new Object[][] {{"name", "Prince Andrew"}, {"year", "1976"}};
				}}
    		) );

    		when( em.find( ResourceBundleEntity.class, Locale.US)).thenReturn( new ResourceBundleEntity( new ListResourceBundle() {

				@Override
				protected Object[][] getContents() {
					return new Object[][] {{"name", "Andrew"}};
				}}
    		) );

    		when( em.find( ResourceBundleEntity.class, SWITZ_DE)).thenReturn( new ResourceBundleEntity( new ListResourceBundle() {

				@Override
				protected Object[][] getContents() {
					return new Object[][] { {"name", "Anders"}, {"year", "1981"} };
				}}
    		) );

    		return em;
    	}
    }
}