package org.hibernate.ogm.hiking;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.ogm.hiking.model.Hike;
import org.hibernate.ogm.hiking.model.Person;
import org.hibernate.ogm.hiking.repository.HikeRepository;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class HikeRepositoryIT {

	private static final String WEBAPP_SRC = "src/main/webapp/";

	@Inject
	private HikeRepository hikeRepository;

	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap
			.create( WebArchive.class, HikeRepositoryIT.class.getSimpleName() + ".war" )
			.addPackage( Hike.class.getPackage() )
			.addPackage( HikeRepository.class.getPackage() )
			.addAsResource( "META-INF/persistence.xml" )
			.addAsWebInfResource( new File( WEBAPP_SRC + "WEB-INF/beans.xml" ) )
			.addAsResource( new StringAsset( "Dependencies: org.hibernate:ogm services, org.hibernate.ogm.mongodb services" ), "META-INF/MANIFEST.MF" );
	}

	@Test
	public void hikeRepositoryShouldPersistHikeAndOrganizer() {
		Hike hike = hikeRepository.createHike( new Hike( "Land's End", "Bristol" ), new Person( "Bob" ) );
		assertEquals( "Bob", hike.organizer.name );
	}

	@Test
	public void hikeRepositoryShouldReturnAllHikes() {
		hikeRepository.createHike( new Hike( "Land's End", "Bristol" ), new Person( "Bob" ) );
		hikeRepository.createHike( new Hike( "Land's End", "London" ), new Person( "Bill" ) );

		List<Hike> allHikes = hikeRepository.getAllHikes();
		assertThat( allHikes ).hasSize( 2 );
	}
}
