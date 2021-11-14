function Get-JavaClassesByPackage {
<#
    .SYNOPSIS
    Imports parsed classes from csv-Files to PSObjects for the command line
#>
    param (
        [Parameter( Mandatory )]
        [string]
        $Package
    )

    process {
        
        $filteredClasses = @()
        $packageString = $Package.Replace(".","_")

        Write-Host "Import-JavaClassesByImport"
        $folderPath = Get-FileLocationImportFolder
        $allSavedFiles = Get-Childitem -Path $folderPath -Recurse -Filter "*.csv"
        foreach($file in $allSavedFiles) {
            $currentPackageString = ($file.FullName | Split-Path -Leaf).Replace(".csv","")
            if ($currentPackageString.StartsWith($packageString)) {
                $importedClasses = Import-Csv -Delimiter "," -Path $file.FullName
                foreach($class in $importedClasses) {
                    $filteredClasses += $class
                }
            }
        }

        return $filteredClasses
    }
}
