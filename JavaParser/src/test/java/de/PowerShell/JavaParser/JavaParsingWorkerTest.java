package de.PowerShell.JavaParser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

import de.PowerShell.JavaParser.parsedResults.JavaSerializableClass;

public class JavaParsingWorkerTest {

    private JavaParsingWorker sut;

    @Before
    public void setUp() {

        sut = new JavaParsingWorker();
    }

    @Test
    public void testClassParsing() throws FileNotFoundException {

        String filePath = "src/main/java/de/PowerShell/JavaParser/exampleClasses/SuperHeroes/IronMan.java";
        File testFile = new File(filePath);
        JavaSerializableClass simpleClass = sut.parse(testFile);

        assertEquals("de.PowerShell.JavaParser.exampleClasses.SuperHeroes.IronMan", simpleClass.getId().getIdAsString());
        assertEquals("de.PowerShell.JavaParser.exampleClasses.SuperHeroes", simpleClass.getPackageOfClass());
        assertEquals("IronMan", simpleClass.getName());
        assertTrue(simpleClass.getMembers().stream().anyMatch(m -> m.getType().equals("int")));
        assertTrue(simpleClass.getMembers().stream().anyMatch(m -> m.getType().equals("java.lang.Integer")));
        assertFalse(simpleClass.isContainsSubclasses());
        assertFalse(simpleClass.isTestClass());
    }

    @Test
    public void testEnumParsing() throws FileNotFoundException {

        String filePath = "src/main/java/de/PowerShell/JavaParser/exampleClasses/SuperHeroes/EvilVillain.java";
        File testFile = new File(filePath);
        JavaSerializableClass simpleClass = sut.parse(testFile);

        assertEquals("de.PowerShell.JavaParser.exampleClasses.SuperHeroes.EvilVillain", simpleClass.getId().getIdAsString());
        assertEquals("de.PowerShell.JavaParser.exampleClasses.SuperHeroes", simpleClass.getPackageOfClass());
        assertEquals("EvilVillain", simpleClass.getName());
        assertTrue(simpleClass.isEnum());
        assertTrue(simpleClass.getImports().isEmpty());
        assertEquals("value", simpleClass.getMembers().get(0).getName());
        assertTrue(simpleClass.getClassAnnotations().isEmpty());
        assertTrue(simpleClass.getExtendsClasses().isEmpty());
        assertTrue(simpleClass.getImplementsInterfaces().isEmpty());
        assertFalse(simpleClass.isContainsSubclasses());
        assertFalse(simpleClass.isTestClass());
    }

    @Test
    public void testAbstractClassWithLineBreak() throws FileNotFoundException {

        String filePath = "src/main/java/de/PowerShell/JavaParser/exampleClasses/Citizens/Citizen.java";
        File testFile = new File(filePath);
        JavaSerializableClass simpleClass = sut.parse(testFile);

        assertEquals("de.PowerShell.JavaParser.exampleClasses.Citizens.Citizen", simpleClass.getId().getIdAsString());
        assertEquals("de.PowerShell.JavaParser.exampleClasses.Citizens", simpleClass.getPackageOfClass());
        assertEquals("Citizen", simpleClass.getName());
        assertTrue(simpleClass.isAbstract());
        assertFalse(simpleClass.isEnum());
        assertFalse(simpleClass.isInterface());
        assertFalse(simpleClass.isContainsSubclasses());
        assertFalse(simpleClass.isTestClass());
    }

    @Test
    public void testAbstractClassWithSubclass() throws FileNotFoundException {

        String filePath = "src/main/java/de/PowerShell/JavaParser/exampleClasses/SuperHeroes/SpiderMan.java";
        File testFile = new File(filePath);
        JavaSerializableClass simpleClass = sut.parse(testFile);

        assertEquals("de.PowerShell.JavaParser.exampleClasses.SuperHeroes.SpiderMan", simpleClass.getId().getIdAsString());
        assertEquals("de.PowerShell.JavaParser.exampleClasses.SuperHeroes", simpleClass.getPackageOfClass());
        assertEquals("SpiderMan", simpleClass.getName());
        assertFalse(simpleClass.isAbstract());
        assertFalse(simpleClass.isEnum());
        assertFalse(simpleClass.isInterface());
        assertEquals(1, simpleClass.getMembers().size());
        assertEquals("de.PowerShell.JavaParser.exampleClasses.SuperHeroes.SpiderMan.SpidermanDetails",
                simpleClass.getMembers().get(0).getType());
        assertEquals("de.PowerShell.JavaParser.exampleClasses.SuperHeroes.ISuperHero",
                simpleClass.getImplementsInterfaces().get(0).getIdAsString());
        assertTrue(simpleClass.isContainsSubclasses());
        assertFalse(simpleClass.isTestClass());
    }

    @Test
    public void testParseATestClass() throws FileNotFoundException {

        String filePath = "src/test/java/de/PowerShell/JavaParser/JavaParsingWorkerTest.java";
        File testFile = new File(filePath);
        JavaSerializableClass simpleClass = sut.parse(testFile);

        assertEquals("de.PowerShell.JavaParser.JavaParsingWorkerTest", simpleClass.getId().getIdAsString());
        assertEquals("de.PowerShell.JavaParser", simpleClass.getPackageOfClass());
        assertEquals("JavaParsingWorkerTest", simpleClass.getName());
        assertFalse(simpleClass.isAbstract());
        assertFalse(simpleClass.isEnum());
        assertFalse(simpleClass.isInterface());
        assertEquals(1, simpleClass.getMembers().size());
        assertEquals("de.PowerShell.JavaParser.JavaParsingWorker",
                simpleClass.getMembers().get(0).getType());
        assertTrue(simpleClass.getImplementsInterfaces().isEmpty());
        assertFalse(simpleClass.isContainsSubclasses());
        assertTrue(simpleClass.isTestClass());
    }
}
