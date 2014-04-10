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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.hibernate.ogm.hiking.model.Person;
import org.hibernate.ogm.hiking.repository.PersonRepository;
import org.hibernate.ogm.hiking.rest.model.ExternalPerson;

@Path("/persons")
@Stateless
public class PersonResource {

	@Inject
	private PersonRepository personRepository;

	public PersonResource() {
	}

	@GET
	@Path("/")
	@Produces("application/json")
	public List<ExternalPerson> getAllPersons() {
		List<Person> persons = personRepository.getAllPersons();
		List<ExternalPerson> externalPersons = new ArrayList<>( persons.size() );

		for ( Person person : persons ) {
			externalPersons.add( new ExternalPerson( person ) );
		}

		return externalPersons;
	}

	@POST
	@Path("/")
	@Consumes("application/json")
	@Produces("application/json")
	public long createPerson(ExternalPerson externalPerson) {
		Person person = new Person( externalPerson.getName() );
		person = personRepository.createPerson( person );
		return person.getId();
	}
}
