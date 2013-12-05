package com.jtaylor.util.datastructures.linqy;

/**
 * Created by IntelliJ IDEA.
 * User: jacob.taylor
 * Date: 11/29/13
 * Time: 3:26 PM
 * To change this template use File | Settings | File Templates.
 */
public interface LinqySelector <S,F>
{
	public S select(F f);
}
