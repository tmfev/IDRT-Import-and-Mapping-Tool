package de.umg.mi.idrt.idrtimporttool.server.commands;

import java.util.LinkedList;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import swing2swt.layout.BorderLayout;
import de.umg.mi.idrt.idrtimporttool.importidrt.Application;
import de.umg.mi.idrt.idrtimporttool.importidrt.SWTResourceManager;
import de.umg.mi.idrt.idrtimporttool.server.Settings.I2B2User;
import de.umg.mi.idrt.idrtimporttool.server.Settings.I2b2Project;
import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.idrtimporttool.server.Settings.ServerList;
import de.umg.mi.idrt.idrtimporttool.server.serverWizard.AddUserWizard;
import de.umg.mi.idrt.idrtimporttool.server.serverWizard.AdminServerContentProvider;
import de.umg.mi.idrt.idrtimporttool.server.serverWizard.AdminServerLabelProvider;
import de.umg.mi.idrt.idrtimporttool.server.serverWizard.AdminServerModel;
import de.umg.mi.idrt.idrtimporttool.server.serverWizard.EditUserWizard;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class AdminTargetServerCommand extends AbstractHandler {

	private Label txtAssignedUsers;
	private Label serverInfo;
	private TreeViewer projectTab_serverViewer;
	private static Tree projectTab_allUserTree;
	private static Tree projectTab_assignedUserTree;

	private static Composite userInfoComposite;
	private Composite projectTab_userInfoComposite;
	private static LinkedList<String> userList;
	private static LinkedList<String> allUserList;

	private static Text projectTab_projectID_answer;
	private static Text projectTab_projectName_answer;
	private Text projectTab_IP_answer;
	private static Text projectTab_projects_answer;
	private Text projectTab_patients_answer;
	private Text projectTab_observations_answer;
	private Display display;
	private static Button adminCheckButton;
	private static Button managerCheckButton;
	private Label lblAdmin;
	private Label lblManager;
	private Shell shell;
	private static Label lblLastQuery;
	private static Label userTab_lastLogin;
	private static Text userTab_userName_answer;
	private static Text userTab_fullname_answer;
	private static Text userTab_eMail_answer;
	private static Text userTab_lastlogin_answer;
	// private Label userTab_numberOfLogins_answer;
	private static Text userTab_lastQuery_answer;
	// private Label userTab_numberOfQueries_answer;
	private static Text userTab_status_answer;

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		Thread a = new Thread(new Runnable() {

			@Override
			public void run() {
				// THREAD START

				display = new Display();

				shell = new Shell(display);
				shell.setSize(1024, 800);

				shell.setText("Server Administration");
				shell.setLayout(new FillLayout(SWT.HORIZONTAL));

				Composite parent = new Composite(shell, SWT.NONE);
				parent.setLayout(new BorderLayout(0, 0));

				Composite composite_1 = new Composite(parent, SWT.NONE);
				composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));

				TabFolder tabFolder = new TabFolder(composite_1, SWT.NONE);
				TabItem tbtmProjects = new TabItem(tabFolder, SWT.NONE);
				tbtmProjects.setText("Projects");

				Composite projectComposite = new Composite(tabFolder, SWT.NONE);
				tbtmProjects.setControl(projectComposite);
				projectComposite.setLayout(new FillLayout(SWT.HORIZONTAL));

				SashForm sashForm = new SashForm(projectComposite, SWT.SMOOTH);
				Composite projectListComposite = new Composite(sashForm,
						SWT.NONE);
				projectListComposite.setLayout(new FillLayout(SWT.HORIZONTAL));

				projectTab_serverViewer = new TreeViewer(projectListComposite,
						SWT.MULTI); // parent
				projectTab_serverViewer
						.setContentProvider(new AdminServerContentProvider());
				projectTab_serverViewer
						.setLabelProvider(new AdminServerLabelProvider());
				projectTab_serverViewer.setInput(new AdminServerModel());
				projectTab_serverViewer.setAutoExpandLevel(1);
				projectTab_serverViewer.setSorter(new ViewerSorter());

				projectTab_serverViewer.getTree().addSelectionListener(
						new SelectionListener() {

							@Override
							public void widgetDefaultSelected(SelectionEvent e) {
							}

							@Override
							public void widgetSelected(SelectionEvent e) {
								TreeItem selectedItem = (TreeItem) e.item;
								String selectedItemString = selectedItem
										.getText();
								// Log.addLog(0,"selected: " +
								// selectedItemString);

								if (selectedItem.getData() instanceof Server) {
									String currentServerID = selectedItem
											.getText();
									Server server = ServerList.getTargetServers()
											.get(currentServerID);
									projectTab_userInfoComposite
											.setRedraw(false);
									projectTab_IP_answer.setText(server.getIp());
									projectTab_IP_answer.pack();
									projectTab_projectID_answer.setText(server
											.getName());
									projectTab_projectID_answer.pack();
									projectTab_projectName_answer.setText("");
									projectTab_projectName_answer.pack();
									projectTab_observations_answer.setText("");
									projectTab_observations_answer.pack();
									projectTab_patients_answer.setText("");
									projectTab_patients_answer.pack();
									projectTab_projects_answer.setText("");
									projectTab_projects_answer.pack();

									allUserList = ServerList
											.getAllUsersFromServer(server);

									projectTab_allUserTree.removeAll();
									if (allUserList != null) {
										for (String item : allUserList) {
											TreeItem newItem = new TreeItem(
													projectTab_allUserTree,
													SWT.NONE);
											newItem.setText(item);
											newItem.setData(item);
										}
									}
									projectTab_assignedUserTree.removeAll();
									projectTab_userInfoComposite
											.setRedraw(true);

								} else if (selectedItem.getData() instanceof I2b2Project) {
									String parentServer = selectedItem
											.getParentItem().getText();
									Server server = ServerList.getTargetServers()
											.get(parentServer);
									projectTab_userInfoComposite
											.setRedraw(false);
									projectTab_IP_answer.setText(server.getIp());
									projectTab_IP_answer.pack();
									projectTab_projects_answer
											.setText(selectedItemString);
									projectTab_projects_answer.pack();
									projectTab_projectID_answer.setText(server
											.getName());
									projectTab_projectID_answer.pack();
									projectTab_observations_answer
											.setText("...");
									projectTab_observations_answer.pack();
									projectTab_patients_answer.setText("...");
									projectTab_patients_answer.pack();
									projectTab_projectName_answer.setText(server
											.getProjectName(selectedItemString));
									projectTab_projectName_answer.pack();
									projectTab_observations_answer.setText(server
											.getConcepts(selectedItemString));
									projectTab_observations_answer.pack();
									projectTab_patients_answer.setText(server
											.getPatients(selectedItemString));
									projectTab_patients_answer.pack();
									projectTab_userInfoComposite
											.setRedraw(true);
									refresh();
									// allUserList =
									// ServerList.getAllUsersFromServer(server);
									// projectTab_allUserTree.removeAll();
									// if (allUserList != null){
									// for (String item : allUserList){
									// TreeItem newItem = new
									// TreeItem(projectTab_allUserTree,
									// SWT.NONE);
									// newItem.setText(item);
									// }
									// }
									// userList =
									// ServerList.getAssignedUsersFromProject(server,selectedItemString);
									// projectTab_assignedUserTree.removeAll();
									// if (userList != null){
									// for (String item : userList){
									// TreeItem newItem = new
									// TreeItem(projectTab_assignedUserTree,
									// SWT.NONE);
									// newItem.setText(item);
									// }
									// }
									// usersInfoComposite.pack();
									// usersInfoComposite.update();
								}
							}
						});

				Composite projectInfoComposite = new Composite(sashForm,
						SWT.BORDER);
				projectInfoComposite.setLayout(new BorderLayout(0, 0));

				SashForm sashForm_1 = new SashForm(projectInfoComposite,
						SWT.VERTICAL);

				Composite composite = new Composite(sashForm_1, SWT.NONE);
				GridData gd_usersInfoComposite = new GridData(SWT.FILL,
						SWT.FILL, true, false, 1, 1);
				gd_usersInfoComposite.widthHint = 161;
				composite.setLayout(new FillLayout(SWT.HORIZONTAL));

				SashForm sashForm_2 = new SashForm(composite, SWT.NONE);

				Composite composite_2 = new Composite(sashForm_2, SWT.NONE);
				composite_2.setLayout(new BorderLayout(0, 0));

				serverInfo = new Label(composite_2, SWT.FILL | SWT.LEFT);
				serverInfo.setFont(SWTResourceManager.getFont("Segoe UI", 10,
						SWT.NORMAL));
				serverInfo.setLayoutData(BorderLayout.NORTH);
				serverInfo.setText("Server Info:");

				projectTab_userInfoComposite = new Composite(composite_2,
						SWT.NONE);
				projectTab_userInfoComposite
						.setLayoutData(gd_usersInfoComposite);
				projectTab_userInfoComposite.setEnabled(true);
				// usersInfoComposite.setLayoutData(BorderLayout.CENTER);
				projectTab_userInfoComposite
						.setLayout(new GridLayout(2, false));

				Label lblName = new Label(projectTab_userInfoComposite,
						SWT.NONE);
				lblName.setFont(SWTResourceManager.getFont("Segoe UI", 9,
						SWT.BOLD));
				lblName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
						false, 1, 1));
				lblName.setText("Server Name:");

				projectTab_projectID_answer = new Text(
						projectTab_userInfoComposite, SWT.NONE);
				projectTab_projectID_answer.setBackground(SWTResourceManager
						.getColor(SWT.COLOR_WHITE));
				projectTab_projectID_answer.setEditable(false);
				projectTab_projectID_answer.setLayoutData(new GridData(
						SWT.FILL, SWT.CENTER, true, false, 1, 1));
				Label lblIp = new Label(projectTab_userInfoComposite, SWT.NONE);
				lblIp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
						false, 1, 1));
				lblIp.setFont(SWTResourceManager.getFont("Segoe UI", 9,
						SWT.BOLD));
				lblIp.setText("IP:");

				projectTab_IP_answer = new Text(projectTab_userInfoComposite,
						SWT.NONE);
				projectTab_IP_answer.setBackground(SWTResourceManager
						.getColor(SWT.COLOR_WHITE));
				projectTab_IP_answer.setEditable(false);
				projectTab_IP_answer.setLayoutData(new GridData(SWT.FILL,
						SWT.CENTER, true, false, 1, 1));

				Label lblProject = new Label(projectTab_userInfoComposite,
						SWT.NONE);
				lblProject.setFont(SWTResourceManager.getFont("Segoe UI", 9,
						SWT.BOLD));
				lblProject.setText("Project ID:");
				projectTab_projects_answer = new Text(
						projectTab_userInfoComposite, SWT.NONE);
				projectTab_projects_answer.setEditable(false);
				projectTab_projects_answer.setBackground(SWTResourceManager
						.getColor(SWT.COLOR_WHITE));
				projectTab_projects_answer.setLayoutData(new GridData(SWT.FILL,
						SWT.CENTER, true, false, 1, 1));

				Label lblProjectName = new Label(projectTab_userInfoComposite,
						SWT.NONE);
				lblProjectName.setText("Project Name:");
				lblProjectName.setFont(SWTResourceManager.getFont("Segoe UI",
						9, SWT.BOLD));

				projectTab_projectName_answer = new Text(
						projectTab_userInfoComposite, SWT.NONE);
				projectTab_projectName_answer.setBackground(SWTResourceManager
						.getColor(SWT.COLOR_WHITE));
				projectTab_projectName_answer.setEditable(false);
				projectTab_projectName_answer.setLayoutData(new GridData(
						SWT.FILL, SWT.CENTER, true, false, 1, 1));
				projectTab_projectName_answer.setText("");
				Label lblPatients = new Label(projectTab_userInfoComposite,
						SWT.NONE);
				lblPatients.setText("Patients:");
				lblPatients.setFont(SWTResourceManager.getFont("Segoe UI", 9,
						SWT.BOLD));

				projectTab_patients_answer = new Text(
						projectTab_userInfoComposite, SWT.NONE);
				projectTab_patients_answer.setEditable(false);
				projectTab_patients_answer.setBackground(SWTResourceManager
						.getColor(SWT.COLOR_WHITE));
				projectTab_patients_answer.setLayoutData(new GridData(SWT.FILL,
						SWT.CENTER, true, false, 1, 1));

				Label lblObservations = new Label(projectTab_userInfoComposite,
						SWT.NONE);
				lblObservations.setText("Observations:");
				lblObservations.setFont(SWTResourceManager.getFont("Segoe UI",
						9, SWT.BOLD));

				projectTab_observations_answer = new Text(
						projectTab_userInfoComposite, SWT.NONE);
				projectTab_observations_answer.setBackground(SWTResourceManager
						.getColor(SWT.COLOR_WHITE));
				projectTab_observations_answer.setEditable(false);
				projectTab_observations_answer.setLayoutData(new GridData(
						SWT.FILL, SWT.CENTER, true, false, 1, 1));

				Composite composite_3 = new Composite(sashForm_2, SWT.NONE);
				composite_3.setLayout(new FillLayout(SWT.HORIZONTAL));
				sashForm_2.setWeights(new int[] { 1, 1 });
				Composite assignedUsersSash1 = new Composite(sashForm_1,
						SWT.NONE);
				assignedUsersSash1.setLayout(new BorderLayout(0, 0));

				SashForm sashForm_4 = new SashForm(assignedUsersSash1, SWT.NONE);
				sashForm_4.setLayoutData(BorderLayout.CENTER);
				sashForm_4.setSashWidth(1);

				Composite allUsersComposite = new Composite(sashForm_4,
						SWT.NONE);
				allUsersComposite.setLayout(new BorderLayout(0, 0));

				projectTab_allUserTree = new Tree(allUsersComposite, SWT.BORDER
						| SWT.MULTI);
				projectTab_allUserTree
						.addSelectionListener(new SelectionListener() {

							@Override
							public void widgetSelected(SelectionEvent e) {
								TreeItem selectedItem = (TreeItem) e.item;
								projectTab_assignedUserTree.deselectAll();
								Server server = ServerList.getTargetServers().get(
										projectTab_projectID_answer.getText());
								I2B2User i2b2user = ServerList.getI2B2UserData(
										selectedItem.getText(), null, server);

								userInfoComposite.setRedraw(false);

								userTab_userName_answer.setText(i2b2user
										.getUserName());
								userTab_userName_answer.pack();
								userTab_eMail_answer.setText(i2b2user
										.getEmail());
								userTab_eMail_answer.pack();
								userTab_fullname_answer.setText(i2b2user
										.getFullname());
								userTab_fullname_answer.pack();
								userTab_lastlogin_answer.setText(i2b2user
										.getLastLogin());
								userTab_lastlogin_answer.pack();
								userTab_status_answer.setText(i2b2user
										.getStatus());
								userTab_status_answer.pack();
								userTab_lastQuery_answer.setText("");
								userTab_lastQuery_answer.pack();
								// adminCheckButton.setSelection(ServerList.getAdmin(server,userTab_userName_answer.getText(),projectTab_projects_answer.getText()));
								adminCheckButton.setSelection(false);
								adminCheckButton.setEnabled(false);
								managerCheckButton.setEnabled(false);
								managerCheckButton.setSelection(false);
								lblAdmin.setEnabled(false);
								lblManager.setEnabled(false);
								// userTab_lastQuery_answer.setEnabled(false);
								lblLastQuery.setEnabled(false);
								userInfoComposite.setRedraw(true);
							}

							@Override
							public void widgetDefaultSelected(SelectionEvent e) {
							}
						});

				Label label_1 = new Label(allUsersComposite, SWT.SHADOW_IN);
				label_1.setFont(SWTResourceManager.getFont("Segoe UI", 10,
						SWT.NORMAL));
				label_1.setText("All Users:");
				label_1.setLayoutData(BorderLayout.NORTH);

				Composite assignBarComposite = new Composite(sashForm_4,
						SWT.NONE);
				assignBarComposite.setLayout(new GridLayout(1, false));
				new Label(assignBarComposite, SWT.NONE);
				new Label(assignBarComposite, SWT.NONE);
				new Label(assignBarComposite, SWT.NONE);
				new Label(assignBarComposite, SWT.NONE);
				new Label(assignBarComposite, SWT.NONE);

				Button unAssignAllButton = new Button(assignBarComposite,
						SWT.NONE);
				unAssignAllButton.setLayoutData(new GridData(SWT.FILL,
						SWT.CENTER, true, false, 1, 1));
				unAssignAllButton.setText("<<");
				unAssignAllButton.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(SelectionEvent e) {

						String project = projectTab_projects_answer.getText();
						if (!project.isEmpty()) {
							unAssignAll(project);
						}
					}

					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
					}
				});

				Button unAssignSingleButton = new Button(assignBarComposite,
						SWT.NONE);
				unAssignSingleButton.setLayoutData(new GridData(SWT.FILL,
						SWT.CENTER, true, false, 1, 1));
				unAssignSingleButton.setText("<");
				unAssignSingleButton
						.addSelectionListener(new SelectionListener() {

							@Override
							public void widgetSelected(SelectionEvent e) {
								String project = projectTab_projects_answer
										.getText();
								if (!project.isEmpty()) {
									unAssignUserFromProject(project);
								}
							}

							@Override
							public void widgetDefaultSelected(SelectionEvent e) {
							}
						});

				Button assignSingleButton = new Button(assignBarComposite,
						SWT.NONE);
				assignSingleButton.setLayoutData(new GridData(SWT.FILL,
						SWT.CENTER, true, false, 1, 1));
				assignSingleButton.setText(">");
				assignSingleButton
						.addSelectionListener(new SelectionListener() {

							@Override
							public void widgetSelected(SelectionEvent e) {

								String project = projectTab_projects_answer
										.getText();
								if (!project.isEmpty()) {
									assignUserToProject(project);
								}
							}

							@Override
							public void widgetDefaultSelected(SelectionEvent e) {

							}
						});

				Button assignAllButton = new Button(assignBarComposite,
						SWT.NONE);
				assignAllButton.setLayoutData(new GridData(SWT.FILL,
						SWT.CENTER, true, false, 1, 1));
				assignAllButton.setText(">>");
				assignAllButton.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(SelectionEvent e) {
						String project = projectTab_projects_answer.getText();
						if (!project.isEmpty()) {
							assignAllUsersToProject(project);
						}
					}

					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
					}
				});

				Composite assignedUsersComposite = new Composite(sashForm_4,
						SWT.NONE);
				assignedUsersComposite.setLayout(new BorderLayout(0, 0));

				txtAssignedUsers = new Label(assignedUsersComposite,
						SWT.SHADOW_IN);
				txtAssignedUsers.setFont(SWTResourceManager.getFont("Segoe UI",
						10, SWT.NORMAL));
				txtAssignedUsers.setLayoutData(BorderLayout.NORTH);
				txtAssignedUsers.setText("Assigned Users:");

				projectTab_assignedUserTree = new Tree(assignedUsersComposite,
						SWT.BORDER | SWT.MULTI);

				projectTab_assignedUserTree
						.addSelectionListener(new SelectionListener() {

							@Override
							public void widgetSelected(SelectionEvent e) {
								TreeItem selectedItem = (TreeItem) e.item;
								Server server = ServerList.getTargetServers().get(
										projectTab_projectID_answer.getText());
								I2B2User i2b2user = ServerList.getI2B2UserData(
										selectedItem.getText(),
										projectTab_projects_answer.getText(),
										server);

								projectTab_allUserTree.deselectAll();
								userInfoComposite.setRedraw(false);
								userTab_userName_answer.setText(i2b2user
										.getUserName());
								userTab_userName_answer.pack();
								userTab_eMail_answer.setText(i2b2user
										.getEmail());
								userTab_eMail_answer.pack();
								userTab_fullname_answer.setText(i2b2user
										.getFullname());
								userTab_fullname_answer.pack();
								userTab_lastlogin_answer.setText(i2b2user
										.getLastLogin());
								userTab_lastlogin_answer.pack();
								userTab_status_answer.setText(i2b2user
										.getStatus());
								userTab_status_answer.pack();
								userTab_lastQuery_answer.setText(i2b2user
										.getLastQuery());
								userTab_lastQuery_answer.pack();
								// refresh();
								// userTab_lastQuery_answer.setEnabled(true);
								lblLastQuery.setEnabled(true);
								lblAdmin.setEnabled(true);
								lblManager.setEnabled(true);
								adminCheckButton.setEnabled(true);
								adminCheckButton.setSelection(ServerList
										.getAdmin(server,
												userTab_userName_answer
														.getText(),
												projectTab_projects_answer
														.getText()));
								managerCheckButton.setEnabled(true);
								managerCheckButton.setSelection(ServerList
										.getManager(server,
												userTab_userName_answer
														.getText(),
												projectTab_projects_answer
														.getText()));
								userInfoComposite.setRedraw(true);
							}

							@Override
							public void widgetDefaultSelected(SelectionEvent e) {
							}
						});

				Composite composite_5 = new Composite(sashForm_4, SWT.NONE);
				composite_5.setLayout(new BorderLayout(0, 0));

				userInfoComposite = new Composite(composite_5, SWT.BORDER);
				userInfoComposite.setLayout(new GridLayout(2, false));

				// Label label_6 = new Label(composite_13, SWT.NONE);
				// label_6.setText("# of Logins:");
				//
				// Label label_7 = new Label(composite_13, SWT.NONE);

				Label lblUsername = new Label(userInfoComposite, SWT.NONE);
				lblUsername.setFont(SWTResourceManager.getFont("Segoe UI", 9,
						SWT.BOLD));
				lblUsername.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
						false, false, 1, 1));
				lblUsername.setText("Username:");

				userTab_userName_answer = new Text(userInfoComposite, SWT.NONE);
				userTab_userName_answer.setBackground(SWTResourceManager
						.getColor(SWT.COLOR_WHITE));
				userTab_userName_answer.setEditable(false);
				userTab_userName_answer.setLayoutData(new GridData(SWT.FILL,
						SWT.CENTER, false, false, 1, 1));
				userTab_userName_answer.setText("");

				Label lblFullname = new Label(userInfoComposite, SWT.NONE);
				lblFullname.setFont(SWTResourceManager.getFont("Segoe UI", 9,
						SWT.BOLD));
				lblFullname.setText("Fullname:");

				userTab_fullname_answer = new Text(userInfoComposite, SWT.NONE);
				userTab_fullname_answer.setEditable(false);
				userTab_fullname_answer.setBackground(SWTResourceManager
						.getColor(SWT.COLOR_WHITE));
				userTab_fullname_answer.setLayoutData(new GridData(SWT.FILL,
						SWT.CENTER, false, false, 1, 1));
				userTab_fullname_answer.setText("");

				Label lblEmail = new Label(userInfoComposite, SWT.NONE);
				lblEmail.setFont(SWTResourceManager.getFont("Segoe UI", 9,
						SWT.BOLD));
				lblEmail.setText("E-Mail:");

				userTab_eMail_answer = new Text(userInfoComposite, SWT.NONE);
				userTab_eMail_answer.setEditable(false);
				userTab_eMail_answer.setBackground(SWTResourceManager
						.getColor(SWT.COLOR_WHITE));
				userTab_eMail_answer.setLayoutData(new GridData(SWT.FILL,
						SWT.CENTER, false, false, 1, 1));
				userTab_eMail_answer.setText("");

				Label lblActive = new Label(userInfoComposite, SWT.NONE);
				lblActive.setFont(SWTResourceManager.getFont("Segoe UI", 9,
						SWT.BOLD));
				lblActive.setText("Status:");
				userTab_status_answer = new Text(userInfoComposite, SWT.NONE);
				userTab_status_answer.setEditable(false);
				userTab_status_answer.setBackground(SWTResourceManager
						.getColor(SWT.COLOR_WHITE));
				userTab_status_answer.setLayoutData(new GridData(SWT.FILL,
						SWT.CENTER, false, false, 1, 1));
				userTab_status_answer.setText("");

				userTab_lastLogin = new Label(userInfoComposite, SWT.NONE);
				userTab_lastLogin.setFont(SWTResourceManager.getFont(
						"Segoe UI", 9, SWT.BOLD));
				userTab_lastLogin.setText("Last Login:");

				userTab_lastlogin_answer = new Text(userInfoComposite, SWT.NONE);
				userTab_lastlogin_answer.setEditable(false);
				userTab_lastlogin_answer.setBackground(SWTResourceManager
						.getColor(SWT.COLOR_WHITE));
				userTab_lastlogin_answer.setLayoutData(new GridData(SWT.FILL,
						SWT.CENTER, true, false, 1, 1));
				userTab_lastlogin_answer.setText("");

				lblLastQuery = new Label(userInfoComposite, SWT.NONE);
				lblLastQuery.setFont(SWTResourceManager.getFont("Segoe UI", 9,
						SWT.BOLD));
				lblLastQuery.setText("Last Query:");
				userTab_lastQuery_answer = new Text(userInfoComposite, SWT.NONE);
				userTab_lastQuery_answer.setEditable(false);
				userTab_lastQuery_answer.setBackground(SWTResourceManager
						.getColor(SWT.COLOR_WHITE));
				userTab_lastQuery_answer.setLayoutData(new GridData(SWT.FILL,
						SWT.CENTER, false, false, 1, 1));
				userTab_lastQuery_answer.setText("");

				lblAdmin = new Label(userInfoComposite, SWT.NONE);
				lblAdmin.setFont(SWTResourceManager.getFont("Segoe UI", 9,
						SWT.BOLD));
				lblAdmin.setText("Admin:");

				adminCheckButton = new Button(userInfoComposite, SWT.CHECK);
				adminCheckButton.setLayoutData(new GridData(SWT.FILL,
						SWT.CENTER, false, false, 1, 1));
				adminCheckButton.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(SelectionEvent e) {
						String project = projectTab_projects_answer.getText();
						if (!project.isEmpty()) {
							setAdmin(project, adminCheckButton.getSelection());
						}
					}

					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
					}
				});
				lblManager = new Label(userInfoComposite, SWT.NONE);
				lblManager.setFont(SWTResourceManager.getFont("Segoe UI", 9,
						SWT.BOLD));
				lblManager.setText("Manager:");
				managerCheckButton = new Button(userInfoComposite, SWT.CHECK);
				managerCheckButton.setLayoutData(new GridData(SWT.FILL,
						SWT.CENTER, false, false, 1, 1));
				managerCheckButton
						.addSelectionListener(new SelectionListener() {

							@Override
							public void widgetSelected(SelectionEvent e) {
								String project = projectTab_projects_answer
										.getText();
								if (!project.isEmpty()) {
									setManager(project,
											managerCheckButton.getSelection());
								}
							}

							@Override
							public void widgetDefaultSelected(SelectionEvent e) {

							}
						});

				Label lblNewLabel = new Label(composite_5, SWT.NONE);
				lblNewLabel.setFont(SWTResourceManager.getFont("Segoe UI", 10,
						SWT.NORMAL));
				lblNewLabel.setLayoutData(BorderLayout.NORTH);
				lblNewLabel.setText("User Info:");
				sashForm_4.setWeights(new int[] { 10, 2, 10, 10 });

				Composite composite_4 = new Composite(assignedUsersSash1,
						SWT.NONE);
				composite_4.setLayoutData(BorderLayout.SOUTH);
				composite_4.setLayout(new GridLayout(3, false));

				Button button = new Button(composite_4, SWT.NONE);
				button.addSelectionListener(new SelectionAdapter() {

					@Override
					public void widgetSelected(SelectionEvent e) {
					}
				});
				button.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true,
						false, 1, 1));
				button.setText("Create User");
				button.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(SelectionEvent e) {
						if (projectTab_projectID_answer.getText().isEmpty()) {
							MessageDialog.openError(Application.getShell(), "Error",
									"No Server Selected");
						} else {
							Server server = ServerList.getTargetServers().get(
									projectTab_projectID_answer.getText());
							createUser(shell, server);
						}
					}

					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
					}
				});

				Button button_1 = new Button(composite_4, SWT.NONE);
				button_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
						false, false, 1, 1));
				button_1.setText("Edit User");
				button_1.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(SelectionEvent e) {
						if (projectTab_projectID_answer.getText().isEmpty()
								|| userTab_userName_answer.getText().isEmpty()) {
							MessageDialog.openError(Application.getShell(), "Error",
									"No Server/User Selected");
						} else {
							Server server = ServerList.getTargetServers().get(
									projectTab_projectID_answer.getText());
							I2B2User i2b2user = ServerList.getI2B2UserData(
									userTab_userName_answer.getText(), null,
									server);
							editUser(shell, i2b2user, server);
						}
					}

					@Override
					public void widgetDefaultSelected(SelectionEvent e) {

					}
				});

				Button button_2 = new Button(composite_4, SWT.CENTER);
				button_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
						false, false, 1, 1));
				button_2.setText("Delete User");
				button_2.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(SelectionEvent e) {
						deleteUser(userTab_userName_answer.getText(), shell);
					}

					@Override
					public void widgetDefaultSelected(SelectionEvent e) {

					}
				});
				sashForm_1.setWeights(new int[] { 1, 2 });
				sashForm.setWeights(new int[] { 1, 2 });

				// TabItem tbtmMisc = new TabItem(tabFolder, SWT.NONE);
				// tbtmMisc.setText("Misc");

				Composite buttonBar = new Composite(parent, SWT.BORDER);
				buttonBar.setLayoutData(BorderLayout.SOUTH);
				GridLayout gl_buttonBar = new GridLayout(1, false);
				gl_buttonBar.marginBottom = 2;
				gl_buttonBar.marginLeft = 2;
				gl_buttonBar.marginRight = 2;
				gl_buttonBar.marginTop = 2;
				gl_buttonBar.horizontalSpacing = 2;
				gl_buttonBar.marginHeight = 2;
				gl_buttonBar.marginWidth = 2;
				buttonBar.setLayout(gl_buttonBar);

				Button okButton = new Button(buttonBar, SWT.CENTER);
				okButton.addSelectionListener(new SelectionAdapter() {

					@Override
					public void widgetSelected(SelectionEvent e) {
					}
				});
				GridData gd_okButton = new GridData(SWT.RIGHT, SWT.CENTER,
						true, false, 2, 1);
				gd_okButton.widthHint = 90;
				okButton.setLayoutData(gd_okButton);
				okButton.setText("OK");
				okButton.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(SelectionEvent e) {
						display.dispose();
					}

					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
					}
				});

				// Button cancelButton = new Button(buttonBar, SWT.CENTER);
				// GridData gd_cancelButton = new GridData(SWT.RIGHT,
				// SWT.CENTER, false, false, 1, 1);
				// gd_cancelButton.widthHint = 90;
				// cancelButton.setLayoutData(gd_cancelButton);
				// cancelButton.setText("Cancel");
				// cancelButton.addSelectionListener(new SelectionListener() {
				// @Override
				// public void widgetSelected(SelectionEvent e) {
				// display.dispose();
				// }
				//
				// @Override
				// public void widgetDefaultSelected(SelectionEvent e) {
				// }
				// });
				shell.open();

				// Set up the event loop.
				while (!shell.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}
				display.dispose();

			}
		});
		// THREAD END
		a.start();
		return null;
	}

	private static void editUser(Shell shell, I2B2User user, Server server) {
		WizardDialog wizardDialog = new WizardDialog(shell, new EditUserWizard(
				user, server));
		wizardDialog.open();
		refresh();
	}

	private static void createUser(Shell shell, Server server) {
		WizardDialog wizardDialog = new WizardDialog(shell, new AddUserWizard(
				server));
		wizardDialog.open();
		refresh();
	}

	private static void setManager(String project, boolean setManager) {
		Server server = ServerList.getTargetServers().get(
				projectTab_projectID_answer.getText());
		String username = userTab_userName_answer.getText();
		I2B2User i2b2user = ServerList.getI2B2UserData(username, null, server);
		ServerList.setManager(i2b2user.getUserName(), project, server,
				setManager);
		refresh();
	}

	private static void setAdmin(String project, boolean setAdmin) {
		Server server = ServerList.getTargetServers().get(
				projectTab_projectID_answer.getText());
		String username = userTab_userName_answer.getText();
		I2B2User i2b2user = ServerList.getI2B2UserData(username, null, server);
		ServerList.setAdmin(i2b2user.getUserName(), project, server, setAdmin);
		refresh();
	}

	private static void unAssignAll(String project) {
		TreeItem[] items = projectTab_assignedUserTree.getItems();
		for (TreeItem item : items) {
			String username = item.getText();
			Server server = ServerList.getTargetServers().get(
					projectTab_projectID_answer.getText());
			I2B2User i2b2user = ServerList.getI2B2UserData(username, null,
					server);
			ServerList.unAssignUserFromProject(i2b2user.getUserName(), project,
					server);
		}
		refresh();
	}

	private static void unAssignUserFromProject(String project) {
		TreeItem[] items = projectTab_assignedUserTree.getSelection();
		for (TreeItem item : items) {
			String username = item.getText();
			Server server = ServerList.getTargetServers().get(
					projectTab_projectID_answer.getText());
			I2B2User i2b2user = ServerList.getI2B2UserData(username, null,
					server);
			ServerList.unAssignUserFromProject(i2b2user.getUserName(), project,
					server);
		}
		refresh();
	}

	private static void assignAllUsersToProject(String project) {

		TreeItem[] items = projectTab_allUserTree.getItems();
		for (TreeItem item : items) {
			String username = item.getText();
			Server server = ServerList.getTargetServers().get(
					projectTab_projectID_answer.getText());
			I2B2User i2b2user = ServerList.getI2B2UserData(username, null,
					server);
			ServerList.assignUserToProject(i2b2user.getUserName(), project,
					server);
		}
		refresh();
	}

	private static void assignUserToProject(String project) {

		TreeItem[] items = projectTab_allUserTree.getSelection();
		for (TreeItem item : items) {
			String username = item.getText();
			Server server = ServerList.getTargetServers().get(
					projectTab_projectID_answer.getText());
			I2B2User i2b2user = ServerList.getI2B2UserData(username, null,
					server);
			ServerList.assignUserToProject(i2b2user.getUserName(), project,
					server);
		}
		refresh();
	}

	private static void deleteUser(String username, Shell shell) {

		boolean confirm = MessageDialog.openConfirm(Application.getShell(), "Confirm Deletion",
				"Do you really want to delete the user " + username + "?");

		if (confirm) {
			Server server = ServerList.getTargetServers().get(
					projectTab_projectID_answer.getText());
			ServerList.removeUser(server, username);
		}
		refresh();
	}

	public static void refresh() {
		Server server = ServerList.getTargetServers().get(
				projectTab_projectID_answer.getText());
		I2B2User i2b2user = ServerList.getI2B2UserData(
				userTab_userName_answer.getText(), null, server);
		userInfoComposite.setRedraw(false);
		// userTab_userName_answer.setText(i2b2user.getUserName());
		// userTab_userName_answer.pack();
		userTab_eMail_answer.setText(i2b2user.getEmail());
		userTab_eMail_answer.pack();
		userTab_fullname_answer.setText(i2b2user.getFullname());
		userTab_fullname_answer.pack();
		// userTab_lastlogin_answer.setText(i2b2user.getLastLogin());
		// userTab_lastlogin_answer.pack();
		userTab_status_answer.setText(i2b2user.getStatus());
		userTab_status_answer.pack();

		adminCheckButton.setSelection(ServerList.getAdmin(server,
				userTab_userName_answer.getText(),
				projectTab_projects_answer.getText()));
		managerCheckButton.setSelection(ServerList.getManager(server,
				userTab_userName_answer.getText(),
				projectTab_projects_answer.getText()));
		// userTab_lastQuery_answer.setText("");
		// userTab_lastQuery_answer.pack();

		allUserList = ServerList.getAllUsersFromServer(server);
		userList = ServerList.getAssignedUsersFromProject(server,
				projectTab_projects_answer.getText());
		projectTab_allUserTree.removeAll();
		if (allUserList != null) {
			for (String item : allUserList) {
				if (!userList.contains(item.toLowerCase())) {
					TreeItem newItem = new TreeItem(projectTab_allUserTree,
							SWT.NONE);
					newItem.setText(item);
				}
			}
		}
		projectTab_allUserTree.redraw();

		projectTab_assignedUserTree.removeAll();
		if (userList != null) {
			for (String item : userList) {
				// I2B2User user = ServerList.getI2B2UserData(item,
				// projectTab_projects_answer.getText(), server);
				TreeItem newItem = new TreeItem(projectTab_assignedUserTree,
						SWT.NONE);
				newItem.setText(item);

				// newItem.setData(user);
			}
		}
		userInfoComposite.setRedraw(true);
	}
}