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
	
	/**
	 * STUDY is the root for an ontology.
	 */
	STUDY,
	/**
	 * METAVERS is version of this study.
	 */
	METAVERS,
	/**
	 * STUDYEVENT is the section of a study.
	 */
	STUDYEVENT,
	/**
	 * FORM is for grouping related item groups.
	 */
	FORM,
	/**
	 * ITEMGROUP is for grouping related items.
	 */
	ITEMGROUP,
	/**
	 * ITEM is a question.
	 */
	ITEM,
	/**
	 * ANSWERGROUP can hold other answergroups and answers.
	 */
	ANSWERGROUP,
	/**
	 * ANSWER is an leaf of the ontology tree.
	 */
	ANSWER,
	/**
	 * ROOT is a gernic root node
	 */
	ROOT,
	/**
	 * GROUP is a gernic group node
	 */
	GROUP,
	/**
	 * META is a node used by the import tool and is excluded
	 * from exporting.
	 */
	META,
	
	/**
	 * 
	 * 
	 */
	GENERAL,
	GENERAL_ROOT,
	GENERAL_FOLDER,
	GENERAL_ITEM,
	GENERAL_ANSWER,
	
	TREEROOT,
	I2B2ROOT
	
	
}