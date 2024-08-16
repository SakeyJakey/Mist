package dev.sakey.mist.modules.settings;

import dev.sakey.mist.modules.settings.impl.BoolSetting;

import java.util.function.Predicate;

public class Parent<T extends Setting> {
	public final static Predicate<BoolSetting> BOOLEAN_CONDITION = BoolSetting::isEnabled;

	private final T parent;
	private final Predicate<T> condition;

	public Parent(T parent, Predicate<T> condition) {
		this.parent = parent;
		this.condition = condition;
	}

	public boolean isShown() {
		return condition.test(parent) && parent.getParents().stream().allMatch(Parent::isShown);
	}

	public T getParent() {
		return parent;
	}
}
