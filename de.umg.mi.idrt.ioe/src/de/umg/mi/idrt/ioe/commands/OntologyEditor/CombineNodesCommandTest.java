package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNode;
import de.umg.mi.idrt.ioe.misc.Regex;

public class CombineNodesCommandTest {
	private List<OntologyTreeNode> oldTreeNodeList;
	private List<OntologyTreeNode> newTreeNodeList;
	private static String ICDREGEX = "[A-TV-Z][0-9][A-Z0-9](\\.[A-Z0-9]{1,4})?";
	private static String currentRegex = "ICD";
	CombineNodesCommand comm;
	
	@Before
	public void setUp() throws Exception {
		oldTreeNodeList  = new LinkedList<OntologyTreeNode>();
		newTreeNodeList = new LinkedList<OntologyTreeNode>();
		comm = new CombineNodesCommand();
		CombineNodesCommand.addRegEx(new Regex("ICD",ICDREGEX,"c_basecode"));
		
		
	}

	@Test
	public void testAddRegEx() {
		fail("Not yet implemented");
	}

	@Test
	public void testClear() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetRegex() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveRegex() {
		fail("Not yet implemented");
	}

	@Test
	public void testExecute() {
		fail("Not yet implemented");
	}

	@Test
	public void testGeneratePerfectPath() {
		String perfPath = comm.generatePerfectPath(oldTreeNodeList, newTreeNodeList, currentRegex);
		
		
		
		System.out.println(perfPath);
	}

	@Test
	public void testGetnewTargetNodes() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetOldTargetNodes() {
		fail("Not yet implemented");
	}

	@Test
	public void testMergeLeafs() {
		fail("Not yet implemented");
	}

}
