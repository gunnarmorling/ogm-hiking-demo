package org.hibernate.ogm.hiking.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;

@Entity
public class Hike {

	private long id;
	private String start;
	private String destination;
	private Person organizer;

	private List<Section> sections = new ArrayList<>();

	Hike() {
	}

	public Hike(String start, String destination) {
		this.start = start;
		this.destination = destination;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	@ElementCollection
	@OrderColumn(name="order")
	public List<Section> getSections() {
		return sections;
	}

	public void setSections(List<Section> sections) {
		this.sections = sections;
	}

	@ManyToOne
	public Person getOrganizer() {
		return organizer;
	}

	public void setOrganizer(Person organizer) {
		this.organizer = organizer;
	}

	@Override
	public String toString() {
		return "Hike [id=" + id + ", start=" + start + ", destination=" + destination + "]";
	}
}
