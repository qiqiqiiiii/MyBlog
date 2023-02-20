package org.njupt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) //运行时执行
@Target(ElementType.METHOD) //添加到方法上
public @interface SystemLog {
    String businessName();
}
