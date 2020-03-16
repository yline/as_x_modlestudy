package com.yline.compile.knife.lib.view.impl;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * javapoet:
 * https://github.com/square/javapoet
 */
final class BindingSet {
    private static final ClassName UNBINDER = ClassName.get("com.yline.compile.knife.lib.view", "Unbinder");

    private final TypeName mTargetTypeName;
    private final ClassName mBindingClassName;
    private final TypeElement mEnclosingElement;

    private final boolean mIsFinal;

    private BindingSet(
            TypeName targetTypeName, ClassName bindingClassName, TypeElement enclosingElement, boolean isFinal) {
        this.mTargetTypeName = targetTypeName;
        this.mBindingClassName = bindingClassName;
        this.mEnclosingElement = enclosingElement;

        this.mIsFinal = isFinal;
    }

    JavaFile brewJava(int sdk, boolean debuggable) {
        TypeSpec typeSpec = createType(sdk, debuggable);
        return JavaFile.builder(mBindingClassName.packageName(), typeSpec)
                .addFileComment("Generated code from Butter Knife. Do not modify!")
                .build();
    }

    private TypeSpec createType(int sdk, boolean debuggable) {
        // public class
        TypeSpec.Builder tBuilder = TypeSpec.classBuilder(mBindingClassName.simpleName())
                .addModifiers(Modifier.PUBLIC)
                .addOriginatingElement(mEnclosingElement);

        // final
        if (mIsFinal) {
            tBuilder.addModifiers(Modifier.FINAL);
        }

        // implements [可能还有多层，这里忽略]
        tBuilder.addSuperinterface(UNBINDER);

        // 将对象，写成 private 的全局变量
        tBuilder.addField(mTargetTypeName, "target", Modifier.PRIVATE);

        // todo 这里就不再衍生了，就属于 JavaPoet 的构建过程

        return tBuilder.build();
    }

    static final class Builder {
        private final TypeName targetTypeName;
        private final ClassName bindingClassName;
        private final TypeElement enclosingElement;
        private final boolean isFinal;
        private final boolean isView;
        private final boolean isActivity;
        private final boolean isDialog;

        private Builder(
                TypeName targetTypeName, ClassName bindingClassName, TypeElement enclosingElement,
                boolean isFinal, boolean isView, boolean isActivity, boolean isDialog) {
            this.targetTypeName = targetTypeName;
            this.bindingClassName = bindingClassName;
            this.enclosingElement = enclosingElement;
            this.isFinal = isFinal;
            this.isView = isView;
            this.isActivity = isActivity;
            this.isDialog = isDialog;
        }


        BindingSet build() {
            return new BindingSet(targetTypeName, bindingClassName, enclosingElement, isFinal);
        }
    }
}
