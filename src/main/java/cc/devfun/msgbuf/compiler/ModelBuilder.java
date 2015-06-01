package cc.devfun.msgbuf.compiler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cc.devfun.msgbuf.compiler.model.Command;
import cc.devfun.msgbuf.compiler.model.CommandArgument;
import cc.devfun.msgbuf.compiler.parser.Location;
import cc.devfun.msgbuf.compiler.parser.mdl.Parser;

public class ModelBuilder {
	public final static String NULL_MESSAGE_BEAN_NAME = "NullMessageBean";
	private final static List<String> NULL_MESSAGE_BEAN_DOC = new ArrayList<String>();
	static {
		NULL_MESSAGE_BEAN_DOC.add("null消息的MessageBean定义");
	}

	private MessageDefinition msgdef;
	private Set<String> parsedMdls;

	public ModelBuilder() {
		msgdef = new MessageDefinition();
		parsedMdls = new HashSet<String>();

		// 创建一个缺省的NullMessageBean
		msgdef.newMessage(NULL_MESSAGE_BEAN_NAME, NULL_MESSAGE_BEAN_DOC, null,
				null);
	}

	public MessageDefinition getMessageDefinition() {
		return msgdef;
	}

	public void addCommand(int commandId, Object input, Object output,
			List<String> doc) {
		msgdef.addCommand(new Command(commandId, (CommandArgument) input,
				(CommandArgument) output, doc));
	}

	public void newMessage(String name, List<String> doc, String parent,
			Location location) {
		msgdef.newMessage(name, doc, parent, location);
	}

	public void addField(Object obj, String name, List<String> doc) {
		msgdef.addField(obj, name, doc);
	}

	public void importMdl(String pathname) {
		if (!parsedMdls.contains(pathname)) { // 判断是否重复import，如果重复就不再parse了。
			Parser.parseMdl(pathname, this);
			parsedMdls.add(pathname);
		}
	}
}
