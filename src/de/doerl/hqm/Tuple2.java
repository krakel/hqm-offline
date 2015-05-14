package de.doerl.hqm;

import de.doerl.hqm.utils.Utils;

public class Tuple2<T1, T2> {
	public final T1 _1;
	public final T2 _2;

	public Tuple2( T1 t1, T2 t2) {
		_1 = t1;
		_2 = t2;
	}

	public static <S1, S2> Tuple2<S1, S2> apply( S1 s1, S2 s2) {
		return new Tuple2<S1, S2>( s1, s2);
	}

	@Override
	public boolean equals( Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		try {
			@SuppressWarnings( "unchecked")
			Tuple2<T1, T2> other = (Tuple2<T1, T2>) obj;
			if (Utils.different( _1, other._1)) {
				return false;
			}
			if (Utils.different( _2, other._2)) {
				return false;
			}
		}
		catch (ClassCastException ex) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int res = 0;
		if (_1 != null) {
			res = _1.hashCode();
		}
		if (_2 != null) {
			res <<= 8;
			res ^= _2.hashCode();
		}
		return res;
	}
}
