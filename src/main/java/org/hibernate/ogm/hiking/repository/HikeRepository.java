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

	public Hike createHike(Hike hike, Person organizer) {
		entityManager.persist( hike );

		if ( organizer != null ) {
			hike.setOrganizer( organizer );
			organizer.getOrganizedHikes().add( hike );
		}

		return hike;
	}

//	public Hike updateHike(Hike hike, Person organizer) {
//		entityManager.merge( hike );
//
//		if ( organizer != null ) {
//			hike.setOrganizer( organizer );
//			organizer.getOrganizedHikes().add( hike );
//		}
//
//		return hike;
//	}

	public Hike getHikeById(long hikeId) {
		return entityManager.find( Hike.class, hikeId );
	}

	public List<Hike> getAllHikes() {
		return entityManager.createQuery( "from Hike", Hike.class ).getResultList();
	}

	public List<Hike> getHikesByFromOrTo(String term) {
		return entityManager.createQuery( "FROM Hike WHERE start LIKE :term or destination LIKE :term", Hike.class )
				.setParameter( "term", "%" + term + "%" )
				.getResultList();
	}

	public Hike saveHike(Hike hike) {
		entityManager.persist( hike );
		return hike;
	}

	public void deleteHike(long hikeId) {
		Hike hike = entityManager.find( Hike.class, hikeId );

		if ( hike != null ) {
			if ( hike.getOrganizer() != null ) {
				hike.getOrganizer().getOrganizedHikes().remove( hike );
			}
			entityManager.remove( hike );
		}
	}
}
