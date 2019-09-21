function Import-JavaClasses {
<#
    .SYNOPSIS
    Imports parsed classes from csv-Files to PSObjects for the command line
#>
    param (
        [Parameter( ValueFromPipeline )]
        [string]
        $Id,

        [string]
        $Path,

        [string]
        $Package
    )

    process {

        Write-Host "Import-JavaClasses"

        # Retures a JavaPipeObject
    }
}
