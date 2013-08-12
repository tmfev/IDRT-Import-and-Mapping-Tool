package de.umg.mi.idrt.idrtimporttool.importidrt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.about.InstallationPage;

public class Testpage extends InstallationPage {
	private Text txtTestOk;

	public Testpage() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
		
		Composite comp = new Composite(parent, SWT.NONE);
		System.out.println("TEST");
		
		txtTestOk = new Text(comp, SWT.BORDER);
		txtTestOk.setText("TEST OK");
		txtTestOk.setBounds(212, 159, 76, 21);
		
	}
}
