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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.hibernate.ogm.hiking.model.Hike;
import org.hibernate.ogm.hiking.repository.HikeRepository;
import org.hibernate.ogm.hiking.rest.model.HikeDescription;

@Path("/hikes")
//@ApplicationScoped
//@Transactional
@Stateless
public class HikeResource {

	@Inject
	private HikeRepository hikeRepository;

	public HikeResource() {
	}

	@GET
	@Path("/{id}")
	@Produces("application/json")
	public HikeDescription getHikeById(@PathParam("id") long hikeId) {
		return new HikeDescription( hikeRepository.getHikeById( hikeId ) );
	}

//	@GET
//	@Path("/")
//	@Produces("application/json")
//	public List<HikeDescription> getAllHikes() {
//		List<Hike> hikes = hikeRepository.getAllHikes();
//		List<HikeDescription> descriptions = new ArrayList<>( hikes.size() );
//
//		for ( Hike hike : hikes ) {
//			descriptions.add( new HikeDescription( hike ) );
//		}
//
//		return descriptions;
//	}

	@GET
	@Path("/")
	@Produces("application/json")
	public List<HikeDescription> findHikes(@QueryParam("q") String searchTerm) {
		List<Hike> hikes = searchTerm != null ? hikeRepository.getHikesByFromOrTo(searchTerm) : hikeRepository.getAllHikes();
		List<HikeDescription> descriptions = new ArrayList<>( hikes.size() );

		for ( Hike hike : hikes ) {
			descriptions.add( new HikeDescription( hike ) );
		}

		return descriptions;
	}

	@POST
	@Path("/")
	@Consumes("application/json")
	@Produces("application/json")
	public HikeDescription createHike(HikeDescription hike) {
		hikeRepository.saveHike( new Hike( hike.getFrom(), hike.getTo() ) );
		return hike;
	}
}
