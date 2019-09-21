<#
    .SYNOPSIS
    [CSV] Sucht in der übergebenen CSV-Datei nach Einträgen, die einen Wert enthalten. Die Suche ist nicht case-sensitive.
#>
function Get-CsvValues {

    param (
        [Parameter( Mandatory )]
        [ValidateNotNullOrEmpty()]
        [string]
        $path,

        [string]
        $key,

        [Alias("value")]
        [string]
        $valueContains
    )

    process {

        Write-Debug "[Get-CsvValues] BLUBB Path: $path, Key: $key, ValueContains: $valueContains"

        $table = Import-Csv -Delimiter "," -Path $path
        if (-not $table) {
            return
        }

        if (-not ($key -and $valueContains)) {
            return $table
        }

        $selectedEntries = $table
        if ($valueContains -ne "*") {
            $selectedEntries = $table | Where-Object { $_.$key.ToLower().Contains($valueContains.ToLower()) }
        }

        return $selectedEntries
    }
}
