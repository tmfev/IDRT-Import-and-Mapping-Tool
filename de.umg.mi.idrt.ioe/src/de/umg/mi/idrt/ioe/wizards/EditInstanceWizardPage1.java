package de.umg.mi.idrt.ioe.wizards;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.umg.mi.idrt.ioe.view.OntologyEditorView;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class EditInstanceWizardPage1 extends WizardPage {
	private Text nameText;
	private Text descrText;
	private Text dateText;

	public EditInstanceWizardPage1() {
		super("Edit Instance");
		setTitle("Edit Instance");
		setDescription("Edit a i2b2 Target Instance");
	}

	@Override
	public void createControl(Composite parent) {
		Composite comp = new Composite(parent, SWT.NULL);

		comp.setLayout(new FillLayout(SWT.HORIZONTAL));

		Composite composite = new Composite(comp, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));

		Label lblName = new Label(composite, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name:");

		nameText = new Text(composite, SWT.BORDER);
		nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		nameText.setText(OntologyEditorView.getInstance().getName());
		nameText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
					if (!nameText.getText().isEmpty())
						setPageComplete(true);
					else
						setPageComplete(false);
			}
		});
		Label lblDescription = new Label(composite, SWT.NONE);
		lblDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 2));
		lblDescription.setText("Description:");

		descrText = new Text(composite, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		descrText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		descrText.setText(OntologyEditorView.getInstance().getDescription());
		Label lblDateCreated = new Label(composite, SWT.NONE);
		lblDateCreated.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDateCreated.setText("Date Edited:");

		dateText = new Text(composite, SWT.BORDER);
		dateText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		dateText.setEditable(false);
		String DATE_FORMAT_NOW = "dd.MM.yyyy - HH:mm:ss";
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		String time = sdf.format(cal.getTime());
		dateText.setText(time);
		setControl(comp);
		setPageComplete(false);
	}
	public String getCreated() {
		return dateText.getText();
	}
	
	public String getDescriptionText() {
		return descrText.getText();
	}
	public String getNameText() {
		return nameText.getText();
	}
	protected boolean isComplete() {
		return !nameText.getText().isEmpty();
	}

	@Override
	public boolean isPageComplete() {
		return !nameText.getText().isEmpty();
	}
}
