package de.umg.mi.idrt.ioe.OntologyTree;

/**
 * This enum is for defining the type of an ontology tree node. Types are
 * taken from the CDISC ODM definitions. The order is as follows:
 * Study -> MetaVers -> StudyEvent -> Form -> ItemGroup -> Item
 * AnswerGroup and Answer are nodes for storing I2B2 search items and
 * are located as children of an Item.
 * 
 * @author	Christian Bauer	
 * @version 0.9
 */

public enum NodeType {
	
	TREEROOT,
	I2B2ROOT,
	
	
}