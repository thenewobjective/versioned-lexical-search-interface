package edu.uwm.cs.lexical_search.model;

import java.io.File;
import java.util.ArrayList;

import edu.uwm.cs.lexical_search.util.Pair;

public class QueryNode {
	private int revNum;
	private String revision;
	private ArrayList<Pair<File, ArrayList<Integer>>> files;
	private SvnType type;
	private String date;
	private String time;

	/**
	 * Empty constructor
	 */
	public QueryNode() {
		this.files = new ArrayList<Pair<File, ArrayList<Integer>>>();
	}

	/**
	 * 
	 * @param revision
	 * @param files
	 */
	public QueryNode(String revision, int revNum, String date, String time,
			ArrayList<Pair<File, ArrayList<Integer>>> files, SvnType type) {
		this.revNum = revNum;
		this.revision = revision;
		this.date = date;
		this.time = time;
		this.files = files;
		this.type = type;
	}

	public String getRevision() {
		return this.revision;
	}

	public ArrayList<Pair<File, ArrayList<Integer>>> getFiles() {
		return this.files;
	}

	public int getRevNum() {
		return revNum;
	}

	public SvnType getType() {
		return type;
	}

	public void setRevNum(int revNum) {
		this.revNum = revNum;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	public void setFiles(ArrayList<Pair<File, ArrayList<Integer>>> files) {
		this.files = files;
	}

	public void addFile(Pair<File, ArrayList<Integer>> pair) {
		this.files.add(pair);
	}

	public void setType(SvnType type) {
		this.type = type;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
