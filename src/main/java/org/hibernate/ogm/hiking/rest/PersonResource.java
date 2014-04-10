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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.hibernate.ogm.hiking.model.Person;
import org.hibernate.ogm.hiking.repository.HikeRepository;
import org.hibernate.ogm.hiking.rest.model.ExternalPerson;

@Path("/persons")
@Stateless
public class PersonResource {

	@Inject
	private HikeRepository hikeRepository;

	public PersonResource() {
	}

	@GET
	@Path("/")
	@Produces("application/json")
	public List<ExternalPerson> getAllPersons() {
		List<Person> persons = hikeRepository.getAllPersons();
		List<ExternalPerson> descriptions = new ArrayList<>( persons.size() );

		for ( Person person : persons ) {
			descriptions.add( new ExternalPerson( person ) );
		}

		return descriptions;
	}
}
