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
package com.ibm.dtfj.javacore.parser.framework.parser;

import java.util.Iterator;

import com.ibm.dtfj.javacore.builder.IImageBuilder;

/**
 * Parses a section of a javacore. Each javacore section has its own section parser.
 *
 */
public interface ISectionParser {
	/**
	 * Pass in a lookahead buffer that is shared amongst all section parsers, pass in the DTFJ factory
	 * to construct the DTFJ objects as tags are being parsed.
	 * @param lookAhead
	 * @param imageBuilder
	 * @throws ParserException
	 */
	public void readIntoDTFJ(ILookAheadBuffer lookAhead, IImageBuilder imageBuilder) throws ParserException;
	public Iterator getErrors();
	public String getSectionName();
	public boolean anyMatched();
}
