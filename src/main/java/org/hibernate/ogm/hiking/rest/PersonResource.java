/**
 *  Copyright 2012 Gunnar Morling (http://www.gunnarmorling.de/)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
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
