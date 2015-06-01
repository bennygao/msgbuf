/******************************************************************************

 ******************************************************************************
 * !!! Generated by MessageCompiler, don't modify manually.
 * Generated time: Sat Sep 22 16:21:07 CST 2012
 *****************************************************************************/
using System;
using System.Collections.Generic;
using System.IO;
using System.IO.Compression;

namespace MessageBuffer
{
	public interface IPooledObject
	{
		void Clear();
		
		void Release();
	}

	public abstract class MessageBean : ICloneable, IPooledObject, IDisposable
	{
		protected MessageBeanFactory objectFactory;
		public int commandId;

		public static MessageBean Instance {
			get { return null; }
		}

		public MessageBean() : this(0)
		{
		}

		public MessageBean(int cmdId)
		{
			commandId = cmdId;
		}

		public void setObjectFactory(MessageBeanFactory factory)
		{
			this.objectFactory = factory;
		}
		
		public virtual object Clone()
		{
			MessageBean newone = objectFactory.BorrowMessageBean(this.GetType());
			newone.Copy(this);
			return newone;
		}

		public void Dispose()
		{
			Release();
		}

		public virtual void Clear()
		{
			commandId = 0;
		}
		
		public virtual void Release()
		{
			objectFactory.ReturnMessageBean(this);
		}

		public virtual void Copy(MessageBean another)
		{
			commandId = another.commandId;

			MemoryStream ms = new MemoryStream();
			BinaryMessageBeanFormat format = new BinaryMessageBeanFormat(ms, objectFactory);
			another.Serialize(format);
			ms.Seek(0, SeekOrigin.Begin);
			this.Deserialize(format);
		}

		public virtual void Serialize(MessageBeanFormat format)
		{
		}
		
		public virtual void Serialize(Stream stream)
		{
		}

		public virtual MessageBean Deserialize(MessageBeanFormat format)
		{
			return this;
		}
		
		public virtual MessageBean Deserialize(Stream stream)
		{
			return this;
		}

		public override string ToString()
		{
			TextMessageBeanFormat format = new TextMessageBeanFormat();
			format.Write(null, this);
			return format.ToString();
		}
	}
	
	public abstract class MessageBeanFactory
	{
		private ObjectPool objectPool;
		private BinaryMessageBeanFormat recvFormat, sendFormat;
		private ReleaseMessageBeanFormat recycler;
		
		public ObjectPool ObjectPool {
			get { return objectPool; }
		}
		
		public MessageBeanFactory()
		{
			objectPool = new ObjectPool(this);
			recvFormat = new BinaryMessageBeanFormat(this);
			sendFormat = new BinaryMessageBeanFormat(this);
			recycler = new ReleaseMessageBeanFormat(this);
		}
		
		public MessageBean BorrowMessageBean(Type type)
		{
			return (MessageBean)objectPool.Borrow(type);
		}
		
		public void ReturnMessageBean(MessageBean mbean)
		{
			if (mbean == null) {
				return;
			}
			
			mbean.Serialize(recycler);
			objectPool.Return(mbean);
		}
		
		public void SerializeMessageBean(Stream stream, MessageBean mbean)
		{
        	/* 根据指令号获得该指令是否需要压缩 */
			bool needZip = NeedZip(mbean.commandId);
			/* 写入一个字节的是否压缩标志 */
			BinaryMessageBeanFormat.PutBool(stream, needZip);
			if (needZip) { /* 压缩流 */
				/* 第三个参数必须指定为true，即Close GzipStream时，保留其引用的流不被关闭。*/
				GZipStream gzip = new GZipStream(stream, CompressionMode.Compress, true);
				sendFormat.Stream = gzip;
				sendFormat.Write(null, mbean);
				/* 必须调用GZipStream.Close()以结束Gzip压缩 */
				gzip.Close();
			} else {
				sendFormat.Stream = stream;
				sendFormat.Write(null, mbean);
			}
//			/* 客户端不支持发送压缩报文 */
//			BinaryMessageBeanFormat.PutBool(stream, false);
//			sendFormat.Stream = stream;
//			sendFormat.Write(null, mbean);
		}
        
		public MessageBean DeserializeMessageBean(Stream stream, int cmdId)
		{
			Type beanType = GetMessageBeanType(cmdId);
			MessageBean mbean = DeserializeMessageBean(stream, beanType);
			if (mbean != null) {
				mbean.commandId = cmdId;
			}
			
			return mbean;
		}
		
		public MessageBean DeserializeMessageBean(Stream stream, Type beanType)
		{
			/* 读出一个字节的压缩与否标志 */
			bool isZipped = BinaryMessageBeanFormat.GetBool(stream);
			Stream input = isZipped ? new GZipStream(stream, CompressionMode.Decompress) : stream;
			recvFormat.Stream = input;
			return (MessageBean)recvFormat.Read(null, beanType);
		}
		
		public abstract Type GetMessageBeanType(int cmdId);

		public abstract bool NeedZip(int cmdId);

		public abstract MessageBean CreateMessageBean(Type type);
	}
}
