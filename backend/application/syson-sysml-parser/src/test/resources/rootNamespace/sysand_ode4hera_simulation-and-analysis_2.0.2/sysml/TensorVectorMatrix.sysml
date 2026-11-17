//
// Tensor, Vector, and Matrix library
//
// Library for modeling Tensor, Vector, and Matrix. Is used by MDAO concepts but can be usefull in another context.
//
// This library has been developed as part of the ODE4HERA project: https://www.ode4hera.eu/
//
// Version: v2.0
// License: MIT
// Maintainer: DLR Institute of System Architectures in Aeronautics
// Contact: digital.engineering@dlr.de
// Topics: MDAO, Multidisciplinary Design Analysis and Optimization
//
// MIT License
//
// Copyright: (c) 2026, The Contributors to the ODE4HERA SysML v2 Libraries.
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.

library package TensorVectorMatrix{
    
    doc
    /*
     * This package defines generic tensor structuring and indexing concepts
     * for grouping model elements (e.g., design variables, parameters, inputs,
     * outputs) into vector- and matrix-like structures.
     *
     * This package is intentionally distinct from Quantities::TensorQuantityValue:
     *  - Quantities::TensorQuantityValue models the numeric value of tensor-valued
     *    physical quantities together with measurement semantics.
     *  - TensorVectorMatrix models the structural organization and indexing of
     *    model elements, independently of their numeric value representation.
     *
     * Typical use cases include:
     *  - grouping scalar design variables into MDAO solver vectors or matrices
     *  - grouping scalar parameters into structured coupling vectors
     *  - defining explicit index mappings for traceability and export
     */

    private import ScalarValues::*;
    private import RealFunctions::*;
    private import ControlFunctions::*;
    private import SequenceFunctions::*;

    enum def TensorLayout {
        doc
        /*
        * Defines the storage and flattening order of tensor elements.
        *
        * This is primarily intended for export to numerical tools
        * (e.g., MDAO solvers) and does not affect the semantic indexing
        * of tensor elements within the model.
        *
        * RowMajor: last index varies fastest (row-wise flattening)
        * ColumnMajor: first index varies fastest (column-wise flattening)
        */
        RowMajor;
        ColumnMajor;
    }

    attribute def TensorDescriptor {
        doc
       /*
        * Describes the abstract tensor structure independently of what
        * is being grouped (design variables, parameters, etc.).
        *
        * The tensor structure is defined by its shape:
        *  - scalar  : shape not set or empty
        *  - vector  : shape = (n)
        *  - matrix  : shape = (r, c)
        *  - higher-order tensors are supported via longer shape sequences
        *
        * TensorDescriptor does not represent numeric values; it only captures
        * structural and organizational information.
        */
        attribute shape : ScalarValues::Integer[0..*];

        // Storage order for flattening (mainly for export)
        attribute layout : TensorLayout default TensorLayout::RowMajor{
            doc
            /*
            * Defines how tensor elements are flattened when exported
            * to external tools.
            */
        }

        attribute dimensionLabels : String[0..*]{
            doc
            /*
            * Optional human-readable labels for each tensor dimension
            * (e.g., ["time"], ["span", "chord"]).
            */
        }

        attribute dimensionSemantics : String[0..*]{
            doc
            /*
            * Optional semantic description of each dimension
            * (e.g., ["NbTimeSteps"], ["NbStations", "NbDOF"]).
            */
        }
    }

    attribute def TensorGroup {
        doc
        /*
        * Generic grouping of model elements organized according to
        * a tensor structure.
        *
        * TensorGroup provides:
        *  - a shared tensor descriptor
        *  - explicit indexing of each grouped element
        *
        * It is intended to be specialized for concrete semantic roles,
        * such as design variables, parameters, or coupling variables.
        */
        attribute tensor : TensorDescriptor[1];

        attribute elementLabels : String[0..*]{
            doc
            /*
            * Optional human-readable labels for grouped elements,
            * typically used for export or reporting.
            */
        }


    }


    attribute def TensorVector :> TensorGroup {
        doc
        /*
        * Specialization of TensorGroup representing a 1D tensor (vector).
        *
        * This enforces:
        *  - tensor rank = 1
        *  - one index per element
        *  - full coverage of the vector length
        */

        attribute values : Base::Anything[1..*] ordered{
             doc
            /*
            * Elements grouped into the tensor
            */
        }

        assert constraint {
            tensor.shape->size() == 1
        }

        assert constraint {
            tensor.shape#(1) == values->size()
        }

    }

    attribute def TensorMatrix :> TensorGroup {
        doc
        /*
        * Specialization of TensorGroup representing a 2D tensor (matrix).
        *
        * This enforces:
        *  - tensor rank = 2
        *  - two indices per element
        *  - full coverage of the matrix shape
        */

        attribute values : Base::Anything[1..*] ordered{
             doc
            /*
            * Elements grouped into the tensor
            */
        }
        
        assert constraint {
            tensor.shape->size() == 2
        }

        assert constraint {
            tensor.shape#(1) * tensor.shape#(2) == values->size()
        }
    }
}