package org.sse.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class ObjectTransCoder {
	class ContextObjectInputStream extends ObjectInputStream {
		ClassLoader mLoader;

		ContextObjectInputStream(InputStream in, ClassLoader loader)
				throws IOException, SecurityException {
			super(in);
			mLoader = loader;
		}

		@Override
		protected Class<?> resolveClass(ObjectStreamClass v)
				throws IOException, ClassNotFoundException {
			if (mLoader == null)
				return super.resolveClass(v);
			else
				return Class.forName(v.getName(), true, mLoader);
		}
	}

	public Object decode(final InputStream input) throws IOException {
		Object obj = null;
		ObjectInputStream ois = new ObjectInputStream(input);
		try {
			obj = ois.readObject();
		} catch (ClassNotFoundException e) {
			throw new IOException(e.getMessage());
		}
		ois.close();
		return obj;
	}

	public void encode(final OutputStream output, final Object object)
			throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(output);
		oos.writeObject(object);
		oos.close();
	}

	public Object decode(InputStream input, ClassLoader classLoader)
			throws IOException {
		Object obj = null;
		ContextObjectInputStream ois = new ContextObjectInputStream(input,
				classLoader);
		try {
			obj = ois.readObject();
		} catch (ClassNotFoundException e) {
			throw new IOException(e.getMessage());
		}
		ois.close();
		return obj;
	}

}
