/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * JBoss, Home of Professional Open Source
 * Copyright 2014 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
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
