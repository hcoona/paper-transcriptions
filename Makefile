all: archive/10.1145_357401.357402.pdf archive/10.1145_3003665.3003674.pdf \
	archive/10.1145_93597.98720.pdf

archive/10.1145_357401.357402.pdf: 10.1145_357401.357402/*.tex common/*.tex
	latexmk -cd -xelatex -latexoption="-shell-escape" 10.1145_357401.357402/main
	mv 10.1145_357401.357402/main.pdf archive/10.1145_357401.357402.pdf

archive/10.1145_3003665.3003674.pdf: 10.1145_3003665.3003674/*.tex common/*.tex
	latexmk -cd -xelatex -latexoption="-shell-escape" 10.1145_3003665.3003674/main
	mv 10.1145_3003665.3003674/main.pdf archive/10.1145_3003665.3003674.pdf

archive/10.1145_93597.98720.pdf: 10.1145_93597.98720/* common/*.tex
	latexmk -cd -xelatex -latexoption="-shell-escape" 10.1145_93597.98720/main
	mv 10.1145_93597.98720/main.pdf archive/10.1145_93597.98720.pdf
