package de.umg.mi.idrt.idrtimporttool.commands;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import de.umg.mi.idrt.idrtimporttool.importidrt.Activator;
import de.umg.mi.idrt.idrtimporttool.importidrt.ServerView;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.osgi.framework.Bundle;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import swing2swt.layout.BorderLayout;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class AboutCommand extends AbstractHandler {
	private Text txtAbout;

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			Display display = ServerView.getMainComposite().getDisplay();
			Shell parent = ServerView.getMainComposite().getShell();
			Shell dialog = new Shell(parent,  SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);

			dialog.setSize(559, 362);
			dialog.setLocation(parent.getLocation().x+(parent.getSize().x-dialog.getSize().x)/2,parent.getLocation().y+(parent.getSize().y-dialog.getSize().y)/2);
			dialog.setText("About");
			dialog.setLayout(new BorderLayout(0, 0));

			Composite composite = new Composite(dialog, SWT.NONE);
			
			composite.setLayout(new BorderLayout(0, 0));

			Composite composite_1 = new Composite(composite, SWT.NONE);
			composite_1.setLayoutData(BorderLayout.WEST);
			GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 1;
			composite_1.setLayout(gridLayout);



			Bundle bundle = Activator.getDefault().getBundle();
			Path imgImportPathTMF = new Path("/images/tmf.jpg"); 

			URL url = FileLocator.find(bundle, imgImportPathTMF,
					Collections.EMPTY_MAP);
			URL fileUrl = FileLocator.toFileURL(url);

			File imgImportFileTMF = new File(fileUrl.getPath());
			Image imgImportTMF = new Image(parent.getDisplay(),
					imgImportFileTMF.getAbsolutePath());
			//			Path imgImportPathUMG = new Path("/images/umg.jpg");
			//			URL urlUMG = FileLocator.find(bundle, imgImportPathUMG,
			//					Collections.EMPTY_MAP);
			//			URL fileUrlUMG = FileLocator.toFileURL(urlUMG);
			//
			//			File imgImportFileUMG = new File(fileUrlUMG.getPath());
			//			Image imgImportUMG = new Image(parent.getDisplay(),
			//					imgImportFileUMG.getAbsolutePath());

			Label labelTMF = new Label(composite_1, SWT.NONE);
			labelTMF.setImage(imgImportTMF);
			
			Composite composite_2 = new Composite(composite, SWT.NONE);
			composite_2.setLayoutData(BorderLayout.CENTER);
			composite_2.setLayout(new FillLayout(SWT.HORIZONTAL));
//			GridData gd_composite_2 = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
//			gd_composite_2.heightHint = 250;
//			composite_2.setLayoutData(gd_composite_2);
			
			txtAbout = new Text(composite_2, SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
			txtAbout.setText("Projekt zur Erstellung und Testung von Instrumenten und Methoden zur Integration und Bereitstellung verteilter und heterogener Datenbestände für die klinische und translationale Forschung"+
"Ziel des Vorhabens ist der Aufbau einer nachhaltig nutzbaren und datenschutzkonformen Plattform für die Integration heterogener Datenbestände in der klinischen und translationalen Forschung. Die Zusammenführung und standardisierte Aufbereitung erlaubt eine langfristige Nachnutzung der erhobenen Forschungsdaten. Hierzu sollen bewährte vorhandene Architekturen an die Bedürfnisse der vernetzten medizinischen Forschung in Deutschland angepasst und zu einem nahtlosen Prozess integriert werden."+
"In medizinischen Forschungsverbünden und an den deutschen Universitätskliniken wurden in den letzten Jahren eine Vielzahl von Forschungsdatenbanken aufgebaut. Diese Datenbestände besitzen über ihren ursprünglichen Forschungszweck hinaus ein großes Nachnutzungspotenzial für aktuelle und zukünftige Forschungshypothesen."+
"Mit dem hier beantragten Projekt soll, basierend auf frei verfügbaren IT-Komponenten, die in den letzten Jahren in TMF- bzw. BMBF-Projekten entstanden sind, der Aufbau einer generischen Methodik und einer nachhaltig nutzbaren und datenschutzkonformen Plattform für die Zusammenführung, Abfrage und Visualisierung heterogener Datenbestände in einem Data Warehouse geschaffen werden. Als Kernkomponente zur generischen Datenhaltung und Datenabfrage wird das Open Source System i2b2 in das zu entwickelnde Integrated Data Repository Toolkit (IDRT) eingebettet. Für den ETL-Prozess selbst soll das Open Source Produkt Talend Open Studio verwendet werden. Zur Verwendung standardisierter Datenitems wird auf die Ergebnisse des BMBF-Projekts Metadata Repository zurückgegriffen. Zur Wahrung der Patientenrechte und des Datenschutzes wird der TMF PID-Generator in den ETL-Prozess integriert."+
"Durch die konsequente Nutzung von Open-Source Komponenten und projektbegleitende Maßnahmen der Dissemination wird die Voraussetzung für eine dauerhafte Community-Nutzung über Verbünde aus der TMF hinaus geschaffen.");
			
			Composite composite_3 = new Composite(dialog, SWT.NONE);
			composite_3.setLayoutData(BorderLayout.SOUTH);
			composite_3.setLayout(new GridLayout(1, false));
			
			Button btnNewButton = new Button(composite_3, SWT.CENTER);
			btnNewButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
			btnNewButton.setText("New Button");
			//			Label labelUMG = new Label(composite_1, SWT.NONE);
			//			labelUMG.setImage(imgImportUMG);
			dialog.open();
			while (!dialog.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}
}
