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

import com.ibm.uma.UMAException;
import com.ibm.uma.om.Artifact;

import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BooleanModel;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateCollectionModel;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateModelIterator;

public class ArtifactDataElement implements TemplateHashModel {

	Artifact artifact;
	String key;
	String [] data;
	
	class ADEIterator implements TemplateModelIterator, TemplateCollectionModel {
		String[] data;
		int pos = 0;
	
		public ADEIterator(String [] data) {
			this.data = data;
		}
		public boolean hasNext() throws TemplateModelException {
			return data != null && pos < data.length;
		}
		public TemplateModel next() throws TemplateModelException {
			return new SimpleScalar(data[pos++]);			
		}
		
		public TemplateModelIterator iterator() throws TemplateModelException {
			return this;
		}
	}
	
	public ArtifactDataElement(String key, Artifact artifact) throws UMAException {
		this.artifact = artifact;
		this.key = key;
		this.data = artifact.getData(key);
	}
	
	public TemplateModel get(String arg0) throws TemplateModelException {
		if (arg0.equals("list")) {
			return new ADEIterator(data);
		}
		if (arg0.equals("present")) {
			return new BooleanModel(data!=null, new BeansWrapper());
		}
		if (arg0.equals("data")) {
			String string = "";
			for (String tmp : data) {
				string += tmp;
			}
			return new SimpleScalar(string);
		}
		if (arg0.equals("underscored_data")) {
			String string = "";
			for (String tmp : data) {
				string += "_" + tmp.toUpperCase();
			}
			return new SimpleScalar(string);
		}
		return null;
	}
	
	public boolean isEmpty() throws TemplateModelException {
		return false;
	}

}
