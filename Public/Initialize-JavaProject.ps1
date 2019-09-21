function Initialize-JavaProject {
<#
    .SYNOPSIS
    Searches for all *.java files in a root-folder, parses the classes and saves it in an internal csv-format
#>
    param (
        [Parameter( Mandatory )]
        [ValidateNotNullOrEmpty()]
        [Alias("path")]
        [string]
        $Folder,

        [int]
        $First,

        [int]
        $Skip
    )

    begin {
        $importFolder = Get-FileLocationImportFolder
        if ((-not $First) -and (-not $Skip)) {
            Remove-Item -Path $importFolder -Recurse -Include *.csv
        }
    }

    process {

        if (-not (Test-Path -PathType Container -Path $Folder)) {
            Write-Error "[Initialize-JavaProject] The path '$Folder' does not point to a valid folder."
            return
        }

        $allJavaFiles = Get-Childitem -Path $Folder -Recurse -Filter "*.java"
        if ($Skip) {
            $allJavaFiles = $allJavaFiles | Select-Object -Skip $Skip
        }
        $totalFilesCount = $allJavaFiles.Length
        if ($totalFilesCount -eq 0) {
            Write-Error "[Initialize-JavaProject] No *.java files found in this path."
            return
        }

        $counter = 0
        Write-Host ([string]::Format("[Initialize-JavaProject] Found {0} java-files.", $totalFilesCount))

        foreach ($file in $allJavaFiles) {
            Write-Host ([string]::Format("[{0}/{1}] Importing {2}.", $counter, $totalFilesCount, $file.FullName))
            $javaPsObject = New-JavaPsObject -Path $file.FullName
            $filePath = Join-Path -Path $importFolder -ChildPath ([string]::Format("{0}.csv", $javaPsObject.package))
            Save-JavaPsObject -JavaClass $javaPsObject -FilePath $filePath
            $counter++
        }
    }
}
