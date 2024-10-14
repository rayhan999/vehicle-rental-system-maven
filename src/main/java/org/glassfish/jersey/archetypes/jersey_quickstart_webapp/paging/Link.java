package org.glassfish.jersey.archetypes.jersey_quickstart_webapp.paging;

public class Link {

	private String url;
	private String rel;

	public Link() {
	}

	public Link(String url, String rel) {
		this.url = url;
		this.rel = rel;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRel() {
		return rel;
	}

	public void setRel(String rel) {
		this.rel = rel;
	}
}
