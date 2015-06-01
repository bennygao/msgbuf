package cc.devfun.msgbuf;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class WriteOnlyMessageBeanFormat implements MessageBeanFormat {
	private static Set<Class<?>> basicTypes = new HashSet<Class<?>>();
	static {
		basicTypes.add(Byte.class);
		basicTypes.add(byte.class);
		basicTypes.add(Boolean.class);
		basicTypes.add(boolean.class);
		basicTypes.add(Short.class);
		basicTypes.add(short.class);
		basicTypes.add(Integer.class);
		basicTypes.add(int.class);
		basicTypes.add(Long.class);
		basicTypes.add(long.class);
		basicTypes.add(Float.class);
		basicTypes.add(float.class);
		basicTypes.add(Double.class);
		basicTypes.add(double.class);
		basicTypes.add(String.class);
	}

	protected boolean isBasicType(Class<?> clazz) {
		return basicTypes.contains(clazz);
	}

	@Override
	public <T> T read(String fieldName, Class<T> clazz) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> List<T> readVector(String fieldName, Class<T> clazz,
			List<T> vector) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <K, V> Map<K, V> readMap(String fieldName, Class<K> keyClazz,
			Class<V> valueClazz, Map<K, V> map) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> Object readArray(String fieldName, Class<T> clazz) {
		throw new UnsupportedOperationException();
	}
}
