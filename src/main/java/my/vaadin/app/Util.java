package my.vaadin.app;

import java.io.File;
import java.util.ArrayList;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.ToStringRenderer;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import it.unibz.krdb.obda.model.OBDAModel;
import it.unibz.krdb.obda.owlrefplatform.core.QuestConstants;
import it.unibz.krdb.obda.owlrefplatform.core.QuestPreferences;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.MappingLoader;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWL;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLConfiguration;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLConnection;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLFactory;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLResultSet;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLStatement;
import java.util.*;

public class Util {
    /*
     * Use the sample database using H2 from
     * https://github.com/ontop/ontop/wiki/InstallingTutorialDatabases
     *
     * Please use the pre-bundled H2 server from the above link
     *
     */
	static final String owlfile = "/Users/isabellyr/Documents/workspace/app/src/main/webapp/recipes.owl";
	static final String obdafile = "/Users/isabellyr/Documents/workspace/app/src/main/webapp/recipes.obda";

    public static List<List<String>> runQuery(String sparqlQuery) throws Exception {
    	List<List<String>> result = new ArrayList<List<String>>();
		/*
         * Load the ontology from an external .owl file.
		 */
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new File(owlfile));

		/*
		 * Load the OBDA model from an external .obda file
		 */
        final OBDAModel obdaModel = new MappingLoader().loadFromOBDAFile(obdafile);

		/*
		 * Prepare the configuration for the Quest instance. The example below shows the setup for
		 * "Virtual ABox" mode
		 */
        QuestPreferences preference = new QuestPreferences();
        preference.setCurrentValueOf(QuestPreferences.ABOX_MODE, QuestConstants.VIRTUAL);

		/*
		 * Create the instance of Quest OWL reasoner.
		 */
        QuestOWLConfiguration config = QuestOWLConfiguration.builder().preferences(preference).obdaModel(obdaModel).build();
        QuestOWLFactory factory = new QuestOWLFactory();
        QuestOWL reasoner = factory.createReasoner(ontology, config);

		/*
		 * Prepare the data connection for querying.
		 */
        QuestOWLConnection conn = reasoner.getConnection();
        QuestOWLStatement st = conn.createStatement();

        try {
            QuestOWLResultSet rs = st.executeTuple(sparqlQuery);
            int columnSize = rs.getColumnCount();
            final ToStringRenderer renderer = ToStringRenderer.getInstance();
            while (rs.nextRow()) {
            	List<String> info = new ArrayList<String>();
                for (int idx = 1; idx <= columnSize; idx++) {
                    OWLObject binding = rs.getOWLObject(idx);
                    String i = renderer.getRendering(binding);
                    i = i.contains("^")? i.substring(1, i.indexOf("^")-1):i;
                    info.add(i);
                }
                result.add(info);
            }
            rs.close();
        } finally {
            st.close();
            conn.close();
            reasoner.dispose();
        }
        return result;
    }
}