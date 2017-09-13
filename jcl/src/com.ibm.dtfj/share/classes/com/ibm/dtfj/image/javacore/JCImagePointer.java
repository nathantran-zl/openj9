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

import java.util.Properties;

import com.ibm.dtfj.image.CorruptDataException;
import com.ibm.dtfj.image.DataUnavailable;
import com.ibm.dtfj.image.ImageAddressSpace;
import com.ibm.dtfj.image.ImagePointer;
import com.ibm.dtfj.image.MemoryAccessException;
import com.ibm.dtfj.java.javacore.JCInvalidArgumentsException;

public class JCImagePointer implements ImagePointer {
	
	private final long fUnderlyingAddress;
	private final JCImageAddressSpace fResidentDomain;
	
	private static final String MEMORY_READ_ERROR = "Can't read into ImageAddressSpace yet";
	
	public JCImagePointer(JCImageAddressSpace resident, long localAddress) throws JCInvalidArgumentsException{
		if (resident == null) {
			throw new JCInvalidArgumentsException("An image pointer must have an associated address space");
		}
		if (!resident.isValidAddressID(localAddress)) {
			throw new JCInvalidArgumentsException("Invalid address 0x"+Long.toHexString(localAddress));
		}
		fUnderlyingAddress = localAddress;
		fResidentDomain = resident;
	}

	
	/**
	 * 
	 */
	public ImagePointer add(long offset) {
		return fResidentDomain.getPointer(fUnderlyingAddress + offset);
	}
	
	
	/**
	 * 
	 */
	public long getAddress() {
		return fUnderlyingAddress;
	}

	
	/**
	 * 
	 */
	public ImageAddressSpace getAddressSpace() {
		return fResidentDomain;
	}

	/**
	 * 
	 */
	public ImagePointer getPointerAt(long index) throws MemoryAccessException,
			CorruptDataException {
		throw new MemoryAccessException(fResidentDomain.getPointer(index + fUnderlyingAddress), MEMORY_READ_ERROR);
	}

	
	/**
	 * 
	 */
	public short getShortAt(long index) throws MemoryAccessException,
			CorruptDataException {
		throw new MemoryAccessException(fResidentDomain.getPointer(index + fUnderlyingAddress), MEMORY_READ_ERROR);
	}

	
	/**
	 * 
	 */
	public boolean isExecutable() throws DataUnavailable {
		throw new DataUnavailable();
	}

	/**
	 * 
	 */
	public boolean isReadOnly() throws DataUnavailable {
		throw new DataUnavailable();
	}

	
	/**
	 * 
	 */
	public boolean isShared() throws DataUnavailable {
		throw new DataUnavailable();
	}
	
	/**
	 * 
	 */
	public Properties getProperties() {
		return new Properties();
	}
	
	/**
	 * 
	 */
	public byte getByteAt(long index) throws MemoryAccessException,
	CorruptDataException {
		throw new MemoryAccessException(fResidentDomain.getPointer(index + fUnderlyingAddress), MEMORY_READ_ERROR);
	}
	
	/**
	 * 
	 */
	public double getDoubleAt(long index) throws MemoryAccessException,
		CorruptDataException {
		throw new MemoryAccessException(fResidentDomain.getPointer(index + fUnderlyingAddress), MEMORY_READ_ERROR);
	}
	
	
	/**
	 * 
	 */
	public float getFloatAt(long index) throws MemoryAccessException,
		CorruptDataException {
		throw new MemoryAccessException(fResidentDomain.getPointer(index + fUnderlyingAddress), MEMORY_READ_ERROR);
	}
	
	
	/**
	 * 
	 */
	public int getIntAt(long index) throws MemoryAccessException,
		CorruptDataException {
		throw new MemoryAccessException(fResidentDomain.getPointer(index + fUnderlyingAddress), MEMORY_READ_ERROR);
	}
	
	
	/**
	 * 
	 */
	public long getLongAt(long index) throws MemoryAccessException,
		CorruptDataException {
		throw new MemoryAccessException(fResidentDomain.getPointer(index + fUnderlyingAddress), MEMORY_READ_ERROR);
	}


	/**
	 * Auto-generated by Eclipse 3.2.2
	 */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((fResidentDomain == null) ? 0 : fResidentDomain.hashCode());
		result = PRIME * result + (int) (fUnderlyingAddress ^ (fUnderlyingAddress >>> 32));
		return result;
	}

	/**
	 * Auto-generated by Eclipse 3.2.2
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final JCImagePointer other = (JCImagePointer) obj;
		if (fResidentDomain == null) {
			if (other.fResidentDomain != null)
				return false;
		} else if (!fResidentDomain.equals(other.fResidentDomain))
			return false;
		if (fUnderlyingAddress != other.fUnderlyingAddress)
			return false;
		return true;
	}
}
