package com.os.pg;


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class SaxHandler extends DefaultHandler {

    public void modifyActivity(File file, String activityName) {
        try {
            //<activity android:name=".activity.Abc"/>
            SAXReader reader = new SAXReader();
            Document document = reader.read(file);
            Element root = document.getRootElement();
            Element application = (Element) root.elements("application").get(0);
//            List<Element> activityList = application.elements("activity");
//            if (activityList.size() > 0) {
//                for (Element e : activityList) {
//                    String value = e.attribute("android:name").getValue();
//                    if (!activityName.equals(value)) {
//                        application.addElement("activity").addAttribute("android:name", activityName);
//
//                    }
//
//                }
//            } else
                application.addElement("activity").addAttribute("android:name", activityName);
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("utf-8");
            XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
            writer.write(document);
            writer.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void create(File file, Document document) {
        try {
            OutputFormat format = OutputFormat.createPrettyPrint();


            format.setEncoding("utf-8");
            XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
            writer.write(document);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //        <?xml version="1.0" encoding="utf-8"?>
    //        <layout>
    //            <data>
    //                <variable
    //                        name="viewModel"
    //                type="com.os.pangoo.BaseViewModel" />
    //            </data>
    //            <androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    //                android:layout_width="match_parent"
    //                android:layout_height="match_parent">
    //
    //            </androidx.constraintlayout.widget.ConstraintLayout>
    //        </layout>

    public void createLayout(File layoutFile, String viewModelFullName) {

        Document doc = DocumentHelper.createDocument();
        Element rootEle = doc.addElement("layout");
        rootEle.addNamespace("android", "http://schemas.android.com/apk/res/android");

        Element dataEle = rootEle.addElement("data");
        Element variable = dataEle.addElement("variable");
        variable.addAttribute("name", "viewModel");
        variable.addAttribute("type", viewModelFullName);


        Element layoutEle = rootEle.addElement("androidx.constraintlayout.widget.ConstraintLayout");
        layoutEle.addAttribute("android:layout_width", "match_parent");
        layoutEle.addAttribute("android:layout_height", "match_parent");


        create(layoutFile, doc);

    }

    public Document createDocument(String root, Set<NameSpace> nameSpaces, Element... eles) {
        Document document = DocumentHelper.createDocument();

        Element rootElement = document.addElement(root);
        if (nameSpaces != null && nameSpaces.size() > 0) {
            for (NameSpace ms : nameSpaces) {
                rootElement.addNamespace(ms.name, ms.space);
            }
        }
        if (eles != null && eles.length > 0) {
            for (Element e : eles) {
                rootElement.add(e);
            }
        }
        return document;
    }

    public class NtEle {
        private String node;
        private Ele child;

        public NtEle(String node) {
            this.node = node;
        }

        public String getNode() {
            return node;
        }
    }

    public class Ele extends NtEle {
        private String name;
        private String value;

        public Ele(String node, String name, String value) {
            super(node);
            this.name = name;
            this.value = value;
        }


        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }

    public class NameSpace {
        private String name;
        private String space;

        public NameSpace(String name, String space) {
            this.name = name;
            this.space = space;
        }

        public String getName() {
            return name;
        }

        public String getSpace() {
            return space;
        }
    }


}
