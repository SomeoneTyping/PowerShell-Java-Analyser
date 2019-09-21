# PowerShell Java Analyser

## What is it?

This PowerShell module brings Java-Dependency-Analysis to the PowerShell

## Quickstart

1. Import this module to you PowerShell

Initialize-JavaProject -Path
--> Wenn ein Path angegeben ist, lese aus diesem Path (und lege ihn in den Configurations fest)
--> Wenn kein Path angegeben ist, versuche diesen aus den Configurations zu lesen
--> Konsolenausgebe: Importing... [Dateiname].java, [Dateiname].java,

Import-JavaClasses -id -package -path
--> Kann Java-Klassen laden und in PSObjects konvertieren
--> id und package holen die Objekte dabei aus dem Storage
--> path lädt eine neue Java-Datei

Filter /Find-JavaClass
--> Bekommt ein PsObject rein und kann dieses filtern oder durchleiten

Visualise-JavaPackage -psObject -view
--> Bekommt ein PsObject übergeben und erstellt dazu eienn Graphen

Edit-View -input -add -remove -list
--> Konfort-Funktion um einen View zu editieren und Packages hinzuzufügen bzw zu entfernen
--> Edit-View -list | Where-Object { $_.Contains("ui") } | Edit-View -remove