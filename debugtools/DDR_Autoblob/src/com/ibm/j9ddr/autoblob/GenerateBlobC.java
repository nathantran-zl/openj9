/*******************************************************************************
 * Copyright (c) 2010, 2014 IBM Corp. and others
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
package com.ibm.j9ddr.autoblob;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.xml.sax.SAXException;

import com.ibm.j9ddr.autoblob.config.BlobHeader;
import com.ibm.j9ddr.autoblob.config.Configuration;
import com.ibm.j9ddr.autoblob.datamodel.EnumType;
import com.ibm.j9ddr.autoblob.datamodel.ITypeCollection;
import com.ibm.j9ddr.autoblob.datamodel.StructType;
import com.ibm.j9ddr.autoblob.datamodel.UserDefinedType;
import com.ibm.j9ddr.autoblob.datamodel.Typedef;
import com.ibm.j9ddr.autoblob.datamodel.UnionType;
import com.ibm.j9ddr.autoblob.linenumbers.IHeaderResolver;
import com.ibm.j9ddr.autoblob.linenumbers.PreProcessedSourceParser;
import com.ibm.j9ddr.autoblob.xmlparser.StructureXMLParser;

/**
 * Combines structure data from an XML file generated by the EDG extract_structures tool with
 * constants parsed from C/C++ headers to produce C/C++ blob generation code.
 * 
 * @author andhall
 *
 */
public class GenerateBlobC
{

	private static final String PROPERTIES_ARGUMENT = "-props";
	
	private static final String CFILE_ARGUMENT = "-cfile";
	
	private static final String XMLFILE_ARGUMENT = "-xmlfile";
	
	private static final String OUTFILE_ARGUMENT = "-outfile";
	
	private static final String INCLUDE_ARGUMENT = "-I";
	
	private static final String DEFINE_ARGUMENT = "-D";
	
	private static final String J9_FLAGS = "-j9flags";

	/**
	 * @param args
	 * @throws IOException 
	 * @throws SAXException  
	 */
	public static void main(String[] args) throws IOException, SAXException
	{
		printHeader();
		GenerateBlobCConfig config = handleArgs(args);
		process(config);
	}

	private static void process(GenerateBlobCConfig config) throws IOException, SAXException
	{
		//Do a first pass to extract line numbers
		PreProcessedSourceParser preProcessedSourceParser = new PreProcessedSourceParser(config.cFile, null);
		
		StructureXMLParser parser = new StructureXMLParser(config.xmlFile, preProcessedSourceParser.getLineNumberConvertor());
		writeCFile(config.cFile, parser, config);
	}

	/**
	 * Data class for types parsed from a particular C file.
	 * @author andhall
	 *
	 */
	public static class FileTypes implements ITypeCollection
	{
		FileTypes(File file)
		{
			this.file = file;
		}
		
		public final File file;
		
		public final List<Typedef> typedefs = new LinkedList<Typedef>();
		
		public final Map<String, StructType> structs = new HashMap<String, StructType>();
		
		public final Map<String, UnionType> unions = new HashMap<String, UnionType>();

		public final Map<String, EnumType> enums = new HashMap<String, EnumType>();
		
		public final List<EnumType> anonymousEnums = new LinkedList<EnumType>();
		
		public UserDefinedType findType(String name)
		{
			UserDefinedType toReturn;
			
			toReturn = findTypeIn(name, typedefs);
			if (toReturn != null) {
				return toReturn;
			}
			
			toReturn = findTypeIn(name, structs.values());
			if (toReturn != null) {
				return toReturn;
			}
			
			toReturn = findTypeIn(name, unions.values());
			if (toReturn != null) {
				return toReturn;
			}
			
			toReturn = findTypeIn(name, enums.values());
			if (toReturn != null) {
				return toReturn;
			}
			
			return null;
		}
		
		private UserDefinedType findTypeIn(String name, Collection<? extends UserDefinedType> types)
		{
			for (UserDefinedType t : types) {
				if (t.getName().equals(name)) {
					return t;
				}
			}
			return null;
		}

		public void loadTypes(SortedSet<UserDefinedType> typesToWrite, GenerateBlobCConfig config)
		{
			GenerateBlobC.loadTypes(typesToWrite, typedefs, config);
			GenerateBlobC.loadTypes(typesToWrite, structs.values(), config);
			GenerateBlobC.loadTypes(typesToWrite, enums.values(), config);
			GenerateBlobC.loadTypes(typesToWrite, unions.values(), config);
		}
		
	}
	
	private static void writeCFile(File inputFile, StructureXMLParser parser, GenerateBlobCConfig config) throws IOException, SAXException
	{
		applyInheritanceRelationships(parser, config);
		
		Map<String, FileTypes> typesByFile = extractTypes(config, parser);
		
		PrintWriter out = new PrintWriter(new FileWriter(config.outputFile));
		PrintWriter ssout = new PrintWriter(new FileWriter(config.outputSuperSetFile));
		
		try {
			writeHeader(out, config, parser);
		
			SortedSet<UserDefinedType> typesToWrite = new TreeSet<UserDefinedType>();
			
			loadTypes(typesToWrite, config.autoblobConfiguration.getPseudoTypes().values(), config);
			
			for (BlobHeader header : config.autoblobConfiguration.getBlobHeaders()) {
				String thisFileName = header.getName();
				
				FileTypes types = typesByFile.get(thisFileName);
				
				File file = config.headerResolver.findHeader(header.getName());
				
				if (null == file) {
					continue;
				}
				
				List<UserDefinedType> constantPseudoTypes = header.loadConstants(file, types, parser);
				
				loadTypes(typesToWrite, constantPseudoTypes, config);
				
				if (null != types ) {
					types.loadTypes(typesToWrite, config);
				}
			}
			
			for (UserDefinedType t : typesToWrite) {
				t.writeFieldsAndConstantsAsBlobC(out, ssout);
			}
			
			writeFooter(out, typesToWrite, config, parser);
		} finally {
			out.close();
			ssout.close();
		}
	}
	
	private static void loadTypes(SortedSet<UserDefinedType> typesToWrite, Collection<? extends UserDefinedType> toLoad, GenerateBlobCConfig config)
	{
		for (UserDefinedType t : toLoad) {
			if (t.shouldBeInBlob()) {
				t.applyTypeOverrides(config.autoblobConfiguration);
				typesToWrite.add(t);
			}
		}
	}

	private static void applyInheritanceRelationships(StructureXMLParser parser, GenerateBlobCConfig config)
	{
		Map<String, String> inheritanceRelationships = config.autoblobConfiguration.getInheritanceRelationships();
		for (String subClassName : inheritanceRelationships.keySet()) {
			String superClassName = inheritanceRelationships.get(subClassName);
			
			UserDefinedType subclass = findType(subClassName, parser, config.autoblobConfiguration);
			
			if (null == subclass) {
				System.err.println("Cannot apply inheritance rule " + subClassName + " extends " + superClassName + ": can't match subclass");
				continue;
			}
			
			UserDefinedType superclass = findType(superClassName, parser, config.autoblobConfiguration);
			
			if (null == superclass) {
				System.err.println("Cannot apply inheritance rule " + subClassName + " extends " + superClassName + ": can't match superclass");
				continue;
			}
			
			subclass.setSuperClass(superclass);
		}
	}

	private static UserDefinedType findType(String name, StructureXMLParser parser, Configuration config)
	{
		/* Checking PseudoTypes first allows us to override existing types with PseudoTypes. */
		UserDefinedType t = config.getPseudoTypes().get(name);
		
		if (t == null) {
			t = parser.findType(name);
		}
		
		return t;
	}

	private static void writeHeader(PrintWriter out, GenerateBlobCConfig config, StructureXMLParser parser)
	{
		out.println("/* Generated by J9DDR GenerateBlobC - DO NOT EDIT DIRECTLY */");
		out.println();
		out.println("#include \"j9ddr.h\"");
		config.autoblobConfiguration.writeCIncludes(out);
		out.println();
		
		if (parser.readCPlusPlus()) {
			String className = getCPlusPlusClassName(config);
			out.println("class " + className);
			out.println("{");
			out.println("public:");
			out.println("\tstatic const J9DDRStructDefinition* getStructTable();");
			out.println("};");
			out.println();
			out.println("const J9DDRStructDefinition* " + className + "::getStructTable()");
			out.println("{");
		}
	}

	private static String getCPlusPlusClassName(GenerateBlobCConfig config)
	{
		String className = "J9DDRStructTable" + config.autoblobConfiguration.getName();
		return className;
	}

	private static void writeFooter(PrintWriter out, Set<UserDefinedType> writtenOutTypes, GenerateBlobCConfig config, StructureXMLParser parser)
	{
		if (parser.readCPlusPlus()) {
			writeCPlusPlusFooter(out,writtenOutTypes, config);
		} else {
			writeCFooter(out,writtenOutTypes, config);
		}
	}

	private static void writeCFooter(PrintWriter out, Set<UserDefinedType> writtenOutTypes, GenerateBlobCConfig config)
	{
		out.println();
		out.println("J9DDRStructTableBegin(" + config.autoblobConfiguration.getName() + ")");
		for (UserDefinedType type : writtenOutTypes) {
			type.writeStructDefinitionAsBlobC(out);
		}
		out.println("J9DDRStructTableEnd");
		out.println();
		out.println("const J9DDRStructDefinition* get" + config.autoblobConfiguration.getName() + "StructTable(void * portLib)");
		out.println("{");
		out.println("\treturn J9DDR_" + config.autoblobConfiguration.getName() + "_structs;");
		out.println("}");
	}

	private static void writeCPlusPlusFooter(PrintWriter out,
			Set<UserDefinedType> writtenOutTypes, GenerateBlobCConfig config)
	{
		out.println();
		out.println("static J9DDRStructTableBegin(" + config.autoblobConfiguration.getName() + ")");
		for (UserDefinedType type : writtenOutTypes) {
			type.writeStructDefinitionAsBlobC(out);
		}
		out.println("J9DDRStructTableEnd");
		out.println();
		out.println("\treturn J9DDR_" + config.autoblobConfiguration.getName() + "_structs;");
		out.println("}");
		out.println("extern \"C\" {");
		out.println();
		out.println("\tconst J9DDRStructDefinition* get" + config.autoblobConfiguration.getName() + "StructTable(void * portLib)");
		out.println("\t{");
		out.println("\t\treturn " + getCPlusPlusClassName(config) + "::getStructTable();");
		out.println("\t}");
		out.println();
		out.println("}");
	}
	
	private static Map<String, FileTypes> extractTypes(GenerateBlobCConfig config, StructureXMLParser parser) throws IOException
	{
		Map<String, FileTypes> typesByAbsolutePath = extractTypesByAbsolutePath(parser);
		Map<String, FileTypes> typesByHeaderName = stripPaths(typesByAbsolutePath);
		Map<String, String> absolutePathsByHeaderName = mapFileNamesToAbsolutePaths(typesByAbsolutePath.keySet());
		
		Map<String, FileTypes> typesByShortPath = new HashMap<String, FileTypes>();
		
		for (BlobHeader header: config.autoblobConfiguration.getBlobHeaders()) {
			String headerName = header.getName();
			File headerPath = config.headerResolver.findHeader(headerName);
			
			if (headerPath == null) {
				continue;
			}
			
			String absolutePath = headerPath.getCanonicalPath();
			FileTypes types = typesByAbsolutePath.get(absolutePath);
			
			if (types != null) {
				typesByShortPath.put(headerName, types);
			} else {
				/*
				 * CMVC 193975 - adam
				 * 
				 * When running the build on Windows if a relative path has been used with / delimeters for the name
				 * e.g. common/thrtypes , then Windows will treat the entire string as the file name rather than a directory
				 * followed by a file name as Linux does. In order to correctly fallback to match just the name of the 
				 * header file on Window's systems, only the characters after the last / can be used.
				 */
				int pos = headerName.lastIndexOf('/');
				if((pos != -1) && (pos < (headerName.length() + 1))) {
					System.err.println("Relative header name character found in : " + headerName);
					headerName = headerName.substring(pos + 1);
					System.err.println("Now trying to match header name : " + headerName);
				}
				
				/* Jazz 45699 - lpnguyen
				 * There is a known issue where with #includes using relative paths we can end up with headers in the 
				 * typesByAbsolutePath collection from outside the current working directory (ddr/gc_ddr).  
				 * 
				 * We'll fall back to searching based on just headerName in this case to find its FileTypes 
				 */
				
				types = typesByHeaderName.get(headerName);
				if (types != null) {
					typesByShortPath.put(header.getName(), types);
					System.err.println("Warning: Using " + absolutePathsByHeaderName.get(headerName) + " instead of " + absolutePath);
				}
			}
		}
		
		return typesByShortPath;
	}
	
	/* Generate mapping between header name and their absolute paths */
	private static Map<String, String> mapFileNamesToAbsolutePaths(Collection<String> absolutePaths)
	{
		Map<String, String> absolutePathsByHeaderName = new HashMap<String, String>();
		
		for (String absolutePath : absolutePaths) {
			String fileName = new File(absolutePath).getName();
			String oldValue = absolutePathsByHeaderName.put(fileName, absolutePath);
			if (null != oldValue) {
				System.err.println("Warning: duplicate headers:" + oldValue + " and " + absolutePath + " found");
			}
		}
		
		return absolutePathsByHeaderName;
	}
	
	/* Strip the absolute path in the typesByAbsolutePath mapping keyset */ 
	private static Map<String, FileTypes> stripPaths(Map<String, FileTypes> typesByAbsolutePath)
	{
		Map<String, FileTypes> typesByFileName = new HashMap<String, FileTypes>();
		
		for (String fileName : typesByAbsolutePath.keySet()) {
			String strippedFileName = new File(fileName).getName();
			typesByFileName.put(strippedFileName, typesByAbsolutePath.get(fileName));
			//will report overwrites in mapFileNamesToAbsolutePaths()
		}
		
		return typesByFileName;
	}
	
	private static Map<String, FileTypes> extractTypesByAbsolutePath(StructureXMLParser parser)
	{
		Map<String, FileTypes> typesByFile = new HashMap<String, FileTypes>();
		
		//Typedefs
		for (Typedef typedef : parser.getTypedefs().values()) {
			String filename = typedef.getDefinitionLocation() == null ? "<unknown>" : typedef.getDefinitionLocation().getFileName();
			
			FileTypes types = typesByFile.get(filename);
			
			if (null == types) {
				types = new FileTypes(new File(filename));
				typesByFile.put(filename,types);
			}
			
			types.typedefs.add(typedef);
		}
		
		for (StructType struct : parser.getStructures()) {
			String filename = struct.getDefinitionLocation() == null ? "<unknown>" : struct.getDefinitionLocation().getFileName();
			
			FileTypes types = typesByFile.get(filename);
			
			if (null == types) {
				types = new FileTypes(new File(filename));
				typesByFile.put(filename,types);
			}
			
			types.structs.put(struct.getName(), struct);
		}
		
		for (UnionType union : parser.getUnions()) {
			String filename = union.getDefinitionLocation() == null ? "<unknown>" : union.getDefinitionLocation().getFileName();
			
			FileTypes types = typesByFile.get(filename);
			
			if (null == types) {
				types = new FileTypes(new File(filename));
				typesByFile.put(filename,types);
			}
			
			types.unions.put(union.getName(), union);
		}
		
		for (EnumType thisEnum : parser.getEnums()) {
			String filename = thisEnum.getDefinitionLocation() == null ? "<unknown>" : thisEnum.getDefinitionLocation().getFileName();
			
			FileTypes types = typesByFile.get(filename);
			
			if (null == types) {
				types = new FileTypes(new File(filename));
				typesByFile.put(filename,types);
			}
			
			types.enums.put(thisEnum.getName(), thisEnum);
		}
		
		for (EnumType thisEnum : parser.getTopScopeAnonymousEnums()) {
			String filename = thisEnum.getDefinitionLocation() == null ? "<unknown>" : thisEnum.getDefinitionLocation().getFileName();
			
			FileTypes types = typesByFile.get(filename);
			
			if (null == types) {
				types = new FileTypes(new File(filename));
				typesByFile.put(filename,types);
			}
			
			types.anonymousEnums.add(thisEnum);
		}
		
		return typesByFile;
	}

	private static void printHeader()
	{
		System.out.println("J9DDR Automatic DDR Structure Blob Generator");
	}

	private static GenerateBlobCConfig handleArgs(String[] args) throws IOException, SAXException
	{
		File configFile = null;
		File cFile = null;
		File xmlFile = null;
		File outputFile = null;
		File j9FlagsFile = null;
		List<File> includeSearchPath = new LinkedList<File>();
		
		for (int i=0; i < args.length; i++) {
			String currentArg = args[i];
			boolean argConsumed = false;
			
			if (i < args.length - 1) {
				if (currentArg.equalsIgnoreCase(PROPERTIES_ARGUMENT)) {
					configFile = new File(args[i + 1]);
					//Step over value
					i++;
					argConsumed = true;
				} else if (currentArg.equalsIgnoreCase(CFILE_ARGUMENT)) {
					cFile = new File(args[i + 1]);
					i++;
					argConsumed = true;
				} else if (currentArg.equalsIgnoreCase(XMLFILE_ARGUMENT)) {
					xmlFile = new File(args[i + 1]);
					i++;
					argConsumed = true;
				} else if (currentArg.equalsIgnoreCase(OUTFILE_ARGUMENT)) {
					outputFile = new File(args[i + 1]);
					i++;
					argConsumed = true;
				} else if (currentArg.equalsIgnoreCase(J9_FLAGS)) {
					j9FlagsFile = new File(args[i + 1]);
					i++;
					argConsumed = true;
				}
			}
			
			if (currentArg.startsWith(INCLUDE_ARGUMENT)) {
				File path = new File(currentArg.substring(INCLUDE_ARGUMENT.length()));
				
				includeSearchPath.add(path);
				argConsumed = true;
			} else if (currentArg.startsWith(DEFINE_ARGUMENT)) {
				//Ignore
				argConsumed = true;
			}
			
			if (! argConsumed) {
				System.err.println("Couldn't consume argument: " + currentArg);
				System.exit(1);
			}
		}
		
		boolean badConfig = false;
		if (null == configFile) {
			System.err.println(PROPERTIES_ARGUMENT + " not specified");
			badConfig = true;
		}
		
		if (null == cFile) {
			System.err.println(CFILE_ARGUMENT + " not specified");
			badConfig = true;
		}
		
		if (null == outputFile) {
			System.err.println(OUTFILE_ARGUMENT + " not specified");
			badConfig = true;
		}
		
		if (null == j9FlagsFile) {
			System.err.println(J9_FLAGS + " not specified");
			badConfig = true;
		}
		
		if (badConfig) {
			usage();
			System.exit(1);
		}
		
		return new GenerateBlobCConfig(cFile, xmlFile, outputFile, Configuration.loadConfiguration(configFile, j9FlagsFile), new HeaderResolver(includeSearchPath));
	}
	
	static class HeaderResolver implements IHeaderResolver
	{
		
		private final List<File> includePath;
		
		public HeaderResolver(List<File> includePath)
		{
			this.includePath = includePath;
		}
		
		public File findHeader(String name)
		{
			for (File path : includePath) {
				File possibleFile = new File(path, name);
				if (possibleFile.exists()) {
					return possibleFile;
				}
			}
			
			return null;
		}
		
	}

	private static void usage()
	{
		System.err.println(GenerateBlobC.class.getSimpleName());
		System.err.println("Usage:");
		System.err.println("java " + GenerateBlobC.class.getName() + "-props <config properties file> -infile <pre-processed input> -outfile <output file> -j9flags <j9.flags file> [-I<include path entry>]+");
		System.err.println();
		System.err.println("Note: this tool currently cannot handle C++.");
	}

	static class GenerateBlobCConfig
	{
		public final File cFile;
		
		public final File xmlFile;
		
		public final File outputFile;
		
		public final File outputSuperSetFile;
		
		public final Configuration autoblobConfiguration;
		
		public final IHeaderResolver headerResolver;
		
		public GenerateBlobCConfig(File cFile, File xmlFile, File outputFile, Configuration configuration, IHeaderResolver headerResolver)
		{
			this.cFile = cFile;
			this.xmlFile = xmlFile;
			this.outputFile = outputFile;
			outputSuperSetFile = new File(outputFile.getAbsolutePath() + ".superset");
			this.autoblobConfiguration = configuration;
			this.headerResolver = headerResolver;
		}
	}
	
}
