function Get-JavaClassesByImport {
<#
    .SYNOPSIS
    Imports parsed classes from csv-Files to PSObjects for the command line
#>
    param (
        [Parameter( Mandatory )]
        [string]
        $Package,

        [Parameter( ValueFromPipeline )]
        [PsObject]
        $PipeObject
    )

    process {

        # Modus: Filter in pipeline
        if ($PipeObject -and $PipeObject.imports) {
            $splittedImports = $PipeObject.imports.Split(";")
            $results = $splittedImports | Where-Object { $_.Contains($Package) }
            if ($results) {
                return $PipeObject
            }

            return Out-Null -InputObject $PipeObject
        }

        # Modus: Search in imported files

        $allImportedClasses = Get-AllImportedClasses
        foreach($class in $allImportedClasses) {
            if ($class.imports) {
                $splittedImports = $class.imports.Split(";")
                $results = $splittedImports | Where-Object { $_.Contains($Package) }
                if ($results) {
                    Write-Output $class
                }
            }
        }
    }
}
