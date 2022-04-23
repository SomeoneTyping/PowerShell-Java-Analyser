function Get-JavaClassesByName {
<#
    .SYNOPSIS
    Imports parsed classes from csv-Files to PSObjects for the command line
#>
    param (
        [Parameter( Mandatory )]
        [string]
        $Search,

        [Parameter( ValueFromPipeline )]
        [PsObject]
        $PipeObject
    )

    process {

        # Modus: Filter in pipeline
        if ($PipeObject) {
            $className = $PipeObject.id.Substring($PipeObject.id.LastIndexOf(".")+1)
            if ($className.Contains($Search)) {
                return $PipeObject
            }

            return Out-Null -InputObject $PipeObject
        }

        # Modus: Search in imported files
        $allImportedClasses = Get-AllImportedClasses
        foreach($class in $allImportedClasses) {
            $className = $class.id.Substring($class.id.LastIndexOf(".")+1)
            if ($className.Contains($Search)) {
                Write-Output $class
            }
        }
    }
}
