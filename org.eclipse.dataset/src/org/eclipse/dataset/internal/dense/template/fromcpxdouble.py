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
From complex double dataset generate other classes

$ python fromcpxdouble.py ../ComplexDoubleDataset.java

also generate interfaces

$ python fromdouble.py ../../../dense/ComplexDoubleDataset.java
'''

from markers import transmutate, generateclass, removebase #@UnresolvedImport

# default dataset definition
defds = { "ComplexDoubleDataset":["COMPLEX128", "Double", "double", "getElementDoubleAbs", "DTypeUtils.toReal(obj)", "%.8g",
"NaN"] }

defkey = defds.keys()[0]

# all other dataset definitions
fds = { "ComplexFloatDataset":["COMPLEX64", "Float", "float", "getElementDoubleAbs", "(float) DTypeUtils.toReal(obj)", "%.8g",
"NaN"] }

def main(dclass, end, f):
    handlers  = [ transmutate(__file__, defkey + end, defds[defkey], removebase(d, f) + end, fds[d], True) for d in fds ]
    files  = [ open(removebase(d, f) + end + ".java", "w") for d in fds ]

    generateclass(dclass, handlers, files)

if __name__ == '__main__':
    import sys
    if len(sys.argv) > 1:
        fname = sys.argv[1]
    else:
        fname = "../ComplexDoubleDatasetImpl.java"

    dclass_file = open(fname, 'r')

    if fname.endswith("Impl.java"):
        main(dclass_file, "Impl", False)
    else: # interface
        main(dclass_file, "", True)
