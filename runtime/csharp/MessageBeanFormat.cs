using System;
using System.Collections.Generic;
using System.IO;
using System.Text;

namespace MessageBuffer {
	public interface MessageBeanFormat {
		void BeginWriteBean(Type beanType, string typeName);
		
		void EndWriteBean(Type beanType, string typeName);
		
		void Write<T>(string fieldName, T value);
		
		void WriteVector<T>(string fieldName, IList<T> vector);
		
		void WriteArray<T>(string fieldName, T[] array);
		
		void WriteMap<K, V>(string fieldName, IDictionary<K, V> map);
		
		object Read(string fieldName, Type type);
		
		IList<T> ReadVector<T>(string fieldName, IList<T> vector);
		
		T[] ReadArray<T>(string fieldName);
		
		IDictionary<K, V> ReadMap<K, V>(string fieldName, IDictionary<K, V> map);
	}

	public abstract class WriteOnlyMessageBeanFormat : MessageBeanFormat {
		protected Boolean IsBasicType(Type t) {
			switch (Type.GetTypeCode(t)) {
			case TypeCode.Byte:
			case TypeCode.Boolean:
			case TypeCode.Int16:
			case TypeCode.UInt16:
			case TypeCode.Int32:
			case TypeCode.UInt32:
			case TypeCode.Int64:
			case TypeCode.UInt64:
			case TypeCode.Single:
			case TypeCode.Double:
			case TypeCode.String:
				return true;
					
			default:
				return false;
			}
		}

		public abstract void BeginWriteBean(Type beanType, string typeName);
		
		public abstract void EndWriteBean(Type beanType, string typeName);
		
		public abstract void Write<T>(string fieldName, T value);
		
		public abstract void WriteVector<T>(string fieldName, IList<T> vector);
		
		public abstract void WriteArray<T>(string fieldName, T[] array);
		
		public abstract void WriteMap<K, V>(string fieldName, IDictionary<K, V> map);

		public object Read(string fieldName, Type type) {
			throw new NotImplementedException();
		}
		
		public IList<T> ReadVector<T>(string fieldName, IList<T> vector) {
			throw new NotImplementedException();
		}
		
		public T[] ReadArray<T>(string fieldName) {
			throw new NotImplementedException();
		}
		
		public IDictionary<K, V> ReadMap<K, V>(string fieldName, IDictionary<K, V> map) {
			throw new NotImplementedException();
		}
	}
	
	public class TextMessageBeanFormat : WriteOnlyMessageBeanFormat {
		private int indentLevel = 0;
		private StringWriter sw;
		
		public TextMessageBeanFormat() {
			sw = new StringWriter();
		}
		
		public TextMessageBeanFormat(StringWriter sw) {
			this.sw = sw;
		}
		
		public override string ToString() {
			return sw.ToString();
		}
		
		private void Indent() {
			for (int i = 0; i < indentLevel; ++i) {
				sw.Write("    ");
			}
		}
		
		public override void BeginWriteBean(Type beanType, string typeName) {
			if (!beanType.Name.Equals(typeName)) { /* 是基类输出 */
				Indent();
				sw.Write("base : ");
			}
			
			sw.WriteLine("message {0} {{", typeName);
			++indentLevel;
		}
		
		public override void EndWriteBean(Type beanType, string typeName) {
			--indentLevel;
			Indent();
			sw.WriteLine('}');
		}
		
		private void WriteBean(object obj) {
			if (obj == null) {
				sw.WriteLine("null");
			} else {
				MessageBean mbean = (MessageBean)obj;
				mbean.Serialize(this);
			}
		}
		
		public override void Write<T>(string fieldName, T value) {
			if (fieldName != null) {
				Indent();
				sw.Write("{0} {1} = ", typeof(T).Name, fieldName);
			}
			
			if (value == null) {
				sw.WriteLine("null");
				return;
			}
			
			if (IsBasicType(typeof(T))) {
				sw.WriteLine(value);
			} else {
				WriteBean(value);
			}
		}
		
		public override void WriteVector<T>(string fieldName, IList<T> vector) {
			Indent();
			sw.Write("list<{0}> {1} = ", typeof(T).Name, fieldName);
			if (vector == null) {
				sw.WriteLine("null");
				return;
			} else {
				sw.WriteLine('{');
				
				++indentLevel;
				for (int i = 0; i < vector.Count; ++i) {
					Indent();
					sw.Write("<{0}> = ", i);
					Write<T>(null, vector[i]);
				}
				--indentLevel;
			}
			
			Indent();
			sw.WriteLine('}');
		}
		
		public override void WriteArray<T>(string fieldName, T[] array) {
			Indent();
			sw.Write("{0}[] {1} = ", typeof(T).Name, fieldName);
			if (array == null) {
				sw.WriteLine("null");
				return;
			} else {
				sw.WriteLine('{');
				
				++indentLevel;
				for (int i = 0; i < array.Length; ++i) {
					Indent();
					sw.Write("[{0}] = ", i);
					Write(null, array[i]);
				}
				--indentLevel;
			}
			
			Indent();
			sw.WriteLine('}');
		}
		
		public override void WriteMap<K, V>(string fieldName, IDictionary<K, V> map) {
			Indent();
			sw.Write("map<{0}, {1}> {2} = ", typeof(K).Name,
			         typeof(V).Name, fieldName);
			if (map == null) {
				sw.WriteLine("null");
				return;
			} else {
				sw.WriteLine('{');
				
				int index = 0;
				++indentLevel;
				foreach (K key in map.Keys) {
					V value = map[key];
					
					Indent();
					sw.Write("map<{0}>.key = ", index);
					Write<K>(null, key);
					
					Indent();
					sw.Write("map<{0}>.value = ", index++);
					Write<V>(null, value);
					
					sw.WriteLine();
				}
				--indentLevel;
				
				Indent();
				sw.WriteLine('}');
			}
		}
	}
	
	public class BinaryMessageBeanFormat : MessageBeanFormat {
		const byte MB_NULL = 0XFF;
		const byte MB_NOT_NULL = 0;
		const byte MB_TRUE = 0XFF;
		const byte MB_FALSE = 0;
		const int SIZE_INT16 = 2;
		const int SIZE_INT32 = 4;
		const int SIZE_INT64 = 8;
		const int SIZE_FLOAT = 4;
		const int SIZE_DOUBLE = 8;
		private MessageBeanFactory factory;
		private Stream stream;
		
		public Stream Stream {
			get { return stream; }
			set { stream = value; }
		}
		
		public BinaryMessageBeanFormat(MessageBeanFactory factory) : this(null, factory) {
		}
		
		public BinaryMessageBeanFormat(Stream stream, MessageBeanFactory factory) {
			this.stream = stream;
			this.factory = factory;
		}
		
		public static void ReverseBytesOrder(byte[] array) {
			ReverseBytesOrder(array, 0, array.Length);
		}
		
		public static void ReverseBytesOrder(byte[] array, int offset, int len) {
			int half = len / 2;
			byte v;
			for (int i = 0, j = len - 1; i < half; ++i, --j) {
				v = array[i + offset];
				array[i + offset] = array[j + offset];
				array[j + offset] = v;
			}
		}
		
		public static byte GetByte(Stream stream) {
			return (byte)stream.ReadByte();
		}
		
		public static void PutByte(Stream stream, byte b) {
			stream.WriteByte(b);
		}
		
		public static byte[] GetBytes(Stream stream, int len, bool conv) {
			byte[] bytes = new byte[len];
			stream.Read(bytes, 0, len);
			if (conv && BitConverter.IsLittleEndian) {
				ReverseBytesOrder(bytes);
			}
			
			return bytes;
		}
		
		public static byte[] GetBytes(Stream stream, int len) {
			return GetBytes(stream, len, true);
		}
		
		public static void PutBytes(Stream stream, byte[] bytes, bool conv) {
			if (conv && BitConverter.IsLittleEndian) {
				ReverseBytesOrder(bytes);
			}
			
			stream.Write(bytes, 0, bytes.Length);
		}
		
		public static void PutBytes(Stream stream, byte[] bytes) {
			PutBytes(stream, bytes, true);
		}
		
		public static bool GetBool(Stream stream) {
			return GetByte(stream) == MB_TRUE;
		}
		
		public static void PutBool(Stream stream, bool b) {
			PutByte(stream, b ? MB_TRUE : MB_FALSE);
		}
		
		public static bool GetNull(Stream stream) {
			return GetByte(stream) == MB_NULL;
		}
		
		public static void PutNull(Stream stream, bool n) {
			PutByte(stream, n ? MB_NULL : MB_NOT_NULL);
		}
		
		public static Int16 GetInt16(Stream stream) {
			byte[] bytes = GetBytes(stream, sizeof(Int16));
			return BitConverter.ToInt16(bytes, 0);
		}
		
		public static void PutInt16(Stream stream, Int16 value) {
			byte[] bytes = BitConverter.GetBytes(value);
			PutBytes(stream, bytes);
		}
		
		public static Int32 GetInt32(byte[] buffer, int offset) {
			if (BitConverter.IsLittleEndian) {
				ReverseBytesOrder(buffer, offset, SIZE_INT32);
			}
			
			return BitConverter.ToInt32(buffer, offset);
		}
		
		public static Int32 GetInt32(Stream stream) {
			byte[] bytes = GetBytes(stream, sizeof(Int32));
			return BitConverter.ToInt32(bytes, 0);
		}
		
		public static void PutInt32(Stream stream, Int32 value) {
			byte[] bytes = BitConverter.GetBytes(value);
			PutBytes(stream, bytes);
		}
		
		public static Int64 GetInt64(Stream stream) {
			byte[] bytes = GetBytes(stream, sizeof(Int64));
			return BitConverter.ToInt64(bytes, 0);
		}
		
		public static void PutInt64(Stream stream, Int64 value) {
			byte[] bytes = BitConverter.GetBytes(value);
			PutBytes(stream, bytes);
		}
		
		public static string ReadUTF(Stream stream) {
			Int16 len = GetInt16(stream);
			byte[] bytes = GetBytes(stream, len, false);
			return Encoding.UTF8.GetString(bytes, 0, len);
		}
		
		public static void WriteUTF(Stream stream, string utf) {
			byte[] bytes = Encoding.UTF8.GetBytes(utf);
			PutInt16(stream, (Int16)bytes.Length);
			PutBytes(stream, bytes, false);
		}
		
		public static Int64 ReadVarint(Stream stream) {
			int cnt = 0;
			long value = 0;
			byte b = 0;
			bool neo = false;
			long tmp = 0;
			
			while (true) {
				b = GetByte(stream);
				if (cnt == 0) {
					neo = (b & 0X40) != 0;
					tmp = (long)(b & 0X3F);
					value |= tmp;
				} else {
					tmp = (long)(b & 0X7F);
					value |= tmp;
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
		
		private static int NumberOfLeadingZeros(UInt64 v) {
			int totalBits = sizeof(Int64) * 8;
			UInt64 mask = (UInt64)1L << (totalBits - 1);
			int num = 0;
			for (num = 0; num < totalBits; ++num) {
				if ((mask & v) != 0) {
					return num;
				} else {
					mask >>= 1;
				}
			}
			
			return num;
		}
		
		public static void WriteVarint(Stream stream, Int64 v) {
			/* 绝对值 */
			UInt64 absValue = (UInt64)Math.Abs(v);
			int totalBits = sizeof(Int64) * 8;
			/* 绝对值中有值的位(bit)数 */
			int cnt = totalBits - NumberOfLeadingZeros(absValue);
			/* 计算实际需要占用的位数(bits)和字节数(len) */
			int bits = 6;
			/* 结果字节数 */
			int len = 1;
			/* beginBit是结果字节流中，需要开始存储数据的起始bit位置。 */
			int beginBit = cnt % 7;
			while (bits < cnt) {
				bits += 7;
				beginBit += 8;
				++len;
			}
			
			UInt64 sourceMask = (UInt64)1L << (cnt - 1);
			byte b = 0;
			byte targetMask = (byte)0X80;
			int bitIdx = len * 8;
			for (int i = len; i > 0; --i) {
				b = 0;
				targetMask = (byte)0X80;
				for (int j = 0; j < 8; ++j) {
					if (j == 0) { /* 字节最高位 */
						if (i > 1) { /* 不是最后一个字节 */
							/* 设置后续还有数据标志 */
							b |= targetMask;
						}
					} else if (j == 1 && i == len && v < 0) { /* 第一个字节的次高位，如果值为负数 */
						/* 设置符号位（设置了为负数） */
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
				
				PutByte(stream, b);
			}
		}
		
		private bool ReadIsNull() {
			return GetByte(stream) == MB_NULL;
		}
		
		private bool WriteIsNull(object obj) {
			bool isNull = obj == null;
			PutByte(stream, isNull ? MB_TRUE : MB_FALSE);
			return isNull;
		}
		
		private int ReadArrayIndicator() {
			return ReadIsNull() ? -1 : (int)ReadVarint(stream);
		}
		
		private int WriteArrayIndicator(Array array) {
			if (WriteIsNull(array)) {
				return -1;
			} else {
				int len = array.Length;
				WriteVarint(stream, len);
				return len;
			}
		}
		
		private int WriteMapIndicator<K, V>(IDictionary<K, V> map) {
			if (WriteIsNull(map)) {
				return -1;
			} else {
				int len = map.Count;
				WriteVarint(stream, len);
				return len;
			}
		}
		
		private int WriteVectorIndicator<T>(IList<T> list) {
			if (WriteIsNull(list)) {
				return -1;
			} else {
				int len = list.Count;
				WriteVarint(stream, len);
				return len;
			}
		}
		
		public void BeginWriteBean(Type beanType, string typeName) {
		}
		
		public void EndWriteBean(Type beanType, string typeName) {
		}
		
		public object Read(string fieldName, Type type) {
			byte[] bytes;
			
			switch (Type.GetTypeCode(type)) {
			case TypeCode.Byte:
				return GetByte(stream);
					
			case TypeCode.Boolean:
				return GetByte(stream) == MB_TRUE;
					
			case TypeCode.Int16:
			case TypeCode.UInt16:
				return (Int16)ReadVarint(stream);
					
			case TypeCode.Int32:
			case TypeCode.UInt32:
				return (Int32)ReadVarint(stream);
					
			case TypeCode.Int64:
			case TypeCode.UInt64:
				return (Int64)ReadVarint(stream);
					
			case TypeCode.Single:
				bytes = GetBytes(stream, SIZE_FLOAT);
				return BitConverter.ToSingle(bytes, 0);
					
			case TypeCode.Double:
				bytes = GetBytes(stream, SIZE_DOUBLE);
				return BitConverter.ToDouble(bytes, 0);
					
			case TypeCode.String:
				return ReadIsNull() ? null : ReadUTF(stream);
					
			default:
				MessageBean mbean = null;
				if (!ReadIsNull()) {
					mbean = factory.BorrowMessageBean(type);
					mbean.Deserialize(this);
				}
				return mbean;
			}
		}
		
		public void Write<T>(string fieldName, T value) {
			byte[] bytes;
			
			switch (Type.GetTypeCode(typeof(T))) {
			case TypeCode.Byte:
				PutByte(stream, (byte)(object)value);
				break;
					
			case TypeCode.Boolean:
				bool flag = (bool)(object)value;
				PutByte(stream, flag ? MB_TRUE : MB_FALSE);
				break;
					
			case TypeCode.Int16:
			case TypeCode.UInt16:
				WriteVarint(stream, (Int16)(object)value);
				break;
					
			case TypeCode.Int32:
			case TypeCode.UInt32:
				WriteVarint(stream, (Int32)(object)value);
				break;
					
			case TypeCode.Int64:
			case TypeCode.UInt64:
				WriteVarint(stream, (Int64)(object)value);
				break;
					
			case TypeCode.Single:
				bytes = BitConverter.GetBytes((float)(object)value);
				PutBytes(stream, bytes);
				break;
					
			case TypeCode.Double:
				bytes = BitConverter.GetBytes((double)(object)value);
				PutBytes(stream, bytes);
				break;
					
			case TypeCode.String:
				if (!WriteIsNull(value)) {
					WriteUTF(stream, (string)(object)value);
				}
				break;
					
			default:
				if (!WriteIsNull(value)) {
					MessageBean mbean = (MessageBean)(object)value;
					mbean.Serialize(this);
				}
				break;
			}
		}
		
		public IList<T> ReadVector<T>(string fieldName, IList<T> vector) {
			int len = ReadArrayIndicator();
			if (len >= 0) {
				vector.Clear();
				for (int i = 0; i < len; ++i) {
					vector.Add((T)Read(fieldName, typeof(T)));
				}
				
				return vector;
			} else {
				return null;
			}
		}
		
		public void WriteVector<T>(string fieldName, IList<T> vector) {
			int len = WriteVectorIndicator<T>(vector);
			if (len > 0) {
				for (int i = 0; i < len; ++i) {
					Write<T>(fieldName, vector[i]);
				}
			}
		}
		
		public T[] ReadArray<T>(string fieldName) {
			int len = ReadArrayIndicator();
			T[] array = null;
			if (len > 0) {
				array = new T[len];
				for (int i = 0; i < len; ++i) {
					array[i] = (T)Read(fieldName, typeof(T));
				}
			}
			
			return array;
		}
		
		public void WriteArray<T>(string fieldName, T[] array) {
			int len = WriteArrayIndicator(array);
			if (len > 0) {
				for (int i = 0; i < len; ++i) {
					Write<T>(fieldName, array[i]);
				}
			}
		}
		
		public IDictionary<K, V> ReadMap<K, V>(string fieldName, IDictionary<K, V> map) {
			int len = ReadArrayIndicator();
			if (len >= 0) {
				map.Clear();
				K key;
				V value;
				for (int i = 0; i < len; ++i) {
					key = (K)Read(fieldName, typeof(K));
					value = (V)Read(fieldName, typeof(V));
					map[key] = value;
				}
				return map;
			} else {
				return null;
			}
		}
		
		public void WriteMap<K, V>(string fieldName, IDictionary<K, V> map) {
			int len = WriteMapIndicator(map);
			if (len > 0) {
				foreach (K key in map.Keys) {
					V value = map[key];
					Write<K>(fieldName, key);
					Write<V>(fieldName, value);
				}
			}
		}
	}
	
	public class ReleaseMessageBeanFormat : WriteOnlyMessageBeanFormat {
		private MessageBeanFactory factory;
		
		public ReleaseMessageBeanFormat(MessageBeanFactory factory) {
			this.factory = factory;
		}
		
		public override void BeginWriteBean(Type beanType, string typeName) {
		}
		
		public override void EndWriteBean(Type beanType, string typeName) {
		}
		
		public override void Write<T>(string fieldName, T value) {
			if (IsBasicType(typeof(T)) || value == null) {
				return;
			} else {
				MessageBean mbean = (MessageBean)(object)value;
				factory.ReturnMessageBean(mbean);
			}
		}
		
		public override void WriteVector<T>(string fieldName, IList<T> vector) {
			if (IsBasicType(typeof(T)) || vector == null) {
				return;
			} else {
				MessageBean mbean;
				foreach (T e in vector) {
					if (e != null) {
						mbean = (MessageBean)(object)e;
						factory.ReturnMessageBean(mbean);
					}
				}
			}
		}
		
		public override void WriteArray<T>(string fieldName, T[] array) {
			if (IsBasicType(typeof(T)) || array == null) {
				return;
			} else {
				MessageBean mbean;
				foreach (T e in array) {
					if (e != null) {
						mbean = (MessageBean)(object)e;
						factory.ReturnMessageBean(mbean);
					}
				}
			}
		}
		
		public override void WriteMap<K, V>(string fieldName, IDictionary<K, V> map) {
			if ((map == null) || (IsBasicType(typeof(K)) && IsBasicType(typeof(V)))) {
				return;
			} else {
				MessageBean mbean;
				V value;
				foreach (K key in map.Keys) {
					value = map[key];
					if (!IsBasicType(typeof(K)) && key != null) {
						mbean = (MessageBean)(object)key;
						factory.ReturnMessageBean(mbean);
					}
					
					if (!IsBasicType(typeof(V)) && value != null) {
						mbean = (MessageBean)(object)value;
						factory.ReturnMessageBean(mbean);
					}
				}
			}
		}
	}
}
