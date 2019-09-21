package de.PowerShell.JavaParser.parsedResults;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JavaSerializableClass implements Serializable {

    private Id id;
    private String path = "";
    private String packageOfClass = "";
    private List<Id> imports = new LinkedList<>();
    private List<String> classAnnotations = new LinkedList<>();
    private String name = "";
    private boolean isInterface = false;
    private boolean isEnum = false;
    private boolean isAbstract = false;
    private List<Id> extendsClasses = new LinkedList<>();
    private List<Id> implementsInterfaces = new LinkedList<>();
    private List<Member> members = new LinkedList<>();
    private boolean containsSubclasses = false;
    private boolean isTestClass = false;
}
