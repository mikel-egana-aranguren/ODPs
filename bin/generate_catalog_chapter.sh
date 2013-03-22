#!/bin/sh

./generate_catalog.sh

rm /home/pik/Bioinformatics/PhDThesis/Catalogue/Catalogue_proper.tex
touch /home/pik/Bioinformatics/PhDThesis/Catalogue/Catalogue_proper.tex

echo '\\chapter{The Catalogue of Ontology Design Patterns}\label{chap:catalog_proper}' >> /home/pik/Bioinformatics/PhDThesis/Catalogue/Catalogue_proper.tex
echo ' ' >> /home/pik/Bioinformatics/PhDThesis/Catalogue/Catalogue_proper.tex
echo ' ' >> /home/pik/Bioinformatics/PhDThesis/Catalogue/Catalogue_proper.tex

java -jar owl2latex.jar

cp ../img/*.png /home/pik/Bioinformatics/PhDThesis/Catalogue/

for tex in ../latex/*.tex
do
	less $tex >> /home/pik/Bioinformatics/PhDThesis/Catalogue/Catalogue_proper.tex
done

echo ' ' >> /home/pik/Bioinformatics/PhDThesis/Catalogue/Catalogue_proper.tex
echo ' ' >> /home/pik/Bioinformatics/PhDThesis/Catalogue/Catalogue_proper.tex



