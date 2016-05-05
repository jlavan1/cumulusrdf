package edu.kit.aifb.cumulus.cli;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.ParseException;
import org.slf4j.LoggerFactory;

import edu.kit.aifb.cumulus.cli.log.MessageCatalog;
import edu.kit.aifb.cumulus.log.Log;
import edu.kit.aifb.cumulus.store.Store;
import edu.kit.aifb.cumulus.store.sesame.CumulusRDFSail;
import edu.kit.aifb.cumulus.cli.log.MessageCatalog;
import edu.kit.aifb.cumulus.framework.Environment.ConfigValues;
import edu.kit.aifb.cumulus.log.Log;
import edu.kit.aifb.cumulus.store.CumulusStoreException;
import edu.kit.aifb.cumulus.store.QuadStore;
import edu.kit.aifb.cumulus.store.Store;
import edu.kit.aifb.cumulus.store.TripleStore;

import java.io.File;
import org.openrdf.repository.Repository;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.repository.sail.SailRepositoryConnection;
import org.openrdf.sail.nativerdf.NativeStore;
import org.openrdf.repository.RepositoryException;
import org.openrdf.query.BooleanQuery;
import org.openrdf.query.GraphQuery;
import org.openrdf.query.GraphQueryResult;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryConnection;



public class QueryDB {
	
	public static void main(final String[] args) {

		String txt = args[0];

        	System.out.println("To run query:" + txt); 

		String queryString = "";
        	switch (txt) {
			case "0":
				queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
				"prefix ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#> " +
                        	"SELECT ?x WHERE { ?x rdf:type ub:GraduateStudent . ?x ub:takesCourse <http://www.Department0.University0.edu/GraduateCourse0>}"; 
				break;
			case "1":
				queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "  PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#>"
				+ "SELECT ?x ?y ?z WHERE {?x rdf:type ub:GraduateStudent . ?y rdf:type ub:University . ?z rdf:type ub:Department ."
				+ "?x ub:memberOf ?z . ?z ub:subOrganizationOf ?y . ?x ub:undergraduateDegreeFrom ?y } ";
				break;
			case "2":
				queryString =  "  PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "  PREFIX ub:   <http://swat.cse.lehigh.edu/onto/univ-bench.owl#>"
				+ " SELECT ?x WHERE {?x rdf:type ub:Publication . ?x ub:publicationAuthor <http://www.Department0.University0.edu/AssistantProfessor0>}";
				break;
			case "3":
				queryString = "  PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "  PREFIX ub:   <http://swat.cse.lehigh.edu/onto/univ-bench.owl#>"
				+ "SELECT ?x ?y1 ?y2 ?y3  WHERE { " 
				+ "{ ?x a ub:AssociateProfessor . ?x ub:worksFor <http://www.Department0.University0.edu> . ?x ub:name ?y1 . ?x ub:emailAddress ?y2 . ?x ub:telephone ?y3 . } " 
				+ "union { ?x a ub:AssistantProfessor . ?x ub:worksFor <http://www.Department0.University0.edu> . ?x ub:name ?y1 . ?x ub:emailAddress ?y2 . ?x ub:telephone ?y3 . } "
				+ "union { ?x a ub:FullProfessor . ?x ub:worksFor <http://www.Department0.University0.edu> . ?x ub:name ?y1 . ?x ub:emailAddress ?y2 . ?x ub:telephone ?y3 . } }";
				break;
			case "4":
				queryString = "  PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "  PREFIX ub:   <http://swat.cse.lehigh.edu/onto/univ-bench.owl#>"
				+ " select distinct * where { "
				+ " { ?x a ub:AssociateProfessor . ?x ub:memberOf <http://www.Department0.University0.edu> } union "
				+ " { ?x a ub:FullProfessor . ?x ub:memberOf <http://www.Department0.University0.edu> } union "
				+ " { ?x a ub:AssistantProfessor . ?x ub:memberOf <http://www.Department0.University0.edu> } union "
				+ " { ?x a ub:Lecturer . ?x ub:memberOf <http://www.Department0.University0.edu> } union "
				+ " { ?x a ub:UndergraduateStudent . ?x ub:memberOf <http://www.Department0.University0.edu> } union "
				+ " { ?x a ub:GraduateStudent . ?x ub:memberOf <http://www.Department0.University0.edu> } union "
				+ " { ?x a ub:TeachingAssistant . ?x ub:memberOf <http://www.Department0.University0.edu> } union "
				+ " { ?x a ub:ResearchAssistant . ?x ub:memberOf <http://www.Department0.University0.edu> } union "
				+ " { ?x a ub:AssociateProfessor . ?x ub:worksFor <http://www.Department0.University0.edu> } union "
				+ " { ?x a ub:FullProfessor . ?x ub:worksFor <http://www.Department0.University0.edu> } union "
				+ " { ?x a ub:AssistantProfessor . ?x ub:worksFor <http://www.Department0.University0.edu> } union "
				+ " { ?x a ub:Lecturer . ?x ub:worksFor <http://www.Department0.University0.edu> } union "
				+ " { ?x a ub:UndergraduateStudent . ?x ub:worksFor <http://www.Department0.University0.edu> } union "
				+ " { ?x a ub:GraduateStudent . ?x ub:worksFor <http://www.Department0.University0.edu> } union "
				+ " { ?x a ub:TeachingAssistant . ?x ub:worksFor <http://www.Department0.University0.edu> } union "
				+ " { ?x a ub:ResearchAssistant . ?x ub:worksFor <http://www.Department0.University0.edu> } union "
				+ " { ?x a ub:AssociateProfessor . ?x ub:headOf <http://www.Department0.University0.edu> } union "
				+ " { ?x a ub:FullProfessor . ?x ub:headOf <http://www.Department0.University0.edu> } union "
				+ " { ?x a ub:AssistantProfessor . ?x ub:headOf <http://www.Department0.University0.edu> } union "
				+ " { ?x a ub:Lecturer . ?x ub:headOf <http://www.Department0.University0.edu> } union "
				+ " { ?x a ub:UndergraduateStudent . ?x ub:headOf <http://www.Department0.University0.edu> } union "
				+ " { ?x a ub:GraduateStudent . ?x ub:headOf <http://www.Department0.University0.edu> } union "
				+ " { ?x a ub:TeachingAssistant . ?x ub:headOf <http://www.Department0.University0.edu> } union "
				+ " { ?x a ub:ResearchAssistant . ?x ub:headOf <http://www.Department0.University0.edu> } "
				+ "   }        ";
				break;
			case "5":
				queryString = "  PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "  PREFIX ub:   <http://swat.cse.lehigh.edu/onto/univ-bench.owl#>"
				+ " SELECT distinct * WHERE { "
				+ " { ?x a ub:UndergraduateStudent . } union "
				+ " { ?x a ub:ResearchAssistant . } union "
				+ " { ?x a ub:GraduateStudent . } "
				+ "   }        ";
				break;
			case "6":
				queryString = "  PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "  PREFIX ub:   <http://swat.cse.lehigh.edu/onto/univ-bench.owl#>"
				+ " SELECT distinct * WHERE {" 
				+ " {  ?x a ub:UndergraduateStudent . ?y a ub:Course . <http://www.Department0.University0.edu/AssociateProfessor0> ub:teacherOf ?y . ?x ub:takesCourse ?y . } union "
				+ " {  ?x a ub:UndergraduateStudent . ?y a ub:GraduateCourse . <http://www.Department0.University0.edu/AssociateProfessor0> ub:teacherOf ?y . ?x ub:takesCourse ?y . } union "
				+ " {  ?x a ub:ResearchAssistant . ?y a ub:Course . <http://www.Department0.University0.edu/AssociateProfessor0> ub:teacherOf ?y . ?x ub:takesCourse ?y . } union "
				+ " {  ?x a ub:ResearchAssistant . ?y a ub:GraduateCourse . <http://www.Department0.University0.edu/AssociateProfessor0> ub:teacherOf ?y . ?x ub:takesCourse ?y . } union "
				+ " {  ?x a ub:GraduateStudent . ?y a ub:Course . <http://www.Department0.University0.edu/AssociateProfessor0> ub:teacherOf ?y . ?x ub:takesCourse ?y . } union"
				+ " {  ?x a ub:GraduateStudent . ?y a ub:GraduateCourse . <http://www.Department0.University0.edu/AssociateProfessor0> ub:teacherOf ?y . ?x ub:takesCourse ?y . } "
				+ "   }        ";
				break;
			case "7":
				queryString = "  PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "  PREFIX ub:   <http://swat.cse.lehigh.edu/onto/univ-bench.owl#>"
				+ " SELECT distinct * WHERE { " 
				+ " { ?x a ub:UndergraduateStudent . ?y a ub:Department . ?x ub:memberOf ?y . ?y ub:subOrganizationOf <http://www.University0.edu> . ?x ub:emailAddress ?z } union "
				+ " { ?x a ub:UndergraduateStudent . ?y a ub:Department . ?x ub:worksFor ?y . ?y ub:subOrganizationOf <http://www.University0.edu> . ?x ub:emailAddress ?z } union "
				+ " { ?x a ub:UndergraduateStudent . ?y a ub:Department . ?x ub:headOf ?y . ?y ub:subOrganizationOf <http://www.University0.edu> . ?x ub:emailAddress ?z } union "
				+ " { ?x a ub:ResearchAssistant . ?y a ub:Department . ?x ub:memberOf ?y . ?y ub:subOrganizationOf <http://www.University0.edu> . ?x ub:emailAddress ?z } union "
				+ " { ?x a ub:ResearchAssistant . ?y a ub:Department . ?x ub:worksFor ?y . ?y ub:subOrganizationOf <http://www.University0.edu> . ?x ub:emailAddress ?z } union "
				+ " { ?x a ub:ResearchAssistant . ?y a ub:Department . ?x ub:headOf ?y . ?y ub:subOrganizationOf <http://www.University0.edu> . ?x ub:emailAddress ?z } union "
				+ " { ?x a ub:GraduateStudent . ?y a ub:Department . ?x ub:memberOf ?y . ?y ub:subOrganizationOf <http://www.University0.edu> . ?x ub:emailAddress ?z } union "
				+ " { ?x a ub:GraduateStudent . ?y a ub:Department . ?x ub:worksFor ?y . ?y ub:subOrganizationOf <http://www.University0.edu> . ?x ub:emailAddress ?z } union "
				+ " { ?x a ub:GraduateStudent . ?y a ub:Department . ?x ub:headOf ?y . ?y ub:subOrganizationOf <http://www.University0.edu> . ?x ub:emailAddress ?z } "
				+ "   }        ";
				break;
			case "8":
				queryString = "  PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "  PREFIX ub:   <http://swat.cse.lehigh.edu/onto/univ-bench.owl#>"
				+ " SELECT distinct * WHERE { "
				+ " { ?x a ub:ResearchAssistant . ?y a ub:Lecturer . ?z a ub:Course . ?x ub:advisor ?y . ?x ub:takesCourse ?z . ?y ub:teacherOf ?z . } union "
				+ " { ?x a ub:ResearchAssistant . ?y a ub:PostDoc . ?z a ub:Course . ?x ub:advisor ?y . ?x ub:takesCourse ?z . ?y ub:teacherOf ?z . } union "
				+ " { ?x a ub:ResearchAssistant . ?y a ub:VisitingProfessor . ?z a ub:Course . ?x ub:advisor ?y . ?x ub:takesCourse ?z . ?y ub:teacherOf ?z . } union "
				+ " { ?x a ub:ResearchAssistant . ?y a ub:AssistantProfessor . ?z a ub:Course . ?x ub:advisor ?y . ?x ub:takesCourse ?z . ?y ub:teacherOf ?z . } union "
				+ " { ?x a ub:ResearchAssistant . ?y a ub:AssociateProfessor . ?z a ub:Course . ?x ub:advisor ?y . ?x ub:takesCourse ?z . ?y ub:teacherOf ?z . } union "
				+ " { ?x a ub:ResearchAssistant . ?y a ub:FullProfessor . ?z a ub:Course . ?x ub:advisor ?y . ?x ub:takesCourse ?z . ?y ub:teacherOf ?z . } union "
				+ " { ?x a ub:ResearchAssistant . ?y a ub:Lecturer . ?z a ub:GraduateCourse . ?x ub:advisor ?y . ?x ub:takesCourse ?z . ?y ub:teacherOf ?z . } union "
				+ " { ?x a ub:ResearchAssistant . ?y a ub:PostDoc . ?z a ub:GraduateCourse . ?x ub:advisor ?y . ?x ub:takesCourse ?z . ?y ub:teacherOf ?z . } union "
				+ " { ?x a ub:ResearchAssistant . ?y a ub:VisitingProfessor . ?z a ub:GraduateCourse . ?x ub:advisor ?y . ?x ub:takesCourse ?z . ?y ub:teacherOf ?z . } union "
				+ " { ?x a ub:ResearchAssistant . ?y a ub:AssistantProfessor . ?z a ub:GraduateCourse . ?x ub:advisor ?y . ?x ub:takesCourse ?z . ?y ub:teacherOf ?z . } union "
				+ " { ?x a ub:ResearchAssistant . ?y a ub:AssociateProfessor . ?z a ub:GraduateCourse . ?x ub:advisor ?y . ?x ub:takesCourse ?z . ?y ub:teacherOf ?z . } union "
				+ " { ?x a ub:ResearchAssistant . ?y a ub:FullProfessor . ?z a ub:GraduateCourse . ?x ub:advisor ?y . ?x ub:takesCourse ?z . ?y ub:teacherOf ?z . } union "
				+ " { ?x a ub:UndergraduateStudent . ?y a ub:Lecturer . ?z a ub:Course . ?x ub:advisor ?y . ?x ub:takesCourse ?z . ?y ub:teacherOf ?z . } union "
				+ " { ?x a ub:UndergraduateStudent . ?y a ub:PostDoc . ?z a ub:Course . ?x ub:advisor ?y . ?x ub:takesCourse ?z . ?y ub:teacherOf ?z . } union "
				+ " { ?x a ub:UndergraduateStudent . ?y a ub:VisitingProfessor . ?z a ub:Course . ?x ub:advisor ?y . ?x ub:takesCourse ?z . ?y ub:teacherOf ?z . } union "
				+ " { ?x a ub:UndergraduateStudent . ?y a ub:AssistantProfessor . ?z a ub:Course . ?x ub:advisor ?y . ?x ub:takesCourse ?z . ?y ub:teacherOf ?z . } union "
				+ " { ?x a ub:UndergraduateStudent . ?y a ub:AssociateProfessor . ?z a ub:Course . ?x ub:advisor ?y . ?x ub:takesCourse ?z . ?y ub:teacherOf ?z . } union "
				+ " { ?x a ub:UndergraduateStudent . ?y a ub:FullProfessor . ?z a ub:Course . ?x ub:advisor ?y . ?x ub:takesCourse ?z . ?y ub:teacherOf ?z . } union "
				+ " { ?x a ub:UndergraduateStudent . ?y a ub:Lecturer . ?z a ub:GraduateCourse . ?x ub:advisor ?y . ?x ub:takesCourse ?z . ?y ub:teacherOf ?z . } union "
				+ " { ?x a ub:UndergraduateStudent . ?y a ub:PostDoc . ?z a ub:GraduateCourse . ?x ub:advisor ?y . ?x ub:takesCourse ?z . ?y ub:teacherOf ?z . } union "
				+ " { ?x a ub:UndergraduateStudent . ?y a ub:VisitingProfessor . ?z a ub:GraduateCourse . ?x ub:advisor ?y . ?x ub:takesCourse ?z . ?y ub:teacherOf ?z . } union "
				+ " { ?x a ub:UndergraduateStudent . ?y a ub:AssistantProfessor . ?z a ub:GraduateCourse . ?x ub:advisor ?y . ?x ub:takesCourse ?z . ?y ub:teacherOf ?z . } union "
				+ " { ?x a ub:UndergraduateStudent . ?y a ub:AssociateProfessor . ?z a ub:GraduateCourse . ?x ub:advisor ?y . ?x ub:takesCourse ?z . ?y ub:teacherOf ?z . } union "
				+ " { ?x a ub:UndergraduateStudent . ?y a ub:FullProfessor . ?z a ub:GraduateCourse . ?x ub:advisor ?y . ?x ub:takesCourse ?z . ?y ub:teacherOf ?z . } union "
				+ " { ?x a ub:GraduateStudent . ?y a ub:Lecturer . ?z a ub:Course . ?x ub:advisor ?y . ?x ub:takesCourse ?z . ?y ub:teacherOf ?z . } union "
				+ " { ?x a ub:GraduateStudent . ?y a ub:PostDoc . ?z a ub:Course . ?x ub:advisor ?y . ?x ub:takesCourse ?z . ?y ub:teacherOf ?z . } union "
				+ " { ?x a ub:GraduateStudent . ?y a ub:VisitingProfessor . ?z a ub:Course . ?x ub:advisor ?y . ?x ub:takesCourse ?z . ?y ub:teacherOf ?z . } union "
				+ " { ?x a ub:GraduateStudent . ?y a ub:AssistantProfessor . ?z a ub:Course . ?x ub:advisor ?y . ?x ub:takesCourse ?z . ?y ub:teacherOf ?z . } union "
				+ " { ?x a ub:GraduateStudent . ?y a ub:AssociateProfessor . ?z a ub:Course . ?x ub:advisor ?y . ?x ub:takesCourse ?z . ?y ub:teacherOf ?z . } union "
				+ " { ?x a ub:GraduateStudent . ?y a ub:FullProfessor . ?z a ub:Course . ?x ub:advisor ?y . ?x ub:takesCourse ?z . ?y ub:teacherOf ?z . } union "
				+ " { ?x a ub:GraduateStudent . ?y a ub:Lecturer . ?z a ub:GraduateCourse . ?x ub:advisor ?y . ?x ub:takesCourse ?z . ?y ub:teacherOf ?z . } union "
				+ " { ?x a ub:GraduateStudent . ?y a ub:PostDoc . ?z a ub:GraduateCourse . ?x ub:advisor ?y . ?x ub:takesCourse ?z . ?y ub:teacherOf ?z . } union "
				+ " { ?x a ub:GraduateStudent . ?y a ub:VisitingProfessor . ?z a ub:GraduateCourse . ?x ub:advisor ?y . ?x ub:takesCourse ?z . ?y ub:teacherOf ?z . } union "
				+ " { ?x a ub:GraduateStudent . ?y a ub:AssistantProfessor . ?z a ub:GraduateCourse . ?x ub:advisor ?y . ?x ub:takesCourse ?z . ?y ub:teacherOf ?z . } union "
				+ " { ?x a ub:GraduateStudent . ?y a ub:AssociateProfessor . ?z a ub:GraduateCourse . ?x ub:advisor ?y . ?x ub:takesCourse ?z . ?y ub:teacherOf ?z . } union "
				+ " { ?x a ub:GraduateStudent . ?y a ub:FullProfessor . ?z a ub:GraduateCourse . ?x ub:advisor ?y . ?x ub:takesCourse ?z . ?y ub:teacherOf ?z . } "
				+ "   }        ";
					break;
			case "9":
				queryString = "  PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "  PREFIX ub:   <http://swat.cse.lehigh.edu/onto/univ-bench.owl#>"
				+ " SELECT * WHERE {  "
				+ " { ?x a ub:ResearchAssistant . ?x ub:takesCourse <http://www.Department0.University0.edu/GraduateCourse0> . } union "
				+ " { ?x a ub:UndergraduateStudent . ?x ub:takesCourse <http://www.Department0.University0.edu/GraduateCourse0> . } union "
				+ " { ?x a ub:GraduateStudent . ?x ub:takesCourse <http://www.Department0.University0.edu/GraduateCourse0> . } "
				+ "   }        ";
				
				break;
			case "10":
				queryString = "  PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "  PREFIX ub:   <http://swat.cse.lehigh.edu/onto/univ-bench.owl#>"
				+ " SELECT ?x WHERE { ?x rdf:type ub:ResearchGroup . ?x ub:subOrganizationOf ?y . ?y ub:subOrganizationOf <http://www.University0.edu> "
				+ "   }        ";
				break;
			case "11":
				queryString = "  PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "  PREFIX ub:   <http://swat.cse.lehigh.edu/onto/univ-bench.owl#>"
				+ " SELECT * WHERE { "
				+ " { ?x a ub:FullProfessor . ?y a ub:Department . ?x ub:headOf ?y . ?y ub:subOrganizationOf <http://www.University0.edu> . } union "
				+ " { ?x a ub:AssistantProfessor . ?y a ub:Department . ?x ub:headOf ?y . ?y ub:subOrganizationOf <http://www.University0.edu> . } union "
				+ " { ?x a ub:AssociateProfessor . ?y a ub:Department . ?x ub:headOf ?y . ?y ub:subOrganizationOf <http://www.University0.edu> . } "
				+ "   }        ";
				break;
			case "12":
				queryString = "  PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "  PREFIX ub:   <http://swat.cse.lehigh.edu/onto/univ-bench.owl#>"
				+ " SELECT * WHERE { " 
				+ " { ?x a ub:AssociateProfessor . ?x ub:doctoralDegreeFrom <http://www.University0.edu> . } union "
				+ " { ?x a ub:FullProfessor . ?x ub:doctoralDegreeFrom <http://www.University0.edu> . } union "
				+ " { ?x a ub:AssistantProfessor . ?x ub:doctoralDegreeFrom <http://www.University0.edu> . } union "
				+ " { ?x a ub:Lecturer . ?x ub:doctoralDegreeFrom <http://www.University0.edu> . } union "
				+ " { ?x a ub:UndergraduateStudent . ?x ub:doctoralDegreeFrom <http://www.University0.edu> . } union "
				+ " { ?x a ub:GraduateStudent . ?x ub:doctoralDegreeFrom <http://www.University0.edu> . } union "
				+ " { ?x a ub:TeachingAssistant . ?x ub:doctoralDegreeFrom <http://www.University0.edu> . } union "
				+ " { ?x a ub:ResearchAssistant . ?x ub:doctoralDegreeFrom <http://www.University0.edu> . } union "
				+ " { ?x a ub:AssociateProfessor . ?x ub:mastersDegreeFrom <http://www.University0.edu> . } union "
				+ " { ?x a ub:FullProfessor . ?x ub:mastersDegreeFrom <http://www.University0.edu> . } union "
				+ " { ?x a ub:AssistantProfessor . ?x ub:mastersDegreeFrom <http://www.University0.edu> . } union "
				+ " { ?x a ub:Lecturer . ?x ub:mastersDegreeFrom <http://www.University0.edu> . } union "
				+ " { ?x a ub:UndergraduateStudent . ?x ub:mastersDegreeFrom <http://www.University0.edu> . } union "
				+ " { ?x a ub:GraduateStudent . ?x ub:mastersDegreeFrom <http://www.University0.edu> . } union "
				+ " { ?x a ub:TeachingAssistant . ?x ub:mastersDegreeFrom <http://www.University0.edu> . } union "
				+ " { ?x a ub:ResearchAssistant . ?x ub:mastersDegreeFrom <http://www.University0.edu> . } union "
				+ " { ?x a ub:AssociateProfessor . ?x ub:undergraduateDegreeFrom <http://www.University0.edu> . } union "
				+ " { ?x a ub:FullProfessor . ?x ub:undergraduateDegreeFrom <http://www.University0.edu> . } union "
				+ " { ?x a ub:AssistantProfessor . ?x ub:undergraduateDegreeFrom <http://www.University0.edu> . } union "
				+ " { ?x a ub:Lecturer . ?x ub:undergraduateDegreeFrom <http://www.University0.edu> . } union "
				+ " { ?x a ub:UndergraduateStudent . ?x ub:undergraduateDegreeFrom <http://www.University0.edu> . } union "
				+ " { ?x a ub:GraduateStudent . ?x ub:undergraduateDegreeFrom <http://www.University0.edu> . } union "
				+ " { ?x a ub:TeachingAssistant . ?x ub:undergraduateDegreeFrom <http://www.University0.edu> . } union "
				+ " { ?x a ub:ResearchAssistant . ?x ub:undergraduateDegreeFrom <http://www.University0.edu> . } "
				+ "   }        ";
				break;
			case "13":
				queryString = "  PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "  PREFIX ub:   <http://swat.cse.lehigh.edu/onto/univ-bench.owl#>"
				+ " SELECT ?x WHERE { ?x rdf:type ub:UndergraduateStudent } ";
				break;

		}


		Store store = new TripleStore();
		try {
			store.open();
		} catch (final CumulusStoreException exception) {
			exception.printStackTrace();
		} finally {
			try {
				store.close();
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}

		SailRepositoryConnection con = null;
		SailRepository repo = null;

		try {
			final CumulusRDFSail sail = new CumulusRDFSail(store);
			sail.initialize(); //yzyan, remove for java.lang.IllegalStateException: sail has already been intialized
			repo = new SailRepository(sail);
			con = repo.getConnection();

			org.openrdf.query.Query parsed_query = con.prepareQuery(QueryLanguage.SPARQL, queryString);

			int i = 0;
			if (parsed_query instanceof TupleQuery) {
				for (final TupleQueryResult result = ((TupleQuery) parsed_query).evaluate(); result.hasNext(); i++) {
					System.out.println(i + ": " + result.next());
				}
			} 

			try { con.close(); } catch (Exception ignore) {};
                        try { repo.shutDown(); } catch (Exception ignore) {};

		}
		catch (Exception e) {
   			//Something went wrong during the transaction, so we roll it back
   			//con.rollback();
   			e.printStackTrace(); 
   	    	}
		
		
	}
}
