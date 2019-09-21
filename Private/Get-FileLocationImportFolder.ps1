function Get-FileLocationImportFolder {

    $subPath = "/ImportedClasses/"
    $moduleFolder = Split-Path -Path $PSScriptRoot -Parent
    $filePath = Join-Path -Path $moduleFolder -ChildPath $subPath

    return $filePath
}
