package edu.uwm.cs.lexical_search.util;

import java.io.File;
import java.util.*;

import difflib.*;

public class FileComparator {

	private File originalFile;
	private File revisedFile;

	public FileComparator(File original, File revised) {
		this.originalFile = original;
		this.revisedFile = revised;
	}

	public StringBuilder[] comparableDiff() {

		List<String> original = FileUtils.fileToLines(originalFile);
		List<String> revised = FileUtils.fileToLines(revisedFile);

		StringBuilder leftText = new StringBuilder();
		StringBuilder rightText = new StringBuilder();

		StringBuilder[] files = new StringBuilder[2];

		Patch patch = DiffUtils.diff(original, revised);

		// List<Delta> deltas = patch.getDeltas();

		DiffRowGenerator diffGenerator = new DiffRowGenerator.Builder()
				.showInlineDiffs(false).ignoreWhiteSpaces(true)
				.columnWidth(100).build();

		List<DiffRow> diffRow = diffGenerator.generateDiffRows(original,
				revised, patch);

		for (DiffRow r : diffRow) {

			switch (r.getTag()) {
			case CHANGE:
				leftText.append("! " + r.getOldLine() + '\n');
				rightText.append("! " + r.getNewLine() + '\n');
				break;
			case DELETE:
				leftText.append("- " + r.getOldLine() + '\n');
				rightText.append("  " + "\n");
				break;
			case EQUAL:
				leftText.append("  " + r.getOldLine() + '\n');
				rightText.append("  " + r.getNewLine() + '\n');
				break;
			case INSERT:
				leftText.append("  " + "\n");
				rightText.append("+ " + r.getNewLine() + '\n');
				break;
			default:
				break;
			}

		}
		files[0] = leftText;
		files[1] = rightText;
		return files;
	}
}
