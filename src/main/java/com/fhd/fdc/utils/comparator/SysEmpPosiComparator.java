package com.fhd.fdc.utils.comparator;

import java.util.Comparator;

import com.fhd.sys.entity.orgstructure.SysEmpPosi;

public class SysEmpPosiComparator implements Comparator<SysEmpPosi> {

	public int compare(SysEmpPosi o1, SysEmpPosi o2) {
		// TODO Auto-generated method stub
		return o1.getId().compareTo(o2.getId());
	}

}
