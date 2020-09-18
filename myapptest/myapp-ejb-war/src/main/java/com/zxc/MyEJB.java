package com.zxc;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;


@Stateless
public class MyEJB {
    List<Node> list;

    @PersistenceContext(unitName = "node")
    private EntityManager manager;


    public List<Node> getList(int begin, int end, String str) {

        List<Node> resultList = manager.
                createNativeQuery("db.Node.find({\"name\": \"" + str + "\"})", Node.class).setFirstResult(begin).setMaxResults(end - begin).getResultList();

        return resultList;
    }


    public List<Node> getList(int begin, int end) {
        list = new ArrayList<Node>();

        List<Node> tmpResult = manager.
                createNativeQuery("db.Node.find({parentID: -1})", Node.class).setFirstResult(begin).setMaxResults(end - begin).getResultList();

        int[] params = new int[tmpResult.size()];

        for (int i = 0; i < params.length; i++) {
            params[i] = tmpResult.get(i).get_Id();
        }

        setAllChildren(params);

        list.addAll(tmpResult);

        return list;
    }


    private void setAllChildren(int... params) {


        String query = buildQuery(params);

        List<Node> results = manager
                .createNativeQuery(query, Node.class)
                .getResultList();


        for (Node n : results) {
            list.add(n);
            setAllChildren(n.get_Id());
        }
    }

    private String buildQuery(int... params) {

        StringBuilder sb = new StringBuilder();

        if (params.length == 1) return String.format("db.Node.find({parentID: %d})", params[0]);


        for (int i = 0; i < params.length; i++) {

            if (params.length - 1 != i)
                sb.append(String.format("{ parentID: %d }, ", params[i]));

            else sb.append(String.format("{ parentID: %d } ", params[i]));
        }

        return String.format("db.Node.find( {$or:[ %s ] } )", sb.toString());
    }


    public int save(Request req) {

        int newID;
        Document ID = new MongoClient().getDatabase("node").getCollection("Node").find().sort(new BasicDBObject("_id", -1)).first();
        Document document = new Document();

        try {
            newID = (Integer) ID.get("_id") + 1;
        } catch (NullPointerException e) {
            return -1;
        }

        document.put("_id", newID);
        document.put("name", req.getName());
        document.put("lastName", req.getLastName());
        document.put("parentID", req.getParentID());

        new MongoClient().getDatabase("node").getCollection("Node").insertOne(document);

        return newID;
    }


    public Long delete(int nodeID) {

        BasicDBObject document = new BasicDBObject();

        list = new ArrayList<Node>();
        List<Integer> list_for_delete = new ArrayList<Integer>();

        setAllChildren(nodeID);

        list_for_delete.add(nodeID);

        for (Node n : list)
            list_for_delete.add(n.get_Id());

        document.put("_id", new BasicDBObject("$in", list_for_delete));

        DeleteResult result = new MongoClient().getDatabase("node").getCollection("Node").deleteMany(document);

        return result.getDeletedCount();
    }


    public Long edit(Request request) {


        BasicDBObject newDocument = new BasicDBObject();
        newDocument.put("name", request.getName());
        newDocument.put("lastName", request.getLastName());

        BasicDBObject updateObject = new BasicDBObject();
        updateObject.put("$set", newDocument);

        UpdateResult result = new MongoClient().getDatabase("node").getCollection("Node").updateOne(new BasicDBObject().append("_id", request.getId()), updateObject);
        return result.getMatchedCount();
    }

    public Long getParentCount() {

        return new MongoClient().getDatabase("node").getCollection("Node").countDocuments(Document.parse("{parentID : -1}"));
    }


    public Long getFilterCount(String str) {

        return new MongoClient().getDatabase("node").getCollection("Node").count(Document.parse("{ name : \"" + str + "\"}"));
    }

}
