package de.umg.mi.idrt.idrtimporttool.ImportWizard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.osgi.framework.Bundle;

import de.umg.mi.idrt.idrtimporttool.commands.DBImportCommand;
import de.umg.mi.idrt.idrtimporttool.importidrt.Activator;
import de.umg.mi.idrt.idrtimporttool.messages.Messages;
import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class DBWizardPage3 extends WizardPage {

	private Composite container;
	private static Text folderMainText;
	private static String path = "";
	private static String mainPath = "";
	private static Button checkContext;
	private static Button checkTruncate;
	private static Button checkTerms;
	private static String CSVPath;
	private static Text quoteCharText;
	private Label lblImportingTables;
	private SashForm sashForm;
	private Composite composite;
	private Composite composite_1;
	private List list;
	private Label cleanUpLabel;
	private static Button cleanUpBtn;

	public static boolean getCleanUp() {
		return cleanUpBtn.getSelection();
	}

	public static String getCSVPath() {
		return CSVPath;
	}

	public static String getFolderMainText() {
		return folderMainText.getText();
	}

	public static String getMainPath() {
		return mainPath;
	}

	public static String getPath() {
		return path;
	}

	public static String getQuoteCharText() {
		return quoteCharText.getText();
	}

	public static boolean getSaveContext() {
		return checkContext.getSelection();
	}

	public static boolean getTerms() {
		return checkTerms.getSelection();
	}

	public static boolean getTruncate() {
		return checkTruncate.getSelection();
	}

	public DBWizardPage3() {
		super("DB Import Settings");
		setTitle("DB Import Settings");
		setWizard(DBImportCommand.getImpWiz());
		setDescription("DB Import Settings");
	}

	@Override
	public void createControl(Composite parent) {
		try {
			Bundle bundle = Activator.getDefault().getBundle();
			Path propPath = new Path("/cfg/Default.properties"); 
			URL url = FileLocator.find(bundle, propPath, Collections.EMPTY_MAP);
			URL fileUrl = null;
			fileUrl = FileLocator.toFileURL(url);
			File properties = new File(fileUrl.getPath());
			Properties defaultProps = new Properties();
			defaultProps.load(new FileReader(properties));

			container = new Composite(parent, SWT.NULL);

			setControl(container);
			container.setLayout(new FillLayout(SWT.HORIZONTAL));
			sashForm = new SashForm(container, SWT.NONE);

			composite = new Composite(sashForm, SWT.NONE);
			composite.setLayout(new GridLayout(3, false));
			Label truncateLabel = new Label(composite, SWT.FILL | SWT.CENTER);
			truncateLabel.setText("Truncate i2b2 Project?");
			truncateLabel.setToolTipText("Löscht den Inhalt des Projektes!");

			checkTruncate = new Button(composite, SWT.CHECK);
			checkTruncate.setSelection(false);
			new Label(composite, SWT.NONE);

			cleanUpLabel = new Label(composite, SWT.SHADOW_IN | SWT.CENTER);
			cleanUpLabel.setText(Messages.CSVWizardPageTwo_CleanUp);

			cleanUpBtn = new Button(composite, SWT.CHECK);
			cleanUpBtn.setSelection(Boolean.parseBoolean(defaultProps
					.getProperty("cleanUp")));

			new Label(composite, SWT.NONE);
			Label labelImportTerms = new Label(composite, SWT.NONE);
			labelImportTerms.setText("Import and Map Standardterminologies?");
			labelImportTerms
					.setToolTipText("Importiert die Standardterminologien");
			checkTerms = new Button(composite, SWT.CHECK);
			checkTerms.setSelection(false);
			new Label(composite, SWT.NONE);

			Label labelSaveContext = new Label(composite, SWT.NONE);
			labelSaveContext.setText("Save Settings?");
			checkContext = new Button(composite, SWT.CHECK);
			checkContext.setSelection(false);
			new Label(composite, SWT.NONE);

			mainPath = defaultProps.getProperty("folderMainCSV");
			final DirectoryDialog dlgMain = new DirectoryDialog(
					parent.getShell());
			dlgMain.setText("CSV Main Folder");
			dlgMain.setFilterPath(defaultProps.getProperty("folderMain"));
			// Label folder = new Label(composite, SWT.FILL|SWT.CENTER);
			// folder.setText("DB Main Folder");
			// folder.setToolTipText("Hauptordner mit der IDRT-Struktur (tmp,output)");
			//
			// CSVPath = defaultProps.getProperty("folderCSV");
			// String property = "java.io.tmpdir";

			Path tmpPath = new Path("/misc/input/"); 
			URL tmpurl = FileLocator.find(bundle, tmpPath,
					Collections.EMPTY_MAP);
			URL tmpurl2 = FileLocator.toFileURL(tmpurl);
			File tmpFolder = new File(tmpurl2.getPath());
			// String tempDir = System.getProperty(property);
			CSVPath = tmpFolder.getAbsolutePath();
			CSVPath = CSVPath.replaceAll("\\\\", "/") + "/";
			final DirectoryDialog dlg = new DirectoryDialog(parent.getShell());
			dlg.setText("DB Folder");
			dlg.setFilterPath(defaultProps.getProperty("folderCSV"));

			composite_1 = new Composite(sashForm, SWT.NONE);
			composite_1.setLayout(null);

			lblImportingTables = new Label(composite_1, SWT.NONE);
			lblImportingTables.setBounds(5, 5, 93, 15);
			lblImportingTables.setText("Tables to Import:");

			list = new List(composite_1, SWT.BORDER | SWT.H_SCROLL
					| SWT.V_SCROLL);
			list.setBounds(5, 25, 276, 247);
			HashMap<Server, HashMap<String, java.util.List<String>>> tables = DBWizardPage2
					.getCheckedTables();
			if (tables != null) {
				Iterator<Server> tableServerIterator = tables.keySet()
						.iterator();
				while (tableServerIterator.hasNext()) {
					Server currentServer = tableServerIterator.next();
					HashMap<String, java.util.List<String>> schemaHashMap = tables
							.get(currentServer);
					Iterator<String> schemaHashMapIterator = schemaHashMap
							.keySet().iterator();
					while (schemaHashMapIterator.hasNext()) {
						String currentSchema = schemaHashMapIterator.next();
						java.util.List<String> currentTables = schemaHashMap
								.get(currentSchema);
						Iterator<String> tablesTableIterator = currentTables
								.iterator();
						while (tablesTableIterator.hasNext()) {
							list.add(currentServer.getUniqueID() + ": "
									+ currentSchema + " - "
									+ tablesTableIterator.next());
						}
					}
				}
			}
			sashForm.setWeights(new int[] { 1, 1 });
			setPageComplete(true);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public IWizardPage getPreviousPage() {
		return super.getPreviousPage();
	}

	@Override
	public boolean isPageComplete() {
		return true;
	}
}
