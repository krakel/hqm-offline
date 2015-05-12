package de.doerl.hqm.utils;

import java.security.AccessController;
import java.security.PrivilegedAction;

public final class Security {
	static final ThreadGroup SYSTEM_THREAD_GROUP = AccessController.doPrivileged( new PrivilegedAction<ThreadGroup>() {
		public ThreadGroup run() {
			ThreadGroup group = Thread.currentThread().getThreadGroup();
			for (ThreadGroup p = group.getParent(); p != null; p = group.getParent()) {
				group = p;
			}
			return group;
		}
	});

	private Security() {
	}

	public static boolean getBoolean( final String property) {
		return AccessController.doPrivileged( new PrivilegedAction<Boolean>() {
			public Boolean run() {
				return Boolean.valueOf( Boolean.getBoolean( property));
			}
		}).booleanValue();
	}

	public static int getInteger( final String property, final int def) {
		return AccessController.doPrivileged( new PrivilegedAction<Integer>() {
			public Integer run() {
				return Integer.getInteger( property, def);
			}
		}).intValue();
	}

	public static long getLong( final String property, final long def) {
		return AccessController.doPrivileged( new PrivilegedAction<Long>() {
			public Long run() {
				return Long.getLong( property, def);
			}
		}).longValue();
	}

	public static String getProperty( final String property, final String def) {
		return AccessController.doPrivileged( new PrivilegedAction<String>() {
			public String run() {
				return System.getProperty( property, def);
			}
		});
	}

	public static Thread getThread( final Runnable target, final String name, final boolean daemon) {
		return getThread( SYSTEM_THREAD_GROUP, target, name, daemon);
	}

	public static Thread getThread( final ThreadGroup group, final Runnable target, final String name, final boolean daemon) {
		return AccessController.doPrivileged( new PrivilegedAction<Thread>() {
			public Thread run() {
				Thread t = new Thread( group, target, name);
				t.setContextClassLoader( ClassLoader.getSystemClassLoader());
				t.setDaemon( daemon);
				return t;
			}
		});
	}
}
