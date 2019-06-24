import os

EXCLUDED_DIR = set([
    '.git',
    'archive',
    'common'
])

# TODO: parse the deps from -deps-out=${SOURCE.base}.deps
latexmk = Builder(
    action=[
        'latexmk -cd -xelatex -latexoption="-shell-escape" $SOURCE',
        Move('$TARGET', '${SOURCE.base}.pdf')
    ])
env = Environment(
    ENV=os.environ,
    BUILDERS={'latexmk': latexmk})

dirs = filter(os.path.isdir, os.listdir())
for d in dirs:
    if not d in EXCLUDED_DIR:
        t = env.latexmk('archive/' + d + '.pdf', d + '/main.tex')
        env.Depends(t, Glob('common/*.tex'))
        env.Depends(t, Glob(os.path.join(d, '*.png')))

# TODO: generate README.adoc Index automatically

