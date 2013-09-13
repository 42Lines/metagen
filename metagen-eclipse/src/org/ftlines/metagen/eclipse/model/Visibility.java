package org.ftlines.metagen.eclipse.model;

public enum Visibility {
	PUBLIC("public", 40), PROTECTED("protected", 30), DEFAULT("", 20), PRIVATE("private", 10);

	private final String term;
	private final int rating;

	private Visibility(String term, int rating) {
		this.term = term;
		this.rating = rating;
	}

	public String getTerm() {
		return term;
	}

	public int getRating() {
		return rating;
	}

	public Visibility relax(Visibility other) {
		return other.getRating() > getRating() ? other : this;
	}

}
