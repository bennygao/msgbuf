package cc.devfun.msgbuf;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

public class BinaryMessageBeanFormat implements MessageBeanFormat {
    // //////////////////////////////////////////////////////////////
    // 常量定义
    // //////////////////////////////////////////////////////////////
    public final static byte NULL = -1;
    public final static byte NOT_NULL = 0;
    public final static byte TRUE = -1;
    public final static byte FALSE = 0;

    private CommandFactory commandFactory;
    private DataInput dis;
    private DataOutput dos;

    public BinaryMessageBeanFormat(CommandFactory factory, DataInput dis) {
        this.commandFactory = factory;
        this.dis = dis;
    }

    public BinaryMessageBeanFormat(CommandFactory factory, DataOutput dos) {
        this.commandFactory = factory;
        this.dos = dos;
    }

    public DataInput getDataInput() {
        return dis;
    }

    public void setDataInput(DataInput dis) {
        this.dis = dis;
    }

    public DataOutput getDataOutput() {
        return dos;
    }

    public void setDataOutput(DataOutput dos) {
        this.dos = dos;
    }

    private boolean readIsNull() throws Exception {
        return dis.readByte() == NULL;
    }

    private boolean writeIsNull(Object obj) throws Exception {
        if (obj == null) {
            dos.writeByte(NULL);
            return true;
        } else {
            dos.writeByte(NOT_NULL);
            return false;
        }
    }

    private int readSetIndicator() throws Exception {
        if (readIsNull()) {
            return -1;
        } else {
            return readVarint().intValue();
        }
    }

    private int writeSetIndicator(Object obj) throws Exception {
        int len = 0;
        if (writeIsNull(obj)) {
            return -1;
        } else if (obj instanceof List) {
            len = ((List<?>) obj).size();
        } else if (obj instanceof Map) {
            len = ((Map<?, ?>) obj).size();
        } else {
            len = Array.getLength(obj);
        }

        writeVarint(len);
        return len;
    }

    private byte readByte() throws Exception {
        return dis.readByte();
    }

    private void writeByte(byte value) throws Exception {
        dos.writeByte(value);
    }

    private boolean readBoolean() throws Exception {
        return dis.readByte() == TRUE;
    }

    private void writeBoolean(boolean value) throws Exception {
        dos.writeByte(value ? TRUE : FALSE);
    }

    private short readShort() throws Exception {
        return readVarint().shortValue();
    }

    private void writeShort(short value) throws Exception {
        writeVarint(value);
    }

    private int readInteger() throws Exception {
        return readVarint().intValue();
    }

    private void writeInteger(int value) throws Exception {
        writeVarint(value);
    }

    private long readLong() throws Exception {
        return readVarint().longValue();
    }

    private void writeLong(long value) throws Exception {
        writeVarint(value);
    }

    private float readFloat() throws Exception {
        return dis.readFloat();
    }

    private void writeFloat(float value) throws Exception {
        dos.writeFloat(value);
    }

    private double readDouble() throws Exception {
        return dis.readDouble();
    }

    private void writeDouble(double value) throws Exception {
        dos.writeDouble(value);
    }

    private String readString() throws Exception {
        if (readIsNull()) {
            return null;
        } else {
            return dis.readUTF();
        }
    }

    private void writeString(String value) throws Exception {
        if (!writeIsNull(value)) {
            dos.writeUTF(value);
        }
    }

    private Long readVarint() throws IOException {
        int cnt = 0;
        long value = 0;
        byte b = 0;
        boolean neo = false;

        while (true) {
            b = dis.readByte();
            if (cnt == 0) {
                neo = (b & 0X40) != 0;
                value |= (b & 0X3F);
            } else {
                value |= (b & 0X7F);
            }

            if ((b & 0X80) != 0) {
                value <<= 7;
            } else {
                break;
            }

            ++cnt;
        }

        return neo ? -value : value;
    }

    private void writeVarint(long v) throws IOException {
        // 绝对值
        long absValue = Math.abs(v);
        // 绝对值中有值的位(bit)数
        int cnt = Long.SIZE - Long.numberOfLeadingZeros(absValue);
        // 计算实际需要占用的位数(bits)和字节数(len)
        int bits = 6;
        // 结果字节数
        int len = 1;
        // beginBit是结果字节流中，需要开始存储数据的起始bit位置。
        int beginBit = cnt % 7;
        while (bits < cnt) {
            bits += 7;
            beginBit += 8;
            ++len;
        }

        long sourceMask = 1L << (cnt - 1);
        byte b = 0;
        byte targetMask = (byte) 0X80;
        int bitIdx = len * 8;
        for (int i = len; i > 0; --i) {
            b = 0;
            targetMask = (byte) 0X80;
            for (int j = 0; j < Byte.SIZE; ++j) {
                if (j == 0) { // 字节最高位
                    if (i > 1) { // 不是最后一个字节
                        // 设置后续还有数据标志
                        b |= targetMask;
                    }
                } else if (j == 1 && i == len && v < 0) { // 第一个字节的次高位，如果值为负数
                    // 设置符号位（设置了为负数）
                    b |= targetMask;
                } else if (beginBit >= bitIdx) {
                    if ((sourceMask & absValue) != 0) {
                        b |= targetMask;
                    }

                    sourceMask >>= 1;
                }

                targetMask >>= 1;
                targetMask &= 0X7F;
                --bitIdx;
            }

            dos.writeByte(b);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T readBean(Class<T> clazz) throws Exception {
        if (readIsNull()) {
            return null;
        } else {
            Class<? extends MessageBean> beanClazz = (Class<? extends MessageBean>) clazz;
            MessageBean bean = commandFactory.borrowMessageBean(
                    beanClazz);
            return (T) bean.deserialize(this);
        }
    }

    private void writeBean(Object val, Class<?> clazz) throws Exception {
        if (!writeIsNull(val)) {
            MessageBean mbean = (MessageBean) val;
            mbean.serialize(this);
        }
    }

    @Override
    public <T> List<T> readVector(String fieldName, Class<T> clazz,
                                  List<T> vector) {
        try {
            int len = readSetIndicator();
            if (len < 0) {
                return null;
            } else {
                vector.clear();
                for (int i = 0; i < len; ++i) {
                    vector.add((T) read(null, clazz));
                }

                return vector;
            }
        } catch (Throwable t) {
            throw new MessageBufferException(t);
        }
    }

    @Override
    public void writeVector(String fieldName, List<?> vector, Class<?> clazz) {
        try {
            int size = writeSetIndicator(vector);
            if (size > 0) {
                for (Object val : vector) {
                    write(fieldName, val, clazz);
                }
            }
        } catch (Throwable t) {
            throw new MessageBufferException(t);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T read(String fieldName, Class<T> clazz) {
        try {
            if (clazz.equals(Byte.class) || clazz.equals(byte.class)) {
                return (T) (Byte) readByte();
            } else if (clazz.equals(Boolean.class)
                    || clazz.equals(boolean.class)) {
                return (T) (Boolean) readBoolean();
            } else if (clazz.equals(Short.class) || clazz.equals(short.class)) {
                return (T) (Short) readShort();
            } else if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
                return (T) (Integer) readInteger();
            } else if (clazz.equals(Long.class) || clazz.equals(long.class)) {
                return (T) (Long) readLong();
            } else if (clazz.equals(Float.class) || clazz.equals(float.class)) {
                return (T) (Float) readFloat();
            } else if (clazz.equals(Double.class) || clazz.equals(double.class)) {
                return (T) (Double) readDouble();
            } else if (clazz.equals(String.class)) {
                return (T) (String) readString();
            } else {
                Class<? extends MessageBean> beanClazz = (Class<? extends MessageBean>) clazz;
                return (T) readBean(beanClazz);
            }
        } catch (Throwable t) {
            throw new MessageBufferException(t);
        }
    }

    @Override
    public void write(String fieldName, Object obj, Class<?> clazz) {
        try {
            if (clazz.equals(Byte.class) || clazz.equals(byte.class)) {
                writeByte((Byte) obj);
            } else if (clazz.equals(Boolean.class)
                    || clazz.equals(boolean.class)) {
                writeBoolean((Boolean) obj);
            } else if (clazz.equals(Short.class) || clazz.equals(short.class)) {
                writeShort((Short) obj);
            } else if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
                writeInteger((Integer) obj);
            } else if (clazz.equals(Long.class) || clazz.equals(long.class)) {
                writeLong((Long) obj);
            } else if (clazz.equals(Float.class) || clazz.equals(float.class)) {
                writeFloat((Float) obj);
            } else if (clazz.equals(Double.class) || clazz.equals(double.class)) {
                writeDouble((Double) obj);
            } else if (clazz.equals(String.class)) {
                writeString((String) obj);
            } else {
                writeBean(obj, clazz);
            }
        } catch (Throwable t) {
            throw new MessageBufferException(t);
        }
    }

    @Override
    public <T> Object readArray(String fieldName, Class<T> clazz) {
        try {
            int len = readSetIndicator();
            if (len < 0) {
                return null;
            }

            Object array = Array.newInstance(clazz, len);
            T value;
            for (int i = 0; i < len; ++i) {
                value = read(null, clazz);
                Array.set(array, i, value);
            }

            return array;
        } catch (Throwable t) {
            throw new MessageBufferException(t);
        }
    }

    @Override
    public void writeArray(String fieldName, Object array, Class<?> clazz) {
        try {
            int len = writeSetIndicator(array);
            if (len > 0) {
                Object value;
                for (int i = 0; i < len; ++i) {
                    value = Array.get(array, i);
                    write(fieldName, value, clazz);
                }
            }
        } catch (Throwable t) {
            throw new MessageBufferException(t);
        }
    }

    @Override
    public <K, V> Map<K, V> readMap(String fieldName, Class<K> keyClazz,
                                    Class<V> valueClazz, Map<K, V> map) {
        try {
            int len = readSetIndicator();
            if (len <= 0) {
                return null;
            }

            map.clear();
            K key = null;
            V value = null;
            for (int i = 0; i < len; ++i) {
                key = (K) read(null, keyClazz);
                value = (V) read(null, valueClazz);
                map.put(key, value);
            }

            return map;
        } catch (Throwable t) {
            throw new MessageBufferException(t);
        }
    }

    @Override
    public void writeMap(String fieldName, Map<?, ?> map, Class<?> keyClazz,
                         Class<?> valueClazz) {
        try {
            int len = writeSetIndicator(map);
            if (len > 0) {
                Object key, value;
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    key = entry.getKey();
                    value = entry.getValue();

                    if (key == null) {
                        throw new MessageProcessException(
                                "Key of map entry cannot be null.");
                    }

                    if (value == null) {
                        throw new MessageProcessException(
                                "Value of map entry cannot be null.");
                    }

                    write(fieldName, key, keyClazz);
                    write(fieldName, value, valueClazz);
                }
            }
        } catch (Throwable t) {
            throw new MessageBufferException(t);
        }
    }

    @Override
    public void beginWriteBean(Class<?> beanClazz, String clazzName) {
    }

    @Override
    public void endWriteBean(Class<?> beanClazz, String clazzName) {
    }
}
