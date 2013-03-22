/**
 * 
 * Copyright Mikel Egana Aranguren 
 * The RENDERER.java software is free software and is licensed under the terms of the 
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
package gong.manchester.ac.uk.owl2latex.render;

import java.io.BufferedWriter;
import java.io.IOException;

import org.semanticweb.owl.model.OWLAnnotation;


public class RENDERER {
	private BufferedWriter odp;
	private String RendererName;
	public RENDERER(BufferedWriter odp, String RendererName) {
		this.odp = odp;
		this.RendererName=RendererName;
	}
	public String Render (OWLAnnotation annot, String odp_name) throws IOException{
		String annotValue = (annot.getAnnotationValue().toString().split("@en"))[0];
		String annotProp = annot.getAnnotationURI().getFragment().toString();
		if(annotProp.equals("name")){
			odp.write(" ");
			odp.write("\\section{" + annotValue + " ODP}");
			odp.write("\\begin{description}");
			odp_name = annotValue;
		}
		else if(annotProp.equals("structure")){
			odp.newLine();
			String img = (annotValue.split(".png")[0]).replaceFirst("../img/", "");
			odp.write("\\item [" + annotProp.toUpperCase() + ":] See Figure \\ref{odp:"+ img +"}.");
			odp.newLine();
			odp.write("\\begin{figure}[]\\centering\\includegraphics[width=\\textwidth]{Catalogue/"+ img +"}" +
					"\\caption{\\label{odp:"+ img +"} Abstract structure of the " + odp_name + " ODP.}"+
					"\\end{figure}");
			odp.newLine();
		}	
		else if(annotProp.equals("sample")){
			odp.newLine();
			String img = (annotValue.split(".png")[0]).replaceFirst("../img/", "");
			odp.write("\\item [" + annotProp.toUpperCase() + ":] See Figure \\ref{odp:"+ img +"}.");
			odp.newLine();
			odp.write("\\begin{figure}[]\\centering\\includegraphics[width=\\textwidth]{Catalogue/"+ img +"}" +
					"\\caption{\\label{odp:"+ img +"} Sample structure of the " + odp_name + " ODP.}"+
					"\\end{figure}");
			odp.newLine();
		}
		else{
			odp.newLine();
			odp.write("\\item [" + annotProp.toUpperCase().replaceAll("_", " ") +":] " + annotValue.replace("_", "\\_") + ".");
			odp.newLine();
		}
		return odp_name;
	}
	public String getRendererName (){
		return this.RendererName;
	}
}
