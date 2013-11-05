package de.umg.mi.idrt.ioe.view;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.FocusCellHighlighter;
import org.eclipse.jface.viewers.FocusCellOwnerDrawHighlighter;
import org.eclipse.jface.viewers.TreeViewerFocusCellManager;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerRow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.GCData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

/**
 * Hack for solving the jface Bug 268135 (https://bugs.eclipse.org/bugs/show_bug.cgi?id=268135) [Viewers] [CellEditors]
 * Table with SWT.MULTI and TableViewerEditor problem. 
 * 
 * @see FocusCellOwnerDrawHighlighter
 */
public class FocusCellOwnerDrawHighlighterForMultiselection extends FocusCellHighlighter {
    
    private static final Logger LOGGER = Logger.getLogger(FocusCellOwnerDrawHighlighterForMultiselection.class);
    
    /**
     * Create a new instance which can be passed to a {@link TreeViewerFocusCellManager}
     * @param viewer the viewer
     */
    public FocusCellOwnerDrawHighlighterForMultiselection(ColumnViewer viewer) {
        super(viewer);
        hookListener(viewer);
    }

    @Override
    protected void focusCellChanged(ViewerCell newCell, ViewerCell oldCell) {
        super.focusCellChanged(newCell, oldCell);

        // Redraw new area
        if (newCell != null) {
            Rectangle rect = newCell.getBounds();
            int x = newCell.getColumnIndex() == 0 ? 0 : rect.x;
            int width = newCell.getColumnIndex() == 0 ? rect.x + rect.width : rect.width;
            // 1 is a fix for Linux-GTK
            newCell.getControl().redraw(x, rect.y - 1, width, rect.height + 1, true);
        }

        if (oldCell != null) {
            Rectangle rect = oldCell.getBounds();
            int x = oldCell.getColumnIndex() == 0 ? 0 : rect.x;
            int width = oldCell.getColumnIndex() == 0 ? rect.x + rect.width : rect.width;
            // 1 is a fix for Linux-GTK
            oldCell.getControl().redraw(x, rect.y - 1, width, rect.height + 1, true);
        }
    }
 
    /**
     * The color to use when rendering the background of the selected cell when the control has the input focus
     * @param cell the cell which is colored
     * @return the color or <code>null</code> to use the default
     */
    protected Color getSelectedCellBackgroundColor(ViewerCell cell) {
    	Device dev = new Device() {
			
			@Override
			public void internal_dispose_GC(int hDC, GCData data) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public int internal_new_GC(GCData data) {
				// TODO Auto-generated method stub
				return 0;
			}
		};
		Color col = new Color(dev,203,232,246);
		return col;
//        return cell.getItem().getDisplay().getSystemColor(new Color(dev,203,232,246));
    }
  
    /**
     * The color to use when rendering the background of the selected cell when the control has <b>no</b> input focus
     * @param cell the cell which is colored
     * @return the color or <code>null</code> to use the same used when control has focus
     * @since 3.4
     */
    protected Color getSelectedCellBackgroundColorNoFocus(ViewerCell cell) {
        return null;
    }

    /**
     * The color to use when rendering the foreground (=text) of the selected cell when the control has the input focus
     * @param cell the cell which is colored
     * @return the color or <code>null</code> to use the default
     */
    protected Color getSelectedCellForegroundColor(ViewerCell cell) {
        return null;
    }

    /**
     * The color to use when rendering the foreground (=text) of the selected cell when the control has <b>no</b> input
     * focus
     * @param cell the cell which is colored
     * @return the color or <code>null</code> to use the same used when control has focus
     * @since 3.4
     */
    protected Color getSelectedCellForegroundColorNoFocus(ViewerCell cell) {
        return null;
    }

    private void hookListener(final ColumnViewer viewer) {

        Listener listener = new Listener() {

            public void handleEvent(Event event) {
                if ((event.detail & SWT.SELECTED) > 0) {
                    ViewerCell focusCell = getFocusCell();

                    try {
                        Method m = viewer.getClass().getDeclaredMethod("getViewerRowFromItem", Widget.class);
                        boolean access = m.isAccessible();
                        if (!access) {
                            m.setAccessible(true);
                        }
                        ViewerRow row = (ViewerRow) m.invoke(viewer, event.item);
                        m.setAccessible(access);
                        
                        Assert.isNotNull(row,
                                "Internal structure invalid. Item without associated row is not possible."); //$NON-NLS-1$

                        ViewerCell cell = row.getCell(event.index);

                        if (focusCell != null && cell.equals(focusCell)) {
                            markFocusedCell(event, cell);
                        }
                    } catch (Exception e) {
                        LOGGER.error(e.getLocalizedMessage(), e);
                    }

                }
            }

        };
        viewer.getControl().addListener(SWT.EraseItem, listener);
    }

    private void markFocusedCell(Event event, ViewerCell cell) {
        Color background = (cell.getControl().isFocusControl()) ? getSelectedCellBackgroundColor(cell)
                : getSelectedCellBackgroundColorNoFocus(cell);
        Color foreground = (cell.getControl().isFocusControl()) ? getSelectedCellForegroundColor(cell)
                : getSelectedCellForegroundColorNoFocus(cell);

        if (foreground != null || background != null || onlyTextHighlighting(cell)) {
            GC gc = event.gc;

            if (background == null) {
                background = cell.getItem().getDisplay().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
//                background = cell.getItem().getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION);
            }

            if (foreground == null) {
                foreground = cell.getItem().getDisplay().getSystemColor(SWT.COLOR_BLACK);
//                foreground = cell.getItem().getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT);
            }

            gc.setBackground(background);
            gc.setForeground(foreground);

            if (onlyTextHighlighting(cell)) {
                Rectangle area = event.getBounds();
                Rectangle rect = cell.getTextBounds();
                if (rect != null) {
                    area.x = rect.x;
                }
                gc.fillRectangle(area);
            } else {
                gc.fillRectangle(event.getBounds());
            }

            event.detail &= ~SWT.SELECTED;
        }
    }

    /**
     * Controls whether the whole cell or only the text-area is highlighted
     * @param cell the cell which is highlighted
     * @return <code>true</code> if only the text area should be highlighted
     * @since 3.4
     */
    protected boolean onlyTextHighlighting(ViewerCell cell) {
        return false;
    }
}