package edu.uwm.cs.lexical_search.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.uwm.cs.lexical_search.util.CommandPrompt;
import edu.uwm.cs.lexical_search.util.FileUtils;
import edu.uwm.cs.lexical_search.util.Pair;

@SuppressWarnings("unused")
public class Query extends Observable {

	private URL repository;
	private QueryResult results;
	private File localRepo;
	private File searchDatabase;

	private CommandPrompt cmd;
	private File lexDataPath = new File(System.getenv("APPDATA")
			+ File.separator + "LexicalSearch");
	private String revPath;
	private long searchTime;

	/**
	 * Constructor for the query class without a repository URL
	 */
	public Query() {
		this.cmd = new CommandPrompt();
		this.results = new QueryResult();
	}

	/**
	 * Constructor for the query class
	 * 
	 * @param repository
	 */
	public Query(URL repository) {
		this.repository = repository;
		this.cmd = new CommandPrompt();
		this.results = new QueryResult();

		try {
			if (!lexDataPath.exists()) {
				lexDataPath.mkdir();
				System.out.println("Creating Lexical Search folder: "
						+ lexDataPath);
			}

			String repo = URLEncoder.encode(repository.toString(), "UTF-8");
			searchDatabase = new File(lexDataPath.getAbsolutePath()
					+ File.separator + "searchdatabase" + File.separator + repo);

			if (!searchDatabase.exists()) {
				searchDatabase.mkdirs();
				System.out.println("Creating database folder: "
						+ searchDatabase);
			}

			localRepo = new File(lexDataPath.getAbsolutePath() + File.separator
					+ repo);

			if (!localRepo.exists()) {
				localRepo.mkdir();
				System.out.println("Creating repository folder: " + localRepo);
			}
		} catch (IOException e) {
			setChanged();
			notifyObservers(MsgType.ERROR.getTag() + "IOException"
					+ e.getMessage());
		}
	}

	/**
	 * Returns the result after executing a query in the lexical database
	 * 
	 * @param rev1
	 * @param rev2
	 * @param searchString
	 * @throws IOException
	 */
	public void executeQuery(int rev1, int rev2, String searchString)
			throws IOException {
		long timeStart = new Date().getTime();

		if (rev1 < 1 || rev2 < 1) {
			setChanged();
			notifyObservers(MsgType.ERROR.getTag()
					+ "Revisions cannot be negative");
			return;
		}

		if (rev2 < rev1) {
			setChanged();
			notifyObservers(MsgType.ERROR.getTag()
					+ "The first revision must be smaller than the second revision");
			return;
		}
		if (searchString == null || searchString.trim().length() == 0) {
			setChanged();
			notifyObservers(MsgType.ERROR.getTag()
					+ "The query can not be empty");
			return;
		}

		// TODO: fails if svn command not available as well as the Semantic
		// Design tools.
		// There needs to be error checking
		String svnLogCmd = "svn log -r " + rev1 + ":" + rev2 + " " + repository
				+ " --verbose";

		setChanged();
		notifyObservers(MsgType.SVN_COMMAND.getTag() + svnLogCmd);

		String response = null;
		try {

			response = cmd.commandLine(svnLogCmd);

			setChanged();
			notifyObservers(MsgType.SVN_OUTPUT.getTag() + response);

		} catch (InterruptedException e) {
			setChanged();
			notifyObservers(MsgType.ERROR.getTag()
					+ "Running the SVN command  " + svnLogCmd);
		}

		Pattern p = Pattern.compile("\\nr(\\d+)[\\s\\S]+?\\n-"),
		// Regular expression for file names
		p2 = Pattern.compile("\\x20{3}[AMR]\\x20([^\\n]+?.java)\\n"),
		// Regular expression for Date and time Pattern
		p3 = Pattern
				.compile("(\\d{4}-\\d{2}-\\d{2}) (\\d{2}[:]\\d{2}[:]\\d{2})");

		Matcher m = p.matcher(response);
		char slash = File.separatorChar;

		while (m.find()) { // For each revision
			String rev = m.group(1);
			Matcher m2 = p2.matcher(m.group(0));
			Matcher m3 = p3.matcher(m.group(0));

			setChanged();
			notifyObservers(Integer.parseInt(rev));

			revPath = localRepo.getAbsolutePath() + slash + rev;
			String lexProjContent = "Java~Java1_6 LexemeExtractor 1.0\n"
					+ "\n<" + revPath + "\n>" + revPath;

			String dbProjContent = "SearchEngine 1.0";

			String nodeDate = "";
			String nodeTime = "";

			m3.find();
			nodeDate = m3.group(1);
			nodeTime = m3.group(2);

			dbProjContent += "\n>" + searchDatabase + File.separator + rev;

			File localPath = null;
			while (m2.find()) { // For each file
				String remotePath = m2.group(1).substring(1);
				URL remoteFile = new URL(repository.toString() + remotePath);
				localPath = new File(revPath + slash
						+ remotePath.substring(0, remotePath.lastIndexOf('/')));

				setChanged();
				notifyObservers(MsgType.INFO.getTag()
						+ "Creating local folder in revision " + rev + ":\n"
						+ localPath);

				if (!localPath.exists()) {
					localPath.mkdirs();
				} else {
					setChanged();
					notifyObservers(MsgType.INFO.getTag() + "Local folder "
							+ localPath + " already exists");
				}

				File localFile = new File(localPath.toString() + slash
						+ remotePath.substring(remotePath.lastIndexOf('/') + 1));

				setChanged();
				notifyObservers(MsgType.INFO.getTag() + "Exporting file "
						+ localFile.getName() + " for revision " + rev);

				if (!localFile.exists()) {
					localFile.createNewFile();
					lexProjContent += "\n" + remotePath;

					String command = "svn export --force " + remoteFile + "@"
							+ rev + " " + localFile;

					setChanged();
					notifyObservers(MsgType.SVN_COMMAND.getTag() + command);

					String exportResponse;

					try {
						exportResponse = cmd.commandLine(command);

						setChanged();
						notifyObservers(MsgType.SVN_OUTPUT.getTag()
								+ exportResponse);
					} catch (InterruptedException e) {
						setChanged();
						notifyObservers(MsgType.ERROR.getTag()
								+ "Running the SVN command " + command);
					}
				} else {
					setChanged();
					notifyObservers(MsgType.INFO.getTag() + "File "
							+ localFile.getName() + " already exists");
				}
			}

			// Do the search if there is a local folder for the current revision
			if (localPath != null) {
				File lexemeFile = createLexemes(localPath, lexProjContent, rev);
				dbProjContent += "\n" + lexemeFile;
				File db = createDatabase(dbProjContent, rev);
				String result = executeSearch(db.getAbsolutePath(), rev,
						searchString);
				parseData(rev, nodeDate, nodeTime, result);
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				setChanged();
				notifyObservers(MsgType.INFO.getTag() + "Revision " + rev
						+ " does not have any Java files");
			}
		}

		this.searchTime = new Date().getTime() - timeStart;

		setChanged();
		notifyObservers(MsgType.INFO.getTag() + "Search completed in: "
				+ searchTime + "ms");

		setChanged();
		notifyObservers(results);

	}

	/**
	 * Method used to parse the data returned from the SVN commands
	 * 
	 * @param revision
	 * @param nodeTime
	 * @param nodeDate
	 * @param result
	 */
	private void parseData(String revision, String nodeDate, String nodeTime,
			String result) {
		Pattern p = Pattern
				.compile("\\nLine: (\\d+) File: ([^\\n]+)[\\r\\n]+([^\\n]+)");
		Matcher m = p.matcher(result);
		Hashtable<File, ArrayList<Integer>> fl = new Hashtable<File, ArrayList<Integer>>();

		/*
		 * Line: {39} File: C:\Users\Alex\AppData\Roaming\Temp\Disassembler.java
		 */
		while (m.find()) { // for each match
			int line = Integer.parseInt(m.group(1));
			File f = new File(m.group(2));
			// String code = m.group(3);

			if (!fl.containsKey(f)) {
				fl.put(f, new ArrayList<Integer>());
				fl.get(f).add(line);
			} else {
				fl.get(f).add(line);
			}
		}

		ArrayList<Pair<File, ArrayList<Integer>>> files = new ArrayList<Pair<File, ArrayList<Integer>>>();

		QueryNode queryTrunkNode = new QueryNode("REV " + revision,
				Integer.parseInt(revision), nodeDate, nodeTime,
				new ArrayList<Pair<File, ArrayList<Integer>>>(), SvnType.TRUNK);

		QueryNode queryTagNode = new QueryNode("REV " + revision,
				Integer.parseInt(revision), nodeDate, nodeTime,
				new ArrayList<Pair<File, ArrayList<Integer>>>(), SvnType.TAG);

		QueryNode queryBranchNode = new QueryNode("REV " + revision,
				Integer.parseInt(revision), nodeDate, nodeTime,
				new ArrayList<Pair<File, ArrayList<Integer>>>(), SvnType.BRANCH);

		for (File f : fl.keySet()) {

			if (f.getAbsolutePath().indexOf("\\trunk\\") > -1) {
				queryTrunkNode.addFile(new Pair<File, ArrayList<Integer>>(f, fl
						.get(f)));
			} else if (f.getAbsolutePath().indexOf("\\branches\\") > -1) {
				queryBranchNode.addFile(new Pair<File, ArrayList<Integer>>(f,
						fl.get(f)));
			} else if (f.getAbsolutePath().indexOf("\\tags\\") > -1) {
				queryTagNode.addFile(new Pair<File, ArrayList<Integer>>(f, fl
						.get(f)));
			} else {
				// if there is an exception delete the folders
				setChanged();
				notifyObservers(MsgType.ERROR.getTag()
						+ "Unexpected search results.");
				return;
			}
		}

		if (queryTrunkNode.getFiles().size() > 0)
			results.addTrunkNode(queryTrunkNode);
		if (queryTagNode.getFiles().size() > 0)
			results.addTagNode(queryTagNode);
		if (queryBranchNode.getFiles().size() > 0)
			results.addBranchNode(queryBranchNode);
	}

	private String executeSearch(String db, String rev, String searchString) {
		setChanged();
		notifyObservers(MsgType.INFO.getTag() + "Executing query "
				+ searchString + " in revision " + rev);

		String command = "DMSSearchEngine " + db + " -query:" + searchString;

		setChanged();
		notifyObservers(MsgType.LEXI_SEARCH_CMD.getTag() + command);

		String response = "";

		try {
			response = cmd.commandLine(command);

			setChanged();
			notifyObservers(MsgType.LEXI_SEARCH_CMD_OUTPUT.getTag() + response);
		} catch (InterruptedException e) {
			setChanged();
			notifyObservers(MsgType.ERROR.getTag()
					+ "Running the DMSSearchEngine command tool");
		} catch (IOException e) {
			setChanged();
			notifyObservers(MsgType.ERROR.getTag()
					+ "Running the DMSSearchEngine command tool");
		}

		return response;
	}

	private File createLexemes(File path, String lexProjContent, String rev) {
		File p = null;
		try {
			p = new File(path.toString() + File.separatorChar + "lexProj.lxe");

			setChanged();
			notifyObservers(MsgType.INFO.getTag()
					+ "Creating lexeme project file for revision " + rev
					+ ":\n" + p);

			if (!p.exists()) {

				setChanged();
				notifyObservers(MsgType.INFO.getTag()
						+ "Creating lexeme files for revision " + rev);

				FileWriter lexProj = new FileWriter(p);
				BufferedWriter out = new BufferedWriter(lexProj);
				out.write(lexProjContent);
				out.close();

				String command = "DMSLexemeExtractor Java~Java1_6 @" + p;

				setChanged();
				notifyObservers(MsgType.LEXI_SEARCH_CMD.getTag() + command);

				String response;

				try {
					response = cmd.commandLine(command);

					setChanged();
					notifyObservers(MsgType.LEXI_SEARCH_CMD_OUTPUT.getTag()
							+ response);
				} catch (InterruptedException e) {
					setChanged();
					notifyObservers(MsgType.ERROR.getTag()
							+ "Running the DMSLexemeExtractor command tool");
				}
			} else {
				setChanged();
				notifyObservers(MsgType.INFO.getTag() + "Lexeme Project File: "
						+ p.getName() + " already exists");
			}
		} catch (IOException e) {
			setChanged();
			notifyObservers(MsgType.ERROR.getTag() + "IOException "
					+ e.getMessage());

		}
		return p;
	}

	private File createDatabase(String dbProjContent, String rev) {
		File p = null;
		File dbFolder = new File(searchDatabase.getAbsolutePath()
				+ File.separator + rev);

		try {
			p = new File(dbFolder.getAbsolutePath() + File.separator + "db.prj");

			if (!dbFolder.exists()) {
				dbFolder.mkdir();
			} else {
				return p;
			}

			setChanged();
			notifyObservers(MsgType.INFO.getTag()
					+ "Creating database project file for revision " + rev
					+ ":\n" + p);

			if (!p.exists()) {
				FileWriter dbProj = new FileWriter(p);
				BufferedWriter out = new BufferedWriter(dbProj);
				out.write(dbProjContent);
				out.close();

				setChanged();
				notifyObservers(MsgType.INFO.getTag()
						+ "Creating database files for revision " + rev);

				String command = "DMSSearchEngineIndex " + p;

				setChanged();
				notifyObservers(MsgType.LEXI_SEARCH_CMD.getTag() + command);

				String response = "";

				try {
					response = cmd.commandLine(command);
				} catch (InterruptedException e) {
					setChanged();
					notifyObservers(MsgType.ERROR.getTag()
							+ "InterruptedException "
							+ "creating the database project file");
				}

				setChanged();
				notifyObservers(MsgType.LEXI_SEARCH_CMD_OUTPUT.getTag()
						+ response);
			} else {
				setChanged();
				notifyObservers(MsgType.INFO.getTag()
						+ "Database Project File: " + p.getName()
						+ " already exists");
			}
		} catch (IOException e) {
			setChanged();
			notifyObservers(MsgType.ERROR.getTag() + "IOException "
					+ "creating the database project file");
		}
		return p;
	}

	public QueryResult getSearchResults() {
		return results;
	}

	public URL getRepository() {
		return repository;
	}

	public CommandPrompt getCmd() {
		return cmd;
	}

	/**
	 * Temp method to add fake data to the model
	 * 
	 * @param query
	 */
	public void createFakeData() {
		File file_1 = new File("./TestFiles/AddSubjectDialog.java");
		File file_2 = new File("./TestFiles/EasierGridLayout.java");
		File file_3 = new File("./TestFiles/EditGradeDialog.java");
		File file_4 = new File("./TestFiles/EditStudentDialog.java");
		File file_5 = new File("./TestFiles/EnrollDialog.java");
		File file_6 = new File("./TestFiles/GradesDialog.java");
		File file_7 = new File("./TestFiles/RegistryDialog.java");

		ArrayList<Integer> lineNum = new ArrayList<Integer>(Arrays.asList(10,
				20, 30, 40));

		ArrayList<Pair<File, ArrayList<Integer>>> tagRes = new ArrayList<Pair<File, ArrayList<Integer>>>();
		tagRes.add(new Pair<File, ArrayList<Integer>>(file_1, lineNum));
		tagRes.add(new Pair<File, ArrayList<Integer>>(file_2, lineNum));
		tagRes.add(new Pair<File, ArrayList<Integer>>(file_3, lineNum));
		tagRes.add(new Pair<File, ArrayList<Integer>>(file_4, lineNum));

		ArrayList<Pair<File, ArrayList<Integer>>> trunkRes = new ArrayList<Pair<File, ArrayList<Integer>>>();
		trunkRes.add(new Pair<File, ArrayList<Integer>>(file_2, lineNum));
		trunkRes.add(new Pair<File, ArrayList<Integer>>(file_3, lineNum));
		trunkRes.add(new Pair<File, ArrayList<Integer>>(file_7, lineNum));

		ArrayList<Pair<File, ArrayList<Integer>>> branchRes = new ArrayList<Pair<File, ArrayList<Integer>>>();
		branchRes.add(new Pair<File, ArrayList<Integer>>(file_5, lineNum));
		branchRes.add(new Pair<File, ArrayList<Integer>>(file_6, lineNum));
		branchRes.add(new Pair<File, ArrayList<Integer>>(file_7, lineNum));

		ArrayList<QueryNode> tagNode = new ArrayList<QueryNode>();
		ArrayList<QueryNode> trunkNode = new ArrayList<QueryNode>();
		ArrayList<QueryNode> branchNode = new ArrayList<QueryNode>();
		Random rand = new Random();

		for (int i = 1; i <= 20; ++i) {
			if (rand.nextInt(2) == 1)
				tagNode.add(new QueryNode("REV " + i, i, "2012-XX-XX",
						"12:12:59", tagRes, SvnType.TAG));
			if (rand.nextInt(2) == 1)
				trunkNode.add(new QueryNode("REV " + i, i, "2012-XX-XX",
						"12:12:59", trunkRes, SvnType.TRUNK));
			if (rand.nextInt(2) == 1)
				branchNode.add(new QueryNode("REV " + i, i, "2012-XX-XX",
						"12:12:59", branchRes, SvnType.BRANCH));
		}
		results = new QueryResult(tagNode, trunkNode, branchNode);

		setChanged();
		notifyObservers(results);
	}
}
