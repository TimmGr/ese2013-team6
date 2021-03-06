package com.ese2013.mub.util;

import java.util.List;

import com.ese2013.mub.model.Mensa;

public abstract class AbstractMensaFactory {
	public abstract List<Mensa> createMensaList() throws MensaDownloadException, MensaLoadException;
}