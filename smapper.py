#!/bin/env python3


import os
import sys
import re
from pathlib import Path


SEP = "------------------------------------------------------------------"


# Retrieve list of complete absolute paths of files given extension, ignoring
# out directory
def getFilesofType(rootPath, extension):
    results = list()

    jsps = sorted(Path(sys.argv[1]).rglob("*"+extension))
    for j in jsps:
        results.append(j.resolve())

    return results


# Now have two dicts, one of JSP names and paths and another
# of smap names and paths
# for name in smaps.keys():
#    with smaps[name].open() as file:
#        for line in file:
#            print(line.strip())
def load_jsp(filename, jsps):
    result = list()
    with jsps[filename].open() as file:
        for line in file:
            if ".jsp" in line and "include" in line:
                result.append((list(), ""))
                matches = re.search(r"[\'\"](.*.jsp)", line)
                if matches:
                    chunk = load_jsp(matches.group(1), jsps)
                    result = result + chunk
            else:
                result.append((list(), line.strip()))
    return result


def printVirtFile(virtFile):
    lineCount = 0
    for (index, line) in enumerate(virtFile):
        if len(line[0]) > lineCount:
            lineCount = len(line[0])
    print(lineCount)
    print(" JAVA |  JSP | Line")
    print(SEP)
    for (index, line) in enumerate(virtFile):
        print((" {:>"+str(4*lineCount)+"s} : {:4d} : {}").format(",".join(line[0]), index+1, line[1]))


def load_smap_jsp(jsp_name, virt_jsp, smaps, jsps):
    pattern = r''
    for piece in jsp_name.split("."):
        if "_" in piece:
            for chunk in piece.split("_"):
                pattern += chunk + ".*"
        else:
            pattern += piece + ".*"

    for name in smaps:
        if re.search(pattern, name):
            print("JSP: {}    SMAP: {}".format(jsp_name, name))
            with smaps[name].open() as smap:
                files = list()
                context = 0

                for line in smap:
                    if "+" in line:
                        chunks = line.split()
                        num_lines = 0
                        with jsps[chunks[2]].open() as file:
                            for line in file:
                                num_lines += 1
                        files.append(num_lines)
                    elif ":" in line:
                        input_start_line = None
                        line_file_id = None
                        repeat_count = None
                        output_start_line = None
                        output_line_increment = None

                        chunks = line.split(":")
                        jsp_piece = chunks[0].split(",")
                        java_piece = chunks[1].split(",")
                        if "#" in jsp_piece[0]:
                            input_start_line = int(jsp_piece[0].split("#")[0])
                            line_file_id = int(jsp_piece[0].split("#")[1])
                        else:
                            input_start_line = int(jsp_piece[0])
                        if len(jsp_piece) > 1:
                            repeat_count = int(jsp_piece[1])
                        output_start_line = int(java_piece[0])
                        if len(java_piece) > 1:
                            output_line_increment = int(java_piece[1])

                        if line_file_id is not None:
                            context = line_file_id

                        if repeat_count is None:
                            repeat_count = 1

                        if output_line_increment is None:
                            output_line_increment = 1

                        offset = 0
                        for x in range(0, context):
                            offset += files[x]

                        index = input_start_line + offset - 1
                        out = output_start_line
                        #print("{}#{},{}:{},{} --> {},{},{}".format(input_start_line, line_file_id, repeat_count, output_start_line, output_line_increment, offset, index, out))

                        if index >= len(virt_jsp):
                            print("Invalid index: "+str(index))
                            continue
                        virt_jsp[index][0].append(str(index))

            break
    return virt_jsp


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
    for jsp_name in ["wall.jsp"]:
        print(SEP)
        print("File: {}".format(jsp_name))
        print(SEP)
        virt_jsp = load_jsp(jsp_name, jsps)
        mapped_virt_jsp = load_smap_jsp(jsp_name, virt_jsp, smaps, jsps)
        printVirtFile(mapped_virt_jsp)
        print("\n\n")


if __name__ == '__main__':
    main()
