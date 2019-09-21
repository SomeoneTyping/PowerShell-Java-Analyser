function Find-JavaClassesByImport {
<#
    .SYNOPSIS
    Imports parsed classes from csv-Files to PSObjects for the command line
#>
    param (
        [PSObject]
        $psObject,

        [string]
        $importsId,

        [string]
        $importsPackage
    )

    process {

        Write-Host "Import-JavaClassesByImport"
        $file = Get-FileLocationConfigurationsCsv
        Get-CsvValues -path $file -key "key" -valueContains "hallo"

        # Passes the psObject or not
    }
}
