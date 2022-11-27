function Get-JavaAllPackages {

    $allPackages = @()

    $folderPath = Get-FileLocationImportFolder
    $allSavedFiles = Get-Childitem -Path $folderPath -Recurse -Filter "*.csv"
    foreach($file in $allSavedFiles) {
        $packageName = $file.Name.Replace(".csv","").Replace("_",".")
        $importedClasses = Import-Csv -Delimiter "," -Path $file.FullName
        $newPackage = New-Object PsObject
        Add-Member -InputObject $newPackage -Name "Path" -Value $packageName -MemberType NoteProperty
        Add-Member -InputObject $newPackage -Name "Count" -Value $importedClasses.Length -MemberType NoteProperty
        $allPackages += $newPackage
    }

    return $allPackages
}
