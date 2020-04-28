package edu.uwm.cs.lexical_search.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.SWT;

import edu.uwm.cs.lexical_search.controller.TreeWindowController;
import edu.uwm.cs.lexical_search.model.QueryNode;
import edu.uwm.cs.lexical_search.util.Pair;
import edu.uwm.cs.lexical_search.view.TabWindowComposite.GraphNodes;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

public class TreeWindowComposite extends Composite implements Observer {

	private TreeWindowController treeWindowController;
	private Tree treeFileViewer; 
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public TreeWindowComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		GridData data = new GridData(GridData.FILL_BOTH);
		treeFileViewer = new Tree(this, SWT.BORDER | SWT.MULTI);
		treeFileViewer.setToolTipText("Tree structure to display search results");
		treeFileViewer.setHeaderVisible(true);
		treeFileViewer.setLinesVisible(true);
		treeFileViewer.setLayoutData(data);
	}
	
	/**
	 * Helper method to add items to the file browser
	 * @param gn
	 */
	public void addSubTreeItems(GraphNodes gn){
		
		QueryNode node = gn.getQueryNode();
        String treeRootTag = node.getRevision() +" - " + node.getType();
        
        if (checkIfNodeExist(treeRootTag))
        {
        	TreeItem revItem = new TreeItem(treeFileViewer, 0);
        	revItem.setText(treeRootTag);
        	revItem.setData(gn);
	        
	        // Populate the tree with the data in the node
		    for (Pair<File, ArrayList<Integer>> q : node.getFiles()) 
		    {
				TreeItem fileNameItem = new TreeItem(revItem, 0);
				fileNameItem.setText(q.getLeft().getName());
				fileNameItem.setData(q.getLeft());
				
				for (Integer i : q.getRight()) 
				{
					TreeItem lineNumItem = new TreeItem(fileNameItem, 0);
					lineNumItem.setText("Result line " + i);
					lineNumItem.setData(q);
	            }
		    }
		    addMouseMenu();
        }
	}
	
	/**
	 * Helper method to check the file browser (tree view) 
	 * for name recurrences
	 * @param nodeTag
	 * @return
	 */
	private boolean checkIfNodeExist(String nodeTag){
		
		TreeItem[] items =  treeFileViewer.getItems();
		
		for (int i = 0; i < items.length; ++i)
		{
			if (items[i].getText().equals(nodeTag))
				return false;				
		}
		
		return true;
	}
	/**
	 * Method implementation to add the context menu
	 * in the Tree composite window	 
	 */
	private void addMouseMenu(){
	    Menu popupMenu = new Menu(treeFileViewer);
	    treeFileViewer.setMenu(popupMenu);
	    
	    MenuItem openItem = new MenuItem(popupMenu, SWT.CASCADE);
	    openItem.setText("Open");
	    openItem.addSelectionListener(treeWindowController);
	    
	    MenuItem separator =  new MenuItem(popupMenu, SWT.SEPARATOR);
	    separator.setText("");
	    
	    MenuItem deleteItem = new MenuItem(popupMenu, SWT.NONE);
	    deleteItem.setText("Remove");
	    deleteItem.addSelectionListener(treeWindowController);
	    
	    MenuItem compareItem = new MenuItem(popupMenu, SWT.NONE);
	    compareItem.setText("Compare");
	    compareItem.addSelectionListener(treeWindowController);

	}
	
	public Tree getTree(){
		return treeFileViewer;
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	public void update(Observable o, Object arg) {

	}
	
	/**
	 * Method implementation to add the controller to this view
	 * @param controller
	 */
	public void addController(TreeWindowController controller) {
		treeWindowController = controller;
		treeFileViewer.addMouseListener(controller);
	}
}
