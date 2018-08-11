package com.bride.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.jar.JarOutputStream;


import jdk.nashorn.internal.parser.JSONParser;
import org.apache.commons.io.FileUtils;
import org.apache.jena.atlas.json.JsonException;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDFS;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;




/*
*  String file="....\\Ontology.owl";
    File f=new File(file);
    FileReader r=new FileReader(f);
    OntModel m=ModelFactory.createOntologyModel();
    m.read(r,null);

    String req ="" +
        "PREFIX m: <http://www.NewOnto1.org/Ontology#>"+
        "PREFIX aut:<http://www.NewOnto1.org/Citations#AuthorCite>"+
        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
        "SELECT ?AuthorName ?AuthorField"+
        "WHERE{"+
            "?x  rdf:type m:AuthorCite."+
            "?x m:AuthorName ?AuthorName."+
            "?x m:AuthorBelongsToField ?AuthorField."+
            "FILTER regex(str(?AuthorField),\""+field+"\")."+
        "}";

    com.hp.hpl.jena.query.Query query = QueryFactory.create(req);

    QueryExecution qe = QueryExecutionFactory.create(query, m);
    com.hp.hpl.jena.query.ResultSet res = qe.execSelect();

    //ResultSetFormatter.out(System.out, res, query);

    int leng=0;
    while(res.hasNext()){
        leng++;
        res.next();
    }
    System.out.println("Length : "+leng);*/

@Path("/json")
public class JSONService {


    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Output> getOutputInJSON() {
        ArrayList<Output> outputs = new ArrayList<Output>();

        try {

            URL url = getClass().getResource("/pizza.owl");
            File f = new File(url.getFile());
            FileReader r = new FileReader(f);
            OntModel m = ModelFactory.createOntologyModel();
            m.read(r, null);
            String req = "PREFIX pizza: <http://www.co-ode.org/ontologies/pizza/pizza.owl#>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                    "\n" +
                    "SELECT DISTINCT ?s WHERE {\n" +
                    "  ?s rdfs:subClassOf ?restriction .\n" +
                    "  ?restriction owl:onProperty pizza:hasSpiciness .\n" +
                    "  ?restriction owl:someValuesFrom pizza:Hot .\n" +
                    "}";
                    /*"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>  " +
                    " PREFIX  : <http://www.semanticweb.org/hp/ontologies/2016/2/disease-ontologies.owl#>" +
                    "select ?s ?p ?o "
                    +
                    "where { " +
                    "?s ?p ?o}";*/

            Query query = QueryFactory.create(req);
            QueryExecution qe = QueryExecutionFactory.create(query, m);
            ResultSet res = qe.execSelect();
            int leng = 0;

            while (res.hasNext()) {
                leng++;
                QuerySolution binding = res.nextSolution();

                Resource subject = (Resource) binding.get("s");
                Resource predicate = (Resource) binding.get("p");
                RDFNode object = (RDFNode) binding.get("o");

                Output output = new Output();

                output.setSubject(subject.getLocalName());

                outputs.add(output);

                System.out.println(subject.getNameSpace());
                try {
                    res.next();
                } catch (NoSuchElementException e) {
                    break;
                }

            }
            System.out.println("Length : " + leng);

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


        return outputs;

    }

    @GET
    @Path("/getAll/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Output> getAll(@PathParam("name") String brideName) {
        ArrayList<Output> outputs = new ArrayList<Output>();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {

            URL url = getClass().getResource("/bride_03.owl");
            File f = new File(url.getFile());
            FileReader r = new FileReader(f);
            OntModel m = ModelFactory.createOntologyModel();
            m.read(r, null);
            String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>  " +
                    "PREFIX bride: <http://www.semanticweb.org/aeshana/ontologies/2018/7/Bride#>" +
                    "select ?p ?o "
                    +
                    "where { " +
                    "bride:"+brideName+" ?p ?o.}";
            Query query = QueryFactory.create(queryString);
            QueryExecution qexec = QueryExecutionFactory.create(query, m);
            try {
                ResultSetRewindable results = ResultSetFactory.makeRewindable(qexec.execSelect());

                while (results.hasNext()) {
                    QuerySolution binding = results.nextSolution();
                    Resource subject = (Resource) binding.get("s");
                    Resource predicate = (Resource) binding.get("p");
                    if (binding.get("o").isResource()) {
                        Output output = new Output();

                        Resource object = (Resource) binding.get("o");
                        //output.setSubject(subject.getLocalName());
                        output.setPredicate(predicate.getLocalName());
                        output.setObject(object.getLocalName());
                        outputs.add(output);

                    } else if (binding.get("o").isLiteral()) {
                        Output output = new Output();
                        Literal object = (Literal) binding.get("o");
                       //output.setSubject(subject.getLocalName());
                        output.setPredicate(predicate.getLocalName());
                        output.setObject(object.getString());
                        outputs.add(output);
                    }


                }

                /*ResultSetFormatter.outputAsJSON(outputStream, results);


                JSONObject json = new JSONObject(outputStream.toString());
                return json;*/
                return outputs;

            } finally {
                qexec.close();
            }


        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return outputs;
    }

    @POST
    @Path("/post")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createOutputInJSON(Output track) {

        String result = "Output saved : " + track;
        return Response.status(201).entity(result).build();

    }

}