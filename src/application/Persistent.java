package application;

import java.io.Serializable;

public interface Persistent<KT> extends Serializable {
	public KT getId();

	public void setId(KT id);
}
