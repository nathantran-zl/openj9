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
package com.ibm.uma.freemarker;

import com.ibm.uma.UMA;
import com.ibm.uma.UMAException;

import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BooleanModel;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class Tool implements TemplateHashModel {

	String toolName;
	String toolValue;
	Boolean needed = false;
	
	public Tool(String name) throws UMAException {
		toolName = name;
		toolValue = UMA.getUma().getPlatform().replaceMacro("uma_make_cmd_" + toolName);
		if ( toolValue != null ) needed = true;
	}
	
	public TemplateModel get(String arg0) throws TemplateModelException {
		if (arg0.equals("name")) return new SimpleScalar(toolValue);
		if (arg0.equals("needed")) return new BooleanModel(needed, new BeansWrapper());
		return null;
	}

	public boolean isEmpty() throws TemplateModelException {
		return false;
	}

}
