package com.os.pg.plugin

import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import org.gradle.api.Plugin
import org.gradle.api.Project

import javax.lang.model.element.Modifier

class InitPlugin implements Plugin<Project> {
    private Project project

    @Override
    void apply(Project project) {
        this.project = project

        project.extensions.create("pg", PgConfigExtension)

        // 取得外部参数
        if (project.android.hasProperty("applicationVariants")) {
            project.android.applicationVariants.all { variant ->
                String variantName = variant.name.capitalize()
                def projectAbsolutePath = project.projectDir.absolutePath
                String pkg = null
                if ((pkg = project.pg.pkg) == null) {
                    pkg = project.property("android").properties.get("defaultConfig").properties.get("applicationId")
                }

                def source = project.pg.sid
                def layout = project.pg.lid
                def vm = project.pg.vm
                def activity = project.pg.activity


                String pp
                if (pkg.contains(".")) {
                    pp = pkg.replaceAll("\\.", File.separator + File.separator)
                }
                def configFile = new File(projectAbsolutePath + File.separatorChar + source + File.separatorChar + pp + File.separatorChar + "config")
                if (!configFile.exists()) {
                    configFile.mkdirs()
                }
                def sr = new File(configFile.getAbsolutePath() + File.separatorChar + "PangooConfig.java")
                sr.delete()
                sr.createNewFile()

//
                def sourceFilePath = new File(source).getAbsolutePath()
                def layoutFilePath = new File(layout).getAbsolutePath()


                project.logger.info("---------------------" + project.pg.pkg)

                ClassName pangoo = ClassName.get("com.os.lib", "Pangoo")
                AnnotationSpec configAnnotation = AnnotationSpec.builder(pangoo)
                        .addMemberForValue("source", sourceFilePath) // addMember设置注解属性
                        .addMemberForValue("vm", vm)
                        .addMemberForValue("activity", activity)
                        .addMemberForValue("pkg", pkg)
                        .addMemberForValue("layout", layoutFilePath)
                        .build()


                TypeSpec its = TypeSpec.interfaceBuilder("PangooConfig")
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(configAnnotation)
                        .build()

                JavaFile javaFile = JavaFile.builder(pkg+".config", its)
                        .addFileComment("this codes are generated automatically. Do not modifyActivity!")
                        .build()
                sr.append(javaFile.toString())
            }
        }

    }
}