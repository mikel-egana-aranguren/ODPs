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
package gong.manchester.ac.uk.owl2html.render;

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
	public void Render (OWLAnnotation annot) throws IOException{
		String annotValue = (annot.getAnnotationValue().toString().split("@en"))[0];
		String annotProp = annot.getAnnotationURI().getFragment().toString();
		if(annotProp.equals("structure") || annotProp.equals("sample")){
			odp.write("<p><b>" + annotProp.toUpperCase() +":</b></p><img src=\""+ annotValue +"\">");
		}
		else{
			odp.write("<p><b>" + annotProp.toUpperCase().replaceAll("_", " ") +":</b> " + annotValue + ".</p>");
		}
	}
	public String getRendererName (){
		return this.RendererName;
	}
}
