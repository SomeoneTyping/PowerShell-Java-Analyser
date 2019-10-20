<#
    .SYNOPSIS
    Reads and parses a Java-class given by a path
#>
function New-JavaPsObject {

    param (
        [Parameter( Mandatory )]
        [ValidateNotNullOrEmpty()]
        [string]
        $Path,

        [string]
        $Timestamp
    )

    begin {
        $subPath = "/JavaParser/JavaParser.jar"
        $moduleFolder = Split-Path -Path $PSScriptRoot -Parent
        $pathToParsingJar = Join-Path -Path $moduleFolder -ChildPath $subPath
    }

    process {

        if ((-not (Test-Path $Path)) -or (-not ($Path.EndsWith(".java")))) {
            throw "[New-JavaPsObject] The path '$Path' does not point to an existing *.java file"
        }

        $programOutput = java -cp $pathToParsingJar "de.PowerShell.JavaParser.Application" $Path
        if ($?) {
            return $programOutput | ConvertFrom-Json | Convert-JsonObjectToJavaClass -ImportTimestamp $Timestamp
        } else {
            throw $programOutput
        }
    }
}
