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
package com.ibm.uma.om.parser;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ibm.uma.om.Option;

public class OptionParser {
	static public Option parse(Node node, String containingFile) {
		Option option = new Option(containingFile);
		NodeList nodeList = node.getChildNodes();
		NamedNodeMap attributes = node.getAttributes();
		option.setName(attributes.getNamedItem("name").getNodeValue());
		Node dataNode = attributes.getNamedItem("data");
		if ( dataNode != null ) {
			option.setData(dataNode.getNodeValue().split("\\s"));
		}

		Parser.populatePredicateList(nodeList, option);
		return option;
	}



}
