package de.umg.mi.idrt.ioe;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class Settings extends FieldEditorPreferencePage  implements
		IWorkbenchPreferencePage {

	public Settings() {
		super(GRID);
	}

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("A demonstration of a preference page implementation");


	}

	@Override
	protected void createFieldEditors() {
		/*
		addField(new DirectoryFieldEditor("PATH", "&Directory preference:",
				getFieldEditorParent()));
		addField(new BooleanFieldEditor("BOOLEAN_VALUE",
				"&An example of a boolean preference", getFieldEditorParent()));
		addField(new RadioGroupFieldEditor("CHOICE",
				"An example of a multiple-choice preference", 1,
				new String[][] { { "&Choice 1", "choice1" },
						{ "C&hoice 2", "choice2" } }, getFieldEditorParent()));
		*/
		/*
		addField(new StringFieldEditor(Resource.ID.Variables.Preferences.ORACLE_HOST, "Oracle Host IP:",
				getFieldEditorParent()));
		addField(new StringFieldEditor(Resource.ID.Variables.Preferences.ORACLE_PORT, "Oracle Host Port:",
				getFieldEditorParent()));
		addField(new StringFieldEditor(Resource.ID.Variables.Preferences.ORACLE_SID, "Oracle Host SID:",
				getFieldEditorParent()));
		addField(new StringFieldEditor(Resource.ID.Variables.Preferences.ORACLE_USERNAME, "Oracle User Login:",
				getFieldEditorParent()));
		
		StringFieldEditor passwordEditor = new StringFieldEditor(Resource.ID.Variables.Preferences.ORACLE_PASSWORD, "Oracle User Login Password:",
				getFieldEditorParent());
		passwordEditor.getTextControl(getFieldEditorParent()).setEchoChar('*');
		addField(passwordEditor);
		*/
	}

}
