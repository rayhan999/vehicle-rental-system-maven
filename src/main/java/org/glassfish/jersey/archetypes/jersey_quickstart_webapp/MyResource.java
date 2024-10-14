package org.glassfish.jersey.archetypes.jersey_quickstart_webapp;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.archetypes.jersey_quickstart_webapp.resources.AuthResource;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Root resource (exposed at "myresource" path)
 */
@ApplicationPath("webapi")
public class MyResource extends ResourceConfig {
	public MyResource() {
	
	}
}
