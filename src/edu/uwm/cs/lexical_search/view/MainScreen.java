package edu.uwm.cs.lexical_search.view;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import edu.uwm.cs.lexical_search.controller.MainScreenController;
import edu.uwm.cs.lexical_search.controller.QueryController;
import edu.uwm.cs.lexical_search.controller.TabWindowController;
import edu.uwm.cs.lexical_search.controller.TreeWindowController;
import edu.uwm.cs.lexical_search.model.Query;
import edu.uwm.cs.lexical_search.util.DialogUtils;

public class MainScreen{

	protected Shell mainWindow;
	private QueryComposite queryWindowComposite;
	private TreeWindowComposite treeWindowComposite;
	private TabWindowComposite tabWindowComposite;
	private MainScreenController mainScreenController;
	
	private static final int REPOSITORY_URL_INDEX = 0;
	private static final int REPOSITORY_HEAD_REV_INDEX = 1;
	private static final int REPOSITORY_FIRST_REV_INDEX = 2;
	private static final int REPOSITORY_LAST_REV_INDEX = 3;

	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainScreen window = new MainScreen();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 * @throws MalformedURLException 
	 */
	public void open() throws MalformedURLException{
		Display display = Display.getDefault();
		
		MainScreenController mainScreenController = new MainScreenController(this);
		this.addController(mainScreenController);

		createContents();
		DialogUtils.centerScreen(mainWindow);

		mainWindow.open();
		mainWindow.layout();

		//TODO: probably want a drop down list of the most recent entries instead
		// Open the repository dialog before creating the initial model
		String[] repoInfo = new RepositoryPromptWindow(mainWindow, SWT.DIALOG_TRIM).open("http://jpcsp.googlecode.com/svn/");

		// Add Observers and controllers to the Model and views
		createModel(repoInfo);
		
		while (!mainWindow.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	/**
	 * Creates a new model for the MVC pattern
	 * @param repoInfo
	 * @throws MalformedURLException
	 */
	public void createModel(String[] repoInfo) throws MalformedURLException{
		
		String repoURL = repoInfo[REPOSITORY_URL_INDEX];
		mainWindow.setText("Lexical Search - Repository: " + repoURL);
		
		queryWindowComposite.setLblRepoDir(repoURL);
		queryWindowComposite.setHeadRevTextBox(repoInfo[REPOSITORY_HEAD_REV_INDEX]);
		queryWindowComposite.setFirstRevTextBox(repoInfo[REPOSITORY_FIRST_REV_INDEX]);
		queryWindowComposite.setLastRevTextBox(repoInfo[REPOSITORY_LAST_REV_INDEX]);
		
		Query model = new Query(new URL(repoURL));
		addModelOberserverAndController(model);
	}

	/**
	 * @param model
	 * @throws MalformedURLException 
	 */
	private void addModelOberserverAndController(Query model) {
		
		model.addObserver(tabWindowComposite);
		//TODO: treeWindowComposite unused?
		model.addObserver(treeWindowComposite);
		model.addObserver(queryWindowComposite);

		QueryController queryController = new QueryController(model, this);
		queryWindowComposite.addController(queryController);
		
		TabWindowController tabWindowController = new TabWindowController(this);
		tabWindowComposite.addController(tabWindowController);
		
		TreeWindowController treeWindowController = new TreeWindowController(this);
		treeWindowComposite.addController(treeWindowController);
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		mainWindow = new Shell();
		mainWindow.setSize(1200, 700);
		mainWindow.setText("Lexical Search");
		
		GridLayout layout = new GridLayout();
	    layout.numColumns = 2;
	    layout.makeColumnsEqualWidth = false;

	    mainWindow.setLayout(layout);
	    
		Menu menu = new Menu(mainWindow, SWT.BAR);
		mainWindow.setMenuBar(menu);
		
		MenuItem mntmFile = new MenuItem(menu, SWT.CASCADE);
		mntmFile.setText("File");
		
		Menu fileMenu = new Menu(mntmFile);
		mntmFile.setMenu(fileMenu);
		
		MenuItem mntmOpen = new MenuItem(fileMenu, SWT.NONE);
		mntmOpen.addSelectionListener(mainScreenController);
		mntmOpen.setText("Open");

		MenuItem mntmRepository = new MenuItem(fileMenu, SWT.NONE);
		mntmRepository.addSelectionListener(mainScreenController);
		mntmRepository.setText("Change Repository");
		
		MenuItem mntmExit = new MenuItem(fileMenu, SWT.NONE);
		mntmExit.addSelectionListener(mainScreenController);	
		mntmExit.setText("Exit");

		MenuItem mntmHelp = new MenuItem(menu, SWT.CASCADE);
		mntmHelp.setText("Help");
		
		Menu helpMenu = new Menu(mntmHelp);
		mntmHelp.setMenu(helpMenu);
		
		MenuItem mntmHelpMenuItem = new MenuItem(helpMenu, SWT.NONE);
		mntmHelpMenuItem.addSelectionListener(mainScreenController);
		mntmHelpMenuItem.setText("Help");
        
	    GridData data = new GridData(GridData.FILL_VERTICAL);
	    data.widthHint = 200;
		treeWindowComposite = new TreeWindowComposite(mainWindow, SWT.BORDER | SWT.RESIZE);
		treeWindowComposite.setLayoutData(data);

	    data = new GridData(GridData.FILL_BOTH);
	    data.verticalSpan = 2;
		tabWindowComposite = new TabWindowComposite(mainWindow, SWT.BORDER | SWT.RESIZE);
		tabWindowComposite.setLayoutData(data);
		
		data = new GridData();
	    data.widthHint = 200;
	    data.heightHint = 192;
		queryWindowComposite = new QueryComposite(mainWindow, SWT.BORDER);
		queryWindowComposite.setLayoutData(data);
		
	}
    
	public Shell getMainWindow() {
		return mainWindow;
	}

	public QueryComposite getQueryComposite() {
		return queryWindowComposite;
	}
	public TreeWindowComposite getTreeComposite() {
		return treeWindowComposite;
	}
	public TabWindowComposite getTabwindowComposite() {
		return tabWindowComposite;
	}
	
	private void addController(MainScreenController controller){
		mainScreenController = controller;
	}
}
