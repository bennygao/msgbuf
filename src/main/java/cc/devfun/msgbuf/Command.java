package cc.devfun.msgbuf;

import java.io.InputStream;
import java.io.OutputStream;

import static java.lang.String.*;

public class Command {
	private int id;
	private CommandArgument input;
	private CommandArgument output;

	public Command(int id, CommandArgument input, CommandArgument output) {
		this.id = id;
		this.input = input;
		this.output = output;
	}

	public void serializeOutputArgument(OutputStream ostream) throws Exception {
		CommandArgument arg = output;
		if (arg.getClazz() == null) {
			throw new MessageBufferException(format(
					"Command %d output argument is void.", id));
		} else {
			arg.serialize(ostream);
		}
	}

	public MessageBean deserializeOutputArgument(InputStream istream)
			throws Exception {
		CommandArgument arg = output;
		if (arg.getClazz() == null) {
			throw new MessageBufferException(format(
					"Command %d output argument is void.", id));
		} else {
			return arg.deserialize(istream);
		}
	}

	public void serializeInputArgument(OutputStream ostream) throws Exception {
		CommandArgument arg = input;
		if (arg.getClazz() == null) {
			throw new MessageBufferException(format(
					"Command %d input argument is void.", id));
		} else {
			arg.serialize(ostream);
		}
	}

	public MessageBean deserializeInputArgument(InputStream istream)
			throws Exception {
		CommandArgument arg = input;
		if (arg.getClazz() == null) {
			throw new MessageBufferException(format(
					"Command %d input argument is void.", id));
		} else {
			return arg.deserialize(istream);
		}
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

	@Override
	public Object clone() throws CloneNotSupportedException {
		return new Command(id, (CommandArgument) input.clone(),
				(CommandArgument) output.clone());
	}

	@Override
	public String toString() {
		return format("%d %s %s", id, input.toString(),
				output.toString());
	}
}
