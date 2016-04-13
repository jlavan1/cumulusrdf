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

import java.io.File;
import org.openrdf.repository.Repository;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.nativerdf.NativeStore;
import org.openrdf.repository.RepositoryException;
import org.openrdf.query.BooleanQuery;
import org.openrdf.query.GraphQuery;
import org.openrdf.query.GraphQueryResult;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryConnection;

public class Benchmark {
	

	public static void main(final String[] args) {

		Log _log = new Log(LoggerFactory.getLogger(Cirrus.class));
		/*
		if (args.length < 1) {
			System.out.println("Please pass args");;
			System.exit(1);
		}
		*/
		try {
			File dataDir = new File("/home/yzyan/.aduna/openrdf-sesame-console/repositories/yzyan28/");
			String indexes = "spoc,posc,cosp";
			Repository repo = new SailRepository(new NativeStore(dataDir, indexes));
			repo.initialize();
			String query = "select ?s where {?s ?p ?o}";
			RepositoryConnection con = repo.getConnection();
			org.openrdf.query.Query parsed_query = con.prepareQuery(QueryLanguage.SPARQL, query);
			int i = 0;
			if (parsed_query instanceof BooleanQuery) {
				_log.info(MessageCatalog._00019_PARSED_ASK_QUERY, parsed_query);
				_log.info(MessageCatalog._00020_PARSED_ASK_ANSWER, ((BooleanQuery) parsed_query).evaluate());
			} else if (parsed_query instanceof TupleQuery) {
				_log.info(MessageCatalog._00021_PARSED_SELECT_ANSWER);
				for (final TupleQueryResult result = ((TupleQuery) parsed_query).evaluate(); result.hasNext(); i++) {
					System.out.println(i + ": " + result.next());
				}
			} else if (parsed_query instanceof GraphQuery) {
				_log.info(MessageCatalog._00022_CONSTRUCT_ASK_QUERY, parsed_query);
				_log.info(MessageCatalog._00023_CONSTRUCT_ASK_ANSWER);
				for (final GraphQueryResult result = ((GraphQuery) parsed_query).evaluate(); result.hasNext(); i++) {
					_log.info(i + ": " + result.next());
				}
			}	
		}
		catch (Exception e) {
   			//Something went wrong during the transaction, so we roll it back
   			//con.rollback();
   			e.printStackTrace(); 
   	    	}
	}
}
