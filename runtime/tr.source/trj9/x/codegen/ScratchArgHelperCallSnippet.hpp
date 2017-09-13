/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corp. and others
 *
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which accompanies this
 * distribution and is available at https://www.eclipse.org/legal/epl-2.0/
 * or the Apache License, Version 2.0 which accompanies this distribution and
 * is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * This Source Code may also be made available under the following
 * Secondary Licenses when the conditions for such availability set
 * forth in the Eclipse Public License, v. 2.0 are satisfied: GNU
 * General Public License, version 2 with the GNU Classpath
 * Exception [1] and GNU General Public License, version 2 with the
 * OpenJDK Assembly Exception [2].
 *
 * [1] https://www.gnu.org/software/classpath/license.html
 * [2] http://openjdk.java.net/legal/assembly-exception.html
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 *******************************************************************************/

#ifndef SCRATCHARGHELPERCALLSNIPPET_INCL
#define SCRATCHARGHELPERCALLSNIPPET_INCL

#include "x/codegen/HelperCallSnippet.hpp"

#include <stdint.h>
#include "env/jittypes.h"

namespace TR { class CodeGenerator; }
namespace TR { class LabelSymbol; }
namespace TR { class Node; }
namespace TR { class SymbolReference; }

namespace TR {

class X86ScratchArgHelperCallSnippet : public TR::X86HelperCallSnippet
   {
   uintptrj_t _scratchArg;

   public:

   X86ScratchArgHelperCallSnippet(TR::CodeGenerator   *cg,
                                     TR::Node            *node,
                                     TR::LabelSymbol      *restartlab,
                                     TR::LabelSymbol      *snippetlab,
                                     TR::SymbolReference *helper,
                                     uintptrj_t          scratchArg,
                                     int32_t             stackPointerAdjustment=0)
      :_scratchArg(scratchArg), TR::X86HelperCallSnippet(cg, node, restartlab, snippetlab, helper, stackPointerAdjustment){}

   virtual Kind getKind() { return IsScratchArgHelperCall; }

   uintptrj_t getScratchArg(){ return _scratchArg; }

   virtual uint8_t *genHelperCall(uint8_t *buffer);
   virtual uint32_t getLength(int32_t estimatedSnippetStart);

   };

class X86StackOverflowCheckSnippet : public TR::X86ScratchArgHelperCallSnippet
   {
   public:

   X86StackOverflowCheckSnippet(
      TR::Node            *node,
      TR::LabelSymbol      *restartLabel,
      TR::LabelSymbol      *snippetLabel,
      TR::SymbolReference *helper,
      uintptrj_t          scratchArg,
      int32_t             stackPointerAdjustment,
      TR::CodeGenerator   *cg) :
         TR::X86ScratchArgHelperCallSnippet(cg, node, restartLabel, snippetLabel, helper, scratchArg, stackPointerAdjustment)
      {
      }

   virtual uint8_t *genHelperCall(uint8_t *buffer);
   };

}

#endif
