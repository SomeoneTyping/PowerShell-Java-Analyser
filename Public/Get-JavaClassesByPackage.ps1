function Get-JavaClassesByPackage {
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
        if ($PipeObject -and $PipeObject.package) {
            if ($PipeObject.package.Contains($Package)) {
                return $PipeObject
            }

            return Out-Null -InputObject $PipeObject
        }

        # Modus: Search in imported files
        $packageString = $Package.Replace(".","_")

        $folderPath = Get-FileLocationImportFolder
        $allSavedFiles = Get-Childitem -Path $folderPath -Recurse -Filter "*.csv"
        foreach($file in $allSavedFiles) {
            $currentPackageString = ($file.FullName | Split-Path -Leaf).Replace(".csv","")
            if ($currentPackageString.Contains($packageString)) {
                $importedClasses = Import-Csv -Delimiter "," -Path $file.FullName
                foreach($class in $importedClasses) {
                    return $class
                }
            }
        }
    }
}
