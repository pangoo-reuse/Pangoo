package com.os.lib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface Pangoo {
    /*
    def source = project.pg.sid
    def layout = project.pg.lid
    def vm = project.pg.vm
    def activity = project.pg.activity
    /**
     * source file dir path ,within ~ java/
     *
     * @return
     */
    String source() default "";

    /**
     * view model package location , default  vm
     *
     * @return
     */
    String vm() default "vm";

    String layout();

    String activity() default "activity";

    String pkg();

    boolean databinding() default true;
}
