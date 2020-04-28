package edu.uwm.cs.lexical_search.model;

import java.util.ArrayList;

public class QueryResult {
	private ArrayList<QueryNode> tagNodes;
	private ArrayList<QueryNode> trunkNodes;
	private ArrayList<QueryNode> branchNodes;

	public QueryResult() {
		tagNodes = new ArrayList<QueryNode>();
		trunkNodes = new ArrayList<QueryNode>();
		branchNodes = new ArrayList<QueryNode>();
	}

	public QueryResult(ArrayList<QueryNode> tagNodes,
			ArrayList<QueryNode> trunkNodes, ArrayList<QueryNode> branchNodes) {
		this.tagNodes = tagNodes;
		this.trunkNodes = trunkNodes;
		this.branchNodes = branchNodes;
	}

	public void addTagNode(QueryNode n) {
		tagNodes.add(n);
	}

	public void addTrunkNode(QueryNode n) {
		trunkNodes.add(n);
	}

	public void addBranchNode(QueryNode n) {
		branchNodes.add(n);
	}

	public ArrayList<QueryNode> getTagNodes() {
		return tagNodes;
	}

	public ArrayList<QueryNode> getTrunkNodes() {
		return trunkNodes;
	}

	public ArrayList<QueryNode> getBranchNodes() {
		return branchNodes;
	}
}
