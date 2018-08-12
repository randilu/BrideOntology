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
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.jar.JarOutputStream;


import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
import com.google.gson.Gson;
import jdk.nashorn.internal.parser.JSONParser;
import org.apache.commons.io.FileUtils;
import org.apache.jena.atlas.json.JsonException;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.reasoner.ValidityReport;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.PrintUtil;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;


//import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.*;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

import java.util.Iterator;
import java.util.Set;



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
    @Path("/getFromCombination/{casteName}/{religion}/{race}")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Output> getFromCombination(
            @PathParam("casteName") String casteName,
            @PathParam("religion") String religion,
            @PathParam("race") String race
    ) {
        ArrayList<Output> outputs = new ArrayList<Output>();
        try {

            URL url = getClass().getResource("/bride_08.owl");
            File f = new File(url.getFile());
            FileReader r = new FileReader(f);
            OntModel m = ModelFactory.createOntologyModel();
            m.read(r, null);
            String append = "";
            if(!casteName.equals("empty")){ append = "?bride bride:hasCaste bride:"+casteName+"."; }
            if(!religion.equals("empty")){append = append+" ?bride bride:hasReligion bride:"+religion+".";}
            if(!race.equals("empty")){append = append+" ?bride bride:hasRace bride:"+race+".";}
            String queryString ="PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>  " +
                    "PREFIX bride: <http://www.semanticweb.org/aeshana/ontologies/2018/7/Bride#>" +
                    "select ?bride "
                    +
                    "where { "+append+" }";
            System.out.println(queryString);
            Query query = QueryFactory.create(queryString);
            QueryExecution qexec = QueryExecutionFactory.create(query, m);
            try {
                ResultSetRewindable results = ResultSetFactory.makeRewindable(qexec.execSelect());

                while (results.hasNext()) {
                    QuerySolution binding = results.nextSolution();
                    Output output = new Output();
                    Resource resource = (Resource) binding.get("bride");
                    output.setSubject(resource.getLocalName());
                    System.out.println(resource.getLocalName());
                    System.out.println("------------------------------");
                    outputs.add(output);


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
    @GET
    @Path("/getAll/{name}/")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Output> getAll(@PathParam("name") String brideName) {
        ArrayList<Output> outputs = new ArrayList<Output>();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {

            URL url = getClass().getResource("/bride_08.owl");
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
                    "bride:" + brideName + " ?p ?o.}";
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

    @GET
    @Path("/inferred/{className}")
    @Consumes(MediaType.APPLICATION_JSON)
    public JSONArray getInferred(@PathParam("className") String className) {
        ArrayList<Output> outputsFinal = new ArrayList<Output>();
        String s;
        try {
            URL url = getClass().getResource("/bride_08.owl");
            File f = new File(url.getFile());
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

            OWLOntology ont = manager.loadOntologyFromOntologyDocument(f);
            System.out.println("Loaded " + ont.getOntologyID());


            OWLReasonerFactory reasonerFactory = new PelletReasonerFactory();

            OWLReasonerConfiguration config = new SimpleConfiguration();

            OWLReasoner reasoner = reasonerFactory.createReasoner(ont, config);


            reasoner.precomputeInferences();

            OWLDataFactory fac = manager.getOWLDataFactory();
            OWLClass country = fac.getOWLClass(IRI.create("http://www.semanticweb.org/aeshana/ontologies/2018/7/Bride#"+className));

            NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(country, true);



            Set<OWLNamedIndividual> individuals = individualsNodeSet.getFlattened();
            Object[] objects = individuals.toArray();
            System.out.println("Instances of "+className+": ");
            int i = 0;
            for (OWLNamedIndividual ind : individuals) {
                System.out.println("    " + ind);
                s = (""+ind.toStringID());
                System.out.println(s);

                FileReader r = new FileReader(f);
                OntModel m = ModelFactory.createOntologyModel();
                m.read(r, null);
                String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>  " +
                        "PREFIX bride: <http://www.semanticweb.org/aeshana/ontologies/2018/7/Bride#>" +
                        "select ?p ?o "
                        + "where { <" +
                        s+"> ?p ?o.}";
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
                            output.setIndex(i);
                            System.out.println(output);
                            outputsFinal.add(output);


                        } else if (binding.get("o").isLiteral()) {
                            Output output = new Output();
                            Literal object = (Literal) binding.get("o");
                            //output.setSubject(subject.getLocalName());
                            output.setPredicate(predicate.getLocalName());
                            output.setObject(object.getString());
                            output.setIndex(i);
                            System.out.println(output);
                            outputsFinal.add(output);
                        }

                        }


                } finally {
                    qexec.close();
                }
                i++;
            }
            System.out.println("\n");
            JSONArray jsonArray = new JSONArray(outputsFinal);
            String json = new Gson().toJson(outputsFinal);
            try {
                JSONArray jsonArray1 = new JSONArray(json);
                return jsonArray1;
            }
            catch (JSONException e){
                System.out.println(e.getMessage());
            }

            System.out.println(jsonArray);
            return jsonArray;
/*
            OWLNamedIndividual mick = fac.getOWLNamedIndividual(IRI.create("http://owl.man.ac.uk/2005/07/sssw/people#Mick"));

            OWLObjectProperty hasPet = fac.getOWLObjectProperty(IRI.create("http://owl.man.ac.uk/2005/07/sssw/people#has_pet"));

            NodeSet<OWLNamedIndividual> petValuesNodeSet = reasoner.getObjectPropertyValues(mick, hasPet);
            Set<OWLNamedIndividual> values = petValuesNodeSet.getFlattened();
            System.out.println("The has_pet property values for Mick are: ");
            for (OWLNamedIndividual ind : values) {
                System.out.println("    " + ind);
            }

            Node<OWLClass> topNode = reasoner.getTopClassNode();
            print(topNode, reasoner, 0);*/

        } catch (UnsupportedOperationException exception) {
            System.out.println("Unsupported reasoner operation.");
        } catch (OWLOntologyCreationException e) {
            System.out.println("Could not load the pizza ontology: " + e.getMessage());
        }catch (FileNotFoundException e){

        }
        return null;
    }
}

class Example8 {

    public static final String DOCUMENT_IRI = "http://owl.cs.manchester.ac.uk/repository/download?ontology=file:/Users/seanb/Desktop/Cercedilla2005/hands-on/people.owl&format=RDF/XML";

    public static ArrayList<Output> main(String[] args) {
        ArrayList<Output> outputs = new ArrayList<Output>();
        try {
            File f = new File("E:\\OWL\\bride_05.owl");

            FileReader r = new FileReader(f);

            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            IRI docIRI = IRI.create(DOCUMENT_IRI);
            OWLOntology ont = manager.loadOntologyFromOntologyDocument(f);
            System.out.println("Loaded " + ont.getOntologyID());


            OWLReasonerFactory reasonerFactory = new PelletReasonerFactory();
            ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();

            OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);

            OWLReasoner reasoner = reasonerFactory.createReasoner(ont, config);


            reasoner.precomputeInferences();


            boolean consistent = reasoner.isConsistent();
            System.out.println("Consistent: " + consistent);
            System.out.println("\n");


            Node<OWLClass> bottomNode = reasoner.getUnsatisfiableClasses();

            Set<OWLClass> unsatisfiable = bottomNode.getEntitiesMinusBottom();
            if (!unsatisfiable.isEmpty()) {
                System.out.println("The following classes are unsatisfiable: ");
                for (OWLClass cls : unsatisfiable) {
                    System.out.println("    " + cls);
                }
            } else {
                System.out.println("There are no unsatisfiable classes");
            }
            System.out.println("\n");

            OWLDataFactory fac = manager.getOWLDataFactory();

            OWLClass colomoPosh = fac.getOWLClass(IRI.create("http://www.semanticweb.org/aeshana/ontologies/2018/7/Bride#Excelent_colombo_bride"));


            NodeSet<OWLClass> subClses = reasoner.getSubClasses(colomoPosh, true);


            Set<OWLClass> clses = subClses.getFlattened();
            System.out.println("Subclasses of vegetarian: ");
            for (OWLClass cls : clses) {
                System.out.println("    " + cls);
            }
            System.out.println("\n");


            OWLClass country = fac.getOWLClass(IRI.create("http://www.semanticweb.org/aeshana/ontologies/2018/7/Bride#Excelent_southern_bride"));

            NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(country, true);
            Output output = new Output();

            Set<OWLNamedIndividual> individuals = individualsNodeSet.getFlattened();
            System.out.println("Instances of pet: ");
            for (OWLNamedIndividual ind : individuals) {
                System.out.println("    " + ind);
                output.setSubject("" + ind);
                outputs.add(output);

            }
            System.out.println("\n");
            OWLNamedIndividual mick = fac.getOWLNamedIndividual(IRI.create("http://owl.man.ac.uk/2005/07/sssw/people#Mick"));

            OWLObjectProperty hasPet = fac.getOWLObjectProperty(IRI.create("http://owl.man.ac.uk/2005/07/sssw/people#has_pet"));

            NodeSet<OWLNamedIndividual> petValuesNodeSet = reasoner.getObjectPropertyValues(mick, hasPet);
            Set<OWLNamedIndividual> values = petValuesNodeSet.getFlattened();
            System.out.println("The has_pet property values for Mick are: ");
            for (OWLNamedIndividual ind : values) {
                System.out.println("    " + ind);
            }

            Node<OWLClass> topNode = reasoner.getTopClassNode();
            print(topNode, reasoner, 0);


        } catch (UnsupportedOperationException exception) {
            System.out.println("Unsupported reasoner operation.");
        } catch (OWLOntologyCreationException e) {
            System.out.println("Could not load the pizza ontology: " + e.getMessage());
        } catch (FileNotFoundException e) {

        }
        return outputs;
    }


    private static void print(Node<OWLClass> parent, OWLReasoner reasoner, int depth) {
        // We don't want to print out the bottom node (containing owl:Nothing and unsatisfiable classes)
        // because this would appear as a leaf node everywhere
        if (parent.isBottomNode()) {
            return;
        }
        // Print an indent to denote parent-child relationships
        printIndent(depth);
        // Now print the node (containing the child classes)
        printNode(parent);
        for (Node<OWLClass> child : reasoner.getSubClasses(parent.getRepresentativeElement(), true)) {
            // Recurse to do the children.  Note that we don't have to worry about cycles as there
            // are non in the inferred class hierarchy graph - a cycle gets collapsed into a single
            // node since each class in the cycle is equivalent.
            print(child, reasoner, depth + 1);
        }
    }

    private static void printIndent(int depth) {
        for (int i = 0; i < depth; i++) {
            System.out.print("    ");
        }
    }

    private static DefaultPrefixManager pm = new DefaultPrefixManager("http://owl.man.ac.uk/2005/07/sssw/people#");

    private static void printNode(Node<OWLClass> node) {
        // Print out a node as a list of class names in curly brackets
        System.out.print("{");
        for (Iterator<OWLClass> it = node.getEntities().iterator(); it.hasNext(); ) {
            OWLClass cls = it.next();
            // User a prefix manager to provide a slightly nicer shorter name
            System.out.print(pm.getShortForm(cls));
            if (it.hasNext()) {
                System.out.print(" ");
            }
        }
        System.out.println("}");
    }
}
