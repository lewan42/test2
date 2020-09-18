package com.zxc;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("data")
public class Service {

    @EJB
    MyEJB myEJB;

    @GET
    @Path("getFilterCount")
    public Long getFilterCount(@QueryParam("str") String str) {

        return myEJB.getFilterCount(str);
    }

    @GET
    @Path("getList")
    public List<Node> getList(@QueryParam("begin") int begin, @QueryParam("end") int end) {

        return myEJB.getList(begin, end);
    }

    @GET
    @Path("getFilterList")
    public List<Node> getList(@QueryParam("begin") int begin, @QueryParam("end") int end, @QueryParam("str") String str) {

        return myEJB.getList(begin, end, str);
    }

    @GET
    @Path("getParentCount")
    public Long getParentCount() {

        return myEJB.getParentCount();
    }



    @POST
    @Path("save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public int save(Request req) {

        return myEJB.save(req);
    }


    @DELETE
    @Path("delete")
    public Long delete(@QueryParam("delete") int nodeID) {

        return myEJB.delete(nodeID);
    }


    @PATCH
    @Path("edit")
    public Long edit(Request req) {

        return myEJB.edit(req);
    }

}
