package de.umg.mi.idrt.ioe.OntologyTree;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * @author sa-user
 *
 */
public interface IToolTipEditor
{
	Control getToolTipControl();
	Shell getShell();
	String getToolTipTitle(Point pt);
	String getToolTipText(Point pt);
}