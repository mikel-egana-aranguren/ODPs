/**
 * 
 * Copyright Mikel Egana Aranguren 
 * The OWL2HTML.java software is free software and is licensed under the terms of the 
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
package gong.manchester.ac.uk.owl2html;

import gong.manchester.ac.uk.owl2html.render.RENDERER;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.OWLAnnotation;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;

import uk.ac.manchester.cs.owl.OWLClassImpl;
import uk.ac.manchester.cs.owl.OWLObjectSomeRestrictionImpl;



public class OWL2HTML {

	public static void main(String[] args) {
	
		try {						
			// Frontpage
			BufferedWriter out = new BufferedWriter(new FileWriter("../html/index.html"));

			
			out.write("<p><div align=\"center\"><h2>ONTOLOGY DESIGN PATTERNS (ODPs) PUBLIC CATALOG</h2></div></p>");
			writeHeader(out);
			writeFooter (out,true);
			out.write("<hr>");

			out.write(
					"<table cellpadding=\"6\" cellspacing=\"0\" border=\"0\" valign=\"top\"><tr><td>"+
					"<h4>INTRO</h4> " +
					"<p>ODPs are ready made modelling solutions for creating and maintaining ontologies; " +
					"they help in creating rich and rigorous ontologies with less effort. " +
					"This is a public catalog of ODPs focused on the biological knowledge domain. " +
					"ODPs in this catalog have been " +
					"collected elsewhere or created \"in house\" and they are open for discussion. " +
					"ODPs can be applied in ontologies using OPPL (<a href=\"http://oppl.sourceforge.net/\">" +
					"Ontology PreProcessor Language</a>), the wizards provided by the " +
					"<a href=\"http://www.co-ode.org\">CO-ODE</a> project, or simply by hand.</p>" +
					"</td><td>" +
					"<h4>BROWSE</h4> " +
					"<p>To browse the ODPs simply click on their names above.</p>  " +
					"<h4>CONTRIBUTE</h4> " +
					"To discuss the existing ODPs or send new ones please refer to the " +
					"<a href=\"http://sourceforge.net/projects/odps/\">sourceforge project site</a>. " +
					"</td></tr><tr><td>" +
					"<h4>TO KNOW MORE</h4>" +
					"<p>Mikel Ega&ntilde;a Aranguren, Erick Antezana, Martin Kuiper, Robert Stevens. " +
					"Ontology Design Patterns for bio-ontologies: a case study on the Cell Cycle Ontology. " +
					"BMC bioinformatics 2008, 9(Suppl 5):S1. " +
					"[<a href=\"http://www.biomedcentral.com/1471-2105/9/S5/S1\">BMC Bioinformatics</a>].</p> " +
					"<p>Mikel Ega&ntilde;a, Alan Rector, Robert Stevens, Erick Antezana. " +
					"Applying Ontology Design Patterns in bio-ontologies. EKAW 2008, LNCS 5268, pp. 7-16." +
					" [<a href=\"http://www.springerlink.com/content/d2lp476v0p281q73/?p=f9d5500ce8b24589b2baf5eef213b0f5&pi=3\">LNCS</a>]</p>" +
					"</td><td>" +
					"<h4>EXTEND</h4> " +
					"<p>This catalog is generated from OWL files (each OWL file describes and ODP, providing semantics" +
					" and annotations altogether for easy sharing). The whole catalog can be downloaded from the " +
					"<a href=\"http://sourceforge.net/projects/odps/\">sourceforge project site</a>" +
					" and, if extended, " +
					"generated again, obtaining a HTML and LaTeX version (software is provided in the bundle).</p>" +
					"</td></tr></table>" +
					"<hr>" + 
					"<div align=\"center\"><p>This instance of the catalog was generated on: " + (new Date()).toGMTString() + ".</p>" 	
					);
			out.write("<p><a href=\"http://sourceforge.net\"><img " +
					"src=\"http://sflogo.sourceforge.net/sflogo.php?group_id=90989&amp;type=1\" " +
					"width=\"88\" height=\"31\" border=\"0\" alt=\"SourceForge.net Logo\" ALIGN=RIGHT/></a></p></div>");
			
			out.close();
			
			// ODP pages
			
			// Load the dirs
			ArrayList dirs = new ArrayList ();
			dirs.add("../owl/Extension_ODP");
			dirs.add("../owl/Good_Practice_ODP");
			dirs.add("../owl/Domain_Modelling_ODP");
			
			// For each ODP in each dir, create a page
			Iterator dirsIterator = dirs.iterator();
			while (dirsIterator.hasNext()){
				File dir = new File ((String)dirsIterator.next());
				File[] files =  dir.listFiles();
				for (int i = 0; i < files.length; i++) {
					File eachfile = files[i];
					owl2html(eachfile);
				}
			}

		} 
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	public static void owl2html (File file) throws IOException{
		String filename = file.getName();
		String filePath = file.getAbsolutePath();
		String filePathLink = (String) filePath.subSequence(0, filePath.length()-4);
		BufferedWriter odp = new BufferedWriter(new FileWriter("../html/" + filename.replaceFirst(".owl", "") + ".html"));
		
		try{
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			URI physicalURI = URI.create("file:" + file.getAbsolutePath());
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
					while(rendersIterator.hasNext()){
						RENDERER renderer = (RENDERER)rendersIterator.next();
						String rendererName = renderer.getRendererName();
						Set<OWLAnnotation> annots = cls.getAnnotations(ontology);
						Iterator annotsIterator = annots.iterator();
						while(annotsIterator.hasNext()){
							
							OWLAnnotation annot = (OWLAnnotation)annotsIterator.next();
							String annotPropName = annot.getAnnotationURI().getFragment().toString().toUpperCase();

							if(annotPropName.equals(rendererName)){
								renderer.Render(annot);
							}
							
						}
					}
					odp.write("<p><b>REFERENCES</b>: " + "<ul>");
					Set<OWLAnnotation> annots = cls.getAnnotations(ontology);
					Iterator annotsIterator = annots.iterator();
					while(annotsIterator.hasNext()){
						OWLAnnotation annot = (OWLAnnotation)annotsIterator.next();
						String annotPropName = annot.getAnnotationURI().getFragment().toString();
						if(annotPropName.equals("reference")){
							String annotValue = (annot.getAnnotationValue().toString().split("@en"))[0];
							if(annotValue.startsWith("http")){
								odp.write("<li><a href=\"" + annotValue + "\">"+ annotValue +"</a></li>");
							}
							else{
								odp.write("<li>" + annotValue + ".</li>");
							}
						}
					}
					odp.write("</ul></p>");
				}
			}
			odp.write("<p><b>URL</b>: <a href=\"" + ontology.getURI() + "\">" + ontology.getURI() + "</a></p>");
		}
        catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
		writeFooter (odp,false);
		odp.close();
	}
	
	public static void writeHeader (BufferedWriter out) throws IOException{
		out.write(
				"<html><head><title>Ontology Design Patterns</title>" +
				"<meta name=\"author\" content=\"Mikel Egaña Aranguren\">" +
				"<meta name=\"keywords\" content=\"Ontology Design Patterns\">" +
				"<meta name=\"description\" content=\"Public catalogue of Ontology Design Patterns\">" +
				"</head><body>"
				);
	}
	public static void writeFooter (BufferedWriter out,boolean frontpage) throws IOException{
		// Footer
		out.write("<hr>");
		
		// Extensional ODPs
		File dirExtensional = new File ("../owl/Extension_ODP");
		out.write("<b>Extension ODPs (by-pass the limitations of OWL):</b>");
		writeODPLinks(dirExtensional,out);
		out.write("<br/>");
		
		// Good Practice ODPs
		File dirGood = new File ("../owl/Good_Practice_ODP");
		out.write("<b>Good Practice ODPs (obtain a more robust, cleaner and easier to maintain ontology):</b>");
		writeODPLinks(dirGood,out);
		out.write("<br/>");
		
		// Domain modelling ODPs
		File dirDomain = new File ("../owl/Domain_Modelling_ODP");
		out.write("<b>Domain Modelling ODPs (solutions for concrete modelling problems in biology):</b>");
		writeODPLinks(dirDomain,out);
		
		if(frontpage == false){
			out.write("<hr>");
			out.write("<a href=\"index.html\">ODPs public catalog</a>");
			out.write("<hr>");
		}
		out.write("</body></html>");
	}
	public static void writeODPLinks (File dir, BufferedWriter out) throws IOException{
		File[] files =  dir.listFiles();
		int allfiles = files.length;
		for (int i = 0; i < allfiles; i++) {
			File file = files[i];
			String filename = file.getName();
			String shortfilename = filename.split(".owl")[0];
			String filePath = file.getAbsolutePath();
			String filePathLink = (String) filePath.subSequence(0, filePath.length()-4);
			out.write( " <a href=\"" + filename.replaceFirst(".owl", "") + ".html" + "\">" + shortfilename + "</a>");
			if(i == (allfiles-1)){
				out.write(".");
			}
			else{
				out.write(",");
			}
		}
	}
}
