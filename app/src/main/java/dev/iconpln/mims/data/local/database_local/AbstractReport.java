package dev.iconpln.mims.data.local.database_local;

import java.io.Serializable;

public interface AbstractReport extends Serializable {
	boolean sendReport();

	String getReturn();

	String getDescription();
}
