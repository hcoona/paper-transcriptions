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

DIRS = filter(
    lambda p: (not p in EXCLUDED_DIR) and (not p.startswith('bazel-')),
    filter(
        os.path.isdir,
        os.listdir()))
for d in DIRS:
    others = filter(
        lambda p: p.endswith('.tex') and p != 'main.tex',
        os.listdir(d))
    for f in others:
        tf = ENV.latexmk(os.path.join(d, f[:-4] + '.pdf'), os.path.join(d, f))
        ENV.Depends(tf, Glob('common/*.tex'))
    t = ENV.latexmk('archive/' + d + '.pdf', d + '/main.tex')
    ENV.Depends(t, Glob('common/*.tex'))
    ENV.Depends(t, Glob(os.path.join(d, '*.png')))
    ENV.Depends(t, Glob(os.path.join(d, '*.pdf')))

# TODO: generate README.adoc Index automatically
