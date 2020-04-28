package edu.uwm.cs.lexical_search.model;

public enum MsgType {
	INFO {

		@Override
		public String getTag() {
			return "\nINFO:\n";
		}
	},
	ERROR {

		@Override
		public String getTag() {
			return "\nERROR:\n";
		}
	},
	WARNING {

		@Override
		public String getTag() {
			return "\nWARNING:\n";
		}

	},
	COMMAND_LINE {

		@Override
		public String getTag() {
			return "\nCOMMAND LINE:\n";
		}

	},
	SVN_COMMAND {

		@Override
		public String getTag() {
			return "\nSVN COMMAND:\n";
		}

	},
	SVN_OUTPUT {

		@Override
		public String getTag() {
			return "\nSVN COMMAND OUTPUT:\n";
		}

	},
	COMMAND_LINE_OUTPUT {

		@Override
		public String getTag() {
			return "\nCOMMAND LINE OUTPUT:\n";
		}
	},
	LEXI_SEARCH_CMD {

		@Override
		public String getTag() {
			return "\nLEXICAL SEARCH COMMAND:\n";
		}

	},
	LEXI_SEARCH_CMD_OUTPUT {

		@Override
		public String getTag() {
			return "\nLEXICAL SEARCH COMMAND OUTPUT:\n";
		}
	};

	public abstract String getTag();

}