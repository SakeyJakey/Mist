package dev.sakey.mist.modules.settings;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class Setting {
    @Getter
    protected String name;

    private List<Parent<? extends Setting>> parents = new ArrayList<Parent<? extends Setting>>();

    public boolean hasParent() {
        return !parents.isEmpty();
    }

    public List<Parent<? extends Setting>> getParents() {
        return parents;
    }

    public void setParents(List<Parent<? extends Setting>> parents) {
        this.parents = parents;
    }

    public void addParent(Parent<? extends Setting> parent) {
        parents.add(parent);
    }

    public <T extends Setting> void addParent(T parent, Predicate<T> condition) {
        addParent(new Parent<>(parent, condition));
    }

    public static <T extends Setting> void addParent(T parent, Predicate<T> condition, Setting... settings) {
        Arrays.asList(settings).forEach(s -> s.addParent(new Parent<>(parent, condition)));
    }

    public boolean isHidden() {
        if (!hasParent()) return false;
        return getParents().stream().noneMatch(Parent::isShown);
    }

}
