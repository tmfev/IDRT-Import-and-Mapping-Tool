package de.umg.mi.idrt.ioe.OntologyTree;


import org.eclipse.swt.widgets.Widget;

/**
 * @author sa-user
 *
 */
public interface ToolTipHelpTextHandler
{
    /**
     * Get help text
     * 
     * @param widget
     *            the widget that is under help
     * @return a help text string
     */
    public String getHelpText(Widget widget);
}