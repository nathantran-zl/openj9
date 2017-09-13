/*******************************************************************************
 * Copyright (c) 2001, 2017 IBM Corp. and others
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
// Test file: LongCompare.java
// Testing the comparison ("==") operation among long type operand variables, and constants.

package jit.test.jitt.math2;

import org.testng.annotations.Test;
import org.testng.Assert;

@Test(groups = { "level.sanity","component.jit" })
public class LongCompare extends jit.test.jitt.Test {

   @Test
   public void testLongCompare() {

        long A, B, C;

        A = 1;
        B = 2;
        C = tstdoCmpEq(A,B);
        if (C != 1)
                Assert.fail("LongCompare->run: Incorrect result for test #1!");


        A = 2;
        B = 2;
        C = tstdoCmpEq(A,B);
        if (C != 0)
                Assert.fail("LongCompare->run: Incorrect result for test #2!");




        A = 3;
        C = tstdoConstCmpEq(A);
        if (C != 0)
                Assert.fail("LongCompare->run: Incorrect result for test #3!");



        A = -1;
        B = -2;
        C = tstdoCmpEq(A,B);
        if (C != 1)
                Assert.fail("LongCompare->run: Incorrect result for test #4!");



        A = -2;
        B = -2;
        C = tstdoCmpEq(A,B);
        if (C != 0)
                Assert.fail("LongCompare->run: Incorrect result for test #5!");



        A = -3;
        C = tstdoConstCmpEq(A);
        if (C != 1)
                Assert.fail("LongCompare->run: Incorrect result for test #6!");



// ---------------

        A = 2;
        C = tstConstCmpLT(A);
        if (C != 1)
                Assert.fail("LongCompare->run: Incorrect result for test #7!");


        A = 3;
        C = tstConstCmpLT(A);
        if (C != 1)
                Assert.fail("LongCompare->run: Incorrect result for test #8!");


        A = 4;
        C = tstConstCmpLT(A);
        if (C != 1)
                Assert.fail("LongCompare->run: Incorrect result for test #9!");


        A = -2;
        C = tstConstCmpLT(A);
        if (C != 1)
                Assert.fail("LongCompare->run: Incorrect result for test #10!");


        A = -3;
        C = tstConstCmpLT(A);
        if (C != 1)
                Assert.fail("LongCompare->run: Incorrect result for test #11!");


        A = -4;
        C = tstConstCmpLT(A);
        if (C != 1)
                Assert.fail("LongCompare->run: Incorrect result for test #12!");


// ---------------

        A = 2;
        C = tstConstCmpGE(A);
        if (C != 0)
                Assert.fail("LongCompare->run: Incorrect result for test #13!");



        A = 3;
        C = tstConstCmpGE(A);
        if (C != 1)
                Assert.fail("LongCompare->run: Incorrect result for test #14!");



        A = 4;
        C = tstConstCmpGE(A);
        if (C != 1)
                Assert.fail("LongCompare->run: Incorrect result for test #15!");


        A = -2;
        C = tstConstCmpGE(A);
        if (C != 0)
                Assert.fail("LongCompare->run: Incorrect result for test #16!");



        A = -3;
        C = tstConstCmpGE(A);
        if (C != 0)
                Assert.fail("LongCompare->run: Incorrect result for test #17!");



        A = -4;
        C = tstConstCmpGE(A);
        if (C != 0)
                Assert.fail("LongCompare->run: Incorrect result for test #18!");


// ---------------

        A = 2;
        C = tstConstCmpLE(A);
        if (C != 1)
                Assert.fail("LongCompare->run: Incorrect result for test #19!");


        A = 3;
        C = tstConstCmpLE(A);
        if (C != 1)
                Assert.fail("LongCompare->run: Incorrect result for test #20!");


        A = 4;
        C = tstConstCmpLE(A);
        if (C != 0)
                Assert.fail("LongCompare->run: Incorrect result for test #21!");



        A = -2;
        C = tstConstCmpLE(A);
        if (C != 1)
                Assert.fail("LongCompare->run: Incorrect result for test #22!");


        A = -3;
        C = tstConstCmpLE(A);
        if (C != 1)
                Assert.fail("LongCompare->run: Incorrect result for test #23!");


        A = -4;
        C = tstConstCmpLE(A);
        if (C != 1)
                Assert.fail("LongCompare->run: Incorrect result for test #24!");


// ---------------

        A = 2;
        C = tstConstCmpGT(A);
        if (C != 1)
                Assert.fail("LongCompare->run: Incorrect result for test #25!");


        A = 3;
        C = tstConstCmpGT(A);
        if (C != 1)
                Assert.fail("LongCompare->run: Incorrect result for test #26!");


        A = 4;
        C = tstConstCmpGT(A);
        if (C != 0)
                Assert.fail("LongCompare->run: Incorrect result for test #27!");


        A = -2;
        C = tstConstCmpGT(A);
        if (C != 1)
                Assert.fail("LongCompare->run: Incorrect result for test #28!");


        A = -3;
        C = tstConstCmpGT(A);
        if (C != 1)
                Assert.fail("LongCompare->run: Incorrect result for test #29!");


        A = -4;
        C = tstConstCmpGT(A);
        if (C != 1)
                Assert.fail("LongCompare->run: Incorrect result for test #30!");


//----------------
   }

   static int tstdoCmpEq(long A, long B){
    if(A==B)
     return 0;
    else
     return 1;
   }

   static int tstdoConstCmpEq(long A){
    if(A==3)
     return 0;
    else
     return 1;
   }

   static int tstdoBigConstCmpEq(long A) {
    return 1;
   }

   static int tstConstCmpLT(long A) {
    if(A<1000)
     return 1;
    else
     return 0;
   }

   static int tstConstCmpGE(long A) {
    if(A>=3)
     return 1;
    else
     return 0;
   }

   static int tstConstCmpLE(long A) {
    if(A<=3)
     return 1;
    else
     return 0;
   }

   static int tstConstCmpGT(long A) {
    if(A<=3)
     return 1;
    else
     return 0;
   }


}
