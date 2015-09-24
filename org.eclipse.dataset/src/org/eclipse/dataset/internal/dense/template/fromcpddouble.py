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
From compound double dataset generate other classes

$ python fromcpddouble.py ../CompoundDoubleDataset.java

also generate interfaces

$ python fromdouble.py ../../../dense/CompoundDoubleDataset.java
'''

from markers import transmutate, generateclass, removebase #@UnresolvedImport

# default dataset definition
defds = { "CompoundDoubleDataset":["FLOAT64", "Double", "double", "getElementDoubleAbs", "DTypeUtils.toReal(obj)", "%.8g",
"NaN"] }

defkey = defds.keys()[0]

# all other dataset definitions
fds = { "CompoundFloatDataset":["FLOAT32", "Float", "float", "getElementDoubleAbs", "(float) DTypeUtils.toReal(obj)", "%.8g",
"NaN"] }

allds = { 
"CompoundIntegerDataset":["INT32", "Integer", "int", "getElementLongAbs", "(int) DTypeUtils.toLong(obj)", "%d",
"MIN_VALUE"],
"CompoundLongDataset":["INT64", "Long", "long", "getElementLongAbs", "DTypeUtils.toLong(obj)", "%d",
"MIN_VALUE"],
"CompoundShortDataset":["INT16", "Short", "short", "getElementLongAbs", "(short) DTypeUtils.toLong(obj)", "%d",
"MIN_VALUE"],
"CompoundByteDataset":["INT8", "Byte", "byte", "getElementLongAbs", "(byte) DTypeUtils.toLong(obj)", "%d",
"MIN_VALUE"]
 }

def main(dclass, end, f):
    handlers  = [ transmutate(__file__, defkey + end, defds[defkey], removebase(d, f) + end, fds[d], True) for d in fds ]
    handlers += [ transmutate(__file__, defkey + end, defds[defkey], removebase(d, f) + end, allds[d], False) for d in allds ]
    files  = [ open(removebase(d, f) + end + ".java", "w") for d in fds ]
    files += [ open(removebase(d, f) + end + ".java", "w") for d in allds ]

    generateclass(dclass, handlers, files)

if __name__ == '__main__':
    import sys
    if len(sys.argv) > 1:
        fname = sys.argv[1]
    else:
        fname = "../CompoundDoubleDatasetImpl.java"

    dclass_file = open(fname, 'r')

    if fname.endswith("Impl.java"):
        main(dclass_file, "Impl", False)
    else: # interface
        main(dclass_file, "", True)
