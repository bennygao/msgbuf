package test.pool;

import java.io.*;

public class Varint {

	public static void main(String[] args) throws Exception {
		// test(Long.MIN_VALUE);
		// test(65);
		testAll();
		// testPipe();
		// double v = 4294967296L / 4700766L;
		// System.out.printf("v=%f", v);
	}

	public static void test(long v) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		dos.writeLong(v >= 0 ? v : -v);
		byte[] bytes = baos.toByteArray();
		System.out.printf("source:%d %d", v, v > 0 ? v : -v);
		for (int i = 0; i < bytes.length; ++i) {
			System.out.printf("[%d]%s ", i, toBinaryString(bytes[i]));
		}
		System.out.println();
		baos.reset();

		writeVarint(v, dos);
		dos.close();
		bytes = baos.toByteArray();
		for (byte b : bytes) {
			System.out.print(toBinaryString(b));
		}
		System.out.println();

		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(
				bytes));
		long r = readVarint(dis);
		System.out.println("source:" + v + " result:" + r);
	}

	public static void testAll() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		long beginMillis = System.currentTimeMillis();
		long r = 0L;
		long count = 0;
		for (long i = Long.MIN_VALUE + 1; i <= Long.MAX_VALUE; ++i, ++count) {			
			writeVarint(i, new DataOutputStream(baos));
			byte[] bytes = baos.toByteArray();
			r = readVarint(new DataInputStream(new ByteArrayInputStream(bytes)));
			if (r != i) {
				System.err.println("Error:" + i + ":" + r);
				break;
			}

			if (count % 1000000 == 0) {
				System.out.println(i);
			}
			baos.reset();
		}
		
		System.out.println("count=" + count + " time=" + (System.currentTimeMillis() - beginMillis));

	}

	public static String toBinaryString(byte b) {
		byte mask = (byte) 0X01;
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < Byte.SIZE; ++i) {
			str.append((b & mask) != 0 ? '1' : '0');
			if (i == 3) {
				str.append(' ');
			}
			mask <<= 1;
		}

		return str.reverse().append('|').toString();
	}

	public static long readVarint(DataInput dis) throws IOException {
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

	public static void writeVarint(long v, DataOutput dos) throws IOException {
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

	public static void testPipe() throws IOException {
		PipedOutputStream pos = new PipedOutputStream();
		PipedInputStream pis = new PipedInputStream();
		pos.connect(pis);
		DataOutputStream dos = new DataOutputStream(pos);
		DataInputStream dis = new DataInputStream(pis);

		dos.writeInt(123);
		int v = dis.readInt();
		System.out.println("v=" + v);
		dos.writeDouble(123.45);
		dos.close();
		double d = dis.readDouble();
		System.out.println("d=" + d);
	}
}
