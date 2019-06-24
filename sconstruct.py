import os

EXCLUDED_DIR = set([
    '.git',
    'archive',
    'common'
])

# TODO: parse the deps from -deps-out=${SOURCE.base}.deps
LATEXMK = Builder(
    action=[
        'latexmk -cd -xelatex -latexoption="-shell-escape" $SOURCE',
        Move('$TARGET', '${SOURCE.base}.pdf')
    ])
ENV = Environment(
    ENV=os.environ,
    BUILDERS={'latexmk': LATEXMK})

DIRS = filter(os.path.isdir, os.listdir())
for d in DIRS:
    if not d in EXCLUDED_DIR:
        t = ENV.latexmk('archive/' + d + '.pdf', d + '/main.tex')
        ENV.Depends(t, Glob('common/*.tex'))
        ENV.Depends(t, Glob(os.path.join(d, '*.png')))

# TODO: generate README.adoc Index automatically
