function Get-JavaClassesByImport {
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

        Write-Host "Import-JavaClassesByImport"
        $folderPath = Get-FileLocationImportFolder
        $allSavedFiles = Get-Childitem -Path $folderPath -Recurse -Filter "*.csv"
        foreach($file in $allSavedFiles) {
            $importedClasses = Import-Csv -Delimiter "," -Path $file.FullName
            foreach($class in $importedClasses) {
                if ($class.imports) {
                    $splittedImports = $class.imports.Split(";")
                    $results = $splittedImports | Where-Object { $_.startsWith($Package) }
                    if ($results) {
                        $filteredClasses += $class
                    }
                }
            }
        }

        return $filteredClasses
    }
}
