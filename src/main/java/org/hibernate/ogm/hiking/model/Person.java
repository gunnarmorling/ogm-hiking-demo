package org.hibernate.ogm.hiking.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

@Entity
public class Person {

	private long id;
	private String name;
	private Set<Hike> organizedHikes = new HashSet<>();

	Person() {
	}

	public Person(String name) {
		this.name = name;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Size(min=3)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany
	public Set<Hike> getOrganizedHikes() {
		return organizedHikes;
	}

	public void setOrganizedHikes(Set<Hike> organizedHikes) {
		this.organizedHikes = organizedHikes;
	}

	@Override
	public String toString() {
		return "Hike [id=" + id + ", name=" + name + "]";
	}
}
