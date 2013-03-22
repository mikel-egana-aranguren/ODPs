/**
 * 
 * Copyright Mikel Egana Aranguren 
 * The OWL2LATEX.java software is free software and is licensed under the terms of the 
 * GNU General Public License (GPL) as published by the Free Software Foundation; 
 * either version 2 of the License, or (at your option) any later version. The Core.java 
 * software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 * See the GPL for more details; a copy of the GPL is included with this product. 
 * 
 * For more info:
 * mikel.eganaaranguren@cs.manchester.ac.uk
 * http://www.gong.manchester.ac.uk
 * 
 */
package gong.manchester.ac.uk.owl2latex;

import gong.manchester.ac.uk.owl2latex.render.RENDERER;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.OWLAnnotation;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;


public class OWL2LATEX {

	public static void main(String[] args) {
	
		try {									
			// Load the dirs
			ArrayList dirs = new ArrayList ();
			dirs.add("../owl/Extension_ODP");
			dirs.add("../owl/Good_Practice_ODP");
			dirs.add("../owl/Domain_Modelling_ODP");
			
			//dirs.add("/home/pik/Bioinformatics/PatternsCatalog/PatternsInTheWeb_OWL/odp/owl/Extension_ODP/");
			//dirs.add("/home/pik/Bioinformatics/PatternsCatalog/PatternsInTheWeb_OWL/odp/owl/Good_Practice_ODP/");
			//dirs.add("/home/pik/Bioinformatics/PatternsCatalog/PatternsInTheWeb_OWL/odp/owl/Domain_Modelling_ODP/");
			
			// For each ODP in each directory, create a latex document
			Iterator dirsIterator = dirs.iterator();
			while (dirsIterator.hasNext()){
				File dir = new File ((String)dirsIterator.next());
				File[] files =  dir.listFiles();
				for (int i = 0; i < files.length; i++) {
					File eachfile = files[i];
					owl2latex(eachfile);
				}
			}

		} 
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	public static void owl2latex (File file) throws IOException{
		System.out.println("-->" + file);
		String filename = file.getName();
		String filePath = file.getAbsolutePath();
		String filePathLink = (String) filePath.subSequence(0, filePath.length()-4);
		BufferedWriter odp = new BufferedWriter(new FileWriter("../latex/" + filename.replaceFirst(".owl", "") + ".tex"));
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		try{
			URI physicalURI = URI.create("file:" + file.getAbsolutePath());
//			odp.write("\\subsection{" + (filename.replace("_", "\\_")).replaceFirst(".owl", "") + "}");
			OWLOntology ontology = manager.loadOntologyFromPhysicalURI(physicalURI);

			ArrayList renders = new ArrayList();
			renders.add(new RENDERER(odp,"NAME"));
			renders.add(new RENDERER(odp,"ALSO_KNOWN_AS"));
			renders.add(new RENDERER(odp,"CLASSIFICATION"));
			renders.add(new RENDERER(odp,"MOTIVATION"));
			renders.add(new RENDERER(odp,"AIM"));
			renders.add(new RENDERER(odp,"STRUCTURE"));
			renders.add(new RENDERER(odp,"SAMPLE"));
			renders.add(new RENDERER(odp,"ELEMENTS"));
			renders.add(new RENDERER(odp,"IMPLEMENTATION"));
			renders.add(new RENDERER(odp,"RESULT"));
			renders.add(new RENDERER(odp,"SIDE_EFFECTS"));
			renders.add(new RENDERER(odp,"KNOWN_USES"));
			renders.add(new RENDERER(odp,"RELATED_ODPS"));
			renders.add(new RENDERER(odp,"ADDITIONAL_INFORMATION"));
			
			Iterator rendersIterator = renders.iterator();
			for(OWLClass cls : ontology.getReferencedClasses()) {
				String className = cls.toString();
				if(className.endsWith("Domain")){
					String odp_name = null;
					while(rendersIterator.hasNext()){
						RENDERER renderer = (RENDERER)rendersIterator.next();
						String rendererName = renderer.getRendererName();
						Set<OWLAnnotation> annots = cls.getAnnotations(ontology);
						Iterator annotsIterator = annots.iterator();
						while(annotsIterator.hasNext()){
							OWLAnnotation annot = (OWLAnnotation)annotsIterator.next();
							String annotPropName = annot.getAnnotationURI().getFragment().toString().toUpperCase();
							if(annotPropName.equals(rendererName)){
								odp_name = renderer.Render(annot,odp_name);
							}
							
						}
					}
					odp.newLine();
					odp.write("\\item [REFERENCES: ] ~" + "\\begin{itemize}");
					Set<OWLAnnotation> annots = cls.getAnnotations(ontology);
					Iterator annotsIterator = annots.iterator();
					while(annotsIterator.hasNext()){
						OWLAnnotation annot = (OWLAnnotation)annotsIterator.next();
						String annotPropName = annot.getAnnotationURI().getFragment().toString();
						if(annotPropName.equals("reference")){
							String annotValue = (annot.getAnnotationValue().toString().split("@en"))[0];
							if(annotValue.startsWith("http")){
								odp.newLine();
								odp.write("\\item \\url{" + annotValue + "}");
							}
							else{
								odp.newLine();
								odp.write("\\item " + annotValue + ".");
							}
						}
					}
					odp.write("\\end{itemize}");
				}
			}
			odp.newLine();
			odp.write("\\item [URL: ] \\url{" + ontology.getURI() + "}");
			odp.write(" ");
			odp.write("\\end{description}");

		} 
		catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}

		odp.close();
	}
}
