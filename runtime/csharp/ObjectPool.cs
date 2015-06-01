using System;
using System.IO;
using System.Collections;
using System.Collections.Generic;
using System.Diagnostics;

namespace MessageBuffer {
	public class ActivePooledObjectTrace {
		private long borrowedTicks;
		private StackTrace stackTrace;
		
		public long BorrowedTicks {
			get { return borrowedTicks; }
		}
		
		public StackTrace StackTrace {
			get { return StackTrace; }
		}

		public void Touch() {
			Touch(true);
		}

		public void Touch(bool detail) {
			borrowedTicks = DateTime.Now.Ticks;
			if (detail) {
				stackTrace = new StackTrace(true);
			}
		}
	}
	
	class Pool : IComparable {
		private MessageBeanFactory factory;
		private Type type;
		private IDictionary<IPooledObject, ActivePooledObjectTrace> idleQ;
		private IDictionary<IPooledObject, ActivePooledObjectTrace> activeQ;
		
		public Pool(Type type, MessageBeanFactory factory) {
			this.type = type;
			this.factory = factory;
			
			idleQ = new Dictionary<IPooledObject, ActivePooledObjectTrace>();
			activeQ = new Dictionary<IPooledObject, ActivePooledObjectTrace>();
		}
		
		public int CompareTo(object obj) {
			Pool another = (Pool)obj;
			int gap = another.ActiveNum - ActiveNum;
			return gap == 0 ? another.IdleNum - IdleNum : gap;
		}
		
		public IPooledObject Borrow() {
			IPooledObject obj = null;
			ActivePooledObjectTrace trace = null;

			if (IdleNum > 0) {
				foreach (IPooledObject key in idleQ.Keys) {
					obj = key;
					break;
				}
					
				trace = idleQ[obj];
				idleQ.Remove(obj);
			} else {
				obj = factory.CreateMessageBean(type);
				trace = new ActivePooledObjectTrace();
			}
				
			trace.Touch();
			activeQ.Add(obj, trace);

			return obj;
		}
		
		public void Return(IPooledObject obj) {
			if (!activeQ.ContainsKey(obj)) {
				return;
			}
			
			ActivePooledObjectTrace trace = activeQ[obj];
			obj.Clear();
			activeQ.Remove(obj);
			idleQ.Add(obj, trace);
		}
		
		public int IdleNum {
			get { return idleQ.Count; }
		}
		
		public int ActiveNum {
			get { return activeQ.Count; }
		}
		
		public override string ToString() {
			StringWriter sw = new StringWriter();
			sw.Write("{0,25} active={1,4} idle={2,4}", type.Name, ActiveNum, IdleNum);
			return sw.ToString();
		}
	}
	
	public class ObjectPool {
		private IDictionary<Type, Pool> keyedPool;
		private MessageBeanFactory factory;
		
		public ObjectPool(MessageBeanFactory factory) {
			this.factory = factory;
			keyedPool = new Dictionary<Type, Pool>();
		}
		
		public IPooledObject Borrow(Type type) {
			lock (this) {
				Pool pool = null;
				if (keyedPool.ContainsKey(type)) {
					pool = keyedPool[type];
				} else {
					pool = new Pool(type, factory);
					keyedPool.Add(type, pool);
				}
				
				return pool.Borrow();
			}
		}
		
		public void Return(IPooledObject obj) {
			if (obj == null) {
				return;
			}
			
			lock (this) {
				Pool pool = keyedPool[obj.GetType()];
				pool.Return(obj);
			}
		}

		public override String ToString() {
			StringWriter sw = new StringWriter();
			List<Pool> list = new List<Pool>();
			list.AddRange(keyedPool.Values);
			list.Sort();
			foreach (Pool pool in list) {
				sw.WriteLine(pool.ToString());
			}
			return sw.ToString();
		}
	}
}