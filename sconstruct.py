import os

EXCLUDED_DIR = set([
    '.git',
    'archive',
    'common'
])

LATEXMK = Builder(
    action=[
        'latexmk -cd -deps-out=${SOURCE.base}.deps -xelatex -latexoption="-shell-escape" $SOURCE',
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
