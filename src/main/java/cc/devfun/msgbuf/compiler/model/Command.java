package cc.devfun.msgbuf.compiler.model;

import java.util.List;

public class Command {
	private int id;
	private CommandArgument input;
	private CommandArgument output;
	private List<String> doc;

	public Command(int id, CommandArgument input, CommandArgument output,
			List<String> doc) {
		this.id = id;
		this.input = input;
		this.output = output;
		this.doc = doc;
	}

	@Override
	public String toString() {
		return new StringBuffer().append("command: ").append(id).append(' ')
				.append(input).append(' ').append(output).toString();
	}

	public int getId() {
		return id;
	}

	public CommandArgument getInput() {
		return input;
	}

	public CommandArgument getOutput() {
		return output;
	}
	
	public boolean hasDoc() {
		return doc == null ? false : doc.size() > 0;
	}

	public List<String> getDoc() {
		return doc;
	}
}
