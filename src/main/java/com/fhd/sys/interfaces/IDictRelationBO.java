
package com.fhd.sys.interfaces;

import org.springframework.stereotype.Service;
import com.fhd.sys.entity.dic.DictEntryRelation;
import com.fhd.sys.entity.dic.DictEntryRelationType;

@Service
public interface IDictRelationBO {	
	public abstract void saveDictEntryRelation(DictEntryRelation dictEntryRelation);
	
	public abstract void removeDictEntryRelation(String id);
	
	public abstract DictEntryRelationType findDictEntryRelationTypeByTypeId(String typeId);
}
