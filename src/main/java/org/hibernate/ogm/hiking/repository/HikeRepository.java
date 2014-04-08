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
package org.hibernate.ogm.hiking.repository;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.ogm.hiking.model.Hike;
import org.hibernate.ogm.hiking.model.Person;

@ApplicationScoped
public class HikeRepository {

	@PersistenceContext(unitName="hike-PU-JTA")
	private EntityManager entityManager;

	public Hike saveHikeAndOrganizer(Hike hike, Person organizer) {
		hike.setOrganizer( organizer );
		organizer.getOrganizedHikes().add( hike );

		entityManager.persist( hike );
		entityManager.persist( organizer );

		return hike;
	}

	public Hike getHikeById(long hikeId) {
		return entityManager.find( Hike.class, hikeId );
	}

	public List<Hike> getAllHikes() {
		System.out.println("getAllHikes");
		return entityManager.createQuery( "from Hike", Hike.class ).getResultList();
	}

	public List<Hike> getHikesByFromOrTo(String term) {
		System.out.println("getHikesByFromOrTo" + term);
		return entityManager.createQuery( "FROM Hike WHERE start LIKE :term or destination LIKE :term", Hike.class )
				.setParameter( "term", "%" + term + "%" )
				.getResultList();
	}

	public Hike saveHike(Hike hike) {
		entityManager.persist( hike );
		return hike;
	}
}
