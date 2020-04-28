package edu.uwm.cs.lexical_search.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.custom.Bullet;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.custom.LineBackgroundEvent;
import org.eclipse.swt.custom.LineBackgroundListener;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GlyphMetrics;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;

import edu.uwm.cs.lexical_search.controller.TabWindowController;
import edu.uwm.cs.lexical_search.model.QueryNode;
import edu.uwm.cs.lexical_search.model.QueryResult;
import edu.uwm.cs.lexical_search.model.SvnType;
import edu.uwm.cs.lexical_search.util.JavaLineStyler;
import edu.uwm.cs.lexical_search.util.Pair;
import edu.uwm.cs.lexical_search.util.SWTResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.custom.CaretListener;
import org.eclipse.swt.custom.CaretEvent;

public class TabWindowComposite extends Composite implements Observer {
	
	public static Image nodeImage = SWTResourceManager.getImage(TabWindowComposite.class, "/Icons/Node.png");
	public static Image nodeSelectedImage = SWTResourceManager.getImage(TabWindowComposite.class, "/Icons/NodeSelected.png");
	private CTabFolder tabFolder;
	private ScrolledComposite graphScrollableComposite;
	private StyledText outputWindow;
	private Canvas canvas;
	private int endingRev = 1;
	static private ArrayList<String> openedTabs = new ArrayList<String>();
	static final int OUTPUT_WINDOW_TAB = 0;
	static final int GRAPH_WINDOW_TAB  = 1;
	static final int TAG_NODES_HEIGHT = 30;
	static final int TRUNK_NODES_HEIGHT = 180;
	static final int BRANCHES_NODES_HEIGHT = 330;
	static final int TIME_LINE_HEIGHT = 430;
	static LineBackground lineBackGround;
	private TabWindowController tabWindowController;
	private QueryResult queryResult;
	private Canvas timeCanvas;
	private ScrolledComposite timeScrollableComposite;
	private ToolItem tltmCopy;
	private ToolItem tltmCut;
	private ToolItem tltmStop;
	public boolean modified = false;
	
	/**
	 * Create the composite and populate it.
	 * @param parent
	 * @param style
	 */
	public TabWindowComposite(Composite parent, int style) {
		super(parent, style);
		
		setLayout(new GridLayout(1, true));
		GridData data = new GridData(GridData.FILL_BOTH);
		tabFolder = new CTabFolder(this, SWT.BORDER);
		tabFolder.setLayoutData(data);

		Composite composite = new Composite(tabFolder, SWT.FILL);
		composite.setLayout(new GridLayout(1, false));
		
		CTabItem tbtmOutput = new CTabItem(tabFolder, SWT.NONE);
		tbtmOutput.setText("Output");
		tbtmOutput.setControl(composite);

		openedTabs.add(tbtmOutput.getText());

		ToolBar toolBar = new ToolBar(composite, SWT.FLAT | SWT.RIGHT);
		toolBar.setBounds(20, 10, 220, 38);
		toolBar.setData(outputWindow);
		
		tltmCopy = new ToolItem(toolBar, SWT.NONE);
		tltmCopy.setText("Copy");
		tltmCopy.setImage(SWTResourceManager.getImage(TabWindowComposite.class, "/Icons/page-copy.png"));
		
		tltmCut = new ToolItem(toolBar, SWT.NONE);
		tltmCut.setText("Cut");
		tltmCut.setImage(SWTResourceManager.getImage(TabWindowComposite.class, "/Icons/cut-icon.png"));
		
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		tltmStop = new ToolItem(toolBar, SWT.NONE);
		tltmStop.setImage(SWTResourceManager.getImage(TabWindowComposite.class, "/Icons/Stop-icon.png"));
		tltmStop.setText("Stop");
		
		data = new GridData(GridData.FILL_BOTH);
		outputWindow = new StyledText(composite, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		outputWindow.setFont(SWTResourceManager.getFont("Consolas", 10, SWT.NORMAL));
		outputWindow.addLineBackgroundListener(new LineBackground());
		outputWindow.addCaretListener(new CaretListener() {
			public void caretMoved(CaretEvent arg0) {
				tabFolder.getItem(OUTPUT_WINDOW_TAB).setText("Output");
			}
		});
		outputWindow.addExtendedModifyListener(new ExtendedModifyListener() {
			
			@Override
			public void modifyText(ExtendedModifyEvent event) {
				modified = true;
			}
		});
		outputWindow.setLayoutData(data);
		
		// Create the Graph-Composite 
		CTabItem tbtmRepositoryGraph = new CTabItem(tabFolder, SWT.NONE);
		tbtmRepositoryGraph.setText("Repository Graph");
		openedTabs.add(tbtmRepositoryGraph.getText());
		
		Composite graphTabComposite = new Composite(tabFolder, SWT.BORDER);
		tbtmRepositoryGraph.setControl(graphTabComposite);
		graphTabComposite.setLayout(new GridLayout(2, false));

		Composite labelComposite = new Composite(graphTabComposite, SWT.BORDER);

		data = new GridData(GridData.FILL_VERTICAL);
		data.widthHint = 110;
		
		Label lblTags = new Label(labelComposite, SWT.NONE);
		lblTags.setBounds(10, 50, 93, 34);
		lblTags.setFont(SWTResourceManager.getFont("Segoe UI", 15, SWT.BOLD));
		lblTags.setText("Tags");
		
		Label lblTrunk = new Label(labelComposite, SWT.NONE);
		lblTrunk.setBounds(10, 200, 93, 34);
		lblTrunk.setText("Trunk");
		lblTrunk.setFont(SWTResourceManager.getFont("Segoe UI", 15, SWT.BOLD));
		
		Label lblBranches = new Label(labelComposite, SWT.NONE);
		lblBranches.setBounds(10, 350, 93, 34);
		lblBranches.setText("Branches");
		lblBranches.setFont(SWTResourceManager.getFont("Segoe UI", 15, SWT.BOLD));
			
		labelComposite.setLayoutData(data);
		
		
		graphScrollableComposite = new ScrolledComposite(graphTabComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		data = new GridData(GridData.FILL_BOTH);
		graphScrollableComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
	
		canvas = new Canvas(graphScrollableComposite, SWT.NONE);
		canvas.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		graphScrollableComposite.setContent(canvas);
		graphScrollableComposite.setLayoutData(data);

		
		Composite timeLabelComposite= new Composite(graphTabComposite,	SWT.BORDER);
		Label lblTime = new Label(timeLabelComposite, SWT.NONE);
		data = new GridData();
		data.widthHint = 110;
		data.heightHint = 115;
		lblTime.setBounds(10, 38, 90, 28);
		lblTime.setText("Time");
		lblTime.setFont(SWTResourceManager.getFont("Segoe UI", 15, SWT.BOLD));
		timeLabelComposite.setLayoutData(data);
		
		timeScrollableComposite = new ScrolledComposite(graphTabComposite, SWT.BORDER | SWT.H_SCROLL);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.heightHint = 100;

		timeScrollableComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		timeCanvas = new Canvas(timeScrollableComposite, SWT.NONE);
		timeCanvas.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			
		timeScrollableComposite.setContent(timeCanvas);
		timeScrollableComposite.setLayoutData(data);
		
		selectOutputWindowTab();
	}
	
	/**
	 * Create a context menu
	 * @param parent
	 */
	private void addPopUpMenu(Control parent) {
		
		Menu contextMenu = new Menu(parent);
		parent.setMenu(contextMenu);
		contextMenu.setData(parent);
		
		MenuItem copyMenu = new MenuItem(contextMenu, SWT.CASCADE);
		copyMenu.setText("Copy");
		copyMenu.addSelectionListener(tabWindowController);
		
		MenuItem cutMenu = new MenuItem(contextMenu, SWT.NONE);
		cutMenu.setText("Cut");
		cutMenu.addSelectionListener(tabWindowController);
		
		MenuItem selectAllMenu = new MenuItem(contextMenu, SWT.NONE);
		selectAllMenu.setText("Select All");
		selectAllMenu.addSelectionListener(tabWindowController);
	}
	
	/**
	 * Install the controller need to use the MVC pattern
	 * @param controller
	 */
	public void addController(TabWindowController controller) {
		tabWindowController = controller;
		addPopUpMenu(outputWindow);
		
		tltmCopy.addSelectionListener(controller);
		tltmCut.addSelectionListener(controller);
		tltmStop.addSelectionListener(controller);
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	/**
	 * Add the content to the Output Window
	 * @param text
	 */
	public void setOutputWindowText(String text)
	{
		selectOutputWindowTab();
		tabFolder.getItem(OUTPUT_WINDOW_TAB).setText("*Output");
		String lines[] = text.split("[\\r\\n]+");
		
		for (int i = 0; i < lines.length; ++i)
		{
			System.out.println(lines[i]);
			outputWindow.append(lines[i] + '\n');
			outputWindow.setTopIndex(outputWindow.getLineCount());
			outputWindow.update();
			// Wait until the output is done updating the text
			while(!modified);
			modified = false;
		}
	}

	/**
	 * 
	 */
	public void selectOutputWindowTab() {
		tabFolder.setSelection(OUTPUT_WINDOW_TAB);
		this.update();
	}
	
	/**
	 * Create a new tab with the new file
	 * @param tabText
	 * @param text
	 * @param lineNumbers 
	 */
	public void createNewEditorTab(String tabText, String text, int startingLine, final ArrayList<Integer> lineNumbers){
		
		if (tabText != null)
		{
			if (!(openedTabs.contains(tabText)))
			{
				final Composite composite = new Composite(tabFolder, SWT.FILL);
				composite.setLayout(new GridLayout(1, false));
				
				final CTabItem tabTextEditor = new CTabItem(tabFolder, SWT.V_SCROLL);
				tabTextEditor.setText(tabText);
				tabTextEditor.setShowClose(true);
				tabTextEditor.setControl(composite);
				
				ToolBar toolBar = new ToolBar(composite, SWT.FLAT | SWT.RIGHT);
				toolBar.setBounds(20, 10, 141, 38);
				
				ToolItem tltmCopy = new ToolItem(toolBar, SWT.NONE);
				tltmCopy.setText("Copy");
				tltmCopy.setImage(SWTResourceManager.getImage(TabWindowComposite.class, "/Icons/page-copy.png"));
				
				ToolItem tltmCut = new ToolItem(toolBar, SWT.NONE);
				tltmCut.setText("Cut");
				tltmCut.setImage(SWTResourceManager.getImage(TabWindowComposite.class, "/Icons/cut-icon.png"));
				
				new ToolItem(toolBar, SWT.SEPARATOR);
				
				GridData data = new GridData(GridData.FILL_BOTH);
				final StyledText textEditor = new StyledText(composite, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
				textEditor.setFont(SWTResourceManager.getFont("Consolas", 10, SWT.NORMAL));
				tabTextEditor.setData(textEditor);
				textEditor.setIndent(4);
				textEditor.setLayoutData(data);
				
				JavaLineStyler styler = new JavaLineStyler();
				// Parse the block comments first
				styler.parseBlockComments(text);
				
				textEditor.addLineStyleListener(styler);
				
				// Parse the rest of the text
				textEditor.setText(text);
				
				// Add line numbers
				textEditor.addLineStyleListener(new LineStyleListener()
				{
				    public void lineGetStyle(LineStyleEvent e)
				    {
				        //Set the line number
				        e.bulletIndex = textEditor.getLineAtOffset(e.lineOffset);
				        //Set the style, 12 pixles wide for each digit
				        StyleRange style = new StyleRange();
				        style.fontStyle = SWT.ITALIC | SWT.BOLD;
				        style.metrics = new GlyphMetrics(0, 0, Integer.toString(textEditor.getLineCount()+1).length()*12);

				        //Create and set the bullet
				        e.bullet = new Bullet(ST.BULLET_NUMBER,style);
				    }
				});
				
				textEditor.addLineBackgroundListener(new LineBackgroundListener() {
					@Override
					public void lineGetBackground(LineBackgroundEvent e) {
						Color color = new Color(getDisplay(), new RGB(255, 255, 100));
						for(Integer ln : lineNumbers)
						{
							if(textEditor.getLineAtOffset(e.lineOffset) == ln.intValue() - 1)
							{
								e.lineBackground = color;
							}
						}
					}
				});
				
				tabTextEditor.addDisposeListener(new DisposeListener() {
					@Override
					public void widgetDisposed(DisposeEvent arg0) {
						CTabItem temp = (CTabItem)(arg0.getSource());
						//remove the tab from the array list 
						openedTabs.remove(temp.getText());
						composite.dispose();
					}
				});
				
				tltmCopy.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						textEditor.copy();
					}
				});
				
				tltmCut.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						textEditor.cut();
					}
				});
				
				addPopUpMenu(textEditor);
				
				// Add new tab to the list 
				openedTabs.add(tabText);
				// Scroll down to the line number 
				textEditor.setTopIndex(startingLine - 1);
				// Select the tab that contains the last opened file
				tabFolder.setSelection(tabTextEditor);
				
			}
			else
			{	
				tabFolder.setSelection(openedTabs.indexOf(tabText));
				StyledText temp = (StyledText)tabFolder.getItem(openedTabs.indexOf(tabText)).getData();
				temp.setTopIndex(startingLine - 1);
			}
		}
	}
	
	/**
	 * Setter for the end revision number 
	 * @param endingRev
	 */
	public void setEndingRev(int endingRev) {
		this.endingRev = endingRev;
	}

	/**
	 * Calculate the next point in the canvas to place a new node
	 * @param x
	 * @return
	 */
	private int calcNextPoint(int x){
		return 20 + Math.abs((120 * x));
	}
	
	/**
	 * Generate a new canvas without any data in it
	 */
	private void createNewCanvas(){
		canvas.dispose();
		canvas = new Canvas(graphScrollableComposite, SWT.DOUBLE_BUFFERED);
		canvas.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		graphScrollableComposite.setContent(canvas);
		graphScrollableComposite.setOrigin(0, 0);
		timeScrollableComposite.setOrigin(0, 0);
	}
	
	/**
	 * Remove all the opened tabs from the TabFolder
	 */
	public void resetTabWindow(){
	
		for (int i = tabFolder.getItemCount() - 1; i > 1; --i)
		{
			CTabItem tab = tabFolder.getItem(i);
			openedTabs.remove(tab.getText());
			tab.dispose();
		}
	}

	/**
	 * Clear the content of the output window
	 */
	public void clearOutputWindow(){
		outputWindow.setText("");
	}
	
	/**
	 * Update method for the MVC pattern
	 */
	@Override
	public void update(Observable o, Object obj) {

		if (obj instanceof QueryResult){
			queryResult = (QueryResult)obj;
			addGraphNodesToCanvas();
//			tabFolder.setSelection(GRAPH_WINDOW_TAB);
		}
		
		if (obj instanceof String){
			String msg = (String)obj;
			setOutputWindowText(msg);
		}
	}
	
	/**
	 * LineBackground listener for the log output window
	 * @author Alex
	 *
	 */
	private class LineBackground implements LineBackgroundListener {
		
		@Override
		public void lineGetBackground(LineBackgroundEvent event) {

			Display display = Display.getDefault();
			String[] msg = {"INFO:", "WARNING:", "COMMAND:", "OUTPUT:", "ERROR:", "DONE"};
			Color[] colors = new Color[] {
											 new Color(display, new RGB(190, 190, 190)), // Gray
											 new Color(display, new RGB(255, 255, 50)),  // Light-Yellow
											 new Color(display, new RGB(255, 228, 181)), // Moccasin
											 new Color(display, new RGB(50, 205, 50)), 	 // Forest-Green
											 new Color(display, new RGB(255, 69, 0)), 	 // Orange-Red
											 new Color(display, new RGB(70, 130, 180))   // Steel Blue
				 						  };
			
			if (event.lineText.length() > 0)
		    {
		    	for (int i = 0; i < msg.length; ++i){
		    		if (event.lineText.indexOf(msg[i]) != -1){
		    			event.lineBackground = colors[i];
		    		}		    			
		    	}
		    }
		}
	}
	
	/**
	 * Populate the canvas with graphnodes using the 
	 * results from the query 
	 */
	private void addGraphNodesToCanvas(){
		
		createNewCanvas();
	
		int x = 0;
		
		final ArrayList<Pair<String, String>> dateAndTime  = new ArrayList<Pair<String, String>>();
		
		final ArrayList<Point> trunkNodePoints = new ArrayList<>();
		final ArrayList<Point> tagNodePoints = new ArrayList<>();
		final ArrayList<Point> branchNodePoints = new ArrayList<>();
		
		ArrayList<QueryNode> trunkNodes = queryResult.getTrunkNodes();
		ArrayList<QueryNode> tagNodes = queryResult.getTagNodes();
		ArrayList<QueryNode> branchNodes = queryResult.getBranchNodes();
		
		while (!trunkNodes.isEmpty() || !tagNodes.isEmpty() || !branchNodes.isEmpty())
		{
			int nextNodeXPos = endingRev;
			boolean tr = false;
			boolean tg = false;
			boolean br = false;
			QueryNode trNode = null;
			QueryNode tgNode = null;
			QueryNode brNode = null;
			
			if (!trunkNodes.isEmpty())
			{
				if (trunkNodes.get(0).getRevNum() <= nextNodeXPos)
				{
					tr = true;
					trNode = trunkNodes.get(0);
					nextNodeXPos = trNode.getRevNum();
				}
				
			}
			
			if (!tagNodes.isEmpty())
			{
				if (tagNodes.get(0).getRevNum() <= nextNodeXPos)
				{	
					tg = true;
					tgNode = tagNodes.get(0);
					nextNodeXPos = tgNode.getRevNum();
				}
			}
			
			if (!branchNodes.isEmpty())
			{
				if (branchNodes.get(0).getRevNum() <= nextNodeXPos)
				{
					br = true;
					brNode = branchNodes.get(0);
					nextNodeXPos = brNode.getRevNum();
				}
			}
			
			if (tr) {
				if (trNode.getRevNum() == nextNodeXPos){
	
					dateAndTime.add(x, new Pair<String, String>(trNode.getDate(), trNode.getTime()));
					trunkNodes.remove(0);
					Point pnt = new Point(calcNextPoint(x), TRUNK_NODES_HEIGHT); 
					trunkNodePoints.add(pnt);
					new GraphNodes(canvas).createGraphNode(trNode, trNode.getType(), pnt);
				}
			}
			if (tg) {
				if (tgNode.getRevNum() == nextNodeXPos){
					dateAndTime.add(x, new Pair<String, String>(tgNode.getDate(), tgNode.getTime()));
					tagNodes.remove(0);
					Point pnt = new Point(calcNextPoint(x), TAG_NODES_HEIGHT);
					tagNodePoints.add(pnt);
					new GraphNodes(canvas).createGraphNode(tgNode, tgNode.getType(), pnt);
				}
			}
			if (br) {
				if (brNode.getRevNum() == nextNodeXPos){
					dateAndTime.add(x, new Pair<String, String>(brNode.getDate(), brNode.getTime()));
					branchNodes.remove(0);
					Point pnt = new Point(calcNextPoint(x), BRANCHES_NODES_HEIGHT);
					branchNodePoints.add(pnt);
					new GraphNodes(canvas).createGraphNode(brNode, brNode.getType(), pnt);
				}
			}
			x++;
		}
		
		final Point canvasSize = canvas.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		canvas.setSize(canvasSize.x + 50, canvasSize.y + 10);			
		// Draw lines
		canvas.addPaintListener(new PaintListener() {
			
			@Override
			public void paintControl(PaintEvent e) {
				int hOffset = nodeImage.getBounds().height / 2;
				int wOffset = nodeImage.getBounds().width / 2;
				
				e.gc.setAntialias(SWT.ON);
				e.gc.setLineWidth(4);
				e.gc.setLineCap(SWT.CAP_ROUND);
				Point prevPnt; 
				
				//Draw lines between trunk nodes
				if (!trunkNodePoints.isEmpty())
				{
					prevPnt = trunkNodePoints.get(0);
					for (Point p : trunkNodePoints){
						e.gc.drawLine(prevPnt.x + wOffset, prevPnt.y + hOffset, p.x + wOffset, p.y + hOffset);
						prevPnt = p;
					}
				}
				//Draw lines between branch nodes
				if (!branchNodePoints.isEmpty())
				{
					prevPnt = branchNodePoints.get(0);
					for (Point p : branchNodePoints){
						e.gc.drawLine(prevPnt.x + wOffset, prevPnt.y + hOffset, p.x + wOffset, p.y + hOffset);
	//					e.gc.drawLine(trunkNodePoints.get(0).x + wOffset, trunkNodePoints.get(0).y + 2 * hOffset, p.x + wOffset, p.y);
						prevPnt = p;
					}
				}
				//Draw lines between tag nodes
				if (!tagNodePoints.isEmpty())
				{
					prevPnt = tagNodePoints.get(0);
					for (Point p : tagNodePoints){
						e.gc.drawLine(prevPnt.x + wOffset, prevPnt.y + hOffset, p.x + wOffset, p.y + hOffset);
						prevPnt = p;
					}
				}
			}	
		});
		
		// Draw the data and time tags
		timeCanvas.setSize(canvasSize.x, 110);
		timeCanvas.addPaintListener(new PaintListener() {
			
			@Override
			public void paintControl(PaintEvent e) {
				int x = 0;
				Rectangle bounds = timeCanvas.getBounds();
				int lineHeight = bounds.height / 3;
				e.gc.setLineWidth(2);
				e.gc.drawLine(50, lineHeight , canvasSize.x, lineHeight);
	
				for (Pair<String, String> p : dateAndTime){
					e.gc.drawString(p.getLeft() , calcNextPoint(x) + 20, lineHeight + 5);
					e.gc.drawString(p.getRight(), calcNextPoint(x) + 30, lineHeight + 20);
					x++;
				}
			}
		});
		
		// Sync the scroll bars
		final ScrollBar timeCanvaScrollBar = timeScrollableComposite.getHorizontalBar();
		timeCanvaScrollBar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				graphScrollableComposite.setOrigin(timeCanvaScrollBar.getSelection(), graphScrollableComposite.getVerticalBar().getSelection());
			}
		});
		final ScrollBar graphCanvasScrollBar = graphScrollableComposite.getHorizontalBar();
		graphCanvasScrollBar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				timeScrollableComposite.setOrigin(graphCanvasScrollBar.getSelection(), 0);
			}
		});
	}

	/**
	 * Object implementation for the nodes used to populate
	 * the graph canvas
	 * @author Alex
	 *
	 */
	public class GraphNodes extends CLabel {
		
		private String nodeType;
		private String revision;
		private QueryNode queryNode;
		
		private GraphNodes(Composite parent) {
			super(parent, SWT.CENTER);
		}
		
		private void createGraphNode(QueryNode node, SvnType type, Point pnt){
			
			GraphNodes graphNode = new GraphNodes(canvas);
			graphNode.setFont(SWTResourceManager.getFont("Arial", 9, SWT.BOLD));
			graphNode.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			graphNode.setBackgroundImage(nodeImage);
			graphNode.setBounds(pnt.x, pnt.y, nodeImage.getBounds().width, nodeImage.getBounds().height);
			graphNode.setText(node.getRevision());
			graphNode.setToolTipText(createToolTip(node));
			graphNode.setRevision(node.getRevision());
			graphNode.setNodeType(type.name());
			graphNode.setQueryNode(node);
			
			graphNode.addMouseListener(tabWindowController);
		}
		
		public QueryNode getQueryNode() {
			return queryNode;
		}

		public void setQueryNode(QueryNode queryNode) {
			this.queryNode = queryNode;
		}

		private void setRevision(String revision) {
			this.revision = revision;
		}

		public String getNodeType() {
			return nodeType;
		}

		private void setNodeType(String nodeType) {
			this.nodeType = nodeType;
		}		
		public String getRevision() {
			return revision;
		}

		private String createToolTip(QueryNode node){
			String toolTip = "";
			for(Pair<File, ArrayList<Integer>> file : node.getFiles())
			{
				toolTip += file.getLeft().getName() + "\n";
			}
			return toolTip;
		}
	}
}
