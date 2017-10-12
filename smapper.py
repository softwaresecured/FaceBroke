#!/bin/env python3


import os
import sys
import re
from pathlib import Path


# Retrieve list of complete absolute paths of files given extension, ignoring
# out directory
def getFilesofType(rootPath, extension):
    results = list()

    jsps = sorted(Path(sys.argv[1]).rglob("*"+extension))
    for j in jsps:
        results.append(j.resolve())

    return results


def buildJspMap(fullPaths):
    results = dict()

    for p in fullPaths:
        results[p.name()] = p

    return results


def buildFullJsp(jspMap):
    results = dict()

    for f in jspMap.keys():
        print(f)

    return results


def main():
    root = sys.argv[1]
    # Get JSPs
    jsps = getFilesofType(root, ".jsp")

    # Only want JSPs not in target/
    tmp = dict()
    for j in jsps:
        if "target/" not in str(j):
            tmp[str(j.name)] = j
    jsps = tmp

    # Get our smap files
    smaps = getFilesofType(root, ".smap")
    tmp = dict()
    for f in smaps:
        tmp[str(f.name)] = f
    smaps = tmp

    # Now have two dicts, one of JSP names and paths and another
    # of smap names and paths
    # for name in smaps.keys():
    #    with smaps[name].open() as file:
    #        for line in file:
    #            print(line.strip())

    for jsp in jsps.keys():
        with jsps[jsp].open() as file:
            print("FILE: "+jsp)
            for line in file:
                if ".jsp" in line and "include" in line:
                    matches = re.search(r"[\'\"](.*.jsp)", line)
                    if matches:
                        print(matches.group(1))



if __name__ == '__main__':
    main()
