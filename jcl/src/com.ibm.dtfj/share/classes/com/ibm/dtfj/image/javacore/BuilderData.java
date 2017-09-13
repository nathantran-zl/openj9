/*[INCLUDE-IF Sidecar18-SE]*/
/*******************************************************************************
 * Copyright (c) 2007, 2017 IBM Corp. and others
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
package com.ibm.dtfj.image.javacore;

import com.ibm.dtfj.image.DataUnavailable;
import com.ibm.dtfj.image.ImagePointer;
import com.ibm.dtfj.javacore.builder.IBuilderData;

public class BuilderData {
	
	

	
	public static String stringIsSet(String value) throws DataUnavailable {
		if (value == null) {
			throw new DataUnavailable();
		}
		return value;
	}
	
	
	public static long valLongIsSet(long value) throws DataUnavailable {
		if (value == IBuilderData.NOT_AVAILABLE) {
			throw new DataUnavailable();
		}
		return value;
	}
	
	
	public static int valIntIsSet(int value) throws DataUnavailable {
		if (value == IBuilderData.NOT_AVAILABLE) {
			throw new DataUnavailable();
		}
		return value;
	}
	
	public static ImagePointer imagePointerIsSet(ImagePointer pointer) throws DataUnavailable {
		if (pointer == null) {
			throw new DataUnavailable();
		}
		return pointer;
	}

}
