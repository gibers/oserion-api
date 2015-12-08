package com.oserion.framework.api.business;

import com.oserion.framework.api.business.beans.ContentElement;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IDBConnection {

	Object getDatabase();
	
}
