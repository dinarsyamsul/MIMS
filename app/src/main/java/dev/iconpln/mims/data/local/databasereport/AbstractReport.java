package dev.iconpln.mims.data.local.databasereport;

import java.io.Serializable;

public interface AbstractReport extends Serializable {
	boolean sendReport();

	String getReturn();

	String getDescription();
}
