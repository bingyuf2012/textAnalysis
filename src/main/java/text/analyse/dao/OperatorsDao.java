package text.analyse.dao;

import java.util.List;

import text.analyse.model.Operators;

public interface OperatorsDao {
	/**
	 * @功能：根据用户名和密码检索操作员信息
	 * @param operators
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public abstract List searchOpeNumPass(Operators operators);

	/**
	 * @功能：统计检索结果总共多少条
	 * @param resultlist
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public abstract int countResult(List resultlist);

	/**
	 * @功能：检索所有操作员的信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public abstract List searchOpeAll();

	/**
	 * @功能：添加新的操作员
	 * @param operators
	 */
	public abstract void addOperator(Operators operators);

	/**
	 * @功能：修改某个操作员的信息
	 * @param operators
	 * @return
	 */
	public abstract int modifyOpenumber(Operators operators);

	/**
	 * @功能：删除某个操作员的信息
	 * @param operators
	 * @return
	 */
	public abstract int deleteOpenumber(Operators operators);

	public abstract String getMaxnumber();

	@SuppressWarnings("unchecked")
	public abstract List getMaxnumbers();
}
