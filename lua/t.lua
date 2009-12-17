require "Vector"
require "Matrix"

local a = Matrix.zero

print( a:uv() )

a:uv({10,29},5)

print( a:uv() )
