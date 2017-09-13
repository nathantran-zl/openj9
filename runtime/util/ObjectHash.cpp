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

#include "util_api.h"
#include "ObjectHash.hpp"

extern "C" {


I_32
computeObjectAddressToHash(J9JavaVM *vm, j9object_t objectPointer)
{
	return VM_ObjectHash::inlineComputeObjectAddressToHash(vm, objectPointer);
}

I_32
convertValueToHash(J9JavaVM *vm, UDATA value)
{
	return VM_ObjectHash::inlineConvertValueToHash(vm, value);
}

I_32
objectHashCode(J9JavaVM *vm, j9object_t objectPointer)
{
	return VM_ObjectHash::inlineObjectHashCode(vm, objectPointer);
}

}
