package com.uml.dependency.b;

public class MacbookBuilder {
    public Macbook create() {
        Macbook computer = new Macbook();

        computer.setBoard("英特尔主板");
        computer.setDisplay("Retina 显示器");
        computer.setOS();

        return computer;
    }
}
