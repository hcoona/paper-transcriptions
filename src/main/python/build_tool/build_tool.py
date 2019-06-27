"""
Entry Point of the customized build tool.
"""

import os
import sys

EXCLUDED_DIRECTORIES = set([
    ".git",
    "archive",
    "common",
    "src",
])

def __main(path):
    directories = list(
        filter(
            os.path.isdir,
            map(
                lambda p: os.path.join(path, p),
                filter(
                    lambda p: not p.startswith('bazel-'),
                    filter(
                        lambda p: p not in EXCLUDED_DIRECTORIES,
                        os.listdir(path))))))
    print(directories)

if __name__ == '__main__':
    if len(sys.argv) == 1:
        path = os.getcwd()
    else:
        path = sys.argv[1]
    __main(path)
