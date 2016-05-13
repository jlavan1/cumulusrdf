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



public class QueryMatrix {
	
	static String lubmPrefix = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                            "prefix ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#> ";
	static String shoeboxPrefix = "http://localhost/shoebox";
	
	static String lubmQ1 = "?x rdf:type ub:GraduateStudent . ?x ub:takesCourse <http://www.Department0.University0.edu/GraduateCourse0>";
	static String lubmQ2 = "?x rdf:type ub:GraduateStudent . ?y rdf:type ub:University . ?z rdf:type ub:Department ."
				+ "?x ub:memberOf ?z . ?z ub:subOrganizationOf ?y . ?x ub:undergraduateDegreeFrom ?y";
	static String lubmQ3 = "?x rdf:type ub:Publication . ?x ub:publicationAuthor <http://www.Department0.University0.edu/AssistantProfessor0>";

	static String lubmQ4 = "{?x a ub:AssociateProfessor . ?x ub:worksFor <http://www.Department0.University0.edu> . ?x ub:name ?y1 . ?x ub:emailAddress ?y2 . ?x ub:telephone ?y3 . } " 
				+ "union { ?x a ub:AssistantProfessor . ?x ub:worksFor <http://www.Department0.University0.edu> . ?x ub:name ?y1 . ?x ub:emailAddress ?y2 . ?x ub:telephone ?y3 . } "
				+ "union { ?x a ub:FullProfessor . ?x ub:worksFor <http://www.Department0.University0.edu> . ?x ub:name ?y1 . ?x ub:emailAddress ?y2 . ?x ub:telephone ?y3 . } ";

	static String lubmQ5 = " { ?x a ub:AssociateProfessor . ?x ub:memberOf <http://www.Department0.University0.edu> } union "
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
				+ " { ?x a ub:ResearchAssistant . ?x ub:headOf <http://www.Department0.University0.edu> } ";

	static String lubmQ6 = " { ?x a ub:UndergraduateStudent . } union "
				+ " { ?x a ub:ResearchAssistant . } union "
				+ " { ?x a ub:GraduateStudent . } ";

	static String lubmQ7 = " {  ?x a ub:UndergraduateStudent . ?y a ub:Course . <http://www.Department0.University0.edu/AssociateProfessor0> ub:teacherOf ?y . ?x ub:takesCourse ?y . } union "
				+ " {  ?x a ub:UndergraduateStudent . ?y a ub:GraduateCourse . <http://www.Department0.University0.edu/AssociateProfessor0> ub:teacherOf ?y . ?x ub:takesCourse ?y . } union "
				+ " {  ?x a ub:ResearchAssistant . ?y a ub:Course . <http://www.Department0.University0.edu/AssociateProfessor0> ub:teacherOf ?y . ?x ub:takesCourse ?y . } union "
				+ " {  ?x a ub:ResearchAssistant . ?y a ub:GraduateCourse . <http://www.Department0.University0.edu/AssociateProfessor0> ub:teacherOf ?y . ?x ub:takesCourse ?y . } union "
				+ " {  ?x a ub:GraduateStudent . ?y a ub:Course . <http://www.Department0.University0.edu/AssociateProfessor0> ub:teacherOf ?y . ?x ub:takesCourse ?y . } union"
				+ " {  ?x a ub:GraduateStudent . ?y a ub:GraduateCourse . <http://www.Department0.University0.edu/AssociateProfessor0> ub:teacherOf ?y . ?x ub:takesCourse ?y . } ";

	static String lubmQ8 = " { ?x a ub:UndergraduateStudent . ?y a ub:Department . ?x ub:memberOf ?y . ?y ub:subOrganizationOf <http://www.University0.edu> . ?x ub:emailAddress ?z } union "
				+ " { ?x a ub:UndergraduateStudent . ?y a ub:Department . ?x ub:worksFor ?y . ?y ub:subOrganizationOf <http://www.University0.edu> . ?x ub:emailAddress ?z } union "
				+ " { ?x a ub:UndergraduateStudent . ?y a ub:Department . ?x ub:headOf ?y . ?y ub:subOrganizationOf <http://www.University0.edu> . ?x ub:emailAddress ?z } union "
				+ " { ?x a ub:ResearchAssistant . ?y a ub:Department . ?x ub:memberOf ?y . ?y ub:subOrganizationOf <http://www.University0.edu> . ?x ub:emailAddress ?z } union "
				+ " { ?x a ub:ResearchAssistant . ?y a ub:Department . ?x ub:worksFor ?y . ?y ub:subOrganizationOf <http://www.University0.edu> . ?x ub:emailAddress ?z } union "
				+ " { ?x a ub:ResearchAssistant . ?y a ub:Department . ?x ub:headOf ?y . ?y ub:subOrganizationOf <http://www.University0.edu> . ?x ub:emailAddress ?z } union "
				+ " { ?x a ub:GraduateStudent . ?y a ub:Department . ?x ub:memberOf ?y . ?y ub:subOrganizationOf <http://www.University0.edu> . ?x ub:emailAddress ?z } union "
				+ " { ?x a ub:GraduateStudent . ?y a ub:Department . ?x ub:worksFor ?y . ?y ub:subOrganizationOf <http://www.University0.edu> . ?x ub:emailAddress ?z } union "
				+ " { ?x a ub:GraduateStudent . ?y a ub:Department . ?x ub:headOf ?y . ?y ub:subOrganizationOf <http://www.University0.edu> . ?x ub:emailAddress ?z } ";

	static String lubmQ9 = " { ?x a ub:ResearchAssistant . ?y a ub:Lecturer . ?z a ub:Course . ?x ub:advisor ?y . ?x ub:takesCourse ?z . ?y ub:teacherOf ?z . } union "
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
				+ " { ?x a ub:GraduateStudent . ?y a ub:FullProfessor . ?z a ub:GraduateCourse . ?x ub:advisor ?y . ?x ub:takesCourse ?z . ?y ub:teacherOf ?z . } ";

	static String lubmQ10 = " { ?x a ub:ResearchAssistant . ?x ub:takesCourse <http://www.Department0.University0.edu/GraduateCourse0> . } union "
				+ " { ?x a ub:UndergraduateStudent . ?x ub:takesCourse <http://www.Department0.University0.edu/GraduateCourse0> . } union "
				+ " { ?x a ub:GraduateStudent . ?x ub:takesCourse <http://www.Department0.University0.edu/GraduateCourse0> . } ";

	static String lubmQ11 =  "?x rdf:type ub:ResearchGroup . ?x ub:subOrganizationOf ?y . ?y ub:subOrganizationOf <http://www.University0.edu> ";

	static String lubmQ12 = " { ?x a ub:FullProfessor . ?y a ub:Department . ?x ub:headOf ?y . ?y ub:subOrganizationOf <http://www.University0.edu> . } union "
				+ " { ?x a ub:AssistantProfessor . ?y a ub:Department . ?x ub:headOf ?y . ?y ub:subOrganizationOf <http://www.University0.edu> . } union "
				+ " { ?x a ub:AssociateProfessor . ?y a ub:Department . ?x ub:headOf ?y . ?y ub:subOrganizationOf <http://www.University0.edu> . } "; 


	static String lubmQ13 = " { ?x a ub:AssociateProfessor . ?x ub:doctoralDegreeFrom <http://www.University0.edu> . } union "
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
				+ " { ?x a ub:ResearchAssistant . ?x ub:undergraduateDegreeFrom <http://www.University0.edu> . } ";

	static String lubmQ14 = "?x rdf:type ub:UndergraduateStudent";

	static String[] allLUBMQueries = {lubmQ1,lubmQ2,lubmQ3,lubmQ4,lubmQ5,lubmQ6,lubmQ7,lubmQ8,lubmQ9,lubmQ10,lubmQ11,lubmQ12,lubmQ13,lubmQ14};


        static String generateQuery(int idxA, int idxB) {
		String ret =  lubmPrefix +
                                "SELECT ?s ?p ?o " +
                                "FROM  <" + 
				shoeboxPrefix + idxA + idxB +
                                "> WHERE { ?s ?p ?o }";
		System.out.println(ret);
		return ret;
	}

	
	public static void main(final String[] args) {

		//Log _log = new Log(LoggerFactory.getLogger(Cirrus.class));

		/*
		if (args.length < 1) {
			System.out.println("Please pass args");;
			System.exit(1);
		}
		*/

		int row = Integer.parseInt(args[0]);
		int col = Integer.parseInt(args[1]);

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

			String graph_query = generateQuery(row,col);

			org.openrdf.query.Query parsed_query = con.prepareQuery(QueryLanguage.SPARQL, graph_query);

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
