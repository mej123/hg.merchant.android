package com.kuku.kkmerchant;

import top.ftas.dunit.annotation.DUnitGroup;
import top.ftas.dunit.group.DUnitRootGroup;

/**
 * Created by tik on 17/8/30.
 */

public class RootGroup {
	@DUnitGroup("其它模块")
	public static class OtherGroup extends  DUnitRootGroup{}

	@DUnitGroup("常用模块")
	public static class CommonUseGroup extends DUnitRootGroup{}

	@DUnitGroup("Hybrid模块")
	public static class HybridGroup extends DUnitRootGroup{}
}
