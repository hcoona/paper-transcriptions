import os

# TODO: parse the deps from -deps-out=${SOURCE.base}.deps
latexmk = Builder(
    action=[
        'latexmk -cd -xelatex -latexoption="-shell-escape" $SOURCE',
        Move('$TARGET', '${SOURCE.base}.pdf')
    ])

env = Environment(
    ENV=os.environ,
    BUILDERS={'latexmk': latexmk})
t = env.latexmk('archive/10.1145_93597.98720.pdf', '10.1145_93597.98720/main.tex')
env.Depends(t, Glob('common/*.tex'))
