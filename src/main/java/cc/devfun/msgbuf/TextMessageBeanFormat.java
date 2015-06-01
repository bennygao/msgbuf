package cc.devfun.msgbuf;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

public class TextMessageBeanFormat extends WriteOnlyMessageBeanFormat {
	private int level = 0;
	private ByteArrayOutputStream baos;
	private PrintStream ps;

	public TextMessageBeanFormat() {
		baos = new ByteArrayOutputStream();
		ps = new PrintStream(baos);
	}
	
	public TextMessageBeanFormat(OutputStream stream) {
		baos = null;
		ps = new PrintStream(stream);
	}

	@Override
	public String toString() {
		return baos == null ? "" : baos.toString();
	}

	private void indent() {
		for (int i = 0; i < level; ++i) {
			ps.print("    ");
		}
	}
	
	@Override
	public void beginWriteBean(Class<?> beanClazz, String clazzName)
			 {
		if (!beanClazz.getSimpleName().equals(clazzName)) { /* 是基类输出 */
            indent();
            ps.printf("base : ");
		}
        
        ps.printf("message %s {", clazzName);
        ps.println();
        ++level;
	}

	@Override
	public void endWriteBean(Class<?> beanClazz, String clazzName)
			 {
    	--level;
		indent();
		ps.println('}');
	}

	@Override
	public void write(String fieldName, Object obj, Class<?> clazz)
			 {
		if (fieldName != null) {
			indent();
			ps.printf("%s %s = ", clazz.getSimpleName(), fieldName);
		}

		if (obj == null) {
			ps.println("null");
			return;
		}

		if (isBasicType(clazz)) {
			if (clazz == String.class) {
				ps.printf("\"%s\"", obj.toString());
				ps.println();
			} else {
				ps.println(obj);
			}
		} else {
			writeBean(obj);
		}
	}

	private void writeBean(Object obj)  {
		if (obj == null) {
			ps.println("null");
		} else {
			MessageBean mbean = (MessageBean) obj;
			mbean.serialize(this);
		}
	}

	@Override
	public void writeVector(String fieldName, List<?> vector, Class<?> clazz)
			 {
		indent();
		ps.printf("list<%s> %s = ", clazz.getSimpleName(), fieldName);
		if (vector == null) {
			ps.println("null");
			return;
		} else {
			ps.println("{");

			int index = 0;
			++level;
			for (Object e : vector) {
				indent();
				ps.printf("<%d> = ", index++);
				write(null, e, clazz);
			}
			--level;
		}

		indent();
		ps.println("}");
	}

	@Override
	public void writeArray(String fieldName, Object array, Class<?> clazz)
			 {
		indent();
		ps.printf("%s[] %s = ", clazz.getSimpleName(), fieldName);
		if (array == null) {
			ps.println("null");
			return;
		} else {
			ps.println("{");

			int len = Array.getLength(array);
			++level;
			for (int index = 0; index < len; ++index) {
				Object e = Array.get(array, index);
				indent();
				ps.printf("[%d] = ", index);
				write(null, e, clazz);
			}
			--level;
		}

		indent();
		ps.println("}");
	}

	@Override
	public void writeMap(String fieldName, Map<?, ?> map, Class<?> keyClazz,
			Class<?> valueClazz)  {
		indent();
		ps.printf("map<%s, %s> %s = ", keyClazz.getSimpleName(),
				valueClazz.getSimpleName(), fieldName);
		if (map == null) {
			ps.println("null");
			return;
		} else {
			ps.println("{");

			int index = 0;
			++level;
			for (Map.Entry<?, ?> e : map.entrySet()) {
				indent();
				ps.printf("entry<%d>.key = ", index);
				write(null, e.getKey(), keyClazz);
				
				indent();
				ps.printf("entry<%d>.value = ", index++);
				write(null, e.getValue(), valueClazz);
				
				ps.println();
			}
			--level;

			indent();
			ps.println("}");
		}
	}
}
