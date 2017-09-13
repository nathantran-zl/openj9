/*******************************************************************************
 * Copyright (c) 2002, 2014 IBM Corp. and others
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
#define true 1
#define false 0
#include "floatsanityg.h"


   void group_JBf2l() {
      int i=0, groupNum=13,numTests=17;
      int testsPassed=0, testsFailed=0;
      unsigned int op_0 [] = {
         0x00000000,
         0x80000000,
         0x3f800000,
         0xbf800000,
         0x42fe0000,
         0xc3000000,
         0x46fffe00,
         0xc7000000,
         0xcf000000,
         0xdf000000,
         0x7f800000,
         0xff800000,
         0x7f7fffff,
         0xff7fffff,
         0x00000001,
         0x80000001,
         0x41500000,
         0x0};
      unsigned int result [] = {
         0x00000000, 0x00000000,
         0x00000000, 0x00000000,
         0x00000000, 0x00000001,
         0xffffffff, 0xffffffff,
         0x00000000, 0x0000007f,
         0xffffffff, 0xffffff80,
         0x00000000, 0x00007fff,
         0xffffffff, 0xffff8000,
         0xffffffff, 0x80000000,
         0x80000000, 0x00000000,
         0x7fffffff, 0xffffffff,
         0x80000000, 0x00000000,
         0x7fffffff, 0xffffffff,
         0x80000000, 0x00000000,
         0x00000000, 0x00000000,
         0x00000000, 0x00000000,
         0x00000000, 0x0000000d,
         0x0, 0x0};
      float *p0=(float *) op_0;
      I_64 *erp=(I_64*) result, r, *rp=&r;
      for (i=0; i<numTests; i++) {
         r = JBf2l(*(p0));
         if ( IS_DNAN(erp) ){
            if ( !(IS_DNAN(&r)) ){
               printf("%d.%d: op_0=0x%08x Expected=0x%08x%08x Actual=0x%08x%08x \n",
                  groupNum, i, 
                  *(int *)p0, *(int *)erp, *((int *) erp + 1), *(int *)&r, *((int *) &r + 1));
               testsFailed++;
            } else {
               testsPassed++;
            }

         } else {
            if ( (HIWORD(&r) != HIWORD(erp)) || (LOWORD(&r) != LOWORD(erp)) ){
               printf("%d.%d: op_0=0x%08x Expected=0x%08x%08x Actual=0x%08x%08x \n",
                  groupNum, i, 
                  *(int *)p0, *(int *)erp, *((int *) erp + 1), *(int *)&r, *((int *) &r + 1));
               testsFailed++;
            } else {
               testsPassed++;
            }
         }

         p0++;erp++;
      }
      printf("[%d] %s Passed %d Failed %d Total %d \n", groupNum,"JBf2l", testsPassed, testsFailed, numTests);
      totalNumTest +=numTests; totalTestsPassed +=testsPassed; totalTestsFailed +=testsFailed;
   }

