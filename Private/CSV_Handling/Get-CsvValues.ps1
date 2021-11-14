<#
    .SYNOPSIS
    Searches in a given *.csv-file for entries that contain a given value under a given key.
    When the file does not exists, it returns $null.
#>
function Get-CsvValues {

    param (
        [Parameter( Mandatory )]
        [ValidateNotNullOrEmpty()]
        [string]
        $Path,

        [string]
        $Key,

        [Alias("value")]
        [string]
        $ValueContains
    )

    process {

        Write-Debug "[Get-CsvValues] Path: $Path, Key: $Key, ValueContains: $ValueContains"

        if (-not (Test-Path $Path)) {
            return $null
        }

        $table = Import-Csv -Delimiter "," -Path $Path

        if (-not ($Key -and $ValueContains)) {
            return $table
        }

        $selectedEntries = $table
        if ($ValueContains -ne "*") {
            $selectedEntries = $table | Where-Object { $_.$Key.ToLower().Contains($ValueContains.ToLower()) }
        }

        return $selectedEntries
    }
}
