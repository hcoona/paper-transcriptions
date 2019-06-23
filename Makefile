all: archive/10.1145_357401.357402.pdf

archive/10.1145_357401.357402.pdf: 10.1145_357401.357402/*.tex
	latexmk -cd -xelatex -latexoption="-shell-escape" 10.1145_357401.357402/main
	mv 10.1145_357401.357402/main.pdf archive/10.1145_357401.357402.pdf
