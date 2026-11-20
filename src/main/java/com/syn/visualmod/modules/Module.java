package com.syn.visualmod.modules;

public abstract class Module {
    public String name;
    public boolean enabled;

    public Module(String name) {
        this.name = name;
        this.enabled = false;
    }

    public void toggle() {
        this.enabled = !this.enabled;
        onToggle();
    }

    public void onToggle() {}
}
