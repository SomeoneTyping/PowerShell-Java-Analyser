function Initialize-JavaProject {
<#
    .SYNOPSIS
    Searches for all *.java files in a root-folder, parses the classes and saves it in an internal csv-format
#>
    param (
        [Alias("path")]
        [string]
        $Folder="."
    )

    begin {

        if (-not (Test-Path -PathType Container -Path $Folder)) {
            Write-Error "[Initialize-JavaProject] The given path does not point to a valid folder."
            return
        }

        Write-Host "[Initialize-JavaProject] Reading already imported classes..."
        $importFolder = Get-FileLocationImportFolder
        $allSavedFiles = Get-Childitem -Path $importFolder -Recurse -Filter "*.csv"
        $allImportedClasses = @()
        $allSavedFiles | ForEach-Object { $allImportedClasses += Import-Csv -Delimiter "," -Path $_.FullName ; Remove-Item $_.FullName }

        Write-Host "[Initialize-JavaProject] Reading Java-Files..."
        $allJavaFiles = Get-Childitem -Path $Folder -Recurse -Filter "*.java"
        $totalFilesCount = $allJavaFiles.Length
        if ($totalFilesCount -eq 0) {
            Write-Error "[Initialize-JavaProject] No *.java files found in this path."
            return
        } else {
            Write-Host ([string]::Format("[Initialize-JavaProject] Found {0} java-files.", $totalFilesCount))
        }

        $counter = 0
        foreach ($file in $allJavaFiles) {

            $hashOfCurrentFile = Get-FileHash -Path $file.FullName -Algorithm MD5 | Select-Object -ExpandProperty Hash
            $existingObject = $allImportedClasses | Where-Object { $_.path -eq $file.FullName }
            if ($existingObject -and ($existingObject.hash -eq $hashOfCurrentFile)) {
                $csvPath = Join-Path -Path $importFolder -ChildPath ([string]::Format("{0}.csv", $existingObject.package.Replace(".","_")))
                Write-Host ([string]::Format("[{0}/{1}] Unchanged: {2}", $counter, $totalFilesCount, $file.FullName))
                Save-JavaPsObject -JavaClass $existingObject -FilePath $csvPath
            } else {
                Write-Host ([string]::Format("[{0}/{1}] Importing: {2}", $counter, $totalFilesCount, $file.FullName))
                $javaPsObject = New-JavaPsObject -Path $file.FullName
                $csvPath = Join-Path -Path $importFolder -ChildPath ([string]::Format("{0}.csv", $javaPsObject.package.Replace(".","_")))
                Save-JavaPsObject -JavaClass $javaPsObject -FilePath $csvPath
            }

            $counter++
        }
    }
}
