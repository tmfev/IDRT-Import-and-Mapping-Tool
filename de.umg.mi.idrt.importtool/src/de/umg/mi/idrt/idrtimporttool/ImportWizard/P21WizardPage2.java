package de.umg.mi.idrt.idrtimporttool.ImportWizard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.core.runtime.Path;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.umg.mi.idrt.idrtimporttool.messages.Messages;
import de.umg.mi.idrt.importtool.misc.FileHandler;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class P21WizardPage2 extends WizardPage {

	public static boolean getCleanUp() {
		return cleanUpBtn.getSelection();
	}

	/**
	 * @return the folderText
	 */
	public static String getFolderCSVText() {
		return folderP21Text.getText();
	}

	/**
	 * @return the folderMainText
	 */
	public static String getFolderMainText() {
		return folderMainText.getText();
	}

	public static String getMainPath() {
		return mainPath;
	}

	public static boolean getP21Selected() {
		return p21VersionCombo.getSelectionIndex() >= 0;
	}

	public static String getP21VersionCombo() {
		return p21VersionCombo.getText();
	}

	/**
	 * @return the path
	 */
	public static String getPath() {
		return P21path;
	}

	public static boolean getSaveContext() {
		return checkContext.getSelection();
	}

	public static boolean getTerms() {
		return checkTerms.getSelection();
	}

	public static boolean getTruncateQueries() {
		return checkTruncateQueries.getSelection();
	}
	public static boolean getTruncate() {
		return checkTruncate.getSelection();
	}

	private Composite container;

	private static Text folderP21Text;
	private static Button checkTruncateQueries;
	private static Text folderMainText;
	private static Combo datePatternCombo;
	private Label lblDatePattern;
	private static String P21path = "";

	private static String mainPath = "";

	private static Button checkContext;

	private static Button checkTruncate;

	private static Button checkTerms;
	private static Combo p21VersionCombo;
	private Label cleanUpLabel;

	private static Button cleanUpBtn;
	private Label label;
	private Button button_1;

	// private static Text csvSeperatorext;

	/**
	 * @return
	 */
	public static String getPattern() {
		return datePatternCombo.getText();
	}

	public P21WizardPage2() {
		super("P21 Import Settings");
		setTitle("P21 Import Settings");
		setDescription("P21 Import Settings");
	}

	@Override
	public void createControl(Composite parent) {
		try {
			File properties = FileHandler.getBundleFile("/cfg/Default.properties");
			Properties defaultProps = new Properties();
			defaultProps.load(new FileReader(properties));

			container = new Composite(parent, SWT.NULL);
			GridLayout layout = new GridLayout(3, false);
			container.setLayout(layout);

			Label truncateLabel = new Label(container, SWT.FILL | SWT.CENTER);
			truncateLabel.setText("Truncate i2b2 Project?");
			truncateLabel.setToolTipText("Löscht den Inhalt des Projektes!");

			checkTruncate = new Button(container, SWT.CHECK);
			checkTruncate.setSelection(false);

			new Label(container, SWT.NONE);
			
			label = new Label(container, SWT.SHADOW_IN | SWT.CENTER);
			label.setToolTipText("L\u00F6scht den Inhalt des Projektes!");
			label.setText("Truncate Previous Queries?");
			
			checkTruncateQueries = new Button(container, SWT.CHECK);
			checkTruncateQueries.setSelection(false);
			new Label(container, SWT.NONE);

			cleanUpLabel = new Label(container, SWT.SHADOW_IN | SWT.CENTER);
			cleanUpLabel.setText(Messages.P21WizardPage2_cleanUpLabel_text);

			cleanUpBtn = new Button(container, SWT.CHECK);
			cleanUpBtn.setSelection(Boolean.parseBoolean(defaultProps
					.getProperty("cleanUp")));
			new Label(container, SWT.NONE);

			Label p21Folder = new Label(container, SWT.FILL | SWT.CENTER);
			p21Folder.setText("P21 Folder");
			p21Folder.setToolTipText("Ordner mit den P21 Dateien");

			P21path = defaultProps.getProperty("p21_input");
			final DirectoryDialog dlg = new DirectoryDialog(parent.getShell());
			dlg.setText("CSV Folder");
			dlg.setFilterPath(defaultProps.getProperty("p21_input"));

			folderP21Text = new Text(container, SWT.FILL);
			folderP21Text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					false, 1, 1));
			folderP21Text.setText(P21path);
			folderP21Text.setEditable(false);
			folderP21Text.addKeyListener(new KeyListener() {

				@Override
				public void keyPressed(KeyEvent e) {
				}

				@Override
				public void keyReleased(KeyEvent e) {
					if (!folderP21Text.getText().isEmpty()) {
						P21WizardPage2.this.setPageComplete(true);
					}
				}
			});

			Button button = new Button(container, SWT.PUSH);
			button.setText("...");
			button.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					P21path = dlg.open().replaceAll("\\\\", "/");
					P21path += "/";
					folderP21Text.setText(P21path);
				}
			});
			final FileDialog fd = new FileDialog(parent.getShell());
			fd.setText("ID File");
			fd.setFilterPath(defaultProps.getProperty("idFile"));

			Label p21Version = new Label(container, SWT.FILL | SWT.CENTER);
			p21Version.setText("P21 Version");

			p21VersionCombo = new Combo(container, SWT.READ_ONLY);
			new Label(container, SWT.NONE);
			
			
			lblDatePattern = new Label(container, SWT.NONE);
			lblDatePattern.setText(Messages.CSVWizardPageTwo_DatePattern);
			
			datePatternCombo = new Combo(container, SWT.NONE);

			datePatternCombo.setItems(new String[] { "yyyyMMddHHmm","yyyy-MM-dd", 
					"yyyy.MM.dd", "dd-MM-yyyy", "dd-MM-yy", "dd.MM.yyyy", "dd.MM.yy" }); 
			datePatternCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
					true, false, 1, 1));
			datePatternCombo.setText(defaultProps.getProperty("datePattern")); 
			
			Path p21Path = new Path("/cfg/p21/"); //$NON-NLS-1$
			File p21 = FileHandler.getBundleFile("/cfg/p21/");
			File[] listOfFiles = p21.listFiles();
			String[] p21FolderNames = new String[listOfFiles.length];
			for (int i = 0; i < listOfFiles.length; i++) {
				p21FolderNames[i] = listOfFiles[i].getName();
			}
			p21VersionCombo.setItems(p21FolderNames);
			p21VersionCombo.select(Integer.parseInt(defaultProps.getProperty("p21Version")));
			p21VersionCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
					true, false, 1, 1));
			p21VersionCombo.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					P21WizardPage2.this.getWizard().getContainer()
							.updateButtons();
				}
			});
			new Label(container, SWT.NONE);

//			Label labelImportTerms = new Label(container, SWT.NONE);
//			labelImportTerms.setText("Import and Map Standardterminologies?");
//			labelImportTerms
//					.setToolTipText("Importiert die Standardterminologien");
//			checkTerms = new Button(container, SWT.CHECK);
//			checkTerms.setSelection(false);
//			new Label(container, SWT.NONE);

			Label labelSaveContext = new Label(container, SWT.NONE);
			labelSaveContext.setText("Save Settings?");
			checkContext = new Button(container, SWT.CHECK);
			checkContext.setSelection(false);

			setControl(container);
			new Label(container, SWT.NONE);
			// new Label(container, SWT.NONE);
			setPageComplete(false);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public boolean isPageComplete() {
		return getP21Selected();
	}
}
