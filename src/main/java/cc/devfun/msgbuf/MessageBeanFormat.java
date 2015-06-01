package cc.devfun.msgbuf;

import java.util.List;
import java.util.Map;

public interface MessageBeanFormat {
	public void beginWriteBean(Class<?> beanClazz, String clazzName);
	public void endWriteBean(Class<?> beanClazz, String clazzName);
	public void write(String fieldName, Object obj, Class<?> clazz);
	public void writeVector(String fieldName, List<?> vector, Class<?> clazz);
	public void writeArray(String fieldName, Object array, Class<?> clazz);
	public void writeMap(String fieldName, Map<?, ?> map, Class<?> keyClazz,
			Class<?> valueClazz);

	public <T> T read(String fieldName, Class<T> clazz);
	public <T> List<T> readVector(String fieldName, Class<T> clazz, List<T> container);
	public <T> Object readArray(String fieldName, Class<T> clazz);
	public <K, V> Map<K, V> readMap(String fieldName, Class<K> keyClazz,
			Class<V> valueClazz, Map<K, V> container);
}
