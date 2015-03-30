package com.fhd.test.interfaces;

import java.util.Collection;

import com.fhd.test.entity.TestMvc;

public interface ITestMvcBO {

	public abstract void save(TestMvc testMvc);

	public abstract void saveBatch(Collection<TestMvc> testMvcs);

	public abstract void update(TestMvc testMvc);

	public abstract void updateBatch(Collection<TestMvc> testMvcs);

	public abstract void delete(TestMvc testMvc);

	public abstract void deleteById(String id);

	public abstract void deleteBatch(Collection<TestMvc> testMvcs);

}
