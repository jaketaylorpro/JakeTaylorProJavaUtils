package com.jtaylor.util.gui.rwindow.progress;

import java.util.Arrays;

abstract public class EnumProgressRunner extends StringListProgressRunner
{
	public EnumProgressRunner(Class enumeration)
	{
		super(Arrays.asList(enumeration.getEnumConstants()));
	}
	public void setValue(Enum enumVal)
	{
		super.setValue(enumVal.toString());
	}
}
