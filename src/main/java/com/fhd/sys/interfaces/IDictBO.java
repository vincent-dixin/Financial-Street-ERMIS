/**
 * IDictBO.java
 * com.fhd.sys.interfaces
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-9-18 		张 雷
 *
 * Copyright (c) 2012, Firsthuida All Rights Reserved.
*/

package com.fhd.sys.interfaces;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.fhd.comm.entity.TimePeriod;
import com.fhd.sys.entity.dic.DictEntry;
import com.fhd.sys.entity.dic.DictEntryI18;
import com.fhd.sys.entity.dic.DictType;
import com.fhd.sys.entity.st.Temp;

/**
 * ClassName:IDictBO
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   张 雷
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-9-18		下午3:47:27
 *
 * @see 	 
 */
@Service
public interface IDictBO {	
	public abstract void saveDictEntry(DictEntry dictEntry);
	public abstract void removeDictEntry(String ids);
	public abstract void removeDictEntry(DictEntry dictEntry, Set<DictEntry> list);
	public abstract void removeDictEntryI18(String ids);
	public abstract void mergeDictEntry(DictEntry dictEntry);
	public abstract void mergeDictEntryI18Batch(String jsonString, String entryId);
	public abstract DictType findDictTypeByTypeId(String dictTypeId);
	public abstract DictEntry findDictEntryById(String dictEntryId);
	public abstract List<DictEntryI18> findDictEntryI18ByDictEntryId(String dictEntryId);
	public abstract List<DictEntryI18> findDictEntryI18(String entryId, String sort);
	public abstract List<DictEntry> findDictEntryByDictTypeId(String dictTypeId);	
}
