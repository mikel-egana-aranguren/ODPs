#!/bin/sh

# Clear
rm ../latex/*

# Generate catalog from OWL files, HTML and LaTeX version
java -jar owl2latex.jar
java -jar owl2html.jar

cp ../img/*.png ../latex/

# Pack everything for distribution
# This is only for my (official) releases of the catalogue ...

# tar --exclude doesn't seem to behave so we simply move 
# temporarily the candidate ODPs we don't want in the distribution

mv ../owl/Candidate_ODPs/ /home/pik/
tar -cvzf ../../odp.tar.gz ../../odp/
mv /home/pik/Candidate_ODPs/ ../owl/





