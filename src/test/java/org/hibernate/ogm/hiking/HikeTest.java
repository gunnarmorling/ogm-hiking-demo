package org.hibernate.ogm.hiking;

import static org.fest.assertions.Assertions.assertThat;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.ogm.hiking.model.Hike;
import org.hibernate.ogm.hiking.model.Person;
import org.hibernate.ogm.hiking.model.Section;
import org.junit.Before;
import org.junit.Test;

public class HikeTest {

	private EntityManager entityManager;

	@Before
	public void setupEntityManager() {
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory( "hike-PU" );
		entityManager = entityManagerFactory.createEntityManager();
	}

	@Test
	public void simpleEntityTest() {
		entityManager.getTransaction().begin();

		Hike hike = new Hike( "Land's End", "Bristol" );
		entityManager.persist( hike );

		entityManager.getTransaction().commit();

		entityManager.getTransaction().begin();
		hike = entityManager.find( Hike.class, hike.getId() );

		assertThat( hike ).isNotNull();
		assertThat( hike.getDestination() ).isEqualTo( "Bristol" );

		entityManager.getTransaction().commit();
	}

	@Test
	public void embeddedCollectionTest() {
		entityManager.getTransaction().begin();

		Hike hike = new Hike( "Land's End", "Bristol" );
		hike.getSections().add( new Section( "Land's End", "Pendeen" ) );
		hike.getSections().add( new Section( "Pendeen", "Perranporth" ) );
		entityManager.persist( hike );

		entityManager.getTransaction().commit();

		entityManager.getTransaction().begin();
		hike = entityManager.find( Hike.class, hike.getId() );

		assertThat( hike.getSections() ).hasSize( 2 );
		assertThat( hike.getSections().get( 0 ).getFrom() ).isEqualTo( "Land's End" );
		assertThat( hike.getSections().get( 1 ).getFrom() ).isEqualTo( "Pendeen" );

		entityManager.getTransaction().commit();
	}

	@Test
	public void associationTest() {
		entityManager.getTransaction().begin();

		Hike hike = new Hike( "Land's End", "Bristol" );
		Person bob = new Person( "Bob" );

		hike.setOrganizer( bob );
		bob.getOrganizedHikes().add( hike );

		entityManager.persist( hike );
		entityManager.persist( bob );

		entityManager.getTransaction().commit();

		entityManager.getTransaction().begin();
		hike = entityManager.find( Hike.class, hike.getId() );

		assertThat( hike.getOrganizer() ).isNotNull();
		assertThat( hike.getOrganizer().getName() ).isEqualTo( "Bob" );

		entityManager.getTransaction().commit();
	}

	@Test
	public void validationTest() {
		entityManager.getTransaction().begin();

		Person bob = new Person( "B" );

		entityManager.persist( bob );

		entityManager.getTransaction().commit();
	}
}
