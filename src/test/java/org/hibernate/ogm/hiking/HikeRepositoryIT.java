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
		Hike hike = hikeRepository.saveHikeAndOrganizer( new Hike( "Land's End", "Bristol" ), new Person( "Bob" ) );
		assertEquals( "Bob", hike.getOrganizer().getName() );
	}

	@Test
	public void hikeRepositoryShouldReturnAllHikes() {
		hikeRepository.saveHikeAndOrganizer( new Hike( "Land's End", "Bristol" ), new Person( "Bob" ) );
		hikeRepository.saveHikeAndOrganizer( new Hike( "Land's End", "London" ), new Person( "Bill" ) );

		List<Hike> allHikes = hikeRepository.getAllHikes();
		assertThat( allHikes ).hasSize( 2 );
	}
}
