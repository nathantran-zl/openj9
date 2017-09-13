
/*******************************************************************************
 * Copyright (c) 1991, 2014 IBM Corp. and others
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

#include "j9.h"
#include "j9cfg.h"
#include "ModronAssertions.h"

#include "RegionListTarok.hpp"


void
MM_RegionListTarok::insertRegion(MM_HeapRegionDescriptorVLHGC *region)
{
	Assert_MM_true(NULL == region->_allocateData._nextInList);
	Assert_MM_true(NULL == region->_allocateData._previousInList);
	if (NULL != _regions) {
		region->_allocateData._nextInList = _regions;
		_regions->_allocateData._previousInList = region;
	}
	_regions = region;
	_listSize += 1;
}

void
MM_RegionListTarok::removeRegion(MM_HeapRegionDescriptorVLHGC *region)
{
	/* the list must contain something */
	Assert_MM_true(_listSize > 0);
	MM_HeapRegionDescriptorVLHGC *next = region->_allocateData._nextInList;
	MM_HeapRegionDescriptorVLHGC *previous = region->_allocateData._previousInList;
	
	if (NULL != next) {
		next->_allocateData._previousInList = previous;
	}
	if (NULL != previous) {
		previous->_allocateData._nextInList = next;
		Assert_MM_false(previous == previous->_allocateData._nextInList);
	} else {
		_regions = next;
	}
	region->_allocateData._nextInList = NULL;
	region->_allocateData._previousInList = NULL;
	_listSize -= 1;
}
