###
# *******************************************************************************
# * Copyright (c) 2011, 2014 Diamond Light Source Ltd.
# * All rights reserved. This program and the accompanying materials
# * are made available under the terms of the Eclipse Public License v1.0
# * which accompanies this distribution, and is available at
# * http://www.eclipse.org/legal/epl-v10.html
# *
# * Contributors:
# *    Peter Chang - initial API and implementation and/or initial documentation
# *******************************************************************************/
###

#!/usr/bin/env python
'''
From double dataset generate other classes

$ python fromdouble.py ../DoubleDatasetImpl.java

also generate interfaces

$ python fromdouble.py ../../../dense/DoubleDataset.java
'''

from markers import transmutate, generateclass, removebase #@UnresolvedImport

# default dataset definition
defds = { "DoubleDataset":["FLOAT64", "Double", "double", "getElementDoubleAbs", "DTypeUtils.toReal(obj)", "%.8g",
"NaN"] }

defkey = defds.keys()[0]

# all other dataset definitions
fds = { "FloatDataset":["FLOAT32", "Float", "float", "getElementDoubleAbs", "(float) DTypeUtils.toReal(obj)", "%.8g",
"NaN"] }

allds = { 
"IntegerDataset":["INT32", "Integer", "int", "getElementLongAbs", "(int) DTypeUtils.toLong(obj)", "%d",
"MIN_VALUE"],
"LongDataset":["INT64", "Long", "long", "getElementLongAbs", "DTypeUtils.toLong(obj)", "%d",
"MIN_VALUE"],
"ShortDataset":["INT16", "Short", "short", "getElementLongAbs", "(short) DTypeUtils.toLong(obj)", "%d",
"MIN_VALUE"],
"ByteDataset":["INT8", "Byte", "byte", "getElementLongAbs", "(byte) DTypeUtils.toLong(obj)", "%d",
"MIN_VALUE"]
 }

bds = {
"BooleanDatasetBase":["BOOL", "Boolean", "boolean", "getElementBooleanAbs", "DTypeUtils.toBoolean(obj)", "%b", "FALSE"]
 }

ods = {
"StringDatasetBase":["STRING", "String", "String", "getStringAbs", "obj.toString()", "%s", "FALSE"],
"ObjectDatasetBase":["OBJECT", "Object", "Object", "getObjectAbs", "obj", "%s", "FALSE"]
 }

def main(dclass, end, f):
    handlers  = [ transmutate(__file__, defkey + end, defds[defkey], removebase(d, f) + end, fds[d], True) for d in fds ]
    handlers += [ transmutate(__file__, defkey + end, defds[defkey], removebase(d, f) + end, allds[d], False) for d in allds ]
    handlers += [ transmutate(__file__, defkey + end, defds[defkey], removebase(d, f) + end, bds[d], False, True) for d in bds ]
    handlers += [ transmutate(__file__, defkey + end, defds[defkey], removebase(d, f) + end, ods[d], False, False, True) for d in ods ]
    files  = [ open(removebase(d, f) + end + ".java", "w") for d in fds ]
    files += [ open(removebase(d, f) + end + ".java", "w") for d in allds ]
    files += [ open(removebase(d, f) + end + ".java", "w") for d in bds ]
    files += [ open(removebase(d, f) + end + ".java", "w") for d in ods ]

    generateclass(dclass, handlers, files)

if __name__ == '__main__':
    import sys
    if len(sys.argv) > 1:
        fname = sys.argv[1]
    else:
        fname = "../DoubleDatasetImpl.java"

    dclass_file = open(fname, 'r')

    if fname.endswith("Impl.java"):
        main(dclass_file, "Impl", False)
    else: # interface
        main(dclass_file, "", True)
