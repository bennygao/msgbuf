package cc.devfun.msgbuf;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

public class ReleaseMessageBeanFormat extends WriteOnlyMessageBeanFormat {
	private MessageBeanPool pool;

	public ReleaseMessageBeanFormat(MessageBeanPool pool) {
		this.pool = pool;
	}

	@Override
	public void beginWriteBean(Class<?> beanClazz, String clazzName) {

	}

	@Override
	public void endWriteBean(Class<?> beanClazz, String clazzName) {
	}

	@Override
	public void write(String fieldName, Object obj, Class<?> clazz) {
		if (isBasicType(clazz) || obj == null) {
			return;
		} else {
			MessageBean mbean = (MessageBean) obj;
			pool.returnMessageBean(mbean);
		}
	}

	@Override
	public void writeVector(String fieldName, List<?> vector, Class<?> clazz) {
		if (isBasicType(clazz) || vector == null) {
			return;
		} else {
			MessageBean mbean;
			for (Object obj : vector) {
				if (obj != null) {
					mbean = (MessageBean) obj;
					pool.returnMessageBean(mbean);
				}
			}
		}
	}

	@Override
	public void writeArray(String fieldName, Object array, Class<?> clazz) {
		if (isBasicType(clazz) || array == null) {
			return;
		} else {
			int len = Array.getLength(array);
			Object e;
			MessageBean mbean;
			for (int index = 0; index < len; ++index) {
				e = Array.get(array, index);
				if (e != null) {
					mbean = (MessageBean) e;
					pool.returnMessageBean(mbean);
				}
			}
		}
	}

	@Override
	public void writeMap(String fieldName, Map<?, ?> map, Class<?> keyClazz,
			Class<?> valueClazz) {
		if ((map == null) || (isBasicType(keyClazz) && isBasicType(valueClazz))) {
			return;
		} else {
			MessageBean mbean;
			for (Map.Entry<?, ?> e : map.entrySet()) {
				if (!isBasicType(keyClazz) && e.getKey() != null) {
					mbean = (MessageBean) e.getKey();
					pool.returnMessageBean(mbean);
				}

				if (!isBasicType(valueClazz) && e.getValue() != null) {
					mbean = (MessageBean) e.getValue();
					pool.returnMessageBean(mbean);
				}
			}
		}
	}
}