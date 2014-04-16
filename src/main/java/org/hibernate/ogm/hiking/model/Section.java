package org.hibernate.ogm.hiking.model;

import javax.persistence.Embeddable;

@Embeddable
public class Section {

	private String from;
	private String to;

	Section() {
	}

	public Section(String from, String to) {
		this.from = from;
		this.to = to;
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

	@Override
	public String toString() {
		return "Section [from=" + from + ", to=" + to + "]";
	}
}
