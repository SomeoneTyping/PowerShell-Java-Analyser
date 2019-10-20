package de.PowerShell.JavaParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import de.PowerShell.JavaParser.IdConverting.IdCreator;
import de.PowerShell.JavaParser.parsedResults.Id;
import de.PowerShell.JavaParser.parsedResults.JavaSerializableClass;
import de.PowerShell.JavaParser.parsedResults.Member;

public class JavaParsingWorker {

    public JavaSerializableClass parse(File fileToAnalyse) throws FileNotFoundException {

        CompilationUnit compilationUnit = StaticJavaParser.parse(fileToAnalyse);

        JavaSerializableClass result = new JavaSerializableClass();

        Optional<PackageDeclaration> packageDeclaration = compilationUnit.findFirst(PackageDeclaration.class);
        packageDeclaration.ifPresent(packageDec -> result.setPackageOfClass(packageDec.getName().asString()));

        List<ImportDeclaration> importDeclarations = compilationUnit.findAll(ImportDeclaration.class);
        grazeImportDeclarations(result, importDeclarations);

        List<ClassOrInterfaceDeclaration> allClasses = compilationUnit.findAll(ClassOrInterfaceDeclaration.class);
        List<String> classIdentifiers = allClasses.stream()
                .filter(cl -> cl.getFullyQualifiedName().isPresent())
                .map(optional -> optional.getFullyQualifiedName().get())
                .collect(Collectors.toList());

        IdCreator idCreator = IdCreator.getCreatorFor(result.getImports(), result.getPackageOfClass(), classIdentifiers);

        result.setContainsSubclasses(allClasses.size() > 1);
        Optional<ClassOrInterfaceDeclaration> firstClassDeclaration = allClasses.stream().findFirst();
        firstClassDeclaration.ifPresent(classOrInterfaceDec -> grazeFirstClassDeclaration(result, idCreator, classOrInterfaceDec));

        Optional<EnumDeclaration> enumDeclaration = compilationUnit.findFirst(EnumDeclaration.class);
        enumDeclaration.ifPresent(enumDec -> graceEnumDeclaration(result, idCreator, enumDec));

        return result;
    }

    private void grazeImportDeclarations(JavaSerializableClass result, List<ImportDeclaration> importDeclarations) {

        List<Id> importsAsIdList = new LinkedList<>();
        for (ImportDeclaration oneImport : importDeclarations) {
            importsAsIdList.add(Id.valueOf(oneImport.getName().toString()));
        }
        result.setImports(importsAsIdList);
    }

    private void grazeFirstClassDeclaration(JavaSerializableClass result, IdCreator idCreator,
            ClassOrInterfaceDeclaration classOrInterfaceDeclaration) {

        result.setInterface(classOrInterfaceDeclaration.isInterface());
        result.setAbstract(classOrInterfaceDeclaration.isAbstract());

        result.setName(classOrInterfaceDeclaration.getName().getIdentifier());
        Optional<String> fullQualifiedName = classOrInterfaceDeclaration.getFullyQualifiedName();
        if (fullQualifiedName.isPresent()) {
            result.setId(Id.valueOf(classOrInterfaceDeclaration.getFullyQualifiedName().get()));
        }

        NodeList<ClassOrInterfaceType> extendsTypes = classOrInterfaceDeclaration.getExtendedTypes();
        result.setExtendsClasses(extendsTypes.stream()
                .map(ext -> ext.isPrimitiveType() ? Id.valueOf(ext.getName().asString()) : idCreator.createAbsoluteId(ext.getName().asString()))
                .collect(Collectors.toList()));

        NodeList<ClassOrInterfaceType> implementsTypes = classOrInterfaceDeclaration.getImplementedTypes();
        result.setImplementsInterfaces(implementsTypes.stream()
                .map(impl -> idCreator.createAbsoluteId(impl.getNameAsString()))
                .collect(Collectors.toList()));

        result.setClassAnnotations(classOrInterfaceDeclaration.getAnnotations().stream()
                .map(a -> a.getName().asString())
                .collect(Collectors.toList()));

        result.setMembers(classOrInterfaceDeclaration.getMembers().stream()
                .filter(BodyDeclaration::isFieldDeclaration)
                .map(mem -> convertFieldDeclarationToMember((FieldDeclaration)mem, idCreator))
                .collect(Collectors.toList()));

        result.setTestClass(classOrInterfaceDeclaration.getMembers().stream()
                .filter(BodyDeclaration::isMethodDeclaration)
                .anyMatch(mem -> analyseAnnotationsIfTheyContainTestAnnotation(mem.getAnnotations())));
    }

    private void graceEnumDeclaration(JavaSerializableClass result, IdCreator idCreator, EnumDeclaration enumDeclaration) {

        result.setEnum(enumDeclaration.isEnumDeclaration());

        result.setName(enumDeclaration.getName().getIdentifier());
        Optional<String> fullQualifiedName = enumDeclaration.getFullyQualifiedName();
        if (fullQualifiedName.isPresent()) {
            result.setId(Id.valueOf(enumDeclaration.getFullyQualifiedName().get()));
        }

        result.setClassAnnotations(enumDeclaration.getAnnotations().stream()
                .map(a -> a.getName().asString())
                .collect(Collectors.toList()));

        result.setEnumEntries(enumDeclaration.getEntries().stream()
                .filter(EnumConstantDeclaration::isEnumConstantDeclaration)
                .map(e -> e.getName().asString())
                .collect(Collectors.toList()));

        result.setMembers(enumDeclaration.getMembers().stream()
                .filter(BodyDeclaration::isFieldDeclaration)
                .map(mem -> convertFieldDeclarationToMember((FieldDeclaration)mem, idCreator))
                .collect(Collectors.toList()));
    }

    private Member convertFieldDeclarationToMember(FieldDeclaration field, IdCreator idFactory) {

        Member generatedMember = new Member();

        generatedMember.setModifiers(field.getModifiers().stream().map(a -> a.getKeyword().asString()).collect(Collectors.joining(" ")));
        String parsedType = field.getElementType().isPrimitiveType()
                ? field.getElementType().asString()
                : idFactory.createAbsoluteId(field.getElementType().asString()).getIdAsString();
        generatedMember.setType(parsedType);

        Optional<String> optionalName = field.getVariables().stream().map(a -> a.getName().asString()).findFirst();
        optionalName.ifPresent(generatedMember::setName);

        return generatedMember;
    }

    private boolean analyseAnnotationsIfTheyContainTestAnnotation(NodeList<AnnotationExpr> annotations) {

        return annotations.stream().anyMatch(a -> a.getName().asString().equals("Test"));
    }

}
