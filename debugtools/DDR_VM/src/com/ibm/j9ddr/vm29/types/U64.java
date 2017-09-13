/*******************************************************************************
 * Copyright (c) 2001, 2014 IBM Corp. and others
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
package com.ibm.j9ddr.vm29.types;

import com.ibm.j9ddr.InvalidDataTypeException;

public class U64 extends UScalar {

	// Constants
	public static final int SIZEOF = 8;
	public static final long MASK = 0xFFFFFFFFFFFFFFFFL;
	public static final U64 MIN = new U64(0x0000000000000000L);
	public static final U64 MAX = new U64(0xFFFFFFFFFFFFFFFFL);

	public U64(long value) {
		super(value);
	}
	
	public U64(Scalar parameter) {
		super(parameter);
	}
	
	// Add
	
	public U64 add(int number) {
		return new U64(data + number);
	}
	
	public U64 add(UScalar parameter) {
		return add(new U64(parameter));
	}
	
	public U64 add(U64 parameter) {
		return new U64(data + parameter.data);
	}
	
	public U64 add(IScalar parameter) {
		return add(new U64(parameter));
	}
	
	// Sub
	
	public U64 sub(int number) {
		return new U64(data - number);
	}
	
	public U64 sub(UScalar parameter) {
		return sub(new U64(parameter));
	}
	
	public U64 sub(U64 parameter) {
		return new U64(data - parameter.data);
	}
	
	public U64 sub(I8 parameter) {
		return sub(new U64(parameter));
	}
	
	public U64 sub(I16 parameter) {
		return sub(new U64(parameter));
	}
	
	public U64 sub(I32 parameter) {
		return sub(new U64(parameter));
	}
	
	public U64 sub(I64 parameter) {
		return sub(new U64(parameter));
	}

	
	public int intValue() {
		if (super.intValue() < 0) {
			throw new InvalidDataTypeException("U_64 contains value larger than Integer.MAX_VALUE");
		} else {
			return super.intValue();
		}
	}
	
	public long longValue() {
		if (super.longValue() < 0) {
			throw new InvalidDataTypeException("U_64 contains value larger than Long.MAX_VALUE");
		} else {
			return super.longValue();
		}
	}
	
	// bitOr
	
	public U64 bitOr(int number) {
		return new U64(data | number);
	}
	
	public U64 bitOr(long number) {
		return new U64(data | number);
	}
	
	public U64 bitOr(UScalar parameter) {
		return bitOr(new U64(parameter));
	}
	
	public U64 bitOr(U64 parameter) {
		return new U64(data | parameter.data);
	}
	
	public U64 bitOr(I8 parameter) {
		return bitOr(new U64(parameter));
	}
	
	public U64 bitOr(I16 parameter) {
		return bitOr(new U64(parameter));
	}
	
	public U64 bitOr(I32 parameter) {
		return bitOr(new U64(parameter));
	}
	
	public U64 bitOr(I64 parameter) {
		return bitOr(new U64(parameter));
	}
	
	// bitXor
	
	public U64 bitXor(int number) {
		return new U64(data ^ number);
	}
	
	public U64 bitXor(long number) {
		return new U64(data ^ number);
	}
	
	public U64 bitXor(Scalar parameter) {
		return bitXor(new U64(parameter));
	}
	
	public U64 bitXor(U64 parameter) {
		return new U64(data ^ parameter.data);
	}
	
	// bitAnd
	public U64 bitAnd(int number) {
		return new U64(data & number);
	}
	
	public U64 bitAnd(long number) {
		return new U64(data & number);
	}
	
	public U64 bitAnd(UScalar parameter) {
		return bitAnd(new U64(parameter));
	}
	
	public U64 bitAnd(U64 parameter) {
		return new U64(data & parameter.data);
	}
	
	public U64 bitAnd(I8 parameter) {
		return bitAnd(new U64(parameter));
	}
	
	public U64 bitAnd(I16 parameter) {
		return bitAnd(new U64(parameter));
	}
	
	public U64 bitAnd(I32 parameter) {
		return bitAnd(new U64(parameter));
	}
	
	public U64 bitAnd(I64 parameter) {
		return bitAnd(new U64(parameter));
	}
	
	public boolean gt(Scalar parameter) {
		checkComparisonValid(parameter);
		
		//If sign bit is set, result it reversed
		if (((data ^ parameter.data) & 0x8000000000000000L) != 0) {
			return data < parameter.data;
		} else {
			return data > parameter.data;
		}
	}
	
	public boolean lt(Scalar parameter) {
		checkComparisonValid(parameter);
		
		//If sign bit is set, result it reversed
		if (((data ^ parameter.data) & 0x8000000000000000L) != 0) {
			return data > parameter.data;
		} else {
			return data < parameter.data;
		}
	}
	
	// leftShift
	public U64 leftShift(int i) {
		return new U64(data << i);
	}
	
	// rightShift
	public U64 rightShift(int i) {
		return new U64(data >>> i);
	}
	
	// bitNot
	public U64 bitNot() {
		return new U64(~data);
	}
	
	public U64 mult(int parameter) {
		return new U64(data * parameter);
	}
	
	@Override
	public boolean eq(Scalar parameter) {
		if (parameter instanceof U64) {
			return data == parameter.data;
		} else if (parameter.isSigned()) {
			return this.eq(new U64(parameter));
		} else {
			if (data < 0) {
				return false;
			} else {
				return longValue() == parameter.longValue();
			}
		}
	}
	
	@Override
	public int sizeof()
	{
		return SIZEOF;
	}
}
