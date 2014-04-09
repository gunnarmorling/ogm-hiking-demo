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
package org.hibernate.ogm.hiking.rest.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.ogm.hiking.model.Hike;
import org.hibernate.ogm.hiking.model.Section;

public class HikeDescription {

	private String from;
	private String to;
	private String organizerName;
	private List<Section> sections = new ArrayList<>();

	public HikeDescription() {
	}

	public HikeDescription(Hike hike) {
		this.from = hike.getStart();
		this.to = hike.getDestination();
		this.organizerName = hike.getOrganizer() != null ? hike.getOrganizer().getName() : null;

		for ( Section section : hike.getSections() ) {
			if ( section != null ) {
				sections.add( section );
			}
		}
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getOrganizerName() {
		return organizerName;
	}

	public void setOrganizerName(String organizerName) {
		this.organizerName = organizerName;
	}

	public List<Section> getSections() {
		return sections;
	}

	public void setSections(List<Section> sections) {
		this.sections = sections;
	}

	@Override
	public String toString() {
		return "HikeDescription [from=" + from + ", to=" + to + ", organizerName=" + organizerName + ", sections=" + sections + "]";
	}
}
