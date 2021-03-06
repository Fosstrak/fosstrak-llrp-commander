/*
 *  
 *  Fosstrak LLRP Commander (www.fosstrak.org)
 * 
 *  Copyright (C) 2008 ETH Zurich
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/> 
 *
 */

package org.fosstrak.llrp.commander.util;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.util.LinkedList;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.llrp.ltkGenerator.CodeGenerator;
import org.llrp.ltkGenerator.generated.Annotation;
import org.llrp.ltkGenerator.generated.ChoiceDefinition;
import org.llrp.ltkGenerator.generated.ChoiceReference;
import org.llrp.ltkGenerator.generated.Description;
import org.llrp.ltkGenerator.generated.EnumerationDefinition;
import org.llrp.ltkGenerator.generated.FieldDefinition;
import org.llrp.ltkGenerator.generated.LlrpDefinition;
import org.llrp.ltkGenerator.generated.MessageDefinition;
import org.llrp.ltkGenerator.generated.ParameterDefinition;
import org.llrp.ltkGenerator.generated.ParameterReference;
import org.w3c.dom.Element;

/**
 * This class can be used to get information on the LLRP protocol.
 * For example, to learn the definition of the "ADD_ROSPEC" message, call
 * <code>LLRP.getMessageDefinition("ADD_ROSPEC").
 * 
 * @author Ulrich Etter, ETHZ
 *
 */
public class LLRP {
	
	private static final String DEFINITIONS_NAME = "Definitions";
	
	private static Logger log = Logger.getLogger(LLRP.class);

	static {
		try {
			String target = System.getProperty("java.io.tmpdir") + "/";
			log.debug(target);
			
			String path = LLRP.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			log.debug(path);
			String decodedPath = URLDecoder.decode(path, "UTF-8");
			log.debug(decodedPath);
			new JarFolderExtractor().extractDirectoryFromJar(decodedPath, DEFINITIONS_NAME, target);
			
			String namespacePrefix = "llrp";
			String jaxBPackage = "org.llrp.ltkGenerator.generated";
			String xmlPath = target + "/" + DEFINITIONS_NAME + "/Core/llrp-1x0-def.xml";
			String xsdPath = target + "/" + DEFINITIONS_NAME + "/Core/llrp-1x0.xsd";
			String[] extensions = {namespacePrefix + ";" + xmlPath + ";" + xsdPath};
			
			log.debug("create new code generator...");
			CodeGenerator cg = new CodeGenerator();					
			log.debug("... generated code generator. deserializing llrp definitions...");
			llrpDefinition = cg.getLLRPDefinition(jaxBPackage, extensions);
			log.debug("deserialized llrp definitions.");
		} catch (IOException e) {
			log.error("could not copy or deserialize the LLRP definition into a temporary folder: ", e);
			System.exit(-1);
		}
		log.debug("done.");
	}
	
	private static LlrpDefinition llrpDefinition;
	
	/**
	 * Returns the LLRP definition object.
	 * 
	 * @return the LLRP definition object
	 */
	public static LlrpDefinition getLlrpDefintion(){
		return llrpDefinition;
	}
    
    /**
     * Returns the definition of the message with the given name.
     * @param messageName the name of a message
     * @return the definition object, or <code>null</code> if no message with the given name exists
     */
    public static MessageDefinition getMessageDefinition(String messageName) {
    	MessageDefinition messageDefinition = null; 
		List<Object> list = llrpDefinition.getMessageDefinitionOrParameterDefinitionOrChoiceDefinition();
		for (Object o : list){
			if (o instanceof MessageDefinition){
				if (((MessageDefinition) o).getName().equals(messageName)){
					messageDefinition = (MessageDefinition) o;
					break;
				}
			}
		}
		return messageDefinition;
	}
    
    /**
     * Returns the definition of the parameter with the given name. 
     * @param parameterName the name of a parameter
     * @return the definition object, or <code>null</code> if no parameter with the given name exists
     */
    public static ParameterDefinition getParameterDefinition(String parameterName) {
		ParameterDefinition parameterDefinition = null; 
		List<Object> list = llrpDefinition.getMessageDefinitionOrParameterDefinitionOrChoiceDefinition();
		for (Object o : list){
			if (o instanceof ParameterDefinition){
				if (((ParameterDefinition) o).getName().equals(parameterName)){
					parameterDefinition = (ParameterDefinition) o;
					break;
				}
			}
		}
		return parameterDefinition;
	}
    
    /**
     * Returns the definition of the choice with the given name. 
     * @param choiceName the name of a choice
     * @return the definition object, or <code>null</code> if no choice with the given name exists
     */
    public static ChoiceDefinition getChoiceDefinition(String choiceName) {
		ChoiceDefinition choiceDefinition = null; 
		List<Object> list = llrpDefinition.getMessageDefinitionOrParameterDefinitionOrChoiceDefinition();
		for (Object o : list){
			if (o instanceof ChoiceDefinition){
				if (((ChoiceDefinition) o).getName().equals(choiceName)){
					choiceDefinition = (ChoiceDefinition) o;
					break;
				}
			}
		}
		return choiceDefinition;
	}
	
    /**
     * Returns the definition of the field with the given name of the message/parameter with the given definition.
     * 
     * @param messageOrParameterDefinition either a <code> MessageDefinition</code> or a <code>ParameterDefinition</code>
     * @param fieldName the name of a field
     * @return the definition of the field, or <code>null</code> if no field with the given name exists 
     * in the message/parameter with the given definition
     */
    public static FieldDefinition getFieldDefinition(Object messageOrParameterDefinition, String fieldName){
    	FieldDefinition fieldDefinition = null;
    	List<Object> fieldOrReservedList = new LinkedList<Object>();
    	if (messageOrParameterDefinition instanceof MessageDefinition){
    		fieldOrReservedList = ((MessageDefinition) messageOrParameterDefinition).getFieldOrReserved();
    	}
    	else if (messageOrParameterDefinition instanceof ParameterDefinition){
    		fieldOrReservedList = ((ParameterDefinition) messageOrParameterDefinition).getFieldOrReserved();
    	}
		for (Object o : fieldOrReservedList){
			if (o instanceof FieldDefinition){
				FieldDefinition fd = ((FieldDefinition) o);
				if (fd.getName().equals(fieldName)){
					fieldDefinition = fd;
					break;
				}
			}
		}
    	return fieldDefinition;
    }
    
    /**
     * Returns the definitions of all fields of the message/parameter with the given name.
     * 
     * @param messageOrParameterDefinition either a <code> MessageDefinition</code> or a <code>ParameterDefinition</code>
     * @return the definitions of all fields of the message/parameter with the given name
     */
    public static List<FieldDefinition> getFieldDefinitions(Object messageOrParameterDefinition){
		List<Object> fieldOrReservedList = new LinkedList<Object>();
		List<FieldDefinition> fieldDefinitions = new LinkedList<FieldDefinition>();
		if (messageOrParameterDefinition instanceof MessageDefinition){
			fieldOrReservedList = ((MessageDefinition) messageOrParameterDefinition).getFieldOrReserved();
		}
		else if (messageOrParameterDefinition instanceof ParameterDefinition){
			fieldOrReservedList = ((ParameterDefinition) messageOrParameterDefinition).getFieldOrReserved();
		}
		for (int i = 0; i < fieldOrReservedList.size(); i++){
			if (fieldOrReservedList.get(i) instanceof FieldDefinition){
				fieldDefinitions.add((FieldDefinition) fieldOrReservedList.get(i));
			}
		}
		return fieldDefinitions;
    }
    
    /**
     * Returns the definition of the enumeration with the given name.
     * @param enumerationName the name of an enumeration
     * @return the definition of the enumeration, or <code>null</code> if no enumeration with the given name exists
     */
    public static EnumerationDefinition getEnumerationDefinition(String enumerationName){
    	List<Object> list = llrpDefinition.getMessageDefinitionOrParameterDefinitionOrChoiceDefinition();
		for (Object o : list){
			if (o instanceof EnumerationDefinition){
				if (((EnumerationDefinition) o).getName().equals(enumerationName)){
					return (EnumerationDefinition) o;
				}
			}
		}
		return null;
    }
	
	/**
	 * Returns the description of the message/parameter with the given definition as a HTML string.
	 * 
	 * @param messageOrParameterDefinition either a <code> MessageDefinition</code> or a <code>ParameterDefinition</code>
	 * @return the description of the message/parameter with the given definition as a HTML string
	 */
	public static String getDescription(Object messageOrParameterDefinition){
		String descriptionString = "";
		java.util.List<Annotation> annotations = getAnnotation(messageOrParameterDefinition);
		if (!annotations.isEmpty()){
			// just use the first annotation
			Annotation firstAnnotation = annotations.get(0);
			if (firstAnnotation != null){
				java.util.List<Object> documentationOrDescription = firstAnnotation.getDocumentationOrDescription();
				Description description = null;
				for (Object o : documentationOrDescription){
					if (o instanceof Description){
						// just use the first description
						description = (Description) o;
						break;
					}
				}
				if (description != null){
					java.util.List<Object> content = description.getContent();
					for (Object o : content){
						if (o instanceof Element){
							Element element = (Element) o;
							
							try{
						      // Set up the output transformer
						      TransformerFactory transfac = TransformerFactory.newInstance();
						      Transformer trans = transfac.newTransformer();
						      trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
						      trans.setOutputProperty(OutputKeys.INDENT, "no");

						      // Print the DOM node
						      StringWriter sw = new StringWriter();
						      StreamResult result = new StreamResult(sw);
						      DOMSource source = new DOMSource(element);
						      trans.transform(source, result);
						      String xmlString = sw.toString();
						      final String NAMESPACE_PREFIX = "h:";
						      xmlString = xmlString.replace(NAMESPACE_PREFIX, "");
						      descriptionString = descriptionString + xmlString;
							}
							catch (TransformerException e) {
								log.error("could not transform the XML element", e);
							}
						}
						else if (o instanceof String){
							descriptionString = descriptionString + ((String) o).trim();
						}
					}

				}
			}
		}
		return descriptionString;
	}
	
	private static List<Annotation> getAnnotation(Object messageOrParameterDefinition){
		List<Annotation> annotations = new LinkedList<Annotation>();
		if (messageOrParameterDefinition instanceof MessageDefinition){
			annotations = ((MessageDefinition) messageOrParameterDefinition).getAnnotation();
		}
		else if (messageOrParameterDefinition instanceof ParameterDefinition){
			annotations = ((ParameterDefinition) messageOrParameterDefinition).getAnnotation();
		}
		return annotations;
	}
	
    /**
     * Returns the names of all parameters and choices of the message/parameter with the given definition.
     * @param messageOrParameterDefinition either a <code> MessageDefinition</code> or a <code>ParameterDefinition</code>
     * @return the names of all parameters and choices of the message/parameter with the given definition
     */
    public static List<String> getParameterAndChoiceNames(Object messageOrParameterDefinition) {
    	List<String> childrenNames = new LinkedList<String>();

		List<Object> parameterOrChoiceList = new LinkedList<Object>();
		if (messageOrParameterDefinition instanceof MessageDefinition){
			parameterOrChoiceList = ((MessageDefinition) messageOrParameterDefinition).getParameterOrChoice();
		}
		else if (messageOrParameterDefinition instanceof ParameterDefinition){
			parameterOrChoiceList = ((ParameterDefinition) messageOrParameterDefinition).getParameterOrChoice();
		}
		for (int i = 0; i < parameterOrChoiceList.size(); i++){
			Object o = parameterOrChoiceList.get(i);
			String name = "";
			if (o instanceof ParameterReference){
				ParameterReference parameterReference = (ParameterReference) o;
				name = parameterReference.getType();
			}
			else if (o instanceof ChoiceReference){
				ChoiceReference choiceReference = (ChoiceReference) o;
				name = choiceReference.getType();
			}
			childrenNames.add(name);
		}
		return childrenNames;
	}
    
	/**
	 * Returns the type of the field with the given definition.
	 * @param fieldDefinition the definition of the field
	 * @return the type of the field with the given definition
	 */
	public static String getFieldType(FieldDefinition fieldDefinition) {
		String result;
		org.llrp.ltkGenerator.Utility utility = new org.llrp.ltkGenerator.Utility(null);
		String baseType = utility.convertType(fieldDefinition.getType().value());
		
		if (fieldDefinition.getFormat() != null){
			String format = fieldDefinition.getFormat().value();
			result = baseType + "_" + format.toUpperCase();
		}
		else{
			result = baseType;
		}
		return result;
	}
    
    /**
     * Returns <code>true</code> if the field with the given definition is an enumeration, 
     * and <code>false</code> otherwise.
     * 
     * @param fieldDefinition the definition of the field
     * @return true if the field is an enumeration, false otherwise.
     */
    public static boolean isEnumeration(FieldDefinition fieldDefinition){
    	return fieldDefinition.getEnumeration() != null;
    }
    
    /**
     * Returns <code>true</code> if the parameter/choice with the given name can occur multiple times in 
     * the message/parameter with the given definition, 
     * and <code>false</code> otherwise.
     * 
     * @param messageOrParameterDefinition either a <code> MessageDefinition</code> or a <code>ParameterDefinition</code>
     * @param parameterOrChoiceName the name of a parameter/choice
     * @return true if a parameter can occur multiple times, false otherwise.
     */
    public static boolean canOccurMultipleTimes(Object messageOrParameterDefinition, String parameterOrChoiceName){
    	boolean result = false;
    	String repeat = getRepeat(messageOrParameterDefinition, parameterOrChoiceName);
    	if (repeat.equals("0-N") || repeat.equals("1-N")){
			result = true;
		}
		return result;
    }

	/**
	 * Returns <code>true</code> if the parameter/choice with the given name must occur at least once in 
     * the message/parameter with the given definition, 
     * and <code>false</code> otherwise.
	 * 
	 * @param messageOrParameterDefinition either a <code> MessageDefinition</code> or a <code>ParameterDefinition</code>
	 * @param parameterOrChoiceName the name of a parameter/choice
	 * @return true if parameter must occur at least once, false otherwise.
	 */
	public static boolean mustOccurAtLeastOnce(Object messageOrParameterDefinition, String parameterOrChoiceName) {
		boolean result = false;
    	String repeat = getRepeat(messageOrParameterDefinition, parameterOrChoiceName);
    	if (repeat.equals("1") || repeat.equals("1-N")){
			result = true;
		}
		return result;
	}
	
	private static String getRepeat(Object messageOrParameterDefinition, String childName){
		String result = "";
		List<Object> parameterOrChoiceList = new LinkedList<Object>();
		if (messageOrParameterDefinition instanceof MessageDefinition){
			parameterOrChoiceList = ((MessageDefinition) messageOrParameterDefinition).getParameterOrChoice();
		}
		else if (messageOrParameterDefinition instanceof ParameterDefinition){
			parameterOrChoiceList = ((ParameterDefinition) messageOrParameterDefinition).getParameterOrChoice();
		}
		for (int i = 0; i < parameterOrChoiceList.size(); i++){
			Object o = parameterOrChoiceList.get(i);
			String name = "";
			String repeat = "";
			if (o instanceof ParameterReference){
				ParameterReference parameterReference = (ParameterReference) o;
				name = parameterReference.getType();
				repeat = parameterReference.getRepeat();
			}
			else if (o instanceof ChoiceReference){
				ChoiceReference choiceReference = (ChoiceReference) o;
				name = choiceReference.getType();
				repeat = choiceReference.getRepeat();
			}
			if (name.equals(childName)){
				result = repeat;
				break;
			}
		}
		return result;
	}

	/**
	 * Returns <code>true</code> if the parameter/choice with the given name, defined in the 
	 * message/parameter with the given definition, is a choice, 
     * and <code>false</code> otherwise.
	 * 
	 * @param messageOrParameterDefinition either a <code> MessageDefinition</code> or a <code>ParameterDefinition</code>
	 * @param parameterOrChoiceName the name of a parameter/choice
	 * @return true if the parameter is a choise parameter, false otherwise.
	 */
	public static boolean isChoice(Object messageOrParameterDefinition, String parameterOrChoiceName) {
		boolean result = false;
		List<Object> parameterOrChoiceList = new LinkedList<Object>();
		if (messageOrParameterDefinition instanceof MessageDefinition){
			parameterOrChoiceList = ((MessageDefinition) messageOrParameterDefinition).getParameterOrChoice();
		}
		else if (messageOrParameterDefinition instanceof ParameterDefinition){
			parameterOrChoiceList = ((ParameterDefinition) messageOrParameterDefinition).getParameterOrChoice();
		}
		for (Object o : parameterOrChoiceList){
			String name = "";
			if (o instanceof ChoiceReference){
				ChoiceReference choiceReference = (ChoiceReference) o;
				name = choiceReference.getType();
				result = true;
			}
			else if (o instanceof ParameterReference){
				ParameterReference parameterReference = (ParameterReference) o;
				name = parameterReference.getType();
				result = false;
			}
			if (name.equals(parameterOrChoiceName)){
				break;
			}
		}
		return result;
	}
}
