package de.umg.mi.idrt.idrtimporttool.ImportWizard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class WizardPageZero {

	private static Composite container;
	private static String schema;
	private Button btnOdm;
	private Button btnCsv;
	private Button btnDb;
	private Button btnP;
	private Composite composite;
	private Composite composite_1;

	public WizardPageZero() {
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public void createControl() {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setSize(237, 300);
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));

		composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setLayout(new GridLayout(1, false));
		composite_1.setSize(200, 100);

		btnP = new Button(composite_1, SWT.NONE);
		btnP.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		btnP.setText("P21");

		btnDb = new Button(composite_1, SWT.NONE);
		btnDb.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		btnDb.setText("DB");

		btnCsv = new Button(composite_1, SWT.NONE);
		btnCsv.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		btnCsv.setText("CSV");

		btnOdm = new Button(composite_1, SWT.NONE);
		btnOdm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		btnOdm.setText("ODM");

	}
}