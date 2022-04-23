function Get-JavaAllClasses {

    $allImportedClasses = @()

    $folderPath = Get-FileLocationImportFolder
    $allSavedFiles = Get-Childitem -Path $folderPath -Recurse -Filter "*.csv"
    foreach($file in $allSavedFiles) {
        $importedClasses = Import-Csv -Delimiter "," -Path $file.FullName
        foreach($class in $importedClasses) {
            $allImportedClasses += $class
        }
    }

    return $allImportedClasses
}
