package com.syn.visualmod;

import com.syn.visualmod.modules.*;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public static final List<Module> modules = new ArrayList<>();

    public static void init() {
        modules.add(new HitboxModule());
        modules.add(new TargetHudModule());
    }

    public static Module getModule(String name) {
        for (Module m : modules) {
            if (m.name.equalsIgnoreCase(name)) return m;
        }
        return null;
    }
}
