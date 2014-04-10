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
package org.hibernate.ogm.hiking.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.hibernate.ogm.hiking.model.Hike;
import org.hibernate.ogm.hiking.model.Person;
import org.hibernate.ogm.hiking.model.Section;
import org.hibernate.ogm.hiking.repository.HikeRepository;
import org.hibernate.ogm.hiking.repository.PersonRepository;
import org.hibernate.ogm.hiking.rest.model.ExternalHike;

@Path("/hikes")
@Stateless
public class HikeResource {

	@Inject
	private HikeRepository hikeRepository;

	@Inject
	private PersonRepository personRepository;

	public HikeResource() {
	}

	@GET
	@Path("/{id}")
	@Produces("application/json")
	public ExternalHike getHikeById(@PathParam("id") long hikeId) {
		return new ExternalHike( hikeRepository.getHikeById( hikeId ) );
	}

	@GET
	@Path("/")
	@Produces("application/json")
	public List<ExternalHike> findHikes(@QueryParam("q") String searchTerm) {
		List<Hike> hikes = searchTerm != null ? hikeRepository.getHikesByFromOrTo(searchTerm) : hikeRepository.getAllHikes();
		List<ExternalHike> descriptions = new ArrayList<>( hikes.size() );

		for ( Hike hike : hikes ) {
			descriptions.add( new ExternalHike( hike ) );
		}

		return descriptions;
	}

	@POST
	@Path("/")
	@Consumes("application/json")
	@Produces("application/json")
	public ExternalHike createHike(ExternalHike externalHike) {
		Hike hike = new Hike( externalHike.getFrom(), externalHike.getTo() );
		Person organizer = null;

		if ( externalHike.getOrganizer() != null ) {
			organizer = personRepository.getPersonById( externalHike.getOrganizer().getId() );
		}

		for(Section section : externalHike.getSections() ) {
			hike.getSections().add( section );
		}

		hikeRepository.createHike( hike, organizer );

		return externalHike;
	}

	@PUT
	@Path("/{id}")
	@Consumes("application/json")
	@Produces("application/json")
	public ExternalHike updateHike(ExternalHike externalHike) {
//		Hike hike = new Hike( externalHike.getFrom(), externalHike.getTo() );
//		hike.setId( externalHike.getId() );
//
//		Person organizer = null;
//
//		if ( externalHike.getOrganizer() != null ) {
//			organizer = personRepository.getPersonById( externalHike.getOrganizer().getId() );
//		}
//
//		for(Section section : externalHike.getSections() ) {
//			hike.getSections().add( section );
//		}
//
//		System.out.println("Updating hike: " + hike);
//		hikeRepository.updateHike( hike, organizer );

		Hike hike = hikeRepository.getHikeById( externalHike.getId() );

		hike.setStart( externalHike.getFrom() );
		hike.setDestination( externalHike.getTo() );

		if ( externalHike.getOrganizer() != null ) {
			Person organizer = personRepository.getPersonById( externalHike.getOrganizer().getId() );
			hike.setOrganizer( organizer );
			organizer.getOrganizedHikes().add( hike );
		}

		hike.getSections().clear();
		hike.getSections().addAll( externalHike.getSections() );

		return externalHike;
	}

	@DELETE
	@Path("/{id}")
	public void deleteHike(@PathParam("id") long id) {
		hikeRepository.deleteHike( id );
	}
}
