package problem.client;

import java.util.Enumeration;
import java.util.Iterator;

public class IteratorToEnumerationAdapter<E> implements Enumeration<E> {
	private Iterator<E> itr;
	
	public IteratorToEnumerationAdapter(Iterator<E> itr) {
		this.itr = itr;
	}

	@Override
	public boolean hasMoreElements() {
		return itr.hasNext();
	}

	@Override
	public E nextElement() {
		return itr.next();
	}
}
