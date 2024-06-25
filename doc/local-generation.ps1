# version : 0.1.0

$Script = $MyInvocation.MyCommand.Definition
$ScriptPath = Split-Path $Script -Parent

docker build ${ScriptPath} -t syson_doc_generator:local

docker run -v "$ScriptPath\..:/usr/app:z" syson_doc_generator:local
